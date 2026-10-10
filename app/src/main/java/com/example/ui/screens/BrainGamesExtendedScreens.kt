package com.example.ui.screens

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import com.example.data.GamificationManager
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.util.AuraSoundManager
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun GameStatHUD(
    secondsLeft: Int,
    currentLives: Int,
    streak: Int,
    accentColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("⏱️ ${secondsLeft}s", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = accentColor)
        Text("❤️ x$currentLives", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NeonPink)
        Text("🔥 Streak: $streak", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NeonLime)
    }
}

@Composable
fun GameOverCard(
    score: Int,
    streak: Int,
    title: String,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit,
    accentColor: Color
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        glowColor = accentColor,
        glowRadius = 16.dp
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Score", fontSize = 11.sp, color = Color.Gray)
                    Text("$score", fontSize = 26.sp, fontWeight = FontWeight.Black, color = accentColor)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Max Streak", fontSize = 11.sp, color = Color.Gray)
                    Text("$streak", fontSize = 26.sp, fontWeight = FontWeight.Black, color = NeonLime)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            GlassButton(text = "Play Again 🔄", onClick = onPlayAgain, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            GlassButton(text = "Exit to Arena", onClick = onExit, isPrimary = false, modifier = Modifier.fillMaxWidth())
        }
    }
}

