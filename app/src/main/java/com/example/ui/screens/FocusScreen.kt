package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.BinauralAudioEngine
import com.example.audio.BrainwaveType
import com.example.audio.SoundscapeType
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassChip
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import kotlinx.coroutines.delay

enum class SessionMode(val title: String, val defaultMinutes: Int) {
    FOCUS("Deep Focus", 25),
    SHORT_BREAK("Short Break", 5),
    LONG_BREAK("Long Break", 15),
    CUSTOM("Custom / Infinite", 45)
}

@Composable
fun FocusScreen(
    binauralEngine: BinauralAudioEngine,
    onOpenMusicStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMode by remember { mutableStateOf(SessionMode.FOCUS) }
    var customMinutes by remember { mutableIntStateOf(25) }
    var isInfiniteMode by remember { mutableStateOf(false) }

    var totalSeconds by remember { mutableIntStateOf(25 * 60) }
    var secondsRemaining by remember { mutableIntStateOf(25 * 60) }
    var infiniteSecondsElapsed by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    var selectedBrainwave by remember { mutableStateOf(BrainwaveType.LONE_WOLF) }
    var selectedSoundscape by remember { mutableStateOf(SoundscapeType.RAIN) }
    var completedSessionsCount by remember { mutableIntStateOf(0) }
    var showCustomDurationPanel by remember { mutableStateOf(false) }

    // Synchronize Binaural Engine parameters
    LaunchedEffect(selectedBrainwave, selectedSoundscape) {
        binauralEngine.activeBrainwave = selectedBrainwave
        binauralEngine.activeSoundscape = selectedSoundscape
    }

    // Timer countdown / infinite countup loop
    LaunchedEffect(isRunning, secondsRemaining, infiniteSecondsElapsed, isInfiniteMode) {
        if (isRunning) {
            if (!binauralEngine.isPlaying) {
                binauralEngine.start()
            }
            delay(1000L)
            if (isInfiniteMode) {
                infiniteSecondsElapsed++
            } else {
                if (secondsRemaining > 0) {
                    secondsRemaining--
                } else {
                    isRunning = false
                    binauralEngine.stop()
                    completedSessionsCount++
                    selectedMode = SessionMode.SHORT_BREAK
                    totalSeconds = 5 * 60
                    secondsRemaining = totalSeconds
                }
            }
        } else {
            if (binauralEngine.isPlaying) {
                binauralEngine.stop()
            }
        }
    }

    // Breathing pulse animation when running
    val pulseScale = remember { Animatable(1f) }
    LaunchedEffect(isRunning) {
        if (isRunning) {
            pulseScale.animateTo(
                targetValue = 1.04f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            pulseScale.snapTo(1f)
        }
    }

    val progress = if (isInfiniteMode) {
        ((infiniteSecondsElapsed % 3600).toFloat() / 3600f)
    } else {
        if (totalSeconds > 0) (secondsRemaining.toFloat() / totalSeconds.toFloat()) else 0f
    }
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    val displayMinutes = if (isInfiniteMode) (infiniteSecondsElapsed / 60) else (secondsRemaining / 60)
    val displaySeconds = if (isInfiniteMode) (infiniteSecondsElapsed % 60) else (secondsRemaining % 60)
    val timeFormatted = String.format("%02d:%02d", displayMinutes, displaySeconds)

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val clockGlowBrush = remember(primaryColor, secondaryColor, isRunning) {
        Brush.radialGradient(
            colors = listOf(
                primaryColor.copy(alpha = if (isRunning) 0.30f else 0.12f),
                secondaryColor.copy(alpha = if (isRunning) 0.18f else 0.05f),
                Color.Transparent
            )
        )
    }
    val arcSweepBrush = remember(primaryColor, secondaryColor) {
        Brush.sweepGradient(
            0.0f to primaryColor,
            0.5f to secondaryColor,
            1.0f to primaryColor
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item(key = "focus_header") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "NeuroBox",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Acoustic Brainwave & Cognitive Space",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    GlassChip(
                        label = if (isInfiniteMode) "♾️ Unlimited Flow" else "Session #$completedSessionsCount",
                        color = if (isInfiniteMode) NeonLime else NeonCyan,
                        selected = true,
                        onClick = {
                            isInfiniteMode = !isInfiniteMode
                            isRunning = false
                            if (isInfiniteMode) {
                                infiniteSecondsElapsed = 0
                            } else {
                                secondsRemaining = totalSeconds
                            }
                        },
                        testTag = "infinite_mode_chip"
                    )
                }
            }
        }

        // Mode Selector Pills
        item(key = "focus_modes") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0x14FFFFFF))
                    .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(24.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SessionMode.values().forEach { mode ->
                    val isSelected = selectedMode == mode
                    val modeBg = if (isSelected) {
                        Brush.horizontalGradient(
                            listOf(primaryColor.copy(alpha = 0.4f), secondaryColor.copy(alpha = 0.4f))
                        )
                    } else {
                        Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                    }
                    val modeBorderBrush = if (isSelected) {
                        Brush.horizontalGradient(listOf(primaryColor, secondaryColor))
                    } else {
                        Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(modeBg)
                            .border(
                                width = if (isSelected) 1.dp else 0.dp,
                                brush = modeBorderBrush,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable {
                                selectedMode = mode
                                if (mode == SessionMode.CUSTOM) {
                                    showCustomDurationPanel = true
                                } else {
                                    isInfiniteMode = false
                                    totalSeconds = mode.defaultMinutes * 60
                                    secondsRemaining = totalSeconds
                                    isRunning = false
                                }
                            }
                            .padding(horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (mode == SessionMode.CUSTOM) "Custom" else mode.title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp
                            ),
                            color = if (isSelected) primaryColor else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Custom Duration Slider Panel
        if (showCustomDurationPanel || selectedMode == SessionMode.CUSTOM) {
            item(key = "focus_custom_panel") {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isInfiniteMode) "Infinite Listening Mode" else "Custom Duration: $customMinutes minutes",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            GlassChip(
                                label = if (isInfiniteMode) "Unlimited ♾️" else "Set Infinite",
                                color = NeonLime,
                                selected = isInfiniteMode,
                                onClick = {
                                    isInfiniteMode = !isInfiniteMode
                                    isRunning = false
                                    infiniteSecondsElapsed = 0
                                }
                            )
                        }

                        if (!isInfiniteMode) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Slider(
                                value = customMinutes.toFloat(),
                                onValueChange = {
                                    customMinutes = it.toInt()
                                    totalSeconds = customMinutes * 60
                                    secondsRemaining = totalSeconds
                                },
                                valueRange = 5f..180f,
                                steps = 34,
                                colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("5m", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("60m", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("180m (3 hrs)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // Central Glassmorphic Countdown / Infinite Ring
        item(key = "focus_clock_dial") {
            Box(
                modifier = Modifier
                    .size(270.dp)
                    .graphicsLayer {
                        scaleX = pulseScale.value
                        scaleY = pulseScale.value
                    }
                    .testTag("pomodoro_clock_disc")
                    .drawBehind {
                        drawCircle(
                            brush = clockGlowBrush,
                            radius = size.minDimension * 0.72f
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
            modifier = Modifier
                .size(270.dp)
                .graphicsLayer {
                    scaleX = pulseScale.value
                    scaleY = pulseScale.value
                }
                .testTag("pomodoro_clock_disc")
                .drawBehind {
                    drawCircle(
                        brush = clockGlowBrush,
                        radius = size.minDimension * 0.72f
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(14.dp)) {
                val strokeWidth = 12.dp.toPx()
                val diameter = size.minDimension - strokeWidth
                val topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
                val arcSize = Size(diameter, diameter)

                drawArc(
                    color = Color(0x18FFFFFF),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                val sweep = 360f * animatedProgress
                drawArc(
                    brush = arcSweepBrush,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = timeFormatted,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (isRunning) NeonLime else NeonAmber)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isRunning) {
                            if (isInfiniteMode) "INFINITE FLOW" else "SYNCHRONIZING..."
                        } else "PAUSED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            letterSpacing = 1.5.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = if (isRunning) NeonLime else NeonAmber
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = selectedBrainwave.title,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = NeonCyan
                )
            }
        }

        // Play / Pause / Reset Controls
        item(key = "focus_playback_controls") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        isRunning = false
                        binauralEngine.stop()
                        if (isInfiniteMode) {
                            infiniteSecondsElapsed = 0
                        } else {
                            secondsRemaining = totalSeconds
                        }
                    },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0x14FFFFFF))
                        .border(1.dp, Color(0x2EFFFFFF), CircleShape)
                        .testTag("timer_reset_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Timer",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(primaryColor, secondaryColor)))
                        .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true, color = Color.White),
                            onClick = {
                                isRunning = !isRunning
                                if (!isRunning) {
                                    binauralEngine.stop()
                                }
                            }
                        )
                        .testTag("timer_play_pause_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Start",
                        tint = Color.Black,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                IconButton(
                    onClick = {
                        if (isInfiniteMode) {
                            infiniteSecondsElapsed += 300
                        } else {
                            secondsRemaining = 0
                        }
                    },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0x14FFFFFF))
                        .border(1.dp, Color(0x2EFFFFFF), CircleShape)
                        .testTag("timer_skip_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.FastForward,
                        contentDescription = "Advance",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // REAL BINAURAL BRAINWAVE TUNER SELECTOR
        item(key = "focus_brainwave_tuner") {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonViolet,
                glowRadius = 12.dp,
                testTag = "binaural_tuner_card"
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Psychology, "Brainwave", tint = NeonViolet, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Brainwave Frequency (Real 100% Entrainment)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BrainwaveType.values().forEach { wave ->
                            val isSelected = selectedBrainwave == wave
                            val waveColor = when (wave) {
                                BrainwaveType.LONE_WOLF, BrainwaveType.GAMMA_PEAK -> NeonPink
                                BrainwaveType.BETA_ACTIVE, BrainwaveType.SMR_CALM -> NeonAmber
                                BrainwaveType.ALPHA_FLOW -> NeonCyan
                                BrainwaveType.THETA_MEDITATION -> NeonViolet
                                BrainwaveType.DELTA_REST, BrainwaveType.SCHUMANN -> NeonLime
                                else -> NeonViolet
                            }
                            GlassChip(
                                label = wave.title,
                                color = waveColor,
                                selected = isSelected,
                                onClick = { selectedBrainwave = wave }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = selectedBrainwave.description,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = NeonCyan
                    )
                }
            }
        }

        // Ambient Soundscape Selector
        item(key = "focus_ambient_soundscape") {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                testTag = "ambient_sound_card"
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.GraphicEq, "Ambient Sounds", tint = NeonCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Acoustic Layer: ${selectedSoundscape.label}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SoundscapeType.values().forEach { sound ->
                            val isPicked = selectedSoundscape == sound
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isPicked) NeonCyan.copy(alpha = 0.25f) else Color(0x10FFFFFF))
                                    .border(1.dp, if (isPicked) NeonCyan else Color(0x18FFFFFF), RoundedCornerShape(12.dp))
                                    .clickable { selectedSoundscape = sound }
                                    .padding(horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(sound.iconEmoji, fontSize = 14.sp)
                                    Text(
                                        text = sound.label,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = if (isPicked) NeonCyan else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 20+ FEATURE MUSIC STUDIO DOCK BANNER
        item(key = "focus_music_studio_dock") {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonPink,
                glowRadius = 10.dp,
                onClick = onOpenMusicStudio,
                testTag = "open_music_studio_card"
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                            .background(NeonPink.copy(alpha = 0.2f))
                            .border(1.dp, NeonPink, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MusicNote, "Music Studio", tint = NeonPink, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Music & Soundscape Studio",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "20+ Features • 3-Track Mixer • Breath Pacer",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = NeonPink
                        )
                    }
                }

                GlassChip(
                    label = "OPEN",
                    color = NeonPink,
                    selected = true,
                    onClick = onOpenMusicStudio
                )
            }
        }
    }
}
