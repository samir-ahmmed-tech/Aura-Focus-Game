package com.example.util

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 100% Offline, Privacy-Preserving Local Analytics Engine.
 * Item 15 Checklist: Set up analytics without compromising user data sovereignty.
 * Zero external network calls. Zero third-party trackers. All stats stay strictly on device.
 */
data class LocalAnalyticsSummary(
    val totalFocusMinutes: Long = 0,
    val completedSessions: Int = 0,
    val totalGamesPlayed: Int = 0,
    val totalToolsUsed: Int = 0,
    val streakDays: Int = 1,
    val favoriteGameId: String = "Mental Math",
    val favoriteToolId: String = "Pomodoro Architect"
)

object AuraLocalAnalytics {
    private const val PREFS_NAME = "neurobox_offline_analytics"
    private const val KEY_TOTAL_MINUTES = "total_focus_minutes"
    private const val KEY_SESSIONS = "completed_sessions"
    private const val KEY_GAMES_COUNT = "total_games_played"
    private const val KEY_TOOLS_COUNT = "total_tools_used"
    private const val KEY_STREAK = "streak_days"

    private var prefs: SharedPreferences? = null
    private val _analyticsState = MutableStateFlow(LocalAnalyticsSummary())
    val analyticsState: StateFlow<LocalAnalyticsSummary> = _analyticsState.asStateFlow()

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            loadState()
        }
    }

    private fun loadState() {
        val p = prefs ?: return
        _analyticsState.value = LocalAnalyticsSummary(
            totalFocusMinutes = p.getLong(KEY_TOTAL_MINUTES, 0L),
            completedSessions = p.getInt(KEY_SESSIONS, 0),
            totalGamesPlayed = p.getInt(KEY_GAMES_COUNT, 0),
            totalToolsUsed = p.getInt(KEY_TOOLS_COUNT, 0),
            streakDays = p.getInt(KEY_STREAK, 1)
        )
    }

    fun recordCompletedSession(minutes: Int) {
        val p = prefs ?: return
        val newMin = p.getLong(KEY_TOTAL_MINUTES, 0L) + minutes
        val newSessions = p.getInt(KEY_SESSIONS, 0) + 1
        p.edit()
            .putLong(KEY_TOTAL_MINUTES, newMin)
            .putInt(KEY_SESSIONS, newSessions)
            .apply()
        loadState()
    }

    fun recordGamePlayed(gameId: String) {
        val p = prefs ?: return
        val count = p.getInt(KEY_GAMES_COUNT, 0) + 1
        p.edit().putInt(KEY_GAMES_COUNT, count).apply()
        loadState()
    }

    fun recordToolUsed(toolId: String) {
        val p = prefs ?: return
        val count = p.getInt(KEY_TOOLS_COUNT, 0) + 1
        p.edit().putInt(KEY_TOOLS_COUNT, count).apply()
        loadState()
    }

    fun clearAllAnalytics() {
        prefs?.edit()?.clear()?.apply()
        loadState()
    }
}
