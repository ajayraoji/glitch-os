package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.MainActivity

class PipelineForegroundService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null

    companion object {
        const val CHANNEL_ID = "video_pipeline_channel"
        const val CHANNEL_NAME = "AI Video Generation Pipeline"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START_OR_UPDATE = "com.example.service.ACTION_START_OR_UPDATE"
        const val ACTION_STOP = "com.example.service.ACTION_STOP"

        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_TEXT = "extra_text"
        const val EXTRA_PROGRESS = "extra_progress"
        const val EXTRA_MAX = "extra_max"
        const val EXTRA_INDETERMINATE = "extra_indeterminate"

        fun startOrUpdate(
            context: Context,
            title: String,
            text: String,
            progress: Int = 0,
            max: Int = 100,
            indeterminate: Boolean = false
        ) {
            val intent = Intent(context, PipelineForegroundService::class.java).apply {
                action = ACTION_START_OR_UPDATE
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_TEXT, text)
                putExtra(EXTRA_PROGRESS, progress)
                putExtra(EXTRA_MAX, max)
                putExtra(EXTRA_INDETERMINATE, indeterminate)
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, PipelineForegroundService::class.java).apply {
                action = ACTION_STOP
            }
            try {
                context.startService(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        acquireWakeLock()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null || intent.action == ACTION_STOP) {
            stopForegroundService()
            return START_NOT_STICKY
        }

        if (intent.action == ACTION_START_OR_UPDATE) {
            val title = intent.getStringExtra(EXTRA_TITLE) ?: "⚡ AI Video Generation"
            val text = intent.getStringExtra(EXTRA_TEXT) ?: "Processing video pipeline in background..."
            val progress = intent.getIntExtra(EXTRA_PROGRESS, 0)
            val max = intent.getIntExtra(EXTRA_MAX, 100)
            val indeterminate = intent.getBooleanExtra(EXTRA_INDETERMINATE, false)

            val notification = buildNotification(title, text, progress, max, indeterminate)
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    var typeFlags = 0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        typeFlags = android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROCESSING
                    } else {
                        typeFlags = android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                    }
                    startForeground(NOTIFICATION_ID, notification, typeFlags)
                } else {
                    startForeground(NOTIFICATION_ID, notification)
                }
            } catch (e: Throwable) {
                try {
                    startForeground(NOTIFICATION_ID, notification)
                } catch (e2: Throwable) {
                    android.util.Log.e("PipelineService", "Unable to start foreground service: ${e2.message}")
                }
            }
        }

        return START_STICKY
    }

    private fun buildNotification(
        title: String,
        text: String,
        progress: Int,
        max: Int,
        indeterminate: Boolean
    ): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)

        if (indeterminate) {
            builder.setProgress(0, 0, true)
        } else if (max > 0) {
            builder.setProgress(max, progress, false)
            builder.setSubText("${((progress.toFloat() / max.toFloat()) * 100).toInt()}%")
        }

        return builder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows live ongoing progress of AI story, voiceover, images, and video rendering"
                setShowBadge(false)
                enableVibration(false)
                enableLights(false)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun acquireWakeLock() {
        try {
            val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager
            wakeLock = powerManager?.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "AiStudio:PipelineForegroundWakeLock"
            )?.apply {
                acquire(60 * 60 * 1000L) // 60 minutes safety timeout
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun releaseWakeLock() {
        try {
            if (wakeLock?.isHeld == true) {
                wakeLock?.release()
            }
            wakeLock = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopForegroundService() {
        releaseWakeLock()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        releaseWakeLock()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
