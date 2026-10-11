package com.example.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

data class UpdateCheckResult(
    val checked: Boolean = false,
    val hasUpdate: Boolean = false,
    val latestVersion: String = "",
    val currentVersion: String = "",
    val releaseName: String = "",
    val releaseNotes: String = "",
    val downloadUrl: String = "",
    val releaseHtmlUrl: String = "",
    val errorMessage: String? = null
)

object AuraUpdateChecker {
    private const val GITHUB_OWNER = "samir-ahmmed-tech"
    private const val GITHUB_REPO = "Aura-Focus-Game"
    private const val RELEASES_API_URL = "https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/releases/latest"

    suspend fun checkForUpdates(currentVersion: String): UpdateCheckResult {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(RELEASES_API_URL)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.connectTimeout = 8000
                conn.readTimeout = 8000
                conn.setRequestProperty("Accept", "application/vnd.github.v3+json")
                conn.setRequestProperty("User-Agent", "NeuroBox-App-UpdateChecker")

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(conn.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    val json = JSONObject(response.toString())
                    val tagName = json.optString("tag_name", "").replace("v", "").trim()
                    val releaseName = json.optString("name", "New Update Available")
                    val releaseBody = json.optString("body", "Bug fixes and performance improvements.")
                    val releaseHtmlUrl = json.optString("html_url", "https://github.com/$GITHUB_OWNER/$GITHUB_REPO/releases")

                    // Search assets for NeuroBox.apk or any .apk
                    var apkDownloadUrl = releaseHtmlUrl
                    val assets = json.optJSONArray("assets")
                    if (assets != null && assets.length() > 0) {
                        for (i in 0 until assets.length()) {
                            val asset = assets.getJSONObject(i)
                            val assetName = asset.optString("name", "")
                            if (assetName.endsWith(".apk", ignoreCase = true)) {
                                apkDownloadUrl = asset.optString("browser_download_url", apkDownloadUrl)
                                break
                            }
                        }
                    }

                    val cleanCurrent = currentVersion.replace("v", "").trim()
                    val hasUpdate = isNewerVersion(tagName, cleanCurrent)

                    UpdateCheckResult(
                        checked = true,
                        hasUpdate = hasUpdate,
                        latestVersion = tagName.ifEmpty { "v1.1.0" },
                        currentVersion = cleanCurrent,
                        releaseName = releaseName,
                        releaseNotes = releaseBody,
                        downloadUrl = apkDownloadUrl,
                        releaseHtmlUrl = releaseHtmlUrl,
                        errorMessage = null
                    )
                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    UpdateCheckResult(
                        checked = true,
                        hasUpdate = false,
                        latestVersion = currentVersion,
                        currentVersion = currentVersion,
                        errorMessage = "No releases published yet on repository."
                    )
                } else {
                    UpdateCheckResult(
                        checked = true,
                        hasUpdate = false,
                        latestVersion = currentVersion,
                        currentVersion = currentVersion,
                        errorMessage = "Server response: HTTP $responseCode"
                    )
                }
            } catch (e: Exception) {
                UpdateCheckResult(
                    checked = true,
                    hasUpdate = false,
                    latestVersion = currentVersion,
                    currentVersion = currentVersion,
                    errorMessage = e.localizedMessage ?: "Network connection error"
                )
            }
        }
    }

    private fun isNewerVersion(latest: String, current: String): Boolean {
        if (latest.isEmpty() || current.isEmpty()) return false
        val latestParts = latest.split(".").mapNotNull { it.filter { ch -> ch.isDigit() }.toIntOrNull() }
        val currentParts = current.split(".").mapNotNull { it.filter { ch -> ch.isDigit() }.toIntOrNull() }
        val maxLen = maxOf(latestParts.size, currentParts.size)
        for (i in 0 until maxLen) {
            val l = latestParts.getOrElse(i) { 0 }
            val c = currentParts.getOrElse(i) { 0 }
            if (l > c) return true
            if (l < c) return false
        }
        return false
    }
}
