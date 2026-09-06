package com.example.util

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import com.example.BuildConfig
import java.io.File
import java.util.Locale
import java.util.TimeZone

object DeviceDataCollector {

    private const val TAG = "DeviceDataCollector"

    /**
     * Collects exhaustive device, system, hardware, network, battery, and app data
     */
    fun collectAllTelemetry(context: Context): Map<String, Any> {
        val data = mutableMapOf<String, Any>()

        try {
            data["device_id"] = getDeviceId(context)
            data["timestamp_local"] = System.currentTimeMillis()
            data["time_zone"] = TimeZone.getDefault().id
            data["time_zone_display"] = TimeZone.getDefault().displayName
            data["locale"] = Locale.getDefault().toString()
            data["language"] = Locale.getDefault().language
            data["country"] = Locale.getDefault().country

            // System & OS Info
            data["hardware_and_os"] = getHardwareAndOsInfo(context)

            // Display & Screen Specs
            data["display"] = getDisplayInfo(context)

            // Memory & RAM Stats
            data["memory"] = getMemoryInfo(context)

            // Storage Stats
            data["storage"] = getStorageInfo()

            // Battery Status
            data["battery"] = getBatteryInfo(context)

            // Network & Connectivity Status
            data["network"] = getNetworkInfo(context)

            // Application Specs
            data["app"] = getAppInfo(context)

        } catch (e: Throwable) {
            Log.e(TAG, "Error collecting device telemetry", e)
            data["collection_error"] = e.message ?: "Unknown error"
        }

        return data
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return try {
            val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            if (!androidId.isNullOrBlank()) androidId else "device_${Build.BOARD}_${Build.MODEL.replace(" ", "_")}"
        } catch (e: Exception) {
            "device_${System.currentTimeMillis()}"
        }
    }

    private fun getHardwareAndOsInfo(context: Context): Map<String, Any> {
        val info = mutableMapOf<String, Any>()
        try {
            info["manufacturer"] = Build.MANUFACTURER
            info["brand"] = Build.BRAND
            info["model"] = Build.MODEL
            info["device"] = Build.DEVICE
            info["product"] = Build.PRODUCT
            info["hardware"] = Build.HARDWARE
            info["board"] = Build.BOARD
            info["fingerprint"] = Build.FINGERPRINT
            info["bootloader"] = Build.BOOTLOADER
            info["host"] = Build.HOST
            info["android_version"] = Build.VERSION.RELEASE
            info["sdk_int"] = Build.VERSION.SDK_INT
            info["incremental"] = Build.VERSION.INCREMENTAL
            info["security_patch"] = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Build.VERSION.SECURITY_PATCH else "N/A"
            info["supported_abis"] = Build.SUPPORTED_ABIS.toList()
            info["supported_32_bit_abis"] = Build.SUPPORTED_32_BIT_ABIS.toList()
            info["supported_64_bit_abis"] = Build.SUPPORTED_64_BIT_ABIS.toList()
            info["processor_cores"] = Runtime.getRuntime().availableProcessors()
            info["is_emulator"] = isEmulator()
        } catch (e: Exception) {
            Log.w(TAG, "Hardware info error", e)
        }
        return info
    }

    private fun isEmulator(): Boolean {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")
    }

    private fun getDisplayInfo(context: Context): Map<String, Any> {
        val info = mutableMapOf<String, Any>()
        try {
            val metrics: DisplayMetrics = context.resources.displayMetrics
            val orientation = when (context.resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> "Landscape"
                Configuration.ORIENTATION_PORTRAIT -> "Portrait"
                else -> "Undefined"
            }
            info["width_px"] = metrics.widthPixels
            info["height_px"] = metrics.heightPixels
            info["density_dpi"] = metrics.densityDpi
            info["density_scale"] = metrics.density
            info["scaled_density"] = metrics.scaledDensity
            info["xdpi"] = metrics.xdpi
            info["ydpi"] = metrics.ydpi
            info["orientation"] = orientation
            info["font_scale"] = context.resources.configuration.fontScale
        } catch (e: Exception) {
            Log.w(TAG, "Display info error", e)
        }
        return info
    }

    private fun getMemoryInfo(context: Context): Map<String, Any> {
        val info = mutableMapOf<String, Any>()
        try {
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager?.getMemoryInfo(memInfo)

            val totalMemMb = memInfo.totalMem / (1024 * 1024)
            val availMemMb = memInfo.availMem / (1024 * 1024)
            val usedMemMb = totalMemMb - availMemMb
            val usedPercentage = if (totalMemMb > 0) (usedMemMb.toDouble() / totalMemMb * 100).toInt() else 0

            info["total_ram_mb"] = totalMemMb
            info["available_ram_mb"] = availMemMb
            info["used_ram_mb"] = usedMemMb
            info["used_ram_percentage"] = usedPercentage
            info["is_low_memory"] = memInfo.lowMemory
            info["threshold_ram_mb"] = memInfo.threshold / (1024 * 1024)

            val runtime = Runtime.getRuntime()
            info["jvm_max_memory_mb"] = runtime.maxMemory() / (1024 * 1024)
            info["jvm_total_memory_mb"] = runtime.totalMemory() / (1024 * 1024)
            info["jvm_free_memory_mb"] = runtime.freeMemory() / (1024 * 1024)
        } catch (e: Exception) {
            Log.w(TAG, "Memory info error", e)
        }
        return info
    }

