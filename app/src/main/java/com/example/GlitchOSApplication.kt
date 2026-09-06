package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class GlitchOSApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 1. Safe Firebase Initialization
        try {
            FirebaseApp.initializeApp(this)
            Log.d("GlitchOSApplication", "Firebase successfully initialized.")
        } catch (e: Throwable) {
            Log.e("GlitchOSApplication", "Error initializing FirebaseApp: ${e.message}", e)
        }

        // 2. Global Uncaught Exception Protector to prevent app crashes
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("GlitchOSApplication", "🔥 [CrashPreventer] Unhandled exception in thread ${thread.name}: ${throwable.message}", throwable)

            val isMainThread = thread.name == "main" || android.os.Looper.getMainLooper().thread == thread
            val isFatalMemory = throwable is OutOfMemoryError

            // If it occurred on a background thread/coroutine and is not OOM, do NOT terminate the app!
            if (!isMainThread && !isFatalMemory) {
                Log.w("GlitchOSApplication", "🛡️ Suppressed background thread crash in [${thread.name}]: ${throwable.message}")
                return@setDefaultUncaughtExceptionHandler
            }

            // On main thread or critical OOM, delegate cleanly to default handler
            try {
                defaultHandler?.uncaughtException(thread, throwable)
            } catch (t: Throwable) {
                Log.e("GlitchOSApplication", "Secondary crash in exception handler", t)
            }
        }
    }
}
