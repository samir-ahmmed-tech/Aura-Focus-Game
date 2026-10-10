package com.example.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.SoundEffectConstants
import android.view.View

/**
 * Reliable acoustic tap sound & haptics manager.
 * Uses hardware ToneGenerator on STREAM_MUSIC to ensure crisp audible feedback
 * regardless of whether the user's OS has disabled system touch sounds.
 */
object AuraSoundManager {
    private var toneGenerator: ToneGenerator? = null

    init {
        initTone()
    }

    private fun initTone() {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        } catch (_: Exception) {
            try {
                toneGenerator = ToneGenerator(AudioManager.STREAM_SYSTEM, 100)
            } catch (_: Exception) {
                try {
                    toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
                } catch (_: Exception) {}
            }
        }
    }

    /**
     * Plays a crisp, satisfying mechanical click sound on tap.
     */
    fun playTapSound(context: Context?, view: View? = null) {
        val ctx = context ?: view?.context

        // 1. Play Android AudioManager system click effect
        try {
            val audioManager = ctx?.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
            audioManager?.playSoundEffect(AudioManager.FX_KEY_CLICK, 1.0f)
        } catch (_: Exception) {}

        // 2. Play view sound effect with soundEffectsEnabled forced to true
        try {
            if (view != null) {
                view.isSoundEffectsEnabled = true
                view.playSoundEffect(SoundEffectConstants.CLICK)
            }
        } catch (_: Exception) {}

        // 3. Play tone generator beep on STREAM_MUSIC
        try {
            if (toneGenerator == null) initTone()
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 30)
        } catch (_: Exception) {
            initTone()
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 30)
            } catch (_: Exception) {}
        }

        // 4. Subtle tactile haptic pulse
        performHaptic(ctx, 10)
    }

    /**
     * Plays a success chime when achieving a goal or high score.
     */
    fun playSuccessSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 70)
        } catch (_: Exception) {}
    }

    /**
     * Plays a pleasant drop sound when harvesting fruits or gaining rewards.
     */
    fun playHarvestSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 60)
        } catch (_: Exception) {}
    }

    /**
     * Optional subtle tactile pulse.
     */
    fun performHaptic(context: Context?, durationMs: Long = 15) {
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
