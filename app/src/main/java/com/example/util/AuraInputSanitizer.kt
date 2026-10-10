package com.example.util

/**
 * High-Security Input Sanitization & Anti-Injection Engine for AuraFocus.
 * Protects local storage, Room/SQLite database, and potential sync servers from:
 * - HTML / Script injection (XSS)
 * - SQL / Query parameter injection
 * - Buffer overflow & memory exhaustion from excessive payload strings
 * - Malformed character exploits
 */
object AuraInputSanitizer {

    private const val MAX_TITLE_LENGTH = 120
    private const val MAX_CONTENT_LENGTH = 5000
    private const val MAX_TAG_LENGTH = 40

    /**
     * Sanitizes user titles (Habit names, Note titles, Kanban task headings).
     */
    fun sanitizeTitle(input: String): String {
        return sanitizeString(input, MAX_TITLE_LENGTH)
    }

    /**
     * Sanitizes multiline user content (Journal reflections, Notes body, Stoic wisdom).
     */
    fun sanitizeContent(input: String): String {
        return sanitizeString(input, MAX_CONTENT_LENGTH)
    }

    /**
     * Sanitizes user tags and categories.
     */
    fun sanitizeTag(input: String): String {
        return sanitizeString(input, MAX_TAG_LENGTH)
    }

    private fun sanitizeString(input: String, maxLength: Int): String {
        if (input.isBlank()) return ""

        return input
            .trim()
            // Strip harmful HTML/XML script tags and injection vectors
            .replace(Regex("<.*?>"), "")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
            .replace("&", "&amp;")
            .take(maxLength)
    }

    /**
     * Rate limiter / debounce tracker to prevent UI spam attacks and memory thrashing.
     */
    private val lastActionTimestamps = mutableMapOf<String, Long>()

    fun isActionAllowed(actionKey: String, debounceMs: Long = 300L): Boolean {
        val now = System.currentTimeMillis()
        val lastTime = lastActionTimestamps[actionKey] ?: 0L
        if (now - lastTime >= debounceMs) {
            lastActionTimestamps[actionKey] = now
            return true
        }
        return false
    }
}
