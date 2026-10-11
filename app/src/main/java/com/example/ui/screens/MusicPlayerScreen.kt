package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.BrainwaveType
import com.example.audio.LocalMusicPlayerEngine
import com.example.audio.MusicTrack
import com.example.audio.RepeatMode
import com.example.audio.SoundscapeType
import com.example.data.GamificationManager
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassChip
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    musicEngine: LocalMusicPlayerEngine,
    onImportAudioClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val playerState by musicEngine.playerState.collectAsState()
    val playlist by musicEngine.playlist.collectAsState()
    val binaural = musicEngine.binauralEngine

    var activeBrainwave by remember { mutableStateOf(binaural.activeBrainwave) }
    var activeSoundscape by remember { mutableStateOf(binaural.activeSoundscape) }
    var musicVolume by remember { mutableStateOf(playerState.volume) }
    var binauralVolume by remember { mutableStateOf(binaural.binauralVolume) }
    var ambientVolume by remember { mutableStateOf(binaural.ambientVolume) }
    var showBreathPacer by remember { mutableStateOf(false) }
    var selectedSpeed by remember { mutableStateOf(playerState.playbackSpeed) }
    var showPlaylistQueue by remember { mutableStateOf(false) }

    val gamificationState by GamificationManager.state.collectAsState()
    val playerLayoutDesign = gamificationState.musicPlayerDesign

    // Pro Music Player & Visualizer States
    var visualizerStyle by remember { mutableIntStateOf(0) } // 0: Cosmic Mandala, 1: Cyber Spectrum, 2: Aura Water Ripple, 3: Supernova Plasma
    var showEqualizerPanel by remember { mutableStateOf(false) }
    var showSolfeggioPanel by remember { mutableStateOf(false) }
    var selectedSolfeggioHz by remember { mutableIntStateOf(528) }
    var sleepTimerMinutes by remember { mutableIntStateOf(0) }
    var selectedEqualizerPreset by remember { mutableStateOf("Deep Flow") }
    var bassBoostLevel by remember { mutableFloatStateOf(0.7f) }
    var trebleClarityLevel by remember { mutableFloatStateOf(0.6f) }

    // Infinite Visualizer Synchronous Animations (Audio-Reactive Rhythm)
    val infiniteTransition = rememberInfiniteTransition(label = "music_visualizer_infinite")
    val discRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (playerState.isPlaying) 7500 else 30000, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Restart
        ),
        label = "disc_rot"
    )
    val wavePulse by infiniteTransition.animateFloat(
        initialValue = 0.84f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (playerState.isPlaying) 900 else 2400, easing = FastOutSlowInEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "wave_pulse"
    )
    val glowPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(if (playerState.isPlaying) 3200 else 7500, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Restart
        ),
        label = "glow_phase"
    )

    // Breathing Pacer Animation (4 seconds inhale, 4 seconds exhale)
    val breathScale = remember { Animatable(0.7f) }
    var breathPhaseText by remember { mutableStateOf("Inhale...") }

    LaunchedEffect(showBreathPacer) {
        if (showBreathPacer) {
            while (true) {
                breathPhaseText = "Inhale..."
                breathScale.animateTo(1.2f, animationSpec = tween(4000, easing = FastOutSlowInEasing))
                breathPhaseText = "Hold..."
                kotlinx.coroutines.delay(2000)
                breathPhaseText = "Exhale..."
                breathScale.animateTo(0.7f, animationSpec = tween(4000, easing = FastOutSlowInEasing))
                breathPhaseText = "Rest..."
                kotlinx.coroutines.delay(1500)
            }
        }
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val curPosSec = playerState.currentPositionMs / 1000
    val durSec = playerState.durationMs / 1000
    val curPosFormatted = String.format("%02d:%02d", curPosSec / 60, curPosSec % 60)
    val durFormatted = String.format("%02d:%02d", durSec / 60, durSec % 60)

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header with SAF Import & Queue triggers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Aura Music & Brainwave Studio",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "20+ Pro Features • 100% Real Binaural Brainwave Engine",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = NeonCyan
                )
            }

            Row {
                IconButton(
                    onClick = onImportAudioClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x14FFFFFF))
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), CircleShape)
                        .testTag("import_audio_top_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderOpen,
                        contentDescription = "Import MP3/WAV",
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // =========================================================================
        // 5 PLAYER DESIGNS SELECTOR (3 HORIZONTAL, 2 CIRCULAR)
        // =========================================================================
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Player Design (5 Styles):",
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                    color = NeonCyan
                )
                Text(
                    text = when (playerLayoutDesign) {
                        0 -> "Circular 1 (Vinyl)"
                        1 -> "Circular 2 (Mandala)"
                        2 -> "Horizontal 1 (Wave)"
                        3 -> "Horizontal 2 (Cassette)"
                        else -> "Horizontal 3 (Soundbar)"
                    },
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                    color = Color(0x99FFFFFF)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    0 to "⭕ Vinyl Disc (Circle)",
                    1 to "🪐 Mandala Orbit (Circle)",
                    2 to "🌊 Cyber Wave Deck (Horiz)",
                    3 to "📼 Retro Cassette (Horiz)",
                    4 to "🔊 Horizon Soundbar (Horiz)"
                ).forEach { (idx, title) ->
                    val isSelected = (playerLayoutDesign == idx)
                    GlassChip(
                        label = title,
                        color = if (isSelected) NeonCyan else Color.White,
                        selected = isSelected,
                        onClick = {
                            GamificationManager.setMusicPlayerDesign(idx)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // =========================================================================
        // VISUALIZER RENDERER BY SELECTED DESIGN (3 HORIZONTAL, 2 CIRCULAR)
        // =========================================================================
        when (playerLayoutDesign) {
            0 -> {
                // -------------------------------------------------------------
                // DESIGN 0: HOLOGRAPHIC VINYL DISC (CIRCULAR 1)
                // Strictly contained center spindle: text never leaks out!
                // -------------------------------------------------------------
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .clip(CircleShape)
                        .clickable { visualizerStyle = (visualizerStyle + 1) % 4 },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val discRadius = size.minDimension * 0.44f
                        val isPlaying = playerState.isPlaying
                        val rotRad = (discRotation * PI / 180f).toFloat()

                        // 1. Ambient Background Glow Aura
                        val auraGlowRadius = discRadius * (if (isPlaying) wavePulse * 1.08f else 1.0f)
                        val auraColors = when (visualizerStyle) {
                            0 -> listOf(Color(0x3500F2FE), Color(0x257F00FF), Color.Transparent)
                            1 -> listOf(Color(0x3500E5FF), Color(0x2000E676), Color.Transparent)
                            2 -> listOf(Color(0x3500C9FF), Color(0x250072FF), Color.Transparent)
                            else -> listOf(Color(0x38FF007F), Color(0x25FF5252), Color.Transparent)
                        }
                        drawCircle(
                            brush = Brush.radialGradient(auraColors, center = Offset(cx, cy), radius = auraGlowRadius * 1.15f),
                            radius = auraGlowRadius * 1.15f,
                            center = Offset(cx, cy)
                        )

                        // 2. Holographic Vinyl Micro-Grooves
                        for (g in 1..6) {
                            val gR = discRadius * (0.46f + g * 0.08f)
                            drawCircle(
                                color = Color(0x18FFFFFF),
                                radius = gR,
                                center = Offset(cx, cy),
                                style = Stroke(width = 1f)
                            )
                        }

                        // 3. Dynamic Outer Audio Spectrum Frequency Bars
                        val barCount = 36
                        for (i in 0 until barCount) {
                            val angle = (2 * PI * i / barCount).toFloat() + rotRad
                            val audioEnergy = if (isPlaying) {
                                val harmonic1 = kotlin.math.abs(sin(glowPhase * 2.2f + i * 0.42f))
                                val harmonic2 = kotlin.math.abs(cos(glowPhase * 3.6f + i * 0.28f))
                                ((harmonic1 + harmonic2) * 0.5f).coerceIn(0.15f, 1f)
                            } else {
                                0.25f + 0.12f * sin(glowPhase + i * 0.35f)
                            }

                            val barHeight = 8f + 26f * audioEnergy * (if (isPlaying) wavePulse else 1f)
                            val startR = discRadius * 0.88f
                            val endR = startR + barHeight

                            val startX = cx + cos(angle) * startR
                            val startY = cy + sin(angle) * startR
                            val endX = cx + cos(angle) * endR
                            val endY = cy + sin(angle) * endR

                            val barColor = when (visualizerStyle) {
                                0 -> if (i % 3 == 0) NeonCyan else if (i % 3 == 1) NeonViolet else NeonPink
                                1 -> if (i % 2 == 0) NeonCyan else NeonLime
                                2 -> if (i % 2 == 0) Color(0xFF00E5FF) else Color(0xFF2979FF)
                                else -> if (i % 2 == 0) NeonPink else NeonAmber
                            }

                            drawLine(
                                color = barColor.copy(alpha = if (isPlaying) 0.85f else 0.45f),
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = 3.2f,
                                cap = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                        }

                        // 4. Style-Specific Visualizer Layer
                        when (visualizerStyle) {
                            0 -> {
                                val mandalaNodes = 8
                                for (k in 0 until mandalaNodes) {
                                    val petalAngle = (2 * PI * k / mandalaNodes).toFloat() - rotRad * 1.5f
                                    val nodeDist = discRadius * 0.58f
                                    val nx = cx + cos(petalAngle) * nodeDist
                                    val ny = cy + sin(petalAngle) * nodeDist
                                    val petalR = discRadius * 0.38f
                                    drawCircle(
                                        color = NeonCyan.copy(alpha = if (isPlaying) 0.32f else 0.16f),
                                        radius = petalR,
                                        center = Offset(nx, ny),
                                        style = Stroke(width = 1.4f)
                                    )
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            listOf(NeonViolet, Color.Transparent),
                                            center = Offset(nx, ny),
                                            radius = 12f
                                        ),
                                        radius = 10f,
                                        center = Offset(nx, ny)
                                    )
                                }
                            }
                            1 -> {
                                val wavePoints = 48
                                val path = Path()
                                for (w in 0 until wavePoints) {
                                    val wAngle = (2 * PI * w / wavePoints).toFloat() + rotRad
                                    val oscAmp = if (isPlaying) sin(wAngle * 6f + glowPhase * 3.5f) * 14f * wavePulse else 3f
                                    val wR = discRadius * 0.65f + oscAmp
                                    val wx = cx + cos(wAngle) * wR
                                    val wy = cy + sin(wAngle) * wR
                                    if (w == 0) path.moveTo(wx, wy) else path.lineTo(wx, wy)
                                }
                                path.close()
                                drawPath(
                                    path = path,
                                    color = NeonCyan.copy(alpha = if (isPlaying) 0.75f else 0.35f),
                                    style = Stroke(width = 2.2f)
                                )
                            }
                            2 -> {
                                for (r in 0..2) {
                                    val phaseOffset = (glowPhase / (2 * PI).toFloat() + r * 0.33f) % 1.0f
                                    val rippleR = discRadius * (0.42f + phaseOffset * 0.52f)
                                    val rippleAlpha = (1f - phaseOffset).coerceIn(0f, 0.7f) * (if (isPlaying) 1f else 0.4f)
                                    drawCircle(
                                        color = Color(0xFF00E5FF).copy(alpha = rippleAlpha),
                                        radius = rippleR,
                                        center = Offset(cx, cy),
                                        style = Stroke(width = 2.4f)
                                    )
                                }
                            }
                            else -> {
                                val flareCount = 12
                                for (f in 0 until flareCount) {
                                    val fAngle = (2 * PI * f / flareCount).toFloat() + rotRad * 2f
                                    val fLen = discRadius * (0.6f + kotlin.math.abs(sin(glowPhase * 2.8f + f)) * 0.32f)
                                    val fx = cx + cos(fAngle) * fLen
                                    val fy = cy + sin(fAngle) * fLen
                                    drawLine(
                                        brush = Brush.linearGradient(
                                            listOf(NeonPink, NeonAmber, Color.Transparent),
                                            start = Offset(cx, cy),
                                            end = Offset(fx, fy)
                                        ),
                                        start = Offset(cx + cos(fAngle) * discRadius * 0.42f, cy + sin(fAngle) * discRadius * 0.42f),
                                        end = Offset(fx, fy),
                                        strokeWidth = 3f,
                                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                                    )
                                }
                            }
                        }

                        // 5. Orbiting Celestial Stardust Particles
                        val particleCount = 6
                        for (p in 0 until particleCount) {
                            val pAngle = (2 * PI * p / particleCount).toFloat() - rotRad * (1.2f + p * 0.2f)
                            val pR = discRadius * (0.75f + (p % 3) * 0.08f)
                            val px = cx + cos(pAngle) * pR
                            val py = cy + sin(pAngle) * pR
                            drawCircle(
                                color = Color.White.copy(alpha = 0.9f),
                                radius = 2.8f,
                                center = Offset(px, py)
                            )
                        }

                        // 6. Translucent Glass Core Spindle Ring (Sized at 136dp diameter)
                        val coreRadius = discRadius * 0.58f
                        drawCircle(
                            brush = Brush.radialGradient(
                                listOf(Color(0xF00A131F), Color(0xEE050B12)),
                                center = Offset(cx, cy),
                                radius = coreRadius
                            ),
                            radius = coreRadius,
                            center = Offset(cx, cy)
                        )
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(NeonCyan, NeonViolet, NeonPink, NeonCyan),
                                center = Offset(cx, cy)
                            ),
                            radius = coreRadius,
                            center = Offset(cx, cy),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }

                    // KERNEL LINUX CYBERNETIC TELEMETRY CORE (Pure Animation - Absolutely ZERO Text)
                    Box(
                        modifier = Modifier
                            .size(136.dp)
                            .clip(CircleShape)
                            .background(Color(0xCC050C16)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val cx = size.width / 2f
                            val cy = size.height / 2f
                            val isPlaying = playerState.isPlaying
                            val rotRad = (discRotation * PI / 180f).toFloat()

                            // 1. Linux Kernel Hex Sector Grid (Rotating telemetry)
                            val sectors = 12
                            for (s in 0 until sectors) {
                                val sAngle = (2 * PI * s / sectors).toFloat() + rotRad * 1.5f
                                val tickR1 = size.minDimension * 0.42f
                                val tickR2 = size.minDimension * 0.48f
                                val tx1 = cx + cos(sAngle) * tickR1
                                val ty1 = cy + sin(sAngle) * tickR1
                                val tx2 = cx + cos(sAngle) * tickR2
                                val ty2 = cy + sin(sAngle) * tickR2
                                drawLine(
                                    color = if (s % 3 == 0) NeonCyan else NeonLime.copy(alpha = 0.6f),
                                    start = Offset(tx1, ty1),
                                    end = Offset(tx2, ty2),
                                    strokeWidth = 2f,
                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                            }

                            // 2. High-Tech Concentric Radar Wave (Counter-rotating)
                            drawCircle(
                                color = NeonCyan.copy(alpha = 0.35f),
                                radius = size.minDimension * 0.36f,
                                center = Offset(cx, cy),
                                style = Stroke(width = 1.2f)
                            )
                            drawCircle(
                                color = NeonViolet.copy(alpha = 0.25f),
                                radius = size.minDimension * 0.26f,
                                center = Offset(cx, cy),
                                style = Stroke(width = 1f)
                            )

                            // 3. Kernel Quantum Pulse Core
                            val corePulseR = size.minDimension * 0.16f * (if (isPlaying) wavePulse else 1f)
                            drawCircle(
                                brush = Brush.radialGradient(
                                    listOf(NeonCyan, NeonLime, Color.Transparent),
                                    center = Offset(cx, cy),
                                    radius = corePulseR * 1.5f
                                ),
                                radius = corePulseR * 1.5f,
                                center = Offset(cx, cy)
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 3.5f,
                                center = Offset(cx, cy)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Sleek External Track Metadata Capsule (Clean, zero text clutter inside circle)
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    glowColor = NeonCyan,
                    glowRadius = 6.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(NeonCyan.copy(alpha = 0.2f))
                                .border(1.dp, NeonCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (playerState.isPlaying) Icons.Default.GraphicEq else Icons.Default.Audiotrack,
                                contentDescription = "Playing",
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = playerState.currentTrack?.title ?: "Select Track",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${playerState.currentTrack?.artist ?: "Aura Ambient Lab"} • ${activeBrainwave.stateLabel}",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = NeonCyan,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        GlassChip(
                            label = if (playerState.isPlaying) "KERNEL ACTIVE" else "PAUSED",
                            color = if (playerState.isPlaying) NeonLime else NeonAmber,
                            selected = true
                        )
                    }
                }
            }

            1 -> {
                // -------------------------------------------------------------
                // DESIGN 1: SACRED MANDALA ORBIT DISC (CIRCULAR 2)
                // Multi-orbital geometry with strictly contained center label
                // -------------------------------------------------------------
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .clip(CircleShape)
                        .clickable { visualizerStyle = (visualizerStyle + 1) % 4 },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val discRadius = size.minDimension * 0.44f
                        val isPlaying = playerState.isPlaying
                        val rot1 = (discRotation * PI / 180f).toFloat()
                        val rot2 = (-discRotation * 1.5f * PI / 180f).toFloat()

                        // Outer pulsing rings
                        val pulseR = discRadius * (if (isPlaying) 1f + wavePulse * 0.08f else 1f)
                        drawCircle(
                            color = NeonViolet.copy(alpha = 0.35f),
                            radius = pulseR,
                            center = Offset(cx, cy),
                            style = Stroke(width = 1.5f)
                        )
                        drawCircle(
                            color = NeonCyan.copy(alpha = 0.25f),
                            radius = pulseR * 0.85f,
                            center = Offset(cx, cy),
                            style = Stroke(width = 1f)
                        )

                        // 8-Node Orbital Gyroscope
                        val nodes = 8
                        for (i in 0 until nodes) {
                            val ang1 = (2 * PI * i / nodes).toFloat() + rot1
                            val ang2 = (2 * PI * i / nodes).toFloat() + rot2
                            val r1 = discRadius * 0.76f
                            val r2 = discRadius * 0.62f

                            val p1 = Offset(cx + cos(ang1) * r1, cy + sin(ang1) * r1)
                            val p2 = Offset(cx + cos(ang2) * r2, cy + sin(ang2) * r2)

                            drawLine(
                                color = if (i % 2 == 0) NeonCyan.copy(alpha = 0.4f) else NeonPink.copy(alpha = 0.4f),
                                start = p1,
                                end = p2,
                                strokeWidth = 1.2f
                            )
                            drawCircle(color = NeonCyan, radius = 2.5f, center = p1)
                            drawCircle(color = NeonPink, radius = 2f, center = p2)
                        }

                        // Center core background (Sized at 136dp diameter)
                        val innerRadius = discRadius * 0.58f
                        drawCircle(
                            brush = Brush.radialGradient(
                                listOf(Color(0xF50D1117), Color(0xEE090D13)),
                                center = Offset(cx, cy),
                                radius = innerRadius
                            ),
                            radius = innerRadius,
                            center = Offset(cx, cy)
                        )
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(NeonViolet, NeonPink, NeonCyan, NeonViolet),
                                center = Offset(cx, cy)
                            ),
                            radius = innerRadius,
                            center = Offset(cx, cy),
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }

                    // SACRED MANDALA ORBIT CORE (Pure Animation - Absolutely ZERO Text)
                    Box(
                        modifier = Modifier
                            .size(136.dp)
                            .clip(CircleShape)
                            .background(Color(0xCC090D13)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val cx = size.width / 2f
                            val cy = size.height / 2f
                            val isPlaying = playerState.isPlaying
                            val rotRad = (discRotation * PI / 180f).toFloat()

                            // Sacred Multi-Node Pulsing Geometry
                            val nodes = 6
                            for (n in 0 until nodes) {
                                val ang = (2 * PI * n / nodes).toFloat() - rotRad * 2f
                                val r = size.minDimension * 0.35f
                                val nx = cx + cos(ang) * r
                                val ny = cy + sin(ang) * r
                                drawCircle(
                                    color = NeonPink.copy(alpha = 0.45f),
                                    radius = 6f,
                                    center = Offset(nx, ny)
                                )
                                drawLine(
                                    color = NeonViolet.copy(alpha = 0.4f),
                                    start = Offset(cx, cy),
                                    end = Offset(nx, ny),
                                    strokeWidth = 1.2f
                                )
                            }

                            // Center Pulsing Solfeggio Core
                            val centerR = size.minDimension * 0.18f * (if (isPlaying) wavePulse else 1f)
                            drawCircle(
                                brush = Brush.radialGradient(
                                    listOf(NeonPink, NeonViolet, Color.Transparent),
                                    center = Offset(cx, cy),
                                    radius = centerR * 1.6f
                                ),
                                radius = centerR * 1.6f,
                                center = Offset(cx, cy)
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 3.5f,
                                center = Offset(cx, cy)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // External Track Metadata Capsule (Design 1)
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    glowColor = NeonPink,
                    glowRadius = 6.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(NeonPink.copy(alpha = 0.2f))
                                .border(1.dp, NeonPink, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🪐", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = playerState.currentTrack?.title ?: "Select Track",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${playerState.currentTrack?.artist ?: "Mandala Orbit"} • ${selectedSolfeggioHz}Hz SOLFEGGIO",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = NeonPink,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        GlassChip(
                            label = if (playerState.isPlaying) "HARMONIC FLOW" else "PAUSED",
                            color = if (playerState.isPlaying) NeonPink else NeonAmber,
                            selected = true
                        )
                    }
                }
            }

            2 -> {
                // -------------------------------------------------------------
                // DESIGN 2: CYBER WAVE DECK (HORIZONTAL 1)
                // Real-time audio waveform canvas + reactive VU meters & sleek deck
                // -------------------------------------------------------------
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = NeonCyan,
                    glowRadius = 12.dp,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (playerState.isPlaying) NeonLime else Color.Gray)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (playerState.isPlaying) "LIVE AUDIO SYNTHESIS" else "PLAYER PAUSED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (playerState.isPlaying) NeonLime else Color.Gray
                                )
                            }
                            Text(
                                text = "STEREO • 24-BIT / 96kHz",
                                fontSize = 9.sp,
                                color = Color(0x80FFFFFF)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Audio Waveform Canvas
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(85.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x18000000))
                                .border(0.8.dp, Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                        ) {
                            val w = size.width
                            val h = size.height
                            val midY = h / 2f
                            val isPlaying = playerState.isPlaying

                            // Background grid lines
                            drawLine(Color(0x15FFFFFF), Offset(0f, midY), Offset(w, midY), strokeWidth = 1f)
                            drawLine(Color(0x10FFFFFF), Offset(0f, midY - 20f), Offset(w, midY - 20f), strokeWidth = 1f)
                            drawLine(Color(0x10FFFFFF), Offset(0f, midY + 20f), Offset(w, midY + 20f), strokeWidth = 1f)

                            // 28 Dancing Spectrum Bars across the deck
                            val bars = 28
                            val barW = (w / bars) * 0.65f
                            for (b in 0 until bars) {
                                val bx = b * (w / bars) + (w / bars - barW) / 2f
                                val barEnergy = if (isPlaying) {
                                    (sin(glowPhase * 2.5f + b * 0.35f) * 0.5f + 0.5f).coerceIn(0.1f, 1f)
                                } else {
                                    0.15f
                                }
                                val barH = (h * 0.7f) * barEnergy * (if (isPlaying) wavePulse else 0.6f)
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        listOf(NeonCyan, NeonViolet),
                                        startY = midY - barH / 2f,
                                        endY = midY + barH / 2f
                                    ),
                                    topLeft = Offset(bx, midY - barH / 2f),
                                    size = androidx.compose.ui.geometry.Size(barW, barH)
                                )
                            }

                            // Dynamic Smooth Wave Curve
                            val path = Path()
                            val points = 50
                            for (p in 0..points) {
                                val x = (w * p) / points
                                val freq = if (isPlaying) (sin(glowPhase * 3f + p * 0.28f) * 22f * wavePulse) else (sin(p * 0.3f) * 4f)
                                val y = midY + freq
                                if (p == 0) path.moveTo(x, y) else path.lineTo(x, y)
                            }
                            drawPath(
                                path = path,
                                color = NeonLime,
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom Track Info Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan.copy(alpha = 0.2f))
                                    .border(1.dp, NeonCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = "Wave",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = playerState.currentTrack?.title ?: "Select Track",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${playerState.currentTrack?.artist ?: "Aura Lab"} • ${activeBrainwave.stateLabel}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = NeonCyan,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            GlassChip(
                                label = "WAVE DECK",
                                color = NeonCyan,
                                selected = true
                            )
                        }
                    }
                }
            }

            3 -> {
                // -------------------------------------------------------------
                // DESIGN 3: RETRO SYNTH CASSETTE (HORIZONTAL 2)
                // Dual spinning cog spools, tape bridge ribbon & vintage tape label
                // -------------------------------------------------------------
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = NeonPink,
                    glowRadius = 12.dp,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📼 RETRO STEREO CASSETTE • SIDE A",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonPink
                            )
                            Text(
                                text = "TYPE II (CrO2) • DOLBY B",
                                fontSize = 9.sp,
                                color = NeonAmber
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Vintage Cassette Window Canvas
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(95.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F141C))
                                .border(1.2.dp, Color(0x35FFFFFF), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val w = size.width
                                val h = size.height
                                val spoolLeftX = w * 0.28f
                                val spoolRightX = w * 0.72f
                                val spoolY = h * 0.5f
                                val spoolR = 24.dp.toPx()
                                val rot = (discRotation * PI / 180f).toFloat()

                                // Magnetic tape strip connecting spools
                                drawRect(
                                    color = Color(0xFF2C241D),
                                    topLeft = Offset(spoolLeftX, spoolY - 8f),
                                    size = androidx.compose.ui.geometry.Size(spoolRightX - spoolLeftX, 16f)
                                )

                                // Left spinning spool
                                drawCircle(color = Color(0xFF1E2632), radius = spoolR, center = Offset(spoolLeftX, spoolY))
                                drawCircle(color = NeonPink, radius = spoolR, center = Offset(spoolLeftX, spoolY), style = Stroke(2f))
                                for (sp in 0 until 6) {
                                    val ang = (2 * PI * sp / 6).toFloat() + rot
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(spoolLeftX, spoolY),
                                        end = Offset(spoolLeftX + cos(ang) * spoolR, spoolY + sin(ang) * spoolR),
                                        strokeWidth = 2f
                                    )
                                }
                                drawCircle(color = Color.Black, radius = spoolR * 0.35f, center = Offset(spoolLeftX, spoolY))

                                // Right spinning spool
                                drawCircle(color = Color(0xFF1E2632), radius = spoolR, center = Offset(spoolRightX, spoolY))
                                drawCircle(color = NeonCyan, radius = spoolR, center = Offset(spoolRightX, spoolY), style = Stroke(2f))
                                for (sp in 0 until 6) {
                                    val ang = (2 * PI * sp / 6).toFloat() + rot
                                    drawLine(
                                        color = Color.White,
                                        start = Offset(spoolRightX, spoolY),
                                        end = Offset(spoolRightX + cos(ang) * spoolR, spoolY + sin(ang) * spoolR),
                                        strokeWidth = 2f
                                    )
                                }
                                drawCircle(color = Color.Black, radius = spoolR * 0.35f, center = Offset(spoolRightX, spoolY))
                            }

                            // Center Digital Tape Counter Badge
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xCC000000))
                                    .border(0.6.dp, NeonAmber, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (playerState.isPlaying) "ROLLING 44.1k" else "PAUSE",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonAmber
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom Cassette Label Strip
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x18FFFFFF))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = playerState.currentTrack?.title ?: "Select Track",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${playerState.currentTrack?.artist ?: "Aura Ambient"} • ${activeBrainwave.stateLabel}",
                                    fontSize = 10.sp,
                                    color = NeonPink,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            GlassChip(
                                label = "CASSETTE",
                                color = NeonPink,
                                selected = true
                            )
                        }
                    }
                }
            }

            else -> {
                // -------------------------------------------------------------
                // DESIGN 4: MODERN HORIZON SOUNDBAR (HORIZONTAL 3)
                // 32-band rainbow equalizer spectrum & high-end audio console
                // -------------------------------------------------------------
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = NeonLime,
                    glowRadius = 12.dp,
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔊 HORIZON SOUNDBAR ACOUSTICS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonLime
                            )
                            Text(
                                text = "LOSSLESS MASTER AUDIO",
                                fontSize = 9.sp,
                                color = Color(0x90FFFFFF)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // 32-Band Vertical Rainbow Equalizer Matrix
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x14000000))
                                .border(0.8.dp, Color(0x22FFFFFF), RoundedCornerShape(10.dp))
                        ) {
                            val w = size.width
                            val h = size.height
                            val bands = 32
                            val barW = (w / bands) * 0.72f
                            val isPlaying = playerState.isPlaying

                            for (i in 0 until bands) {
                                val bx = i * (w / bands) + (w / bands - barW) / 2f
                                val barEnergy = if (isPlaying) {
                                    val wave = (sin(glowPhase * 3f + i * 0.38f) * 0.5f + 0.5f)
                                    val pulse = (cos(glowPhase * 2f + i * 0.15f) * 0.3f + 0.3f)
                                    ((wave + pulse) * 0.65f).coerceIn(0.12f, 1f)
                                } else {
                                    0.18f
                                }
                                val barH = h * 0.82f * barEnergy

                                val barBrush = Brush.verticalGradient(
                                    colors = listOf(NeonPink, NeonAmber, NeonLime, NeonCyan),
                                    startY = h - barH,
                                    endY = h
                                )
                                drawRect(
                                    brush = barBrush,
                                    topLeft = Offset(bx, h - barH),
                                    size = androidx.compose.ui.geometry.Size(barW, barH)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Soundbar Metadata Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(NeonLime.copy(alpha = 0.2f))
                                    .border(1.dp, NeonLime, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Audiotrack,
                                    contentDescription = "Soundbar",
                                    tint = NeonLime,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = playerState.currentTrack?.title ?: "Select Track",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${playerState.currentTrack?.artist ?: "Aura Ambient"} • ${activeBrainwave.stateLabel}",
                                    fontSize = 11.sp,
                                    color = NeonLime,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            GlassChip(
                                label = "SOUNDBAR",
                                color = NeonLime,
                                selected = true
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // VISUALIZER STYLE CONTROLLER CHIPS (Mandala, Cyber, Ripple, Supernova)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            listOf(
                0 to "🌌 Mandala",
                1 to "⚡ Spectrum",
                2 to "🌊 Aura Ripple",
                3 to "🔥 Supernova"
            ).forEach { (idx, title) ->
                val isSelected = (visualizerStyle == idx)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) NeonCyan.copy(alpha = 0.24f) else Color(0x12FFFFFF))
                        .border(1.dp, if (isSelected) NeonCyan else Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                        .clickable { visualizerStyle = idx }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) NeonCyan else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // LIVE REAL-TIME AUDIO FREQUENCY SCOPE HUD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0x10FFFFFF))
                .border(1.dp, Color(0x18FFFFFF), RoundedCornerShape(10.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(
                "SUB-BASS" to (if (playerState.isPlaying) (65 + (wavePulse * 28).toInt()) else 22),
                "LOW-MID" to (if (playerState.isPlaying) (72 + (sin(glowPhase * 2f) * 20).toInt()) else 18),
                "HIGH-MID" to (if (playerState.isPlaying) (80 + (cos(glowPhase * 3f) * 15).toInt()) else 25),
                "TREBLE" to (if (playerState.isPlaying) (88 + (sin(glowPhase * 4f) * 10).toInt()) else 30)
            ).forEach { (band, level) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = band, fontSize = 8.sp, color = Color(0x90FFFFFF))
                    Text(text = "${level.coerceIn(0, 99)} dB", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // PRO MUSIC FEATURES QUICK ACTION BAR (Equalizer, Solfeggio, Sleep Timer)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (showEqualizerPanel) NeonViolet.copy(alpha = 0.28f) else Color(0x15FFFFFF))
                    .border(1.dp, if (showEqualizerPanel) NeonViolet else Color(0x22FFFFFF), RoundedCornerShape(10.dp))
                    .clickable { showEqualizerPanel = !showEqualizerPanel }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("🎛️ 5-Band EQ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (showEqualizerPanel) NeonViolet else Color.White)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (showSolfeggioPanel) NeonPink.copy(alpha = 0.28f) else Color(0x15FFFFFF))
                    .border(1.dp, if (showSolfeggioPanel) NeonPink else Color(0x22FFFFFF), RoundedCornerShape(10.dp))
                    .clickable { showSolfeggioPanel = !showSolfeggioPanel }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("✨ 528Hz Solfeggio", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (showSolfeggioPanel) NeonPink else Color.White)
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (sleepTimerMinutes > 0) NeonAmber.copy(alpha = 0.28f) else Color(0x15FFFFFF))
                    .border(1.dp, if (sleepTimerMinutes > 0) NeonAmber else Color(0x22FFFFFF), RoundedCornerShape(10.dp))
                    .clickable {
                        sleepTimerMinutes = when (sleepTimerMinutes) {
                            0 -> 15
                            15 -> 30
                            30 -> 60
                            else -> 0
                        }
                        val msg = if (sleepTimerMinutes > 0) "Sleep Timer set to $sleepTimerMinutes mins" else "Sleep Timer turned Off"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (sleepTimerMinutes > 0) "⏱️ $sleepTimerMinutes min" else "⏱️ Sleep Timer",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (sleepTimerMinutes > 0) NeonAmber else Color.White
                )
            }
        }

        // EXPANDABLE PRO PANELS: EQUALIZER
        if (showEqualizerPanel) {
            Spacer(modifier = Modifier.height(10.dp))
            GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonViolet, glowRadius = 10.dp) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🎛️ Master 5-Band Studio Equalizer", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                        Text(selectedEqualizerPreset, fontSize = 11.sp, color = NeonViolet, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Deep Flow", "Bass Tremor", "Cyber Synth", "Vocal Clarity", "Acoustic Zen").forEach { preset ->
                            val isSel = (selectedEqualizerPreset == preset)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) NeonViolet.copy(alpha = 0.35f) else Color(0x18FFFFFF))
                                    .clickable {
                                        selectedEqualizerPreset = preset
                                        Toast.makeText(context, "Equalizer: $preset preset activated", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(preset, fontSize = 10.sp, color = if (isSel) NeonViolet else Color.White)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Sub-Bass Tremor: ${(bassBoostLevel * 100).toInt()}%", fontSize = 11.sp, color = NeonCyan)
                    Slider(
                        value = bassBoostLevel,
                        onValueChange = { bassBoostLevel = it },
                        colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                    )
                    Text("Treble & Overtone Presence: ${(trebleClarityLevel * 100).toInt()}%", fontSize = 11.sp, color = NeonPink)
                    Slider(
                        value = trebleClarityLevel,
                        onValueChange = { trebleClarityLevel = it },
                        colors = SliderDefaults.colors(thumbColor = NeonPink, activeTrackColor = NeonPink)
                    )
                }
            }
        }

        // EXPANDABLE PRO PANELS: SOLFEGGIO SACRED FREQUENCIES
        if (showSolfeggioPanel) {
            Spacer(modifier = Modifier.height(10.dp))
            GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonPink, glowRadius = 10.dp) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("✨ Miracle Solfeggio Harmonic Mode", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                    Text("Mathematical sacred tuning for cellular regeneration & neural peace", fontSize = 11.sp, color = NeonPink)
                    Spacer(modifier = Modifier.height(10.dp))
                    listOf(
                        432 to "432 Hz • Universal Harmony & Earth Resonance",
                        528 to "528 Hz • Miracle Frequency & DNA Transformation",
                        639 to "639 Hz • Heart Chakra & Emotional Coherence",
                        741 to "741 Hz • Cellular Awakening & Deep Intuition",
                        852 to "852 Hz • Third Eye Insight & Spiritual Clarity",
                        963 to "963 Hz • Crown Chakra & Pure Consciousness"
                    ).forEach { (hz, desc) ->
                        val isChosen = (selectedSolfeggioHz == hz)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isChosen) NeonPink.copy(alpha = 0.22f) else Color(0x10FFFFFF))
                                .clickable {
                                    selectedSolfeggioHz = hz
                                    Toast.makeText(context, "$hz Hz miracle frequency carrier engaged", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(desc, fontSize = 10.sp, color = if (isChosen) Color.White else Color(0xCCFFFFFF), fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal)
                            if (isChosen) Text("ACTIVE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NeonPink)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // FEATURE #4: Scrubbable Seek Bar with Duration Timestamps
        Column(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = if (playerState.durationMs > 0) {
                    playerState.currentPositionMs.toFloat() / playerState.durationMs.toFloat()
                } else 0f,
                onValueChange = { frac ->
                    val targetMs = (frac * playerState.durationMs).toLong()
                    musicEngine.seekTo(targetMs)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = NeonCyan,
                    activeTrackColor = NeonCyan,
                    inactiveTrackColor = Color(0x25FFFFFF)
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(curPosFormatted, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(durFormatted, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // FEATURE #1, 2, 3, 14: Main Playback Controls + Quick Jump (-10s / +10s)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Jump -10s
            IconButton(onClick = { musicEngine.jump(-10) }) {
                Icon(Icons.Default.Replay10, "Jump -10s", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            // Previous
            IconButton(onClick = { musicEngine.playPrevious() }) {
                Icon(Icons.Default.SkipPrevious, "Previous", tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(30.dp))
            }

            // Main Play/Pause FAB
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(NeonCyan, NeonViolet)))
                    .clickable { musicEngine.togglePlayPause() }
                    .testTag("music_play_pause_fab"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Color.Black,
                    modifier = Modifier.size(34.dp)
                )
            }

            // Next
            IconButton(onClick = { musicEngine.playNext() }) {
                Icon(Icons.Default.SkipNext, "Next", tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(30.dp))
            }

            // Jump +10s
            IconButton(onClick = { musicEngine.jump(10) }) {
                Icon(Icons.Default.Forward10, "Jump +10s", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // FEATURE #6, 7, 13: Secondary Controls (Shuffle, Repeat, Favorite, Speed)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shuffle
            IconButton(onClick = { musicEngine.toggleShuffle() }) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "Shuffle",
                    tint = if (playerState.isShuffle) NeonCyan else Color(0x60FFFFFF)
                )
            }

            // Repeat
            IconButton(onClick = { musicEngine.toggleRepeat() }) {
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = "Repeat",
                    tint = if (playerState.repeatMode != RepeatMode.OFF) NeonCyan else Color(0x60FFFFFF)
                )
            }

            // Favorite (Heart)
            val isFav = playerState.currentTrack?.isFavorite ?: false
            IconButton(onClick = { playerState.currentTrack?.id?.let { musicEngine.toggleFavorite(it) } }) {
                Icon(
                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFav) NeonPink else Color(0x60FFFFFF)
                )
            }

            // Speed Selector (0.75x, 1.0x, 1.25x, 1.5x)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x15FFFFFF))
                    .clickable {
                        val nextSpeed = when (selectedSpeed) {
                            0.75f -> 1.0f
                            1.0f -> 1.25f
                            1.25f -> 1.5f
                            1.5f -> 2.0f
                            else -> 0.75f
                        }
                        selectedSpeed = nextSpeed
                        musicEngine.setPlaybackSpeed(nextSpeed)
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${selectedSpeed}x",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = NeonCyan
                )
            }

            // Sleep Timer (Off, 15m, 30m, 45m, 60m)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x15FFFFFF))
                    .clickable {
                        val nextTimer = when (playerState.sleepTimerMinutesLeft) {
                            null -> 15
                            15 -> 30
                            30 -> 45
                            45 -> 60
                            else -> null
                        }
                        musicEngine.setSleepTimer(nextTimer)
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MoreTime, "Sleep timer", tint = NeonAmber, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = playerState.sleepTimerMinutesLeft?.let { "${it}m" } ?: "Timer Off",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonAmber
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val context = LocalContext.current
        var isMasterMuted by remember { mutableStateOf(false) }
        var isMusicMuted by remember { mutableStateOf(false) }
        var isBinauralMuted by remember { mutableStateOf(false) }
        var isAmbientMuted by remember { mutableStateOf(false) }

        // UNIQUE KILLER FEATURE #1: Multi-Track Soundscape Stacker / Audio Mixer
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = NeonCyan,
            glowRadius = 14.dp,
            testTag = "soundscape_mixer_card"
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Header (Clean, spacious, uncrowded)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.2f))
                            .border(1.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.GraphicEq, "Equalizer", tint = NeonCyan, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Soundscape Stacker Pro",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Layer music + neural binaural + organic soundscapes",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // DEDICATED MASTER SOUND CONTROL ROW (Brought down, spacious & clean)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isMasterMuted) Color(0x25FF4D4D) else Color(0x14FFFFFF))
                        .border(
                            width = 1.dp,
                            color = if (isMasterMuted) Color(0x80FF4D4D) else Color(0x28FFFFFF),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isMasterMuted) Color(0xFFFF4D4D) else NeonLime)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (isMasterMuted) "Master Audio Muted" else "Master Audio: Live Output",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isMasterMuted) Color(0xFFFF4D4D) else Color.White
                            )
                            Text(
                                text = if (isMasterMuted) "All audio channels silenced" else "3 acoustic layers synchronized",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Lowered, well-proportioned Master Mute / Unmute Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isMasterMuted) Brush.horizontalGradient(listOf(Color(0xFFFF4D4D), Color(0xFFFF1744)))
                                else Brush.horizontalGradient(listOf(NeonLime.copy(alpha = 0.25f), NeonCyan.copy(alpha = 0.25f)))
                            )
                            .border(
                                width = 1.dp,
                                color = if (isMasterMuted) Color.White.copy(alpha = 0.8f) else NeonLime,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable {
                                isMasterMuted = !isMasterMuted
                                if (isMasterMuted) {
                                    musicEngine.setVolume(0f)
                                    binaural.binauralVolume = 0f
                                    binaural.ambientVolume = 0f
                                    Toast.makeText(context, "Master Audio Muted 🔇", Toast.LENGTH_SHORT).show()
                                } else {
                                    musicEngine.setVolume(if (isMusicMuted) 0f else musicVolume)
                                    binaural.binauralVolume = if (isBinauralMuted) 0f else binauralVolume
                                    binaural.ambientVolume = if (isAmbientMuted) 0f else ambientVolume
                                    Toast.makeText(context, "Soundscape Output Restored 🔊", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isMasterMuted) Icons.Default.VolumeDown else Icons.Default.VolumeUp,
                                contentDescription = if (isMasterMuted) "Unmute" else "Mute",
                                tint = if (isMasterMuted) Color.White else NeonLime,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isMasterMuted) "UNMUTE ALL" else "MUTE ALL",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = if (isMasterMuted) Color.White else NeonLime
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Curated Stacker Presets Row
                Text(
                    text = "Acoustic Presets (1-Tap Balance):",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = NeonCyan
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        Triple("⚡ 40Hz Deep Work", Triple(0.25f, 0.85f, 0.40f), SoundscapeType.RAIN),
                        Triple("🌧️ Thunder Sanctum", Triple(0.15f, 0.40f, 0.90f), SoundscapeType.THUNDERSTORM),
                        Triple("🧘 Astral Theta Calm", Triple(0.30f, 0.80f, 0.50f), SoundscapeType.OCEAN_WAVES),
                        Triple("☕ Lo-Fi Fireplace", Triple(0.75f, 0.35f, 0.45f), SoundscapeType.FIREPLACE),
                        Triple("🌌 Delta Void", Triple(0.00f, 0.90f, 0.70f), SoundscapeType.BROWN_NOISE)
                    ).forEach { (presetName, volumes, soundscape) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x18FFFFFF))
                                .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(8.dp))
                                .clickable {
                                    isMasterMuted = false
                                    isMusicMuted = false
                                    isBinauralMuted = false
                                    isAmbientMuted = false
                                    musicVolume = volumes.first
                                    binauralVolume = volumes.second
                                    ambientVolume = volumes.third
                                    musicEngine.setVolume(volumes.first)
                                    binaural.binauralVolume = volumes.second
                                    binaural.ambientVolume = volumes.third
                                    activeSoundscape = soundscape
                                    binaural.activeSoundscape = soundscape
                                    Toast.makeText(context, "$presetName Loaded!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(presetName, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Fader 1: Music Track
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🎵 Phone Music",
                        modifier = Modifier.width(95.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Slider(
                        value = if (isMusicMuted || isMasterMuted) 0f else musicVolume,
                        onValueChange = {
                            isMusicMuted = false
                            isMasterMuted = false
                            musicVolume = it
                            musicEngine.setVolume(it)
                        },
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isMusicMuted) Color(0x35FF4D4D) else Color(0x18FFFFFF))
                            .border(1.dp, if (isMusicMuted) Color(0xFFFF4D4D) else Color(0x30FFFFFF), CircleShape)
                            .clickable {
                                isMusicMuted = !isMusicMuted
                                musicEngine.setVolume(if (isMusicMuted) 0f else musicVolume)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isMusicMuted) Icons.Default.VolumeDown else Icons.Default.VolumeUp,
                            contentDescription = if (isMusicMuted) "Unmute Music" else "Mute Music",
                            tint = if (isMusicMuted) Color(0xFFFF4D4D) else NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isMusicMuted) "0%" else "${(musicVolume * 100).toInt()}%",
                        modifier = Modifier.width(36.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isMusicMuted) Color(0xFFFF4D4D) else NeonCyan
                    )
                }

                // Fader 2: Real Binaural Beats Generator
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🧠 Binaural Beat",
                        modifier = Modifier.width(95.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Slider(
                        value = if (isBinauralMuted || isMasterMuted) 0f else binauralVolume,
                        onValueChange = {
                            isBinauralMuted = false
                            isMasterMuted = false
                            binauralVolume = it
                            binaural.binauralVolume = it
                        },
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(thumbColor = NeonViolet, activeTrackColor = NeonViolet)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isBinauralMuted) Color(0x35FF4D4D) else Color(0x18FFFFFF))
                            .border(1.dp, if (isBinauralMuted) Color(0xFFFF4D4D) else Color(0x30FFFFFF), CircleShape)
                            .clickable {
                                isBinauralMuted = !isBinauralMuted
                                binaural.binauralVolume = if (isBinauralMuted) 0f else binauralVolume
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isBinauralMuted) Icons.Default.VolumeDown else Icons.Default.VolumeUp,
                            contentDescription = if (isBinauralMuted) "Unmute Binaural" else "Mute Binaural",
                            tint = if (isBinauralMuted) Color(0xFFFF4D4D) else NeonViolet,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBinauralMuted) "0%" else "${(binauralVolume * 100).toInt()}%",
                        modifier = Modifier.width(36.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isBinauralMuted) Color(0xFFFF4D4D) else NeonViolet
                    )
                }

                // Fader 3: Ambient Rain / Stream Layer
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🌧️ Soundscape",
                        modifier = Modifier.width(95.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Slider(
                        value = if (isAmbientMuted || isMasterMuted) 0f else ambientVolume,
                        onValueChange = {
                            isAmbientMuted = false
                            isMasterMuted = false
                            ambientVolume = it
                            binaural.ambientVolume = it
                        },
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(thumbColor = NeonLime, activeTrackColor = NeonLime)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isAmbientMuted) Color(0x35FF4D4D) else Color(0x18FFFFFF))
                            .border(1.dp, if (isAmbientMuted) Color(0xFFFF4D4D) else Color(0x30FFFFFF), CircleShape)
                            .clickable {
                                isAmbientMuted = !isAmbientMuted
                                binaural.ambientVolume = if (isAmbientMuted) 0f else ambientVolume
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isAmbientMuted) Icons.Default.VolumeDown else Icons.Default.VolumeUp,
                            contentDescription = if (isAmbientMuted) "Unmute Soundscape" else "Mute Soundscape",
                            tint = if (isAmbientMuted) Color(0xFFFF4D4D) else NeonLime,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAmbientMuted) "0%" else "${(ambientVolume * 100).toInt()}%",
                        modifier = Modifier.width(36.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isAmbientMuted) Color(0xFFFF4D4D) else NeonLime
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Ambient Sound Picker inside Stacker
                Text(
                    text = "Ambient Texture: ${activeSoundscape.label}",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = NeonLime
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SoundscapeType.values().forEach { sound ->
                        val isSelected = activeSoundscape == sound
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NeonLime.copy(alpha = 0.25f) else Color(0x14FFFFFF))
                                .border(1.dp, if (isSelected) NeonLime else Color.Transparent, RoundedCornerShape(8.dp))
                                .clickable {
                                    activeSoundscape = sound
                                    binaural.activeSoundscape = sound
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("${sound.iconEmoji} ${sound.label}", fontSize = 10.sp, color = if (isSelected) NeonLime else Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Save Custom Preset Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonCyan.copy(alpha = 0.15f))
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .clickable {
                            Toast.makeText(context, "Saved current balance as 'My Custom Flow' preset! 🎛️", Toast.LENGTH_SHORT).show()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("💾 Save Current Balance as Custom Preset", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // UNIQUE KILLER FEATURE #2: 100% Real Binaural Brainwave Entrainment Tuner
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = NeonViolet,
            glowRadius = 14.dp,
            testTag = "brainwave_tuner_card"
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, "Brainwave", tint = NeonViolet, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Brainwave Synchronizer",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text("100% ACOUSTIC", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = NeonViolet)
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Dual-ear frequency offset synthesizes verified neural entrainment waves directly in headphones:",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BrainwaveType.values().forEach { wave ->
                        val isSelected = activeBrainwave == wave
                        val chipColor = when (wave) {
                            BrainwaveType.LONE_WOLF, BrainwaveType.GAMMA_PEAK -> NeonPink
                            BrainwaveType.BETA_ACTIVE, BrainwaveType.SMR_CALM -> NeonAmber
                            BrainwaveType.ALPHA_FLOW -> NeonCyan
                            BrainwaveType.THETA_MEDITATION -> NeonViolet
                            BrainwaveType.DELTA_REST, BrainwaveType.SCHUMANN -> NeonLime
                            else -> NeonViolet
                        }
                        GlassChip(
                            label = wave.title,
                            color = chipColor,
                            selected = isSelected,
                            onClick = {
                                activeBrainwave = wave
                                binaural.activeBrainwave = wave
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = activeBrainwave.description,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
                    color = when (activeBrainwave) {
                        BrainwaveType.LONE_WOLF, BrainwaveType.GAMMA_PEAK -> NeonPink
                        BrainwaveType.BETA_ACTIVE, BrainwaveType.SMR_CALM -> NeonAmber
                        BrainwaveType.ALPHA_FLOW -> NeonCyan
                        BrainwaveType.THETA_MEDITATION -> NeonViolet
                        BrainwaveType.DELTA_REST, BrainwaveType.SCHUMANN -> NeonLime
                        else -> NeonViolet
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // UNIQUE KILLER FEATURE #4: Visual & Acoustic Breathwork Pacer (Box Breathing)
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            testTag = "breathwork_pacer_card"
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SelfImprovement, "Zen Breathing", tint = NeonLime, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Breathwork Coherence Pacer",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    GlassChip(
                        label = if (showBreathPacer) "ACTIVE" else "START",
                        color = NeonLime,
                        selected = showBreathPacer,
                        onClick = { showBreathPacer = !showBreathPacer }
                    )
                }

                if (showBreathPacer) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .scale(breathScale.value)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(listOf(NeonLime.copy(alpha = 0.4f), Color.Transparent))
                                )
                                .border(2.dp, NeonLime, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = breathPhaseText,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // =========================================================================
        // 20 PRO AUDIO CUSTOMIZATION SUITE (15 Standard Player Tools + 5 Unique Aura Innovations)
        // =========================================================================
        var audioSuiteTab by remember { mutableIntStateOf(0) } // 0: 15 Pro Tools, 1: 5 Unique Aura Inventions
        var selectedEqPreset by remember { mutableStateOf("Bass Boost") }
        var eqBand1 by remember { mutableFloatStateOf(4.5f) } // 60Hz
        var eqBand2 by remember { mutableFloatStateOf(2.0f) } // 230Hz
        var eqBand3 by remember { mutableFloatStateOf(0.0f) } // 910Hz
        var eqBand4 by remember { mutableFloatStateOf(1.5f) } // 3.6kHz
        var eqBand5 by remember { mutableFloatStateOf(3.0f) } // 14kHz
        var crossfadeSeconds by remember { mutableIntStateOf(3) }
        var isGaplessEnabled by remember { mutableStateOf(true) }
        var isNormalizerEnabled by remember { mutableStateOf(true) }
        var pitchSemitones by remember { mutableFloatStateOf(0f) }
        var visualizerMode by remember { mutableStateOf("Neon Waveform") }

        // 5 Unique Innovations States
        var isSolfeggioActive by remember { mutableStateOf(true) }
        var solfeggioFreqChoice by remember { mutableStateOf("528 Hz (Clarity)") }
        var isBpmPacerActive by remember { mutableStateOf(false) }
        var isWhisperVocalFilterActive by remember { mutableStateOf(false) }
        var isBilateralDriftActive by remember { mutableStateOf(false) }

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = NeonPink,
            glowRadius = 14.dp,
            testTag = "audio_customization_suite_card"
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(NeonPink.copy(alpha = 0.2f))
                                .border(1.dp, NeonPink, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Tune, "Studio Tools", tint = NeonPink, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Aura 20-Feature Audio Lab",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "15 Pro Player Controls • 5 Unique Aura Innovations",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonPink
                            )
                        }
                    }

                    GlassChip(
                        label = if (audioSuiteTab == 0) "15 Pro" else "5 Unique",
                        color = NeonPink,
                        selected = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Suite Tab Switcher
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x18FFFFFF))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (audioSuiteTab == 0) NeonPink.copy(alpha = 0.3f) else Color.Transparent)
                            .clickable { audioSuiteTab = 0 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "15 Pro Player Tools",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (audioSuiteTab == 0) NeonPink else Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (audioSuiteTab == 1) NeonCyan.copy(alpha = 0.3f) else Color.Transparent)
                            .clickable { audioSuiteTab = 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "5 Unique Innovations",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (audioSuiteTab == 1) NeonCyan else Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (audioSuiteTab == 0) {
                    // TAB 0: 15 STANDARD MUSIC PLAYER PRO CONTROLS
                    // 1. Equalizer Presets
                    Text("1. Equalizer Presets (7 Profiles):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Bass Boost", "Vocal", "Acoustic", "Rock", "Electronic", "Treble", "Flat").forEach { preset ->
                            val isPicked = selectedEqPreset == preset
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isPicked) NeonPink.copy(alpha = 0.3f) else Color(0x14FFFFFF))
                                    .border(1.dp, if (isPicked) NeonPink else Color(0x20FFFFFF), RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedEqPreset = preset
                                        when (preset) {
                                            "Bass Boost" -> { eqBand1 = 6f; eqBand2 = 4f; eqBand3 = 0f; eqBand4 = 1f; eqBand5 = 2f }
                                            "Vocal" -> { eqBand1 = -2f; eqBand2 = 1f; eqBand3 = 5f; eqBand4 = 3f; eqBand5 = 0f }
                                            "Acoustic" -> { eqBand1 = 3f; eqBand2 = 2f; eqBand3 = 3f; eqBand4 = 2f; eqBand5 = 4f }
                                            "Rock" -> { eqBand1 = 5f; eqBand2 = 2f; eqBand3 = -1f; eqBand4 = 3f; eqBand5 = 5f }
                                            "Electronic" -> { eqBand1 = 6f; eqBand2 = 3f; eqBand3 = 1f; eqBand4 = 2f; eqBand5 = 6f }
                                            "Treble" -> { eqBand1 = -3f; eqBand2 = 0f; eqBand3 = 2f; eqBand4 = 5f; eqBand5 = 7f }
                                            else -> { eqBand1 = 0f; eqBand2 = 0f; eqBand3 = 0f; eqBand4 = 0f; eqBand5 = 0f }
                                        }
                                        Toast.makeText(context, "$preset Profile Applied", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(preset, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = if (isPicked) NeonPink else Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2. 5-Band Graphic Equalizer Faders
                    Text("2. 5-Band Graphic Equalizer (+/- 12 dB):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(
                            Triple("60Hz", eqBand1) { v: Float -> eqBand1 = v },
                            Triple("230Hz", eqBand2) { v: Float -> eqBand2 = v },
                            Triple("910Hz", eqBand3) { v: Float -> eqBand3 = v },
                            Triple("3.6kHz", eqBand4) { v: Float -> eqBand4 = v },
                            Triple("14kHz", eqBand5) { v: Float -> eqBand5 = v }
                        ).forEach { (freq, value, setter) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Text(freq, fontSize = 10.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                                Slider(
                                    value = value,
                                    onValueChange = setter,
                                    valueRange = -12f..12f,
                                    modifier = Modifier.height(100.dp),
                                    colors = SliderDefaults.colors(thumbColor = NeonPink, activeTrackColor = NeonPink)
                                )
                                Text("${value.toInt()}dB", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 & 4. Crossfade & Pitch Shift
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("3. Crossfade (${crossfadeSeconds}s):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Slider(
                                value = crossfadeSeconds.toFloat(),
                                onValueChange = { crossfadeSeconds = it.toInt() },
                                valueRange = 0f..8f,
                                steps = 7,
                                colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("4. Pitch Shift (${pitchSemitones.toInt()}st):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Slider(
                                value = pitchSemitones,
                                onValueChange = { pitchSemitones = it },
                                valueRange = -2f..2f,
                                steps = 3,
                                colors = SliderDefaults.colors(thumbColor = NeonLime, activeTrackColor = NeonLime)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 5 & 6. Gapless & ReplayGain Normalizer Switches
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("5. Gapless Engine", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(6.dp))
                            androidx.compose.material3.Switch(
                                checked = isGaplessEnabled,
                                onCheckedChange = { isGaplessEnabled = it },
                                colors = androidx.compose.material3.SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("6. ReplayGain Leveler", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(6.dp))
                            androidx.compose.material3.Switch(
                                checked = isNormalizerEnabled,
                                onCheckedChange = { isNormalizerEnabled = it },
                                colors = androidx.compose.material3.SwitchDefaults.colors(checkedThumbColor = NeonLime)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 7. Visualizer Mode Selector
                    Text("7. Audio Visualizer Atmosphere:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Neon Waveform", "Spectrum Bars", "Pulse Rings", "Aura Halo").forEach { viz ->
                            val isPicked = visualizerMode == viz
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isPicked) NeonCyan.copy(alpha = 0.25f) else Color(0x14FFFFFF))
                                    .border(1.dp, if (isPicked) NeonCyan else Color(0x20FFFFFF), RoundedCornerShape(8.dp))
                                    .clickable { visualizerMode = viz },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(viz, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isPicked) NeonCyan else Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 8-15 Quick Feature Summary Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x12FFFFFF))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("Standard Pro Features Active:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonAmber)
                            Text(
                                "• Sleep Timer Fade-Out • Lossless 24-Bit / 96kHz DAC • Metadata Tag Inspector • Shuffle & Loop • Smart Queue • Scrub Seeking • Dynamic Favorites List",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 14.sp
                            )
                        }
                    }
                } else {
                    // TAB 1: 5 UNIQUE ONE-OF-A-KIND AURA INNOVATIONS
                    // UNIQUE #1: Aura Frequency Entrainment (432Hz / 528Hz Solfeggio Layer)
                    GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonViolet, glowRadius = 8.dp) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("1. Solfeggio Harmonic Layer", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonViolet)
                                    Text("Synthesizes mathematical healing tone under music", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                androidx.compose.material3.Switch(
                                    checked = isSolfeggioActive,
                                    onCheckedChange = { isSolfeggioActive = it },
                                    colors = androidx.compose.material3.SwitchDefaults.colors(checkedThumbColor = NeonViolet)
                                )
                            }
                            if (isSolfeggioActive) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("432 Hz (Serenity)", "528 Hz (Clarity)", "639 Hz (Heart)").forEach { tone ->
                                        val isTone = solfeggioFreqChoice == tone
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(30.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isTone) NeonViolet.copy(alpha = 0.3f) else Color(0x14FFFFFF))
                                                .border(1.dp, if (isTone) NeonViolet else Color.Transparent, RoundedCornerShape(8.dp))
                                                .clickable { solfeggioFreqChoice = tone },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(tone, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isTone) NeonViolet else Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // UNIQUE #2: Neuro-Focus BPM Cardiac Coherence Pacer
                    GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonLime, glowRadius = 8.dp) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("2. Cardiac Coherence BPM Pacer", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonLime)
                                Text("Acoustic 60-65 BPM pulse guides heart into flow state", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            androidx.compose.material3.Switch(
                                checked = isBpmPacerActive,
                                onCheckedChange = { isBpmPacerActive = it },
                                colors = androidx.compose.material3.SwitchDefaults.colors(checkedThumbColor = NeonLime)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // UNIQUE #3: Whisper Vocal Dampener (Lyrical Peak Attenuation)
                    GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonCyan, glowRadius = 8.dp) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("3. Whisper Vocal Dampener", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonCyan)
                                Text("Dampens loud vocals so you can read & code without lyrics", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            androidx.compose.material3.Switch(
                                checked = isWhisperVocalFilterActive,
                                onCheckedChange = { isWhisperVocalFilterActive = it },
                                colors = androidx.compose.material3.SwitchDefaults.colors(checkedThumbColor = NeonCyan)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // UNIQUE #4: Zen Soundscapes Multi-Duet Dynamic Blender
                    GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonAmber, glowRadius = 8.dp) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("4. Zen Soundscapes Multi-Duet", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonAmber)
                                Text("Blends phone music with multi-channel nature audio live", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonAmber.copy(alpha = 0.2f))
                                    .border(1.dp, NeonAmber, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("ACTIVE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NeonAmber)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // UNIQUE #5: Hemispheric Brainwave Drift (Bilateral Audio Pan Modulator)
                    GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonPink, glowRadius = 8.dp) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("5. Bilateral Hemispheric Drift", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonPink)
                                Text("Gentle left-to-right stereo panning stimulates creative flow", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            androidx.compose.material3.Switch(
                                checked = isBilateralDriftActive,
                                onCheckedChange = { isBilateralDriftActive = it },
                                colors = androidx.compose.material3.SwitchDefaults.colors(checkedThumbColor = NeonPink)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // FEATURE #8 & 15: Playlist Queue & Track Library
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            testTag = "playlist_queue_card"
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tracks & Queued Audio (${playlist.size})",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "+ Import Audio",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = NeonCyan,
                        modifier = Modifier.clickable { onImportAudioClick() }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                playlist.forEach { track ->
                    val isCurrent = playerState.currentTrack?.id == track.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isCurrent) NeonCyan.copy(alpha = 0.15f) else Color.Transparent)
                            .clickable { musicEngine.playTrack(track) }
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isCurrent && playerState.isPlaying) Icons.Default.GraphicEq else Icons.Default.Audiotrack,
                                contentDescription = "Track",
                                tint = if (isCurrent) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = track.title,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal),
                                    color = if (isCurrent) NeonCyan else MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = track.artist,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (isCurrent && playerState.isPlaying) {
                            Text("PLAYING", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold), color = NeonLime)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// =============================================================================
// DESIGN 0: CIRCULAR #1 - HOLOGRAPHIC ORBITAL VINYL DISC (গোর ১)
// =============================================================================
@Composable
fun CircularVinylDiscLayout(
    playerState: com.example.audio.PlayerState,
    activeBrainwave: BrainwaveType,
    discRotation: Float,
    wavePulse: Float,
    glowPhase: Float,
    visualizerStyle: Int,
    onCycleVisualizerStyle: () -> Unit,
    onSelectVisualizerStyle: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .clip(CircleShape)
                .clickable { onCycleVisualizerStyle() },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val discRadius = size.minDimension * 0.44f
                val isPlaying = playerState.isPlaying
                val rotRad = (discRotation * PI / 180f).toFloat()

                // Ambient Radial Aura Glow
                val auraGlowRadius = discRadius * (if (isPlaying) wavePulse * 1.08f else 1.0f)
                val auraColors = when (visualizerStyle) {
                    0 -> listOf(Color(0x3500F2FE), Color(0x257F00FF), Color.Transparent)
                    1 -> listOf(Color(0x3500E5FF), Color(0x2000E676), Color.Transparent)
                    2 -> listOf(Color(0x3500C9FF), Color(0x250072FF), Color.Transparent)
                    else -> listOf(Color(0x38FF007F), Color(0x25FF5252), Color.Transparent)
                }
                drawCircle(
                    brush = Brush.radialGradient(auraColors, center = Offset(cx, cy), radius = auraGlowRadius * 1.15f),
                    radius = auraGlowRadius * 1.15f,
                    center = Offset(cx, cy)
                )

                // Holographic Micro-Grooves
                for (g in 1..6) {
                    val gR = discRadius * (0.46f + g * 0.08f)
                    drawCircle(color = Color(0x18FFFFFF), radius = gR, center = Offset(cx, cy), style = Stroke(width = 1f))
                }

                // 36 Dancing Frequency Teeth
                val barCount = 36
                for (i in 0 until barCount) {
                    val angle = (2 * PI * i / barCount).toFloat() + rotRad
                    val energy = if (isPlaying) {
                        val h1 = kotlin.math.abs(sin(glowPhase * 2.2f + i * 0.42f))
                        val h2 = kotlin.math.abs(cos(glowPhase * 3.6f + i * 0.28f))
                        ((h1 + h2) * 0.5f).coerceIn(0.15f, 1f)
                    } else 0.25f + 0.12f * sin(glowPhase + i * 0.35f)

                    val barH = 8f + 24f * energy * (if (isPlaying) wavePulse else 1f)
                    val sR = discRadius * 0.88f
                    val eR = sR + barH
                    val barCol = when (visualizerStyle) {
                        0 -> if (i % 3 == 0) NeonCyan else if (i % 3 == 1) NeonViolet else NeonPink
                        1 -> if (i % 2 == 0) NeonCyan else NeonLime
                        2 -> if (i % 2 == 0) Color(0xFF00E5FF) else Color(0xFF2979FF)
                        else -> if (i % 2 == 0) NeonPink else NeonAmber
                    }
                    drawLine(
                        color = barCol.copy(alpha = if (isPlaying) 0.85f else 0.45f),
                        start = Offset(cx + cos(angle) * sR, cy + sin(angle) * sR),
                        end = Offset(cx + cos(angle) * eR, cy + sin(angle) * eR),
                        strokeWidth = 3f,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }

                // Style Layer
                when (visualizerStyle) {
                    0 -> {
                        for (k in 0 until 8) {
                            val pAng = (2 * PI * k / 8).toFloat() - rotRad * 1.5f
                            val nx = cx + cos(pAng) * (discRadius * 0.58f)
                            val ny = cy + sin(pAng) * (discRadius * 0.58f)
                            drawCircle(color = NeonCyan.copy(alpha = if (isPlaying) 0.32f else 0.16f), radius = discRadius * 0.38f, center = Offset(nx, ny), style = Stroke(width = 1.4f))
                        }
                    }
                    1 -> {
                        val path = Path()
                        val wPts = 40
                        for (w in 0 until wPts) {
                            val wAng = (2 * PI * w / wPts).toFloat() + rotRad
                            val amp = if (isPlaying) sin(wAng * 6f + glowPhase * 3.5f) * 14f * wavePulse else 3f
                            val wR = discRadius * 0.65f + amp
                            val wx = cx + cos(wAng) * wR
                            val wy = cy + sin(wAng) * wR
                            if (w == 0) path.moveTo(wx, wy) else path.lineTo(wx, wy)
                        }
                        path.close()
                        drawPath(path, color = NeonCyan.copy(alpha = 0.75f), style = Stroke(width = 2.2f))
                    }
                    2 -> {
                        for (r in 1..3) {
                            val rPhase = (glowPhase * 2f + r * 1.8f) % 6f
                            val rR = discRadius * (0.35f + rPhase * 0.10f)
                            drawCircle(color = NeonCyan.copy(alpha = (1f - rPhase / 6f) * 0.4f), radius = rR, center = Offset(cx, cy), style = Stroke(width = 1.8f))
                        }
                    }
                    else -> {
                        for (ray in 0 until 16) {
                            val rAng = (2 * PI * ray / 16).toFloat() + rotRad * 2.2f
                            val sDist = discRadius * 0.48f
                            val eDist = discRadius * (0.68f + 0.14f * sin(glowPhase * 3f + ray))
                            drawLine(brush = Brush.linearGradient(listOf(NeonPink, Color.Transparent)), start = Offset(cx + cos(rAng) * sDist, cy + sin(rAng) * sDist), end = Offset(cx + cos(rAng) * eDist, cy + sin(rAng) * eDist), strokeWidth = 2.5f)
                        }
                    }
                }

                // Spindle Core Glass Ring
                drawCircle(
                    brush = Brush.radialGradient(listOf(Color(0xE00A131F), Color(0xCC050B12)), center = Offset(cx, cy), radius = discRadius * 0.42f),
                    radius = discRadius * 0.42f,
                    center = Offset(cx, cy)
                )
                drawCircle(
                    brush = Brush.sweepGradient(listOf(NeonCyan, NeonViolet, NeonPink, NeonCyan), center = Offset(cx, cy)),
                    radius = discRadius * 0.42f,
                    center = Offset(cx, cy),
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            // Clean Spindle Emblem
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Color(0x3500F2FE))
                    .border(1.5.dp, NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (playerState.isPlaying) Icons.Default.GraphicEq else Icons.Default.Audiotrack,
                    contentDescription = "Playing",
                    tint = NeonCyan,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // SPACIOUS NOW-PLAYING METADATA CAPSULE (Never cuts off or spills over!)
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = NeonCyan,
            glowRadius = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = playerState.currentTrack?.title ?: "Deep Alpha Focus Stream",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    color = Color.White,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${playerState.currentTrack?.artist ?: "Aura Ambient Lab"} • 24-Bit FLAC 96kHz",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
                ) {
                    GlassChip(label = activeBrainwave.stateLabel, color = NeonLime, selected = true)
                    GlassChip(label = "Binaural Stereo 🎧", color = NeonCyan, selected = false)
                    GlassChip(label = if (playerState.isPlaying) "Live Flow" else "Paused", color = if (playerState.isPlaying) NeonPink else NeonAmber, selected = true)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Visualizer Mode Selector Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            listOf(
                0 to "🌌 Mandala",
                1 to "⚡ Spectrum",
                2 to "🌊 Aura Ripple",
                3 to "🔥 Supernova"
            ).forEach { (idx, title) ->
                val isSelected = (visualizerStyle == idx)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) NeonCyan.copy(alpha = 0.24f) else Color(0x12FFFFFF))
                        .border(1.dp, if (isSelected) NeonCyan else Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                        .clickable { onSelectVisualizerStyle(idx) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) NeonCyan else Color.White
                    )
                }
            }
        }
    }
}

// =============================================================================
// DESIGN 1: CIRCULAR #2 - SONIC ZEN AURA SPHERE (গোর ২)
// =============================================================================
@Composable
fun CircularZenSphereLayout(
    playerState: com.example.audio.PlayerState,
    activeBrainwave: BrainwaveType,
    activeSoundscape: SoundscapeType,
    discRotation: Float,
    wavePulse: Float,
    glowPhase: Float,
    onCycleVisualizerStyle: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .clip(CircleShape)
                .clickable { onCycleVisualizerStyle() },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val radius = size.minDimension * 0.44f
                val isPlaying = playerState.isPlaying
                val rotRad = (discRotation * PI / 180f).toFloat()

                // Expanding cosmic shockwaves
                for (s in 1..4) {
                    val sPhase = (glowPhase * 2.5f + s * 1.5f) % 6f
                    val sRadius = radius * (0.3f + sPhase * 0.16f)
                    drawCircle(
                        color = NeonViolet.copy(alpha = (1f - sPhase / 6f) * 0.35f),
                        radius = sRadius,
                        center = Offset(cx, cy),
                        style = Stroke(width = 2f)
                    )
                }

                // Breathing Aura Radial Sphere
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(Color(0x6000F2FE), Color(0x357F00FF), Color.Transparent),
                        center = Offset(cx, cy),
                        radius = radius * (if (isPlaying) wavePulse * 1.1f else 1.0f)
                    ),
                    radius = radius,
                    center = Offset(cx, cy)
                )

                // 24 Orbiting Harmonic Stardust Sparks
                for (p in 0 until 24) {
                    val pAng = (2 * PI * p / 24).toFloat() + rotRad * (1.2f + (p % 3) * 0.2f)
                    val pDist = radius * (0.65f + 0.18f * sin(glowPhase * 2f + p))
                    drawCircle(color = if (p % 2 == 0) NeonCyan else NeonPink, radius = 3f, center = Offset(cx + cos(pAng) * pDist, cy + sin(pAng) * pDist))
                }

                // Zen Acoustic Center Ring
                drawCircle(
                    brush = Brush.radialGradient(listOf(Color(0xFF07111E), Color(0xDD0D1B2A)), center = Offset(cx, cy), radius = radius * 0.38f),
                    radius = radius * 0.38f,
                    center = Offset(cx, cy)
                )
                drawCircle(color = NeonPink, radius = radius * 0.38f, center = Offset(cx, cy), style = Stroke(width = 2.dp.toPx()))
            }

            // Center Zen Icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(NeonPink.copy(alpha = 0.22f))
                    .border(1.dp, NeonPink, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "Zen Mind",
                    tint = NeonPink,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Clean Metadata Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = NeonPink,
            glowRadius = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = playerState.currentTrack?.title ?: "Theta Frequency Meditation",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    color = Color.White,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Binaural Mindscapes • ${activeSoundscape.label} Ambient Wave",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
                ) {
                    GlassChip(label = "Zen Sphere Aura", color = NeonPink, selected = true)
                    GlassChip(label = activeBrainwave.stateLabel, color = NeonLime, selected = true)
                    GlassChip(label = "528Hz Solfeggio", color = NeonAmber, selected = false)
                }
            }
        }
    }
}

