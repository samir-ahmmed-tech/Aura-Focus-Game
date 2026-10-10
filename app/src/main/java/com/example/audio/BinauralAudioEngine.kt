package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

enum class BrainwaveType(
    val title: String,
    val beatFrequency: Float,
    val carrierFreq: Float,
    val description: String,
    val category: String,
    val stateLabel: String
) {
    LONE_WOLF("Lone Wolf (40 Hz)", 40.0f, 216.0f, "Hyper-focus, solitary deep execution & peak neural synchronization", "Brain Boost", "Hyper-Focus"),
    GAMMA_PEAK("Gamma Cognition (40 Hz)", 40.0f, 240.0f, "Peak memory synthesis, rapid problem-solving & mental agility", "Brain Boost", "Peak Cognition"),
    BETA_ACTIVE("Beta Alert (20 Hz)", 20.0f, 250.0f, "High alertness, active coding & analytical calculations", "Focus Boost", "Active Alert"),
    SMR_CALM("SMR Rhythm (14 Hz)", 14.0f, 220.0f, "Sensorimotor rhythm for body stillness & relaxed physical control", "Focus Boost", "Calm Precision"),
    ALPHA_FLOW("Alpha Flow (10 Hz)", 10.0f, 216.0f, "Accelerated learning, creative flow state & stress dissolution", "Flow State", "Creative Flow"),
    SCHUMANN("Schumann Resonance (7.83 Hz)", 7.83f, 208.0f, "Earth's natural frequency for deep grounding & equilibrium", "Healing", "Earth Grounding"),
    THETA_MEDITATION("Theta Intuition (6 Hz)", 6.0f, 200.0f, "Deep subconscious programming, intuition & lucid meditation", "Meditation", "Deep Trance"),
    DELTA_REST("Delta Deep Sleep (2 Hz)", 2.0f, 180.0f, "Cellular restoration, deep mental rest & physical healing", "Healing", "Restoration"),
    SOLFEGGIO_528("528 Hz Miracle Tone", 0.0f, 528.0f, "Ancient Solfeggio frequency for cellular transformation & peace", "Solfeggio", "Miracle Tone"),
    SOLFEGGIO_432("432 Hz Healing Pitch", 0.0f, 432.0f, "Universal natural harmonic pitch for nervous system regulation", "Solfeggio", "Natural Harmony"),
    SOLFEGGIO_396("396 Hz Fear Release", 0.0f, 396.0f, "Dissolves subconscious dread, guilt & mental friction", "Solfeggio", "Anxiety Relief"),
    SOLFEGGIO_639("639 Hz Connection", 0.0f, 639.0f, "Enhances empathy, emotional healing & heart coherence", "Solfeggio", "Heart Coherence"),
    SOLFEGGIO_963("963 Hz Crown Pure", 0.0f, 963.0f, "Highest spiritual clarity, transcendental meditation & mental glow", "Solfeggio", "Higher Mind")
}

enum class SoundscapeType(val label: String, val iconEmoji: String, val desc: String) {
    NONE("Pure Tone", "🎵", "Isolated harmonic wave without noise"),
    RAIN("Gentle Rainfall", "🌧️", "Continuous soft raindrops on foliage"),
    THUNDERSTORM("Heavy Thunderstorm", "⛈️", "Distant thunder rolling with dense rainfall"),
    OCEAN_WAVES("Deep Ocean Surf", "🌊", "Rhythmic Pacific tide rolling against shore"),
    FOREST_WIND("Pine Forest Wind", "🌲", "Subtle wind blowing through forest canopies"),
    FIREPLACE("Cozy Campfire", "🔥", "Warm glowing logs with gentle embers crackling"),
    BROWN_NOISE("Deep Brown Noise", "🟤", "Deep low-frequency rumble proven for ADHD focus"),
    PINK_NOISE("Balanced Pink Noise", "🌸", "Natural 1/f soothing frequency curve"),
    WHITE_NOISE("Pure White Noise", "⚪", "Total sound masking for loud work environments"),
    SINGING_BOWL("Tibetan Bell Chime", "🧘", "Subtle resonant harmonic drone")
}

class BinauralAudioEngine {
    private val sampleRate = 44100
    private var audioTrack: AudioTrack? = null
    private var audioJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    @Volatile
    var isPlaying: Boolean = false
        private set

    @Volatile
    var activeBrainwave: BrainwaveType = BrainwaveType.LONE_WOLF

    @Volatile
    var activeSoundscape: SoundscapeType = SoundscapeType.RAIN

    @Volatile
    var binauralVolume: Float = 0.65f

    @Volatile
    var ambientVolume: Float = 0.50f

    @Volatile
    var masterVolume: Float = 0.85f

