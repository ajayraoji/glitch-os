package com.example.util

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class VideoAgentSettings(
    val videoLengthSeconds: Int = 60, // 15, 30, 45, 60
    val videoFormat: String = "Vertical (9:16) - YouTube Shorts", // YouTube Shorts, Instagram Reels, TikTok
    val videoLanguage: String = "Hinglish",
    val category: String = "Tech & AI",
    val mode: String = "Manual", // "Manual" or "Auto"
    val autoIntervalHours: Int = 1, // 1 to 10
    val isAutoLoopRunning: Boolean = false,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)

object FirestoreService {
    private const val TAG = "FirestoreService"

    /**
     * Saves Video Agent Settings to Firestore
     */
    suspend fun saveVideoAgentSettings(
        context: Context,
        userEmail: String?,
        settings: VideoAgentSettings
    ) = withContext(Dispatchers.IO) {
        try {
            val firestore = getFirestoreSafe(context) ?: return@withContext
            val auth = getFirebaseAuthSafe(context)
            val deviceId = DeviceDataCollector.getDeviceId(context)
            val uid = auth?.currentUser?.uid ?: userEmail?.replace(".", "_")?.replace("@", "_at_") ?: "user_${deviceId.takeLast(6)}"
            val docData = hashMapOf(
                "videoLengthSeconds" to settings.videoLengthSeconds,
                "videoFormat" to settings.videoFormat,
                "videoLanguage" to settings.videoLanguage,
                "category" to settings.category,
                "mode" to settings.mode,
                "autoIntervalHours" to settings.autoIntervalHours,
                "isAutoLoopRunning" to settings.isAutoLoopRunning,
                "lastUpdatedTimestamp" to System.currentTimeMillis(),
                "updatedAt" to FieldValue.serverTimestamp()
            )

            firestore.collection("users").document(uid)
                .collection("settings").document("video_agent")
                .set(docData, SetOptions.merge())
                .await()

            firestore.collection("video_agent_settings").document(uid)
                .set(docData, SetOptions.merge())
                .await()

            Log.d(TAG, "✅ Saved Video Agent Settings to Firestore for $uid")
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to save Video Agent Settings to Firestore", e)
        }
    }

    /**
     * Fetches Video Agent Settings from Firestore for automatic cross-device sync
     */
    suspend fun fetchVideoAgentSettings(
        context: Context,
        userEmail: String?
    ): VideoAgentSettings? = withContext(Dispatchers.IO) {
        try {
            val firestore = getFirestoreSafe(context) ?: return@withContext null
            val auth = getFirebaseAuthSafe(context)
            val deviceId = DeviceDataCollector.getDeviceId(context)
            val uid = auth?.currentUser?.uid ?: userEmail?.replace(".", "_")?.replace("@", "_at_") ?: "user_${deviceId.takeLast(6)}"

            val doc = try {
                firestore.collection("video_agent_settings").document(uid).get(Source.DEFAULT).await()
            } catch (ffe: com.google.firebase.firestore.FirebaseFirestoreException) {
                if (ffe.code == com.google.firebase.firestore.FirebaseFirestoreException.Code.UNAVAILABLE || ffe.message?.contains("offline", ignoreCase = true) == true) {
                    Log.d(TAG, "Offline status detected; trying local Firestore CACHE source.")
                    try {
                        firestore.collection("video_agent_settings").document(uid).get(Source.CACHE).await()
                    } catch (cacheEx: Exception) {
                        Log.d(TAG, "Document not present in local cache: ${cacheEx.message}")
                        null
                    }
                } else {
                    throw ffe
                }
            }

            if (doc != null && doc.exists()) {
                val length = doc.getLong("videoLengthSeconds")?.toInt() ?: 60
                val format = doc.getString("videoFormat") ?: "Vertical (9:16) - YouTube Shorts"
                val lang = doc.getString("videoLanguage") ?: "Hinglish"
                val cat = doc.getString("category") ?: "Tech & AI"
                val mode = doc.getString("mode") ?: "Manual"
                val interval = doc.getLong("autoIntervalHours")?.toInt() ?: 1
                val isLooping = doc.getBoolean("isAutoLoopRunning") ?: false
                val ts = doc.getLong("lastUpdatedTimestamp") ?: System.currentTimeMillis()
                return@withContext VideoAgentSettings(
                    videoLengthSeconds = length,
                    videoFormat = format,
                    videoLanguage = lang,
                    category = cat,
                    mode = mode,
                    autoIntervalHours = interval,
                    isAutoLoopRunning = isLooping,
                    lastUpdatedTimestamp = ts
                )
            }
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to fetch Video Agent Settings from Firestore", e)
        }
        return@withContext null
    }


    private fun getFirestoreSafe(context: Context? = null): FirebaseFirestore? {
        return try {
            if (context != null) {
                if (FirebaseApp.getApps(context).isEmpty()) {
                    FirebaseApp.initializeApp(context)
                }
            }
            FirebaseFirestore.getInstance()
        } catch (e: Throwable) {
            Log.e(TAG, "FirebaseFirestore instance unavailable: ${e.message}")
            null
        }
    }

