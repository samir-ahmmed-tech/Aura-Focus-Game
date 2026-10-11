package com.example.util

import android.os.SystemClock
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * Click debounce & rate-limiting utility.
 * Item 14 Checklist: Spam & accidental double-tap protection.
 * Prevents rapid repeated triggering of actions, audio buffers, or heavy state allocations.
 */
object DebounceHelper {
    private var lastClickTime = 0L
    private const val DEFAULT_DEBOUNCE_MS = 280L

    fun canClick(debounceMs: Long = DEFAULT_DEBOUNCE_MS): Boolean {
        val current = SystemClock.uptimeMillis()
        if (current - lastClickTime < debounceMs) {
            return false
        }
        lastClickTime = current
        return true
    }
}

/**
 * Compose modifier extension for debounced clickable events.
 */
fun Modifier.debouncedClickable(
    debounceMs: Long = 280L,
    indication: androidx.compose.foundation.Indication? = null,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    this.clickable(
        interactionSource = interactionSource,
        indication = indication,
        onClick = {
            if (DebounceHelper.canClick(debounceMs)) {
                onClick()
            }
        }
    )
}
