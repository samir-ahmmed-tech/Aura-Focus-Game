package com.example.util

/**
 * Universal Form Validation & Input Sanitization Suite.
 * Item 13 Checklist: Form validation across all user inputs.
 */
object FormValidationHelper {

    data class ValidationResult(
        val isValid: Boolean,
        val sanitizedValue: String,
        val errorMessage: String? = null
    )

    /**
     * Validates user profile display names.
     */
    fun validateProfileName(name: String): ValidationResult {
        val trimmed = name.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "", "Name cannot be empty")
            trimmed.length < 2 -> ValidationResult(false, trimmed, "Name must be at least 2 characters")
            trimmed.length > 30 -> ValidationResult(false, trimmed.take(30), "Name cannot exceed 30 characters")
            else -> ValidationResult(true, trimmed)
        }
    }

    /**
     * Validates user profile bio descriptions.
     */
    fun validateProfileBio(bio: String): ValidationResult {
        val trimmed = bio.trim()
        return when {
            trimmed.length > 120 -> ValidationResult(false, trimmed.take(120), "Bio cannot exceed 120 characters")
            else -> ValidationResult(true, trimmed)
        }
    }

    /**
     * Validates custom focus duration inputs (in minutes).
     */
    fun validateFocusMinutes(input: String): ValidationResult {
        val trimmed = input.trim()
        val parsed = trimmed.toIntOrNull()
        return when {
            parsed == null -> ValidationResult(false, "25", "Enter a valid positive number")
            parsed < 1 -> ValidationResult(false, "1", "Minimum focus duration is 1 minute")
            parsed > 480 -> ValidationResult(false, "480", "Maximum focus duration is 480 minutes (8 hours)")
            else -> ValidationResult(true, parsed.toString())
        }
    }

    /**
     * Validates note title and task items.
     */
    fun validateTaskContent(text: String): ValidationResult {
        val trimmed = text.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult(false, "", "Input cannot be empty")
            trimmed.length > 200 -> ValidationResult(false, trimmed.take(200), "Task text limited to 200 characters")
            else -> ValidationResult(true, trimmed)
        }
    }

    /**
     * Sanitizes general freeform thoughts & distraction vault entries.
     */
    fun sanitizeJournalEntry(text: String): String {
        return text.trim().take(5000)
    }
}