    private fun getFirebaseAuthSafe(context: Context? = null): FirebaseAuth? {
        return try {
            if (context != null) {
                if (FirebaseApp.getApps(context).isEmpty()) {
                    FirebaseApp.initializeApp(context)
                }
            }
            FirebaseAuth.getInstance()
        } catch (e: Throwable) {
            Log.e(TAG, "FirebaseAuth instance unavailable: ${e.message}")
            null
        }
    }

    /**
     * Saves complete User and Device Data to Firestore as soon as the user logs in
     */
    suspend fun saveCompleteUserAndDeviceData(context: Context, user: GoogleUser) = withContext(Dispatchers.IO) {
        try {
            val firestore = getFirestoreSafe(context) ?: run {
                Log.w(TAG, "Firestore not initialized, skipping save")
                return@withContext
            }

            val auth = getFirebaseAuthSafe(context)
            val deviceId = DeviceDataCollector.getDeviceId(context)
            val uid = auth?.currentUser?.uid ?: user.email?.replace(".", "_")?.replace("@", "_at_") ?: "user_${deviceId.takeLast(6)}"
            val telemetry = DeviceDataCollector.collectAllTelemetry(context)
            val loginTimestamp = System.currentTimeMillis()
            val loginId = "LOGIN_${loginTimestamp}_${deviceId.takeLast(6)}"

            val hardwareMap = telemetry["hardware_and_os"] as? Map<*, *>
            val networkMap = telemetry["network"] as? Map<*, *>
            val appMap = telemetry["app"] as? Map<*, *>
            val batteryMap = telemetry["battery"] as? Map<*, *>
            val displayMap = telemetry["display"] as? Map<*, *>
            val memoryMap = telemetry["memory"] as? Map<*, *>
            val storageMap = telemetry["storage"] as? Map<*, *>

            val deviceModel: String = hardwareMap?.get("model")?.toString() ?: "Android Device"
            val deviceBrand: String = hardwareMap?.get("brand")?.toString() ?: "Android"
            val androidVersion: String = hardwareMap?.get("android_version")?.toString() ?: "Android"
            val transportType: String = networkMap?.get("transport_type")?.toString() ?: "Network"
            val appVersionName: String = appMap?.get("version_name")?.toString() ?: "1.0"
            val batteryPercent: Any = batteryMap?.get("battery_level_percent") ?: -1

            // 1. User Primary Document (users/{uid})
            val userDoc = hashMapOf<String, Any>(
                "uid" to uid,
                "email" to (user.email ?: ""),
                "displayName" to (user.displayName ?: "YouTube Creator"),
                "photoUrl" to (user.photoUrl ?: ""),
                "hasYouTubePermission" to user.hasYouTubePermission,
                "lastLoginAt" to FieldValue.serverTimestamp(),
                "lastLoginAtMillis" to loginTimestamp,
                "lastLoginDevice" to deviceModel,
                "lastDeviceBrand" to deviceBrand,
                "lastDeviceId" to deviceId,
                "lastActiveIpOrNetwork" to transportType,
                "appVersion" to appVersionName,
                "device_overview" to hashMapOf(
                    "model" to deviceModel,
                    "brand" to deviceBrand,
                    "os" to androidVersion,
                    "battery" to batteryPercent,
                    "network" to transportType
                ),
                "device_telemetry" to telemetry
            )

            firestore.collection("users").document(uid)
                .set(userDoc, SetOptions.merge())
                .await()

            Log.d(TAG, "✅ Primary user profile and full device telemetry saved in Firestore for $uid")

            // 2. User's Specific Device Record (Subcollection: users/{uid}/devices/{deviceId})
            val deviceRecord = hashMapOf<String, Any>(
                "device_id" to deviceId,
                "user_id" to uid,
                "user_email" to (user.email ?: ""),
                "display_name" to (user.displayName ?: "User"),
                "device_model" to deviceModel,
                "device_brand" to deviceBrand,
                "android_version" to androidVersion,
                "app_version" to appVersionName,
                "last_sync_at" to FieldValue.serverTimestamp(),
                "last_sync_timestamp_ms" to loginTimestamp,
                "hardware_and_os" to (hardwareMap ?: emptyMap<String, Any>()),
                "network" to (networkMap ?: emptyMap<String, Any>()),
                "app" to (appMap ?: emptyMap<String, Any>()),
                "battery" to (batteryMap ?: emptyMap<String, Any>()),
                "display" to (displayMap ?: emptyMap<String, Any>()),
                "memory" to (memoryMap ?: emptyMap<String, Any>()),
                "storage" to (storageMap ?: emptyMap<String, Any>()),
                "full_telemetry" to telemetry
            )

            firestore.collection("users").document(uid)
                .collection("devices").document(deviceId)
                .set(deviceRecord, SetOptions.merge())
                .await()

            Log.d(TAG, "✅ Complete device record saved to users/$uid/devices/$deviceId")

            // 3. User Login History Audit (Subcollection: users/{uid}/login_history/{loginId})
            val loginHistoryRecord = hashMapOf<String, Any>(
                "login_id" to loginId,
                "user_id" to uid,
                "user_email" to (user.email ?: ""),
                "display_name" to (user.displayName ?: "User"),
                "timestamp" to FieldValue.serverTimestamp(),
                "timestamp_ms" to loginTimestamp,
                "device_id" to deviceId,
                "device_model" to deviceModel,
                "device_brand" to deviceBrand,
                "android_version" to androidVersion,
                "network_type" to transportType,
                "battery_level" to batteryPercent,
                "app_version" to appVersionName,
                "device_snapshot" to telemetry
            )

            firestore.collection("users").document(uid)
                .collection("login_history").document(loginId)
                .set(loginHistoryRecord)
                .await()

            Log.d(TAG, "✅ Login audit recorded in users/$uid/login_history/$loginId")

            // 4. Global Device Telemetry Registry for Diagnostics & Admin Fleet View
            firestore.collection("devices").document(deviceId)
                .set(deviceRecord, SetOptions.merge())
                .await()

            firestore.collection("device_telemetry").document(deviceId)
                .set(deviceRecord, SetOptions.merge())
                .await()

            Log.d(TAG, "✅ Global device telemetry synced for $deviceId")

        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to save user & device data to Firestore", e)
        }
    }

