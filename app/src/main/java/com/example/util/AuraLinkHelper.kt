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
}