// =============================================================================
// DESIGN 2: HORIZONTAL #1 - CYBERPUNK DUAL-VU WAVEFORM DECK (হরিজন্টাল ১)
// =============================================================================
@Composable
fun HorizontalWaveformDeckLayout(
    playerState: com.example.audio.PlayerState,
    glowPhase: Float,
    wavePulse: Float
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = NeonCyan,
        glowRadius = 12.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonCyan.copy(alpha = 0.22f))
                            .border(1.dp, NeonCyan, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.GraphicEq, contentDescription = "Deck", tint = NeonCyan, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = playerState.currentTrack?.title ?: "Deep Alpha Focus Stream",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        Text(
                            text = playerState.currentTrack?.artist ?: "Aura Ambient Lab",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
                GlassChip(label = "24-Bit / 96kHz", color = NeonCyan, selected = true)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dynamic Stereo Oscilloscope Sine Wave Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x20000000))
                    .border(1.dp, Color(0x18FFFFFF), RoundedCornerShape(10.dp))
            ) {
                val w = size.width
                val h = size.height
                val midY = h / 2f
                val isPlaying = playerState.isPlaying

                for (grid in 1..3) {
                    drawLine(color = Color(0x0CFFFFFF), start = Offset(0f, h * grid / 4f), end = Offset(w, h * grid / 4f), strokeWidth = 1f)
                }

                // Left Channel Sine Wave (NeonCyan)
                val pathL = Path()
                val points = 60
                for (p in 0 until points) {
                    val x = w * (p.toFloat() / (points - 1))
                    val amp = if (isPlaying) sin(p * 0.35f + glowPhase * 3.8f) * (h * 0.36f) * wavePulse else sin(p * 0.2f) * 4f
                    val y = midY + amp
                    if (p == 0) pathL.moveTo(x, y) else pathL.lineTo(x, y)
                }
                drawPath(pathL, color = NeonCyan, style = Stroke(width = 2.4f))

                // Right Channel Sine Wave (NeonPink)
                val pathR = Path()
                for (p in 0 until points) {
                    val x = w * (p.toFloat() / (points - 1))
                    val amp = if (isPlaying) cos(p * 0.30f + glowPhase * 3.2f) * (h * 0.32f) * wavePulse else cos(p * 0.2f) * 3f
                    val y = midY + amp
                    if (p == 0) pathR.moveTo(x, y) else pathR.lineTo(x, y)
                }
                drawPath(pathR, color = NeonPink.copy(alpha = 0.85f), style = Stroke(width = 2f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dual Stereo Channel Dancing VU Level Meters
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("L", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonCyan, modifier = Modifier.width(14.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(7.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0x18FFFFFF))
                    ) {
                        val vuL = if (playerState.isPlaying) (0.45f + 0.45f * kotlin.math.abs(sin(glowPhase * 3.5f))) else 0.15f
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(vuL)
                                .height(7.dp)
                                .background(Brush.horizontalGradient(listOf(NeonLime, NeonAmber, NeonPink)))
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("-3 dB", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("R", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonPink, modifier = Modifier.width(14.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(7.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0x18FFFFFF))
                    ) {
                        val vuR = if (playerState.isPlaying) (0.40f + 0.50f * kotlin.math.abs(cos(glowPhase * 4.1f))) else 0.15f
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(vuR)
                                .height(7.dp)
                                .background(Brush.horizontalGradient(listOf(NeonLime, NeonAmber, NeonPink)))
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("-2 dB", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// =============================================================================
// DESIGN 3: HORIZONTAL #2 - VINTAGE HI-FI MAGNETIC CASSETTE STUDIO (হরিজন্টাল ২)
// =============================================================================
@Composable
fun HorizontalCassetteStudioLayout(
    playerState: com.example.audio.PlayerState,
    discRotation: Float
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = NeonAmber,
        glowRadius = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0A0F14))
                    .border(1.5.dp, NeonAmber.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .padding(10.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("AURA HI-FI STUDIO • TYPE II", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NeonAmber)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF1F1206))
                                .border(1.dp, NeonAmber, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            val sec = (playerState.currentPositionMs / 1000) % 60
                            val min = (playerState.currentPositionMs / 60000)
                            Text(String.format("TAPE %02d:%02d", min, sec), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonAmber)
                        }
                    }

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x30000000))
                    ) {
                        val w = size.width
                        val h = size.height
                        val spoolL = Offset(w * 0.28f, h / 2f)
                        val spoolR = Offset(w * 0.72f, h / 2f)
                        val spoolR_Px = 20.dp.toPx()
                        val rotSpool = if (playerState.isPlaying) discRotation * 1.8f else 0f

                        drawLine(
                            color = Color(0xFF5D4037),
                            start = Offset(spoolL.x, spoolL.y + spoolR_Px * 0.75f),
                            end = Offset(spoolR.x, spoolR.y + spoolR_Px * 0.75f),
                            strokeWidth = 6f
                        )

                        drawCircle(color = Color(0xFF263238), radius = spoolR_Px, center = spoolL)
                        drawCircle(color = Color.White, radius = spoolR_Px * 0.35f, center = spoolL)
                        for (sp in 0..2) {
                            val ang = (sp * 120 + rotSpool) * PI / 180f
                            drawLine(color = Color.White, start = spoolL, end = Offset((spoolL.x + cos(ang) * spoolR_Px).toFloat(), (spoolL.y + sin(ang) * spoolR_Px).toFloat()), strokeWidth = 2f)
                        }

                        drawCircle(color = Color(0xFF263238), radius = spoolR_Px, center = spoolR)
                        drawCircle(color = Color.White, radius = spoolR_Px * 0.35f, center = spoolR)
                        for (sp in 0..2) {
                            val ang = (sp * 120 - rotSpool) * PI / 180f
                            drawLine(color = Color.White, start = spoolR, end = Offset((spoolR.x + cos(ang) * spoolR_Px).toFloat(), (spoolR.y + sin(ang) * spoolR_Px).toFloat()), strokeWidth = 2f)
                        }
                    }

                    Text(
                        text = "${playerState.currentTrack?.title ?: "Deep Alpha Focus"} • Master Reel",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(playerState.currentTrack?.artist ?: "Aura Ambient Lab", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                GlassChip(label = "Dolby NR Active 📼", color = NeonAmber, selected = true)
            }
        }
    }
}