    private fun getStorageInfo(): Map<String, Any> {
        val info = mutableMapOf<String, Any>()
        try {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            val availableBlocks = stat.availableBlocksLong

            val totalStorageMb = (totalBlocks * blockSize) / (1024 * 1024)
            val availStorageMb = (availableBlocks * blockSize) / (1024 * 1024)
            val freeStorageGb = availStorageMb.toDouble() / 1024.0
            val totalStorageGb = totalStorageMb.toDouble() / 1024.0

            info["internal_total_mb"] = totalStorageMb
            info["internal_available_mb"] = availStorageMb
            info["internal_total_gb"] = String.format(Locale.US, "%.2f GB", totalStorageGb)
            info["internal_available_gb"] = String.format(Locale.US, "%.2f GB", freeStorageGb)
        } catch (e: Exception) {
            Log.w(TAG, "Storage info error", e)
        }
        return info
    }

    private fun getBatteryInfo(context: Context): Map<String, Any> {
        val info = mutableMapOf<String, Any>()
        try {
            val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, intentFilter)

            if (batteryStatus != null) {
                val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val batteryPct = if (level >= 0 && scale > 0) (level * 100 / scale) else -1

                val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

                val chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                val plugType = when (chargePlug) {
                    BatteryManager.BATTERY_PLUGGED_USB -> "USB"
                    BatteryManager.BATTERY_PLUGGED_AC -> "AC"
                    BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
                    else -> "Battery"
                }

                val health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
                val healthStr = when (health) {
                    BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
                    BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
                    BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
                    BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
                    BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Failure"
                    else -> "Unknown"
                }

                val temp = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
                val tempCelsius = if (temp > 0) temp / 10.0 else 0.0

                info["battery_level_percent"] = batteryPct
                info["is_charging"] = isCharging
                info["power_source"] = plugType
                info["health"] = healthStr
                info["temperature_celsius"] = tempCelsius
            }
        } catch (e: Exception) {
            Log.w(TAG, "Battery info error", e)
        }
        return info
    }

    private fun getNetworkInfo(context: Context): Map<String, Any> {
        val info = mutableMapOf<String, Any>()
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (cm != null) {
                val activeNetwork = cm.activeNetwork
                val caps = cm.getNetworkCapabilities(activeNetwork)
                val isConnected = caps != null && (
                        caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                        )

                val transport = when {
                    caps == null -> "None"
                    caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
                    caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular Mobile Data"
                    caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
                    caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
                    else -> "Other"
                }

                info["is_connected"] = isConnected
                info["transport_type"] = transport
                info["is_metered"] = cm.isActiveNetworkMetered
                if (caps != null) {
                    info["downstream_bandwidth_kbps"] = caps.linkDownstreamBandwidthKbps
                    info["upstream_bandwidth_kbps"] = caps.linkUpstreamBandwidthKbps
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Network info error", e)
        }
        return info
    }

    private fun getAppInfo(context: Context): Map<String, Any> {
        val info = mutableMapOf<String, Any>()
        try {
            val pm = context.packageManager
            val pInfo: PackageInfo = pm.getPackageInfo(context.packageName, 0)

            info["package_name"] = context.packageName
            info["app_name"] = context.applicationInfo.loadLabel(pm).toString()
            info["version_name"] = pInfo.versionName ?: "1.0"
            info["version_code"] = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) pInfo.longVersionCode else pInfo.versionCode
            info["target_sdk"] = context.applicationInfo.targetSdkVersion
            info["min_sdk"] = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) context.applicationInfo.minSdkVersion else 24
            info["first_install_time"] = pInfo.firstInstallTime
            info["last_update_time"] = pInfo.lastUpdateTime
            info["build_type"] = BuildConfig.BUILD_TYPE
            info["app_cache_size_kb"] = AppCacheManager.getCacheSizeBytes(context) / 1024
            info["capabilities"] = listOf(
                "YouTube Data API v3 Upload",
                "Appium / DOM JSON Extraction",
                "NoTrack AI Story Generation",
                "DeepAI Visual Rendering",
                "Edge Neural TTS High-Definition Voice",
                "Multi-Track ExoPlayer 9:16 Video Studio",
                "Firebase Auth & Firestore Sync"
            )
        } catch (e: Exception) {
            Log.w(TAG, "App info error", e)
        }
        return info
    }
}