    /**
     * Backward-compatible simple user profile saver
     */
    suspend fun saveUserProfile(user: GoogleUser) = withContext(Dispatchers.IO) {
        try {
            val firestore = getFirestoreSafe() ?: return@withContext
            val uid = getFirebaseAuthSafe()?.currentUser?.uid ?: user.email?.replace(".", "_")?.replace("@", "_at_") ?: return@withContext
            val userData = hashMapOf(
                "uid" to uid,
                "email" to (user.email ?: ""),
                "displayName" to (user.displayName ?: "YouTube Creator"),
                "photoUrl" to (user.photoUrl ?: ""),
                "hasYouTubePermission" to user.hasYouTubePermission,
                "lastLoginAt" to FieldValue.serverTimestamp()
            )
            firestore.collection("users").document(uid)
                .set(userData, SetOptions.merge())
                .await()
            Log.d(TAG, "User profile saved to Firestore for $uid")
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to save user profile to Firestore", e)
        }
    }

    /**
     * Saves a generated Video Project and all its scenes, audio, and visual details to Firestore
     */
    suspend fun saveVideoProject(
        projectTitle: String,
        storyboardScenes: List<String>,
        visualPrompts: List<String>,
        audioCount: Int,
        imageCount: Int,
        voiceoverDurationSec: Double,
        youtubeUploadUrl: String? = null
    ) = withContext(Dispatchers.IO) {
        try {
            val firestore = getFirestoreSafe() ?: return@withContext
            val currentUser = getFirebaseAuthSafe()?.currentUser
            val uid = currentUser?.uid ?: "anonymous_user"
            val userEmail = currentUser?.email ?: ""
            val projectId = "PROJ_${System.currentTimeMillis()}"

            val projectData = hashMapOf(
                "projectId" to projectId,
                "userId" to uid,
                "userEmail" to userEmail,
                "title" to projectTitle,
                "storyboardScenes" to storyboardScenes,
                "visualPrompts" to visualPrompts,
                "audioCount" to audioCount,
                "imageCount" to imageCount,
                "voiceoverDurationSec" to voiceoverDurationSec,
                "youtubeUploadUrl" to (youtubeUploadUrl ?: ""),
                "status" to if (youtubeUploadUrl.isNullOrBlank()) "COMPLETED" else "UPLOADED_TO_YOUTUBE",
                "createdAt" to FieldValue.serverTimestamp()
            )

            firestore.collection("users").document(uid)
                .collection("video_projects").document(projectId)
                .set(projectData)
                .await()

            firestore.collection("global_video_projects").document(projectId)
                .set(projectData)
                .await()

            Log.d(TAG, "Video project $projectId successfully saved to Firestore!")
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to save video project to Firestore", e)
        }
    }

    /**
     * Saves YouTube Shorts upload status to Firestore
     */
    suspend fun logYouTubeUpload(
        videoTitle: String,
        youtubeUrl: String,
        privacyStatus: String
    ) = withContext(Dispatchers.IO) {
        try {
            val firestore = getFirestoreSafe() ?: return@withContext
            val currentUser = getFirebaseAuthSafe()?.currentUser
            val uid = currentUser?.uid ?: "anonymous_user"
            val uploadId = "YT_${System.currentTimeMillis()}"

            val uploadData = hashMapOf(
                "uploadId" to uploadId,
                "userId" to uid,
                "userEmail" to (currentUser?.email ?: ""),
                "videoTitle" to videoTitle,
                "youtubeUrl" to youtubeUrl,
                "privacyStatus" to privacyStatus,
                "uploadedAt" to FieldValue.serverTimestamp()
            )

            firestore.collection("users").document(uid)
                .collection("youtube_uploads").document(uploadId)
                .set(uploadData)
                .await()

            Log.d(TAG, "YouTube upload logged in Firestore: $uploadId")
        } catch (e: kotlinx.coroutines.CancellationException) {
            throw e
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to log YouTube upload in Firestore", e)
        }
    }
}