// =============================================================
// GAME 17: FULL SCREEN CHROMA CLASH INVERSION (EXTREME STROOP)
// =============================================================
@Composable
fun FullChromaClashScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(45) }
    var maxLives by remember { mutableIntStateOf(3) }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(45) }
    var isGameOver by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableIntStateOf(0) }
    var totalAnswers by remember { mutableIntStateOf(0) }

    val colors = listOf(
        Pair("CYAN", NeonCyan),
        Pair("PINK", NeonPink),
        Pair("LIME", NeonLime),
        Pair("AMBER", NeonAmber),
        Pair("VIOLET", NeonViolet)
    )

    var currentRule by remember { mutableStateOf("MATCH INK COLOR") }
    var wordText by remember { mutableStateOf("CYAN") }
    var wordColor by remember { mutableStateOf(NeonPink) }
    var options by remember { mutableStateOf(colors.shuffled().take(4)) }

    fun nextRound() {
        val rule = if (Random.nextBoolean()) "MATCH INK COLOR" else "MATCH TEXT WORD"
        val wordPair = colors.random()
        val otherColors = colors.filter { it.second != wordPair.second }
        val inkColor = otherColors.random().second

        currentRule = rule
        wordText = wordPair.first
        wordColor = inkColor

        val correctPair = if (rule == "MATCH INK COLOR") {
            colors.first { it.second == inkColor }
        } else {
            wordPair
        }

        val incorrect = colors.filter { it.first != correctPair.first }.shuffled().take(3)
        options = (listOf(correctPair) + incorrect).shuffled()
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) {
                        isGameOver = true
                        val acc = if (totalAnswers > 0) (correctAnswers * 100 / totalAnswers) else 80
                        GamificationManager.recordGameMatch("g17", "Chroma Clash Inversion", score, acc, 240)
                    }
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Chroma Clash Inversion",
            emoji = "💥",
            description = "Neural conflict & reaction duel. Invert your reflexes rapidly: tap the ink color OR the written word based on the active dynamic rule! Pure cognitive neuroplasticity.",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            accentColor = NeonPink,
            onStartGame = {
                score = 0
                streak = 0
                correctAnswers = 0
                totalAnswers = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                isGameOver = false
                nextRound()
                isStarted = true
            },
            onBack = onBack
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("💥 Chroma Clash Inversion", score, difficulty, onBack, NeonPink)

        if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Inhibition Duel Concluded!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    correctAnswers = 0
                    totalAnswers = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    isGameOver = false
                    nextRound()
                },
                onExit = onBack,
                accentColor = NeonPink
            )
        } else {
            GameStatHUD(secondsLeft, currentLives, streak, NeonPink)

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (currentRule == "MATCH INK COLOR") NeonPink.copy(alpha = 0.25f) else NeonCyan.copy(alpha = 0.25f))
                    .border(1.5.dp, if (currentRule == "MATCH INK COLOR") NeonPink else NeonCyan, RoundedCornerShape(14.dp))
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "RULE: $currentRule! ⚡",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (currentRule == "MATCH INK COLOR") NeonPink else NeonCyan
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
                    .height(150.dp),
                glowColor = wordColor,
                glowRadius = 16.dp
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = wordText,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Black,
                        color = wordColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                options.chunked(2).forEach { rowOptions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowOptions.forEach { opt ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0x18FFFFFF))
                                    .border(1.5.dp, opt.second, RoundedCornerShape(14.dp))
                                    .clickable {
                                        totalAnswers++
                                        val isCorrect = if (currentRule == "MATCH INK COLOR") {
                                            opt.second == wordColor
                                        } else {
                                            opt.first == wordText
                                        }

                                        if (isCorrect) {
                                            correctAnswers++
                                            score += 15
                                            streak++
                                            AuraSoundManager.playSuccessSound()
                                            nextRound()
                                        } else {
                                            streak = 0
                                            if (maxLives != -1) {
                                                currentLives--
                                                if (currentLives <= 0) {
                                                    isGameOver = true
                                                    val acc = if (totalAnswers > 0) (correctAnswers * 100 / totalAnswers) else 60
                                                    GamificationManager.recordGameMatch("g17", "Chroma Clash Inversion", score, acc, 310)
                                                }
                                            }
                                            nextRound()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = opt.first,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = opt.second
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// =============================================================
// GAME 18: FULL SCREEN TARGET LOCK RADAR (SPATIAL VISUAL TRACKING)
// =============================================================
data class RadarTrackTarget(
    val id: Int,
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val isTarget: Boolean
)

@Composable
fun FullTargetLockRadarScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var isGameOver by remember { mutableStateOf(false) }

    var radarPhase by remember { mutableIntStateOf(0) }
    var targets by remember { mutableStateOf<List<RadarTrackTarget>>(emptyList()) }
    var targetCount by remember { mutableIntStateOf(5) }
    var targetIndex by remember { mutableIntStateOf(0) }

    fun setupRound() {
        val count = when (difficulty) {
            "Easy" -> 4
            "Hard" -> 6
            "Master" -> 7
            else -> 5
        }
        targetCount = count
        val locked = Random.nextInt(count)
        targetIndex = locked

        val newTargets = mutableListOf<RadarTrackTarget>()
        for (i in 0 until count) {
            val angle = (i * (360f / count)) * (Math.PI / 180f)
            val dist = Random.nextFloat() * 70f + 25f
            val x = (Math.cos(angle) * dist).toFloat()
            val y = (Math.sin(angle) * dist).toFloat()
            val speed = 2.4f + Random.nextFloat() * 1.8f
            val vAngle = Random.nextFloat() * 360f * (Math.PI / 180f)
            val vx = (Math.cos(vAngle) * speed).toFloat()
            val vy = (Math.sin(vAngle) * speed).toFloat()
            newTargets.add(RadarTrackTarget(i, x, y, vx, vy, i == locked))
        }
        targets = newTargets
        radarPhase = 0
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) {
                        isGameOver = true
                        GamificationManager.recordGameMatch("g18", "Target Lock Radar", score, 92, 210)
                    }
                }
            }
        }
    }

    LaunchedEffect(isStarted, radarPhase) {
        if (isStarted && !isGameOver) {
            if (radarPhase == 0) {
                delay(2000L)
                radarPhase = 1
            } else if (radarPhase == 1) {
                val steps = 30
                for (s in 0 until steps) {
                    delay(100L)
                    targets.forEach { t ->
                        t.x += t.vx
                        t.y += t.vy
                        val r = kotlin.math.sqrt(t.x * t.x + t.y * t.y)
                        if (r > 105f) {
                            t.vx = -t.vx
                            t.vy = -t.vy
                        }
                    }
                }
                radarPhase = 2
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Target Lock Radar",
            emoji = "🎯",
            description = "High-velocity spatial trajectory tracking. Memorize which tactical drone locks onto your radar reticle, follow its path as drones shuffle and orbit, then identify the target!",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            accentColor = NeonLime,
            onStartGame = {
                score = 0
                streak = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                isGameOver = false
                setupRound()
                isStarted = true
            },
            onBack = onBack
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("🎯 Target Lock Radar", score, difficulty, onBack, NeonLime)

        if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Radar Sweep Concluded!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    isGameOver = false
                    setupRound()
                },
                onExit = onBack,
                accentColor = NeonLime
            )
        } else {
            GameStatHUD(secondsLeft, currentLives, streak, NeonLime)

            Spacer(modifier = Modifier.height(14.dp))

            val bannerText = when (radarPhase) {
                0 -> "LOCK ACQUIRED: Focus on the Glowing Target! 🎯"
                1 -> "TRACKING: Drones are shuffling trajectories... 🛸"
                else -> "IDENTIFY TARGET: Tap the tracked drone! ⚡"
            }
            Text(
                text = bannerText,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = if (radarPhase == 2) NeonLime else NeonCyan
            )

            Spacer(modifier = Modifier.height(18.dp))

            GlassCard(
                modifier = Modifier.size(280.dp),
                glowColor = NeonLime,
                glowRadius = 12.dp
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cx = size.width / 2f
                        val cy = size.height / 2f

                        drawCircle(color = NeonLime.copy(alpha = 0.25f), radius = 120.dp.toPx(), center = Offset(cx, cy))
                        drawCircle(color = NeonLime.copy(alpha = 0.15f), radius = 80.dp.toPx(), center = Offset(cx, cy))
                        drawCircle(color = NeonLime.copy(alpha = 0.10f), radius = 40.dp.toPx(), center = Offset(cx, cy))
                        drawLine(color = NeonLime.copy(alpha = 0.2f), start = Offset(cx - 120.dp.toPx(), cy), end = Offset(cx + 120.dp.toPx(), cy))
                        drawLine(color = NeonLime.copy(alpha = 0.2f), start = Offset(cx, cy - 120.dp.toPx()), end = Offset(cx, cy + 120.dp.toPx()))

                        targets.forEach { t ->
                            val tx = cx + t.x.dp.toPx()
                            val ty = cy + t.y.dp.toPx()

                            if (radarPhase == 0 && t.isTarget) {
                                drawCircle(color = NeonPink, radius = 18.dp.toPx(), center = Offset(tx, ty))
                                drawCircle(color = Color.White, radius = 10.dp.toPx(), center = Offset(tx, ty))
                            } else {
                                drawCircle(color = NeonLime, radius = 10.dp.toPx(), center = Offset(tx, ty))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (radarPhase == 2) {
                Text("Select the Tracked Drone:", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    targets.forEachIndexed { idx, t ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NeonLime.copy(alpha = 0.18f))
                                .border(1.5.dp, NeonLime, RoundedCornerShape(12.dp))
                                .clickable {
                                    if (t.isTarget) {
                                        score += 25
                                        streak++
                                        AuraSoundManager.playSuccessSound()
                                        setupRound()
                                    } else {
                                        streak = 0
                                        if (maxLives != -1) {
                                            currentLives--
                                            if (currentLives <= 0) {
                                                isGameOver = true
                                                GamificationManager.recordGameMatch("g18", "Target Lock Radar", score, 75, 230)
                                            }
                                        }
                                        setupRound()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🛸 #${idx + 1}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                        }
                    }
                }
            }
        }
    }
}

// =============================================================
// GAME 19: FULL SCREEN PITCH MATCH EAR TRAINER (ACOUSTIC IQ)
// =============================================================
@Composable
fun FullPitchMatchScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var isGameOver by remember { mutableStateOf(false) }

    val tones = listOf(
        Pair("Low Bass C", ToneGenerator.TONE_DTMF_1),
        Pair("Mid Alpha E", ToneGenerator.TONE_DTMF_4),
        Pair("High Gamma G", ToneGenerator.TONE_DTMF_7),
        Pair("Ultra Peak B", ToneGenerator.TONE_DTMF_9)
    )

    var targetTone by remember { mutableStateOf(tones.first()) }
    var options by remember { mutableStateOf(tones) }

    fun playToneSound(toneId: Int) {
        try {
            val gen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
            gen.startTone(toneId, 250)
        } catch (_: Exception) {}
    }

    fun setupRound() {
        val picked = tones.random()
        targetTone = picked
        options = tones.shuffled()
        playToneSound(picked.second)
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) {
                        isGameOver = true
                        GamificationManager.recordGameMatch("g19", "Pitch Match Ear Trainer", score, 90, 260)
                    }
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Pitch Match Ear Trainer",
            emoji = "🎵",
            description = "Neuro-acoustic frequency discrimination. Listen to the golden master reference pitch, test the candidate buttons, and identify the exact pitch harmonic!",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            accentColor = NeonViolet,
            onStartGame = {
                score = 0
                streak = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                isGameOver = false
                setupRound()
                isStarted = true
            },
            onBack = onBack
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("🎵 Pitch Match Ear Trainer", score, difficulty, onBack, NeonViolet)

        if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Acoustic Session Concluded!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    isGameOver = false
                    setupRound()
                },
                onExit = onBack,
                accentColor = NeonViolet
            )
        } else {
            GameStatHUD(secondsLeft, currentLives, streak, NeonViolet)

            Spacer(modifier = Modifier.height(20.dp))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                glowColor = NeonViolet,
                glowRadius = 14.dp,
                onClick = { playToneSound(targetTone.second) }
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🔊 TAP TO HEAR MASTER PITCH", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = NeonViolet)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Listen carefully to the frequency signature", fontSize = 11.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(14.dp))
                    GlassButton(
                        text = "Play Pitch Wave 🎶",
                        onClick = { playToneSound(targetTone.second) },
                        modifier = Modifier.width(180.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text("Which frequency matches the sound?", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                options.forEach { opt ->
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        glowColor = NeonViolet,
                        glowRadius = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(opt.first, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                GlassButton(
                                    text = "Preview 🔊",
                                    onClick = { playToneSound(opt.second) },
                                    isPrimary = false,
                                    modifier = Modifier.width(95.dp)
                                )
                                GlassButton(
                                    text = "Match! 🎯",
                                    onClick = {
                                        if (opt.second == targetTone.second) {
                                            score += 20
                                            streak++
                                            AuraSoundManager.playSuccessSound()
                                            setupRound()
                                        } else {
                                            streak = 0
                                            if (maxLives != -1) {
                                                currentLives--
                                                if (currentLives <= 0) {
                                                    isGameOver = true
                                                    GamificationManager.recordGameMatch("g19", "Pitch Match Ear Trainer", score, 70, 290)
                                                }
                                            }
                                            setupRound()
                                        }
                                    },
                                    modifier = Modifier.width(90.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// =============================================================
// GAME 20: FULL SCREEN CODEBREAKER CIPHER (CRYPTANALYSIS MATRIX)
// =============================================================
@Composable
fun FullCodebreakerCipherScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(50) }
    var maxLives by remember { mutableIntStateOf(3) }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(50) }
    var isGameOver by remember { mutableStateOf(false) }

    val allSymbols = listOf(
        Pair("⚡", 4),
        Pair("⬡", 7),
        Pair("⚛", 2),
        Pair("✦", 9),
        Pair("◈", 5),
        Pair("🌀", 8)
    )

    var currentKey by remember { mutableStateOf(allSymbols) }
    var targetCode by remember { mutableStateOf(listOf(allSymbols[0], allSymbols[1], allSymbols[2])) }
    var userEnteredDigits by remember { mutableStateOf("") }

    fun setupRound() {
        val shuffledKey = allSymbols.shuffled()
        currentKey = shuffledKey
        val len = when (difficulty) {
            "Easy" -> 3
            "Hard" -> 4
            "Master" -> 5
            else -> 3
        }
        targetCode = shuffledKey.shuffled().take(len)
        userEnteredDigits = ""
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) {
                        isGameOver = true
                        GamificationManager.recordGameMatch("g20", "Codebreaker Cipher", score, 94, 195)
                    }
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Codebreaker Cipher",
            emoji = "🕵️",
            description = "Futuristic glyph cryptanalysis. Decipher encrypted alien glyph sequences under high pressure using the live substitution key before the timer runs out!",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            accentColor = NeonAmber,
            onStartGame = {
                score = 0
                streak = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                isGameOver = false
                setupRound()
                isStarted = true
            },
            onBack = onBack
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("🕵️ Codebreaker Cipher", score, difficulty, onBack, NeonAmber)

        if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Cryptanalysis Concluded!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    isGameOver = false
                    setupRound()
                },
                onExit = onBack,
                accentColor = NeonAmber
            )
        } else {
            GameStatHUD(secondsLeft, currentLives, streak, NeonAmber)

            Spacer(modifier = Modifier.height(14.dp))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                glowColor = NeonAmber,
                glowRadius = 6.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("LIVE CIPHER TRANSLATION KEY:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonAmber)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        currentKey.forEach { (sym, digit) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(sym, fontSize = 20.sp)
                                Text("=$digit", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                glowColor = NeonCyan,
                glowRadius = 12.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("DECRYPT TARGET GLYPHS:", fontSize = 11.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        targetCode.forEach { (sym, _) ->
                            Text(sym, fontSize = 38.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Entered: ${userEnteredDigits.ifEmpty { "____" }}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 4.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("C", "0", "OK")
                ).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { key ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (key == "OK") NeonAmber.copy(alpha = 0.3f) else Color(0x18FFFFFF))
                                    .border(1.dp, if (key == "OK") NeonAmber else Color(0x30FFFFFF), RoundedCornerShape(10.dp))
                                    .clickable {
                                        when (key) {
                                            "C" -> userEnteredDigits = ""
                                            "OK" -> {
                                                val expected = targetCode.map { it.second.toString() }.joinToString("")
                                                if (userEnteredDigits == expected) {
                                                    score += 30
                                                    streak++
                                                    AuraSoundManager.playSuccessSound()
                                                    setupRound()
                                                } else {
                                                    streak = 0
                                                    if (maxLives != -1) {
                                                        currentLives--
                                                        if (currentLives <= 0) {
                                                            isGameOver = true
                                                            GamificationManager.recordGameMatch("g20", "Codebreaker Cipher", score, 78, 220)
                                                        }
                                                    }
                                                    userEnteredDigits = ""
                                                }
                                            }
                                            else -> {
                                                if (userEnteredDigits.length < targetCode.size) {
                                                    userEnteredDigits += key
                                                    if (userEnteredDigits.length == targetCode.size) {
                                                        val expected = targetCode.map { it.second.toString() }.joinToString("")
                                                        if (userEnteredDigits == expected) {
                                                            score += 30
                                                            streak++
                                                            AuraSoundManager.playSuccessSound()
                                                            setupRound()
                                                        } else {
                                                            streak = 0
                                                            if (maxLives != -1) {
                                                                currentLives--
                                                                if (currentLives <= 0) {
                                                                    isGameOver = true
                                                                    GamificationManager.recordGameMatch("g20", "Codebreaker Cipher", score, 78, 220)
                                                                }
                                                            }
                                                            userEnteredDigits = ""
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = key,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (key == "OK") NeonAmber else Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// =============================================================
// GAME 21: FULL SCREEN QUANTUM FLOW MAZE (SPATIAL PATHFINDING)
// =============================================================
@Composable
fun FullQuantumFlowMazeScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Normal") }
    var timeLimit by remember { mutableIntStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var isGameOver by remember { mutableStateOf(false) }

    // 5x5 Grid representation: 0=path, 1=wall, 2=start, 3=goal
    var playerX by remember { mutableIntStateOf(0) }
    var playerY by remember { mutableIntStateOf(0) }

    val layouts = remember {
        listOf(
            listOf(
                listOf(0, 0, 1, 0, 0),
                listOf(1, 0, 0, 0, 1),
                listOf(0, 0, 1, 0, 0),
                listOf(0, 1, 0, 0, 0),
                listOf(0, 0, 0, 1, 3)
            ),
            listOf(
                listOf(0, 0, 0, 1, 0),
                listOf(0, 1, 0, 1, 0),
                listOf(0, 1, 0, 0, 0),
                listOf(1, 1, 1, 0, 1),
                listOf(0, 0, 0, 0, 3)
            ),
            listOf(
                listOf(0, 1, 0, 0, 0),
                listOf(0, 0, 0, 1, 0),
                listOf(1, 0, 1, 0, 0),
                listOf(0, 0, 0, 1, 0),
                listOf(0, 1, 0, 0, 3)
            ),
            listOf(
                listOf(0, 0, 0, 0, 1),
                listOf(1, 1, 0, 0, 0),
                listOf(0, 0, 0, 1, 0),
                listOf(0, 1, 0, 1, 0),
                listOf(0, 0, 0, 0, 3)
            )
        )
    }

    var mazeIndex by remember { mutableIntStateOf(0) }
    var mazeGrid by remember { mutableStateOf(layouts[0]) }

    fun setupNewMaze() {
        playerX = 0
        playerY = 0
        mazeIndex = (mazeIndex + 1) % layouts.size
        mazeGrid = layouts[mazeIndex]
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver && timeLimit != -1) {
            while (secondsLeft > 0 && !isGameOver) {
                kotlinx.coroutines.delay(1000)
                secondsLeft--
            }
            if (secondsLeft <= 0) {
                isGameOver = true
                GamificationManager.recordGameMatch("g21", "Quantum Flow Maze", score, 90, 240)
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Quantum Flow Maze",
            emoji = "🌀",
            description = "Navigate the oscillating quantum photon through dynamic labyrinth barriers to reach the exit portal. Boosts visual-spatial reasoning and mental rotation speed!",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            accentColor = NeonCyan,
            onStartGame = {
                score = 0
                streak = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                isGameOver = false
                setupNewMaze()
                isStarted = true
            },
            onBack = onBack
        )
        return
    }

    fun move(dx: Int, dy: Int) {
        val nx = playerX + dx
        val ny = playerY + dy
        if (nx in 0..4 && ny in 0..4) {
            if (mazeGrid[ny][nx] != 1) {
                playerX = nx
                playerY = ny
                AuraSoundManager.playTapSound(context)
                if (mazeGrid[ny][nx] == 3) {
                    score += 35
                    streak++
                    AuraSoundManager.playSuccessSound()
                    setupNewMaze()
                }
            } else {
                AuraSoundManager.playTapSound(context)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF060913)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("🌀 Quantum Flow Maze", score, difficulty, onBack, NeonCyan)

        if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Quantum Sequence Finished!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    isGameOver = false
                    setupNewMaze()
                },
                onExit = onBack,
                accentColor = NeonCyan
            )
        } else {
            GameStatHUD(secondsLeft, currentLives, streak, NeonCyan)

            Spacer(modifier = Modifier.height(14.dp))

            GlassCard(
                modifier = Modifier.size(280.dp),
                glowColor = NeonCyan,
                glowRadius = 14.dp
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (r in 0 until 5) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                for (c in 0 until 5) {
                                    val isPlayer = (r == playerY && c == playerX)
                                    val isGoal = (mazeGrid[r][c] == 3)
                                    val isWall = (mazeGrid[r][c] == 1)

                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                when {
                                                    isPlayer -> NeonCyan.copy(alpha = 0.5f)
                                                    isGoal -> NeonLime.copy(alpha = 0.4f)
                                                    isWall -> Color(0x35FF2A85)
                                                    else -> Color(0x14FFFFFF)
                                                }
                                            )
                                            .border(
                                                1.5.dp,
                                                when {
                                                    isPlayer -> NeonCyan
                                                    isGoal -> NeonLime
                                                    isWall -> NeonPink
                                                    else -> Color(0x20FFFFFF)
                                                },
                                                RoundedCornerShape(8.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        when {
                                            isPlayer -> Text("⚛️", fontSize = 20.sp)
                                            isGoal -> Text("🎯", fontSize = 20.sp)
                                            isWall -> Text("🧱", fontSize = 16.sp)
                                            else -> Text("·", color = Color(0x40FFFFFF), fontSize = 18.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0x2000F2FE))
                        .border(1.5.dp, NeonCyan, CircleShape)
                        .clickable { move(0, -1) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("▲", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(40.dp)) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0x2000F2FE))
                            .border(1.5.dp, NeonCyan, CircleShape)
                            .clickable { move(-1, 0) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("◀", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0x2000F2FE))
                            .border(1.5.dp, NeonCyan, CircleShape)
                            .clickable { move(1, 0) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("▶", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0x2000F2FE))
                        .border(1.5.dp, NeonCyan, CircleShape)
                        .clickable { move(0, 1) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("▼", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
        }
    }
}

// =============================================================
// GAME 22: FULL SCREEN NEURO BALANCE TILT (EQUILIBRIUM & STABILITY)
// =============================================================
@Composable
fun FullNeuroBalanceTiltScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Normal") }
    var timeLimit by remember { mutableIntStateOf(45) }
    var maxLives by remember { mutableIntStateOf(3) }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(45) }
    var isGameOver by remember { mutableStateOf(false) }

    var ballPos by remember { mutableFloatStateOf(0f) }
    var ballVelocity by remember { mutableFloatStateOf(0.015f) }
    var stabilityPercent by remember { mutableIntStateOf(0) }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            val driftFactor = if (difficulty == "Hard") 0.035f else 0.02f
            while (!isGameOver) {
                kotlinx.coroutines.delay(40)
                val drift = (Math.random().toFloat() - 0.5f) * driftFactor
                ballVelocity = (ballVelocity + drift).coerceIn(-0.08f, 0.08f)
                ballPos = (ballPos + ballVelocity).coerceIn(-1f, 1f)

                if (abs(ballPos) <= 0.25f) {
                    stabilityPercent = (stabilityPercent + 1).coerceAtMost(100)
                    if (stabilityPercent >= 100) {
                        score += 50
                        streak++
                        stabilityPercent = 0
                        ballPos = 0f
                        ballVelocity = 0f
                        AuraSoundManager.playSuccessSound()
                    }
                } else if (abs(ballPos) >= 0.95f) {
                    stabilityPercent = (stabilityPercent - 10).coerceAtLeast(0)
                    streak = 0
                    if (maxLives != -1) {
                        currentLives--
                        if (currentLives <= 0) {
                            isGameOver = true
                            GamificationManager.recordGameMatch("g22", "Neuro Balance Tilt", score, 85, 190)
                        }
                    }
                    ballPos = 0f
                    ballVelocity = 0f
                }
            }
        }
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver && timeLimit != -1) {
            while (secondsLeft > 0 && !isGameOver) {
                kotlinx.coroutines.delay(1000)
                secondsLeft--
            }
            if (secondsLeft <= 0) {
                isGameOver = true
                GamificationManager.recordGameMatch("g22", "Neuro Balance Tilt", score, 88, 190)
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Neuro Balance Tilt",
            emoji = "⚖️",
            description = "Stabilize the chaotic kinetic orb inside the sweet spot using lateral impulse thrusters. Direct training for emotional stability, composure and motor equilibrium!",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            accentColor = NeonLime,
            onStartGame = {
                score = 0
                streak = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                ballPos = 0f
                ballVelocity = 0.015f
                stabilityPercent = 0
                isGameOver = false
                isStarted = true
            },
            onBack = onBack
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070B13)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("⚖️ Neuro Balance Tilt", score, difficulty, onBack, NeonLime)

        if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Calibration Complete!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    ballPos = 0f
                    ballVelocity = 0.015f
                    stabilityPercent = 0
                    isGameOver = false
                },
                onExit = onBack,
                accentColor = NeonLime
            )
        } else {
            GameStatHUD(secondsLeft, currentLives, streak, NeonLime)

            Spacer(modifier = Modifier.height(14.dp))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                glowColor = NeonLime,
                glowRadius = 10.dp
            ) {
                Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("HARMONIC EQUILIBRIUM", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                        Text("$stabilityPercent% STABLE", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(Color(0x20FFFFFF))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = (stabilityPercent / 100f).coerceIn(0f, 1f))
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(Brush.horizontalGradient(listOf(NeonCyan, NeonLime)))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF0C121E))
                    .border(1.5.dp, Color(0x30FFFFFF), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val midY = h / 2f

                    drawRect(
                        brush = Brush.radialGradient(
                            listOf(NeonLime.copy(alpha = 0.25f), Color.Transparent),
                            center = Offset(w / 2f, midY),
                            radius = w * 0.25f
                        ),
                        topLeft = Offset(w * 0.35f, 0f),
                        size = androidx.compose.ui.geometry.Size(w * 0.3f, h)
                    )

                    drawLine(
                        color = Color(0x40FFFFFF),
                        start = Offset(20f, midY),
                        end = Offset(w - 20f, midY),
                        strokeWidth = 3f
                    )

                    drawLine(
                        color = NeonLime,
                        start = Offset(w / 2f, midY - 20f),
                        end = Offset(w / 2f, midY + 20f),
                        strokeWidth = 2f
                    )

                    val ballX = (w / 2f) + (ballPos * (w / 2f - 30f))
                    val inZone = abs(ballPos) <= 0.25f
                    val orbColor = if (inZone) NeonLime else NeonPink

                    drawCircle(
                        brush = Brush.radialGradient(listOf(orbColor, Color.Transparent)),
                        radius = 28f,
                        center = Offset(ballX, midY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 12f,
                        center = Offset(ballX, midY)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0x2800E676))
                        .border(2.dp, NeonLime, CircleShape)
                        .clickable {
                            ballVelocity -= 0.05f
                            AuraSoundManager.playTapSound(context)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("◀", fontSize = 24.sp, color = NeonLime, fontWeight = FontWeight.Bold)
                        Text("NUDGE", fontSize = 9.sp, color = Color.White)
                    }
                }

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0x2800E676))
                        .border(2.dp, NeonLime, CircleShape)
                        .clickable {
                            ballVelocity += 0.05f
                            AuraSoundManager.playTapSound(context)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("▶", fontSize = 24.sp, color = NeonLime, fontWeight = FontWeight.Bold)
                        Text("NUDGE", fontSize = 9.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

// =========================================================================
// GAME 23: SYNAPSE SPEED SORT (EXECUTIVE CONTROL & RAPID CATEGORIZATION)
// =========================================================================
data class SynapseItem(val id: Int, val label: String, val isCategoryA: Boolean)

@Composable
fun FullSynapseSpeedSortScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(45) }
    var isGameOver by remember { mutableStateOf(false) }
    var ruleMode by remember { mutableIntStateOf(0) } // 0: Even vs Odd, 1: High (>50) vs Low (<=50)
    var currentItem by remember { mutableStateOf(generateSynapseItem(ruleMode)) }
    var feedbackColor by remember { mutableStateOf<Color?>(null) }

    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (secondsLeft > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g23", "Synapse Speed Sort", score, 92, 195)
        }
    }

    LaunchedEffect(feedbackColor) {
        if (feedbackColor != null) {
            delay(250)
            feedbackColor = null
        }
    }

    fun handleSort(choseA: Boolean) {
        if (isGameOver) return
        val isCorrect = choseA == currentItem.isCategoryA
        if (isCorrect) {
            score += 15 + streak * 3
            streak++
            AuraSoundManager.playTapSound(context)
        } else {
            lives--
            streak = 0
            if (lives <= 0) {
                isGameOver = true
                GamificationManager.recordGameMatch("g23", "Synapse Speed Sort", score, 85, 230)
            }
        }
        feedbackColor = if (isCorrect) NeonLime else NeonPink

        // Dynamic rule shifting every 5 matches
        if (score % 60 == 0) {
            ruleMode = (ruleMode + 1) % 2
        }
        currentItem = generateSynapseItem(ruleMode)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070B14))
            .padding(top = 40.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameStatHUD(secondsLeft, lives, streak, NeonCyan)

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "⚡ Synapse Speed Sort",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = if (ruleMode == 0) "RULE: Even [Left] ◀ vs ▶ [Right] Odd" else "RULE: Low (≤50) [Left] ◀ vs ▶ [Right] High (>50)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = NeonAmber
        )

        Spacer(modifier = Modifier.height(30.dp))

        if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Synapse Overclock Complete! 🧠",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 45
                    ruleMode = 0
                    currentItem = generateSynapseItem(0)
                    isGameOver = false
                },
                onExit = onBack,
                accentColor = NeonCyan
            )
        } else {
            // Quantum Floating Stimulus
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf((feedbackColor ?: NeonCyan).copy(alpha = 0.35f), Color(0x10FFFFFF))
                        )
                    )
                    .border(2.dp, feedbackColor ?: NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = currentItem.label,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Score: $score",
                        fontSize = 12.sp,
                        color = NeonCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(44.dp))

            // Left / Right Sorting Bins
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x2200E5FF))
                        .border(1.5.dp, NeonCyan, RoundedCornerShape(16.dp))
                        .clickable { handleSort(true) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("◀ SORT LEFT", fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 13.sp)
                        Text(if (ruleMode == 0) "EVEN NUMBERS" else "LOW (≤50)", fontSize = 10.sp, color = Color.White)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x22FF2A85))
                        .border(1.5.dp, NeonPink, RoundedCornerShape(16.dp))
                        .clickable { handleSort(false) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("SORT RIGHT ▶", fontWeight = FontWeight.Bold, color = NeonPink, fontSize = 13.sp)
                        Text(if (ruleMode == 0) "ODD NUMBERS" else "HIGH (>50)", fontSize = 10.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

fun generateSynapseItem(ruleMode: Int): SynapseItem {
    val number = Random.nextInt(2, 99)
    val isCatA = if (ruleMode == 0) (number % 2 == 0) else (number <= 50)
    return SynapseItem(number, "$number", isCatA)
}

// =========================================================================
// GAME 24: SPATIAL PATH TRACE (VISUOSPATIAL WORKING MEMORY)
// =========================================================================
@Composable
fun FullSpatialPathTraceScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var pathLength by remember { mutableIntStateOf(4) }
    var isDemonstrating by remember { mutableStateOf(true) }
    var targetPath by remember { mutableStateOf(generateRandomPath(4)) }
    var playerInputIndex by remember { mutableIntStateOf(0) }
    var activeFlashNode by remember { mutableStateOf<Int?>(null) }
    var isGameOver by remember { mutableStateOf(false) }

    LaunchedEffect(targetPath) {
        isDemonstrating = true
        playerInputIndex = 0
        delay(600)
        for (node in targetPath) {
            activeFlashNode = node
            AuraSoundManager.playTapSound(context)
            delay(550)
            activeFlashNode = null
            delay(250)
        }
        isDemonstrating = false
    }

    fun handleNodeTap(nodeIndex: Int) {
        if (isDemonstrating || isGameOver) return
        AuraSoundManager.playTapSound(context)
        if (nodeIndex == targetPath[playerInputIndex]) {
            playerInputIndex++
            if (playerInputIndex == targetPath.size) {
                // Completed Path!
                score += targetPath.size * 25
                streak++
                pathLength = (pathLength + 1).coerceAtMost(8)
                targetPath = generateRandomPath(pathLength)
            }
        } else {
            lives--
            streak = 0
            if (lives <= 0) {
                isGameOver = true
                GamificationManager.recordGameMatch("g24", "Spatial Path Trace", score, 88, 240)
            } else {
                targetPath = generateRandomPath(pathLength)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF080D1A))
            .padding(top = 40.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameStatHUD(pathLength * 5, lives, streak, NeonLime)

        Spacer(modifier = Modifier.height(14.dp))

        Text("✨ Spatial Path Trace", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(
            text = if (isDemonstrating) "MEMORIZE CONSTELLATION PATH 🌟" else "TRACE PATH FROM MEMORY (${playerInputIndex}/${targetPath.size})",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDemonstrating) NeonAmber else NeonLime
        )

        Spacer(modifier = Modifier.height(26.dp))

        if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Constellation Traced!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    pathLength = 4
                    targetPath = generateRandomPath(4)
                    isGameOver = false
                },
                onExit = onBack,
                accentColor = NeonLime
            )
        } else {
            // 4x4 Constellation Matrix Grid
            Column(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x18FFFFFF))
                    .border(1.5.dp, Color(0x30FFFFFF), RoundedCornerShape(20.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                for (r in 0 until 4) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (c in 0 until 4) {
                            val nodeIdx = r * 4 + c
                            val isFlashed = activeFlashNode == nodeIdx
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(if (isFlashed) NeonLime else Color(0x20FFFFFF))
                                    .border(1.5.dp, if (isFlashed) NeonLime else Color(0x40FFFFFF), CircleShape)
                                    .clickable { handleNodeTap(nodeIdx) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isFlashed) "✦" else "•",
                                    fontSize = 18.sp,
                                    color = if (isFlashed) Color.Black else Color(0x80FFFFFF)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun generateRandomPath(length: Int): List<Int> {
    val list = mutableListOf<Int>()
    var prev = Random.nextInt(0, 16)
    list.add(prev)
    while (list.size < length) {
        val next = Random.nextInt(0, 16)
        if (next != prev) {
            list.add(next)
            prev = next
        }
    }
    return list
}

// =========================================================================
// GAME 25: N-BACK AUDIO ECHO (ACOUSTIC WORKING MEMORY)
// =========================================================================
data class AudioEchoStimulus(val toneLabel: String, val frequencyHz: Int, val emoji: String)

@Composable
fun FullNBackAudioEchoScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val stimuli = remember {
        listOf(
            AudioEchoStimulus("Alpha 10Hz", 440, "🎵"),
            AudioEchoStimulus("Theta 6Hz", 330, "🌊"),
            AudioEchoStimulus("Gamma 40Hz", 550, "⚡"),
            AudioEchoStimulus("Delta 2Hz", 220, "🔔")
        )
    }

    var history by remember { mutableStateOf(listOf<AudioEchoStimulus>()) }
    var currentItem by remember { mutableStateOf<AudioEchoStimulus?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var trialsLeft by remember { mutableIntStateOf(20) }
    var isGameOver by remember { mutableStateOf(false) }

    fun nextStimulus() {
        if (trialsLeft <= 0) {
            isGameOver = true
            GamificationManager.recordGameMatch("g25", "N-Back Audio Echo", score, 90, 210)
            return
        }
        val isMatchPlanned = Random.nextFloat() < 0.35f && history.size >= 2
        val chosen = if (isMatchPlanned) history[history.size - 2] else stimuli.random()
        currentItem = chosen
        history = history + chosen
        trialsLeft--
        AuraSoundManager.playTapSound(context)
    }

    LaunchedEffect(Unit) {
        nextStimulus()
    }

    fun handleResponse(playerClaimedMatch: Boolean) {
        if (isGameOver || history.size < 3) {
            nextStimulus()
            return
        }
        val actualMatch = history[history.size - 1] == history[history.size - 3]
        if (playerClaimedMatch == actualMatch) {
            score += 20 + streak * 4
            streak++
        } else {
            streak = 0
        }
        nextStimulus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E18))
            .padding(top = 40.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameStatHUD(trialsLeft * 3, 3, streak, NeonViolet)

        Spacer(modifier = Modifier.height(14.dp))

        Text("🎧 2-Back Audio Echo", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Does this sound match 2 steps ago?", fontSize = 12.sp, color = NeonViolet)

        Spacer(modifier = Modifier.height(30.dp))

        if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Acoustic Memory Mastered!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    trialsLeft = 20
                    history = emptyList()
                    isGameOver = false
                    nextStimulus()
                },
                onExit = onBack,
                accentColor = NeonViolet
            )
        } else {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color(0x207F00FF))
                    .border(2.dp, NeonViolet, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(currentItem?.emoji ?: "🎵", fontSize = 38.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(currentItem?.toneLabel ?: "", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x2200E676))
                        .border(1.5.dp, NeonLime, RoundedCornerShape(16.dp))
                        .clickable { handleResponse(true) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("MATCH (2-Back)", fontWeight = FontWeight.Bold, color = NeonLime, fontSize = 13.sp)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x22FFFFFF))
                        .border(1.5.dp, Color(0x40FFFFFF), RoundedCornerShape(16.dp))
                        .clickable { handleResponse(false) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("NO MATCH", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                }
            }
        }
    }
}