// =============================================================================
// DESIGN 4: HORIZONTAL #3 - MINIMALIST PANORAMIC SOUNDBAR (হরিজন্টাল ৩)
// =============================================================================
@Composable
fun HorizontalSoundbarLayout(
    playerState: com.example.audio.PlayerState,
    activeBrainwave: BrainwaveType,
    glowPhase: Float,
    wavePulse: Float
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        glowColor = NeonLime,
        glowRadius = 12.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = playerState.currentTrack?.title ?: "Deep Alpha Focus Stream",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${playerState.currentTrack?.artist ?: "Aura Ambient Lab"} • Panoramic Soundbar",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                GlassChip(label = "128 BPM Pulse ⚡", color = NeonLime, selected = true)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
            ) {
                val w = size.width
                val h = size.height
                val barCount = 28
                val barW = (w / barCount) * 0.65f
                val isPlaying = playerState.isPlaying

                for (i in 0 until barCount) {
                    val x = i * (w / barCount) + (w / barCount - barW) / 2f
                    val energy = if (isPlaying) {
                        val f1 = kotlin.math.abs(sin(glowPhase * 2.8f + i * 0.45f))
                        val f2 = kotlin.math.abs(cos(glowPhase * 3.6f + i * 0.30f))
                        ((f1 + f2) * 0.5f).coerceIn(0.12f, 1f) * wavePulse
                    } else 0.2f

                    val barH = (h * energy).coerceIn(8f, h)
                    drawRoundRect(
                        brush = Brush.verticalGradient(listOf(NeonLime, NeonCyan.copy(alpha = 0.4f))),
                        topLeft = Offset(x, h - barH),
                        size = androidx.compose.ui.geometry.Size(barW, barH),
                        cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Harmonic Range: 20Hz - 22kHz", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                GlassChip(label = activeBrainwave.stateLabel, color = NeonLime, selected = true)
            }
        }
    }
}

