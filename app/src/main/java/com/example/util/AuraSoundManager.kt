package com.example.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.SoundEffectConstants
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * High-performance, non-blocking acoustic tap sound & haptics manager for NeuroBox.
 * Executes on background dispatchers to guarantee zero UI thread lag or micro-freezes.
 */
object AuraSoundManager {
    private var toneGenerator: ToneGenerator? = null
    private val soundScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var lastTapTime = 0L

    init {
        soundScope.launch {
            initTone()
        }
    }

    private fun initTone() {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
        } catch (_: Exception) {
            try {
                toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, 85)
            } catch (_: Exception) {
                try {
                    toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 85)
                } catch (_: Exception) {}
            }
        }
    }

    /**
     * Non-blocking tap feedback: fires asynchronously so UI thread stays 100% fluid.
     */
    fun playTapSound(context: Context?, view: View? = null) {
        val now = System.currentTimeMillis()
        if (now - lastTapTime < 80L) return // Rate-limit rapid successive clicks
        lastTapTime = now

        val ctx = context ?: view?.context

        // Lightweight instant view click / haptic if available
        try {
            view?.playSoundEffect(SoundEffectConstants.CLICK)
        } catch (_: Exception) {}

        soundScope.launch {
            performHaptic(ctx, 6)
        }
    }

    /**
     * Plays a success chime when achieving a goal or high score.
     */
    fun playSuccessSound(context: Context? = null) {
        soundScope.launch {
            try {
                if (toneGenerator == null) initTone()
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 65)
            } catch (_: Exception) {}
        }
    }

    /**
     * Plays an error alert when clicking incorrect option.
     */
    fun playErrorSound(context: Context? = null) {
        soundScope.launch {
            try {
                if (toneGenerator == null) initTone()
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 60)
            } catch (_: Exception) {}
        }
    }

    /**
     * Plays a pleasant drop sound when harvesting fruits or gaining rewards.
     */
    fun playHarvestSound() {
        soundScope.launch {
            try {
                if (toneGenerator == null) initTone()
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 55)
            } catch (_: Exception) {}
        }
    }

    /**
     * Subtle tactile pulse executed non-blocking.
     */
    fun performHaptic(context: Context?, durationMs: Long = 10) {
        try {
            if (context == null) return
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(durationMs)
                }
            }
        } catch (_: Exception) {}
    }
}
