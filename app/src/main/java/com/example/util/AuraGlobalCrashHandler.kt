package com.example.util

import android.content.Context
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Universal Error Monitoring & Crash Boundary Handler.
 * Item 20 Checklist: Error monitoring & Uncaught Exception Logger.
 * Ensures the app never abruptly crashes to desktop without recording diagnostics.
 */
object AuraGlobalCrashHandler {
    private const val TAG = "NeuroBoxCrashHandler"
    private const val CRASH_LOG_FILE = "last_crash_report.txt"
    private var isInitialized = false

    fun install(context: Context) {
        if (isInitialized) return
        isInitialized = true

        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                Log.e(TAG, "Uncaught exception caught in thread: ${thread.name}", throwable)
                val crashFile = File(context.filesDir, CRASH_LOG_FILE)
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
                val report = buildString {
                    appendLine("NeuroBox Crash Diagnostic Report")
                    appendLine("Timestamp: $timestamp")
                    appendLine("Thread: ${thread.name} (id: ${thread.id})")
                    appendLine("Exception: ${throwable.javaClass.name}: ${throwable.message}")
                    appendLine("--- Stacktrace ---")
                    appendLine(Log.getStackTraceString(throwable))
                }
                crashFile.writeText(report)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to write crash log", e)
            } finally {
                // Pass to default handler or exit cleanly
                defaultHandler?.uncaughtException(thread, throwable)
            }
        }
    }

    fun getLastCrashReport(context: Context): String? {
        val file = File(context.filesDir, CRASH_LOG_FILE)
        return if (file.exists()) file.readText() else null
    }

    fun clearCrashReport(context: Context) {
        val file = File(context.filesDir, CRASH_LOG_FILE)
        if (file.exists()) file.delete()
    }
}