    fun start() {
        if (isPlaying) return
        isPlaying = true

        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val bufferSize = maxOf(minBufferSize, 4096)

        try {
            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack?.play()
        } catch (e: Exception) {
            e.printStackTrace()
            isPlaying = false
            return
        }

        audioJob = scope.launch {
            val chunkFrames = 1024
            val buffer = ShortArray(chunkFrames * 2)
            var phaseLeft = 0.0
            var phaseRight = 0.0
            var noiseFilterState = 0.0f
            var brownFilterState = 0.0f
            var lfoPhase = 0.0
            var oceanLfoPhase = 0.0
            var crackleCounter = 0

            while (isActive && isPlaying) {
                val carrier = activeBrainwave.carrierFreq
                val beat = activeBrainwave.beatFrequency
                val fLeft = carrier
                val fRight = if (beat > 0.0f) carrier + beat else carrier

                val twoPi = 2.0 * PI
                val stepLeft = (twoPi * fLeft) / sampleRate
                val stepRight = (twoPi * fRight) / sampleRate

                val curBinauralVol = binauralVolume * masterVolume
                val curAmbientVol = ambientVolume * masterVolume

                for (i in 0 until chunkFrames) {
                    val sampleSineL = sin(phaseLeft).toFloat()
                    val sampleSineR = sin(phaseRight).toFloat()

                    phaseLeft += stepLeft
                    if (phaseLeft >= twoPi) phaseLeft -= twoPi

                    phaseRight += stepRight
                    if (phaseRight >= twoPi) phaseRight -= twoPi

                    var ambientSampleL = 0.0f
                    var ambientSampleR = 0.0f
                    val white = (Random.nextFloat() * 2.0f - 1.0f)

                    when (activeSoundscape) {
                        SoundscapeType.NONE -> { }
                        SoundscapeType.RAIN -> {
                            noiseFilterState = 0.94f * noiseFilterState + 0.06f * white
                            val dropPulse = if (Random.nextFloat() < 0.003f) 0.5f else 0.0f
                            val rainSample = (noiseFilterState * 0.75f + dropPulse * 0.25f)
                            ambientSampleL = rainSample
                            ambientSampleR = rainSample * (0.9f + Random.nextFloat() * 0.2f)
                        }
                        SoundscapeType.THUNDERSTORM -> {
                            noiseFilterState = 0.92f * noiseFilterState + 0.08f * white
                            brownFilterState = (brownFilterState + (0.02f * white)) / 1.02f
                            ambientSampleL = noiseFilterState * 0.6f + brownFilterState * 0.4f
                            ambientSampleR = noiseFilterState * 0.6f + brownFilterState * 0.45f
                        }
                        SoundscapeType.OCEAN_WAVES -> {
                            brownFilterState = (brownFilterState + (0.04f * white)) / 1.04f
                            oceanLfoPhase += (twoPi * 0.12) / sampleRate
                            if (oceanLfoPhase >= twoPi) oceanLfoPhase -= twoPi
                            val surf = (sin(oceanLfoPhase).toFloat() * 0.45f + 0.55f)
                            ambientSampleL = brownFilterState * surf
                            ambientSampleR = brownFilterState * (1.1f - surf * 0.2f)
                        }
                        SoundscapeType.FOREST_WIND -> {
                            noiseFilterState = 0.97f * noiseFilterState + 0.03f * white
                            lfoPhase += (twoPi * 0.20) / sampleRate
                            if (lfoPhase >= twoPi) lfoPhase -= twoPi
                            val windMod = (sin(lfoPhase).toFloat() * 0.35f + 0.65f)
                            ambientSampleL = noiseFilterState * windMod
                            ambientSampleR = noiseFilterState * (1.3f - windMod)
                        }
                        SoundscapeType.FIREPLACE -> {
                            noiseFilterState = 0.95f * noiseFilterState + 0.05f * white
                            crackleCounter++
                            var crackle = 0.0f
                            if (crackleCounter > 1200 && Random.nextFloat() < 0.02f) {
                                crackle = (Random.nextFloat() * 1.5f - 0.75f)
                                crackleCounter = 0
                            }
                            ambientSampleL = noiseFilterState * 0.6f + crackle
                            ambientSampleR = noiseFilterState * 0.6f + (crackle * 0.8f)
                        }
                        SoundscapeType.BROWN_NOISE -> {
                            brownFilterState = (brownFilterState + (0.03f * white)) / 1.03f
                            ambientSampleL = brownFilterState * 2.2f
                            ambientSampleR = brownFilterState * 2.2f
                        }
                        SoundscapeType.PINK_NOISE -> {
                            noiseFilterState = 0.90f * noiseFilterState + 0.10f * white
                            ambientSampleL = noiseFilterState * 1.5f
                            ambientSampleR = noiseFilterState * 1.5f
                        }
                        SoundscapeType.WHITE_NOISE -> {
                            ambientSampleL = white * 0.5f
                            ambientSampleR = white * 0.5f
                        }
                        SoundscapeType.SINGING_BOWL -> {
                            noiseFilterState = 0.98f * noiseFilterState + 0.02f * white
                            val harmonic = (sin(phaseLeft * 2.5).toFloat() * 0.25f)
                            ambientSampleL = noiseFilterState * 0.4f + harmonic
                            ambientSampleR = noiseFilterState * 0.4f + harmonic
                        }
                    }

                    val outL = (sampleSineL * curBinauralVol + ambientSampleL * curAmbientVol).coerceIn(-1.0f, 1.0f)
                    val outR = (sampleSineR * curBinauralVol + ambientSampleR * curAmbientVol).coerceIn(-1.0f, 1.0f)

                    buffer[i * 2] = (outL * 32767.0f).toInt().toShort()
                    buffer[i * 2 + 1] = (outR * 32767.0f).toInt().toShort()
                }

                audioTrack?.write(buffer, 0, buffer.size)
            }
        }
    }

    fun stop() {
        isPlaying = false
        audioJob?.cancel()
        audioJob = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        audioTrack = null
    }
}
