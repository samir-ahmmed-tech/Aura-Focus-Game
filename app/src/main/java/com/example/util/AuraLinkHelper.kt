package com.example.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * Robust, safe external URL navigation helper.
 * Item 12 Checklist: Fix broken links & prevent crash on missing browser activity.
 */
object AuraLinkHelper {

    fun openUrl(context: Context, url: String) {
        try {
            val validUri = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "https://$url"
            } else {
                url
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validUri)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "No web browser app found to open link", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open link: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun rateOnPlayStore(context: Context) {
        try {
            val marketUri = Uri.parse("market://details?id=com.aistudio.aurafocus.nmhhmt")
            val intent = Intent(Intent.ACTION_VIEW, marketUri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            openUrl(context, "https://play.google.com/store/apps/details?id=com.aistudio.aurafocus.nmhhmt")
        }
    }

    fun shareApp(context: Context) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "NeuroBox - Cognitive Training & Focus Lab")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Experience NeuroBox: 60 Master Brain Games, 50 Power Productivity Tools, and Zero-Lag Focus Space created by Samir Ahmmed! Download on GitHub: https://github.com/samir-ahmmed-tech/Aura-Focus-Game/releases"
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share NeuroBox via").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to launch share: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun contactDeveloper(context: Context) {
        try {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:samir.ahmmed.tech@gmail.com")
                putExtra(Intent.EXTRA_SUBJECT, "[NeuroBox Feedback] Developer Inquiry")
                putExtra(Intent.EXTRA_TEXT, "Hello Samir,\n\nI have the following feedback/bug report regarding NeuroBox:")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(emailIntent)
        } catch (_: Exception) {
            openUrl(context, "https://github.com/samir-ahmmed-tech/Aura-Focus-Game/issues")
        }
    }
}