// =========================================================================
// GAME 26: FOCUS RADAR SACCADE (VISUAL ATTENTION & INHIBITORY CONTROL)
// =========================================================================
@Composable
fun FullFocusRadarSaccadeScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(40) }
    var isGameOver by remember { mutableStateOf(false) }

    var targetSector by remember { mutableIntStateOf(0) } // 0..7 sectors
    var isDecoy by remember { mutableStateOf(false) } // Decoy vs Real Target

    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (secondsLeft > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g26", "Focus Radar Saccade", score, 94, 185)
        }
    }

    LaunchedEffect(isGameOver) {
        while (!isGameOver) {
            targetSector = Random.nextInt(0, 8)
            isDecoy = Random.nextFloat() < 0.35f
            delay(1200)
        }
    }

    fun handleSectorTap(sectorIdx: Int) {
        if (isGameOver) return
        AuraSoundManager.playTapSound(context)
        if (sectorIdx == targetSector) {
            if (isDecoy) {
                // Impulsive error! Tapped a decoy!
                lives--
                streak = 0
                if (lives <= 0) isGameOver = true
            } else {
                score += 20 + streak * 4
                streak++
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF060B12))
            .padding(top = 40.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameStatHUD(secondsLeft, lives, streak, NeonCyan)

        Spacer(modifier = Modifier.height(14.dp))

        Text("🎯 Focus Radar Saccade", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Tap Green Targets 🟢 • Ignore Red Decoys 🔴", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonAmber)

        Spacer(modifier = Modifier.height(30.dp))

        if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Visual Reflexes Certified!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 40
                    isGameOver = false
                },
                onExit = onBack,
                accentColor = NeonCyan
            )
        } else {
            // Radar circular canvas with 8 touch sectors
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(CircleShape)
                    .background(Color(0x1800E5FF))
                    .border(2.dp, NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    drawCircle(Color(0x25FFFFFF), radius = size.minDimension * 0.35f, center = Offset(cx, cy), style = Stroke(1.5f))
                    drawCircle(Color(0x25FFFFFF), radius = size.minDimension * 0.2f, center = Offset(cx, cy), style = Stroke(1.5f))
                    drawLine(Color(0x35FFFFFF), Offset(0f, cy), Offset(size.width, cy), strokeWidth = 1f)
                    drawLine(Color(0x35FFFFFF), Offset(cx, 0f), Offset(cx, size.height), strokeWidth = 1f)
                }

                // Center Fixation Crosshair
                Text("┼", fontSize = 24.sp, color = NeonCyan, fontWeight = FontWeight.Bold)

                // 8 Sector Buttons around perimeter
                for (s in 0 until 8) {
                    val angleRad = (s * 45.0 * Math.PI / 180.0)
                    val r = 100.0
                    val offsetX = (r * Math.cos(angleRad)).dp
                    val offsetY = (r * Math.sin(angleRad)).dp
                    val isTarget = (targetSector == s)

                    Box(
                        modifier = Modifier
                            .offset(x = offsetX, y = offsetY)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isTarget) (if (isDecoy) NeonPink else NeonLime) else Color(0x25FFFFFF))
                            .border(1.dp, if (isTarget) Color.White else Color(0x40FFFFFF), CircleShape)
                            .clickable { handleSectorTap(s) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isTarget) {
                            Text(if (isDecoy) "✖" else "✦", fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}



