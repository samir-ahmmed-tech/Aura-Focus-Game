package com.example.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity

object AuraNotificationHelper {
    const val CHANNEL_FOCUS_ID = "channel_aura_focus"
    const val CHANNEL_MUSIC_ID = "channel_aura_music"
    const val CHANNEL_GAMES_ID = "channel_aura_games"

    private const val NOTIF_FOCUS_ID = 1001
    private const val NOTIF_MUSIC_ID = 1002
    private const val NOTIF_REMINDER_ID = 1003

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val focusChannel = NotificationChannel(
                CHANNEL_FOCUS_ID,
                "Aura Focus Sessions",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows live status for active deep work and meditation sessions"
            }

            val musicChannel = NotificationChannel(
                CHANNEL_MUSIC_ID,
                "Aura Music & Soundscapes",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Displays audio playback and binaural brainwave controls"
            }

            val gamesChannel = NotificationChannel(
                CHANNEL_GAMES_ID,
                "Cognitive Training & Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminds you of daily brain game workouts and streak milestones"
            }

            notificationManager.createNotificationChannels(listOf(focusChannel, musicChannel, gamesChannel))
        }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun showFocusNotification(context: Context, title: String, message: String) {
        if (!hasNotificationPermission(context)) return

        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_FOCUS_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_FOCUS_ID, notification)
        } catch (_: SecurityException) {
        }
    }

    fun clearFocusNotification(context: Context) {
        try {
            NotificationManagerCompat.from(context).cancel(NOTIF_FOCUS_ID)
        } catch (_: Exception) {
        }
    }

    fun showMusicPlaybackNotification(context: Context, trackTitle: String, artist: String) {
        if (!hasNotificationPermission(context)) return

        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_MUSIC_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle("Playing: $trackTitle")
            .setContentText("$artist • Binaural Beats Active")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_MUSIC_ID, notification)
        } catch (_: SecurityException) {
        }
    }

    fun showBrainGameReminderNotification(context: Context, gameTitle: String = "Flanker & Math Sprint") {
        if (!hasNotificationPermission(context)) return

        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_GAMES_ID)
            .setSmallIcon(android.R.drawable.btn_star)
            .setContentTitle("Daily Brain Training Ready 🧠")
            .setContentText("Complete $gameTitle to boost your cognitive score and streak!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIF_REMINDER_ID, notification)
        } catch (_: SecurityException) {
        }
    }
}
