package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GamificationManager
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassChip
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.util.AuraSoundManager
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// =============================================================================
// GAME 27: QUANTUM COLOR CLASH (INVERTED STROOP WITH QUANTUM FLIP)
// =============================================================================
@Composable
fun FullQuantumColorClashScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    val colorOptions = listOf(
        Pair("CYAN", NeonCyan),
        Pair("PINK", NeonPink),
        Pair("LIME", NeonLime),
        Pair("AMBER", NeonAmber),
        Pair("VIOLET", NeonViolet)
    )

    var wordIndex by remember { mutableIntStateOf(0) }
    var inkIndex by remember { mutableIntStateOf(1) }
    var ruleIsInverted by remember { mutableStateOf(false) } // false: Match INK color, true: Match WORD text

    fun nextRound() {
        wordIndex = Random.nextInt(colorOptions.size)
        inkIndex = Random.nextInt(colorOptions.size)
        ruleIsInverted = Random.nextBoolean()
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            nextRound()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g27", "Quantum Color Clash", score, (score * 10).coerceIn(40, 100), 240)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F0C20), Color(0xFF06040A)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonPink
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎨 Quantum Color Clash", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Watch the dynamic rule indicator! When rule is 'INK', select the font color. When rule is 'TEXT', select the word meaning!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Start Quantum Flow ⚡", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Quantum Session Complete!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 30
                    isGameOver = false
                    nextRound()
                },
                onExit = onBack,
                accentColor = NeonPink
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonPink)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GlassChip(
                        label = if (ruleIsInverted) "⚡ RULE: MATCH WORD TEXT" else "🎯 RULE: MATCH INK COLOR",
                        color = if (ruleIsInverted) NeonAmber else NeonCyan,
                        selected = true
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = colorOptions[wordIndex].first,
                        color = colorOptions[inkIndex].second,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Score: $score", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    colorOptions.chunked(2).forEach { rowPair ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            rowPair.forEach { option ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(option.second.copy(alpha = 0.2f))
                                        .border(1.5.dp, option.second, RoundedCornerShape(16.dp))
                                        .clickable {
                                            AuraSoundManager.playTapSound(context)
                                            val targetColor = if (ruleIsInverted) colorOptions[wordIndex].first else colorOptions[inkIndex].first
                                            if (option.first == targetColor) {
                                                score += 10 + (streak * 2)
                                                streak++
                                                AuraSoundManager.playSuccessSound()
                                            } else {
                                                lives--
                                                streak = 0
                                            }
                                            if (lives <= 0) isGameOver = true else nextRound()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(option.first, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 28: PATTERN LOGIC MATRIX (PROGRESSIVE RAVEN'S MATRICES)
// =============================================================================
@Composable
fun FullPatternLogicMatrixScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(40) }
    var isGameOver by remember { mutableStateOf(false) }

    val shapes = listOf("▲", "■", "●", "◆", "★")
    var patternSequence by remember { mutableStateOf(listOf<String>()) }
    var missingIndex by remember { mutableIntStateOf(8) }
    var correctAnswer by remember { mutableStateOf("") }
    var options by remember { mutableStateOf(listOf<String>()) }

    fun generateMatrixRound() {
        val base = shapes.shuffled().take(3)
        // 3x3 repeating sequence: [A, B, C, B, C, A, C, A, B]
        val fullGrid = listOf(base[0], base[1], base[2], base[1], base[2], base[0], base[2], base[0], base[1])
        missingIndex = 8
        correctAnswer = fullGrid[8]
        val opts = (listOf(correctAnswer) + shapes.filter { it != correctAnswer }.shuffled().take(3)).shuffled()
        patternSequence = fullGrid
        options = opts
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generateMatrixRound()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g28", "Pattern Logic Matrix", score, 92, 280)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF091420), Color(0xFF03070C)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonCyan
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🧩 Pattern Logic Matrix", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Analyze the 3x3 symbol matrix pattern across rows and columns. Deduce the missing symbol denoted by '?'!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Test Matrix IQ 🧠", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Matrix Analysis Finished!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 40
                    isGameOver = false
                    generateMatrixRound()
                },
                onExit = onBack,
                accentColor = NeonCyan
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonCyan)

                // 3x3 Matrix Grid
                Column(
                    modifier = Modifier
                        .size(240.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x20FFFFFF))
                        .border(1.dp, NeonCyan, RoundedCornerShape(20.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (row in 0..2) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            for (col in 0..2) {
                                val idx = row * 3 + col
                                val isMissing = idx == missingIndex
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isMissing) NeonCyan.copy(alpha = 0.25f) else Color(0x18FFFFFF))
                                        .border(1.dp, if (isMissing) NeonCyan else Color(0x30FFFFFF), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isMissing) "?" else patternSequence.getOrElse(idx) { "" },
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isMissing) NeonCyan else Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                // Options
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    options.forEach { opt ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(NeonCyan.copy(alpha = 0.2f))
                                .border(1.dp, NeonCyan, RoundedCornerShape(16.dp))
                                .clickable {
                                    AuraSoundManager.playTapSound(context)
                                    if (opt == correctAnswer) {
                                        score += 15 + (streak * 3)
                                        streak++
                                        AuraSoundManager.playSuccessSound()
                                    } else {
                                        lives--
                                        streak = 0
                                    }
                                    if (lives <= 0) isGameOver = true else generateMatrixRound()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(opt, fontSize = 24.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 29: VISUAL SACCADE SPRINT (ANTI-SACCADIC PERIPHERAL JUMP)
// =============================================================================
@Composable
fun FullVisualSaccadeSprintScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var targetX by remember { mutableFloatStateOf(0.5f) }
    var targetY by remember { mutableFloatStateOf(0.5f) }
    var isDistractor by remember { mutableStateOf(false) }

    fun spawnTarget() {
        targetX = Random.nextFloat().coerceIn(0.15f, 0.85f)
        targetY = Random.nextFloat().coerceIn(0.15f, 0.85f)
        isDistractor = Random.nextFloat() < 0.25f // 25% chance of red distractor
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            spawnTarget()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g29", "Visual Saccade Sprint", score, 94, 210)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF140D0A), Color(0xFF070403)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonAmber
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎯 Visual Saccade Sprint", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Rapid peripheral visual search: Tap GREEN pulse nodes immediately. DO NOT tap RED false beacons!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Engage Saccades ⚡", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Saccadic Speed Evaluated!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 30
                    isGameOver = false
                    spawnTarget()
                },
                onExit = onBack,
                accentColor = NeonAmber
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(20.dp))
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonAmber)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .align(
                                androidx.compose.ui.BiasAlignment(
                                    horizontalBias = (targetX - 0.5f) * 2f,
                                    verticalBias = (targetY - 0.5f) * 2f
                                )
                            )
                            .clip(CircleShape)
                            .background(if (isDistractor) NeonPink.copy(alpha = 0.35f) else NeonLime.copy(alpha = 0.35f))
                            .border(2.dp, if (isDistractor) NeonPink else NeonLime, CircleShape)
                            .clickable {
                                AuraSoundManager.playTapSound(context)
                                if (!isDistractor) {
                                    score += 10 + (streak * 2)
                                    streak++
                                    AuraSoundManager.playSuccessSound()
                                } else {
                                    lives--
                                    streak = 0
                                }
                                if (lives <= 0) isGameOver = true else spawnTarget()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (isDistractor) "❌" else "🎯", fontSize = 26.sp)
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 30: DUAL SWITCH ODD/EVEN & HIGH/LOW (TASK-SET SWITCHING)
// =============================================================================
@Composable
fun FullDualSwitchOddevenScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(35) }
    var isGameOver by remember { mutableStateOf(false) }

    var currentNumber by remember { mutableIntStateOf(5) }
    var currentRule by remember { mutableStateOf("ODD_EVEN") } // "ODD_EVEN" or "HIGH_LOW"

    fun nextNumber() {
        currentNumber = Random.nextInt(1, 10) // 1..9
        currentRule = if (Random.nextBoolean()) "ODD_EVEN" else "HIGH_LOW"
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            nextNumber()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g30", "Dual Task-Set Switch", score, 91, 250)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F1A15), Color(0xFF040A07)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonLime
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🔄 Dual Task Switcher", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Test executive cognitive flexibility! When rule is 'PARITY', tap ODD or EVEN. When rule is 'MAGNITUDE', tap LOW (1-4) or HIGH (6-9)!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Start Switch Duel ⚡", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Cognitive Flexibility Complete!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 35
                    isGameOver = false
                    nextNumber()
                },
                onExit = onBack,
                accentColor = NeonLime
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonLime)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GlassChip(
                        label = if (currentRule == "ODD_EVEN") "🔢 RULE: ODD vs EVEN" else "⚖️ RULE: LOW (1-4) vs HIGH (6-9)",
                        color = if (currentRule == "ODD_EVEN") NeonCyan else NeonAmber,
                        selected = true
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color(0x20FFFFFF))
                            .border(2.dp, NeonLime, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$currentNumber", fontSize = 56.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Score: $score", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val leftLabel = if (currentRule == "ODD_EVEN") "ODD" else "LOW (1-4)"
                    val rightLabel = if (currentRule == "ODD_EVEN") "EVEN" else "HIGH (6-9)"

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(NeonLime.copy(alpha = 0.22f))
                            .border(1.5.dp, NeonLime, RoundedCornerShape(16.dp))
                            .clickable {
                                AuraSoundManager.playTapSound(context)
                                val isCorrect = if (currentRule == "ODD_EVEN") currentNumber % 2 != 0 else currentNumber < 5
                                if (isCorrect) {
                                    score += 10 + (streak * 2)
                                    streak++
                                    AuraSoundManager.playSuccessSound()
                                } else {
                                    lives--
                                    streak = 0
                                }
                                if (lives <= 0) isGameOver = true else nextNumber()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(leftLabel, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(NeonCyan.copy(alpha = 0.22f))
                            .border(1.5.dp, NeonCyan, RoundedCornerShape(16.dp))
                            .clickable {
                                AuraSoundManager.playTapSound(context)
                                val isCorrect = if (currentRule == "ODD_EVEN") currentNumber % 2 == 0 else currentNumber >= 5
                                if (isCorrect) {
                                    score += 10 + (streak * 2)
                                    streak++
                                    AuraSoundManager.playSuccessSound()
                                } else {
                                    lives--
                                    streak = 0
                                }
                                if (lives <= 0) isGameOver = true else nextNumber()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(rightLabel, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 31: SPATIAL 3D ROTATION (POLYHEDRAL MENTAL ROTATION)
// =============================================================================
@Composable
fun FullSpatialRotation3DScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(40) }
    var isGameOver by remember { mutableStateOf(false) }

    var targetAngle by remember { mutableIntStateOf(90) }
    var isMirrored by remember { mutableStateOf(false) }

    fun nextPuzzle() {
        targetAngle = listOf(90, 180, 270).random()
        isMirrored = Random.nextBoolean()
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            nextPuzzle()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g31", "Spatial 3D Rotation", score, 88, 310)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF160D24), Color(0xFF07040C)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonViolet
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📐 Spatial 3D Rotation", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Mental Rotation IQ: Determine if the rotated shape is identical (ROTATED ONLY) or an impossible reflection (MIRRORED)!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Engage Spatial IQ 🧠", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Spatial IQ Assessment Complete!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 40
                    isGameOver = false
                    nextPuzzle()
                },
                onExit = onBack,
                accentColor = NeonViolet
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonViolet)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Reference vs Rotated Shape", color = Color.LightGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Original
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x20FFFFFF))
                                .border(1.dp, NeonCyan, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⚡", fontSize = 48.sp)
                        }

                        Text("➔", color = Color.White, fontSize = 24.sp)

                        // Rotated/Mirrored
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0x20FFFFFF))
                                .border(1.dp, NeonViolet, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (isMirrored) "🪞" else "⚡",
                                fontSize = 48.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Is this shape a pure rotation or mirrored?", color = Color.White, fontSize = 13.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(NeonLime.copy(alpha = 0.22f))
                            .border(1.5.dp, NeonLime, RoundedCornerShape(16.dp))
                            .clickable {
                                AuraSoundManager.playTapSound(context)
                                if (!isMirrored) {
                                    score += 15 + (streak * 2)
                                    streak++
                                    AuraSoundManager.playSuccessSound()
                                } else {
                                    lives--
                                    streak = 0
                                }
                                if (lives <= 0) isGameOver = true else nextPuzzle()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("PURE ROTATION", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(NeonPink.copy(alpha = 0.22f))
                            .border(1.5.dp, NeonPink, RoundedCornerShape(16.dp))
                            .clickable {
                                AuraSoundManager.playTapSound(context)
                                if (isMirrored) {
                                    score += 15 + (streak * 2)
                                    streak++
                                    AuraSoundManager.playSuccessSound()
                                } else {
                                    lives--
                                    streak = 0
                                }
                                if (lives <= 0) isGameOver = true else nextPuzzle()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("MIRRORED / FALSE", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 32: ECHO RHYTHM SIMON (ACOUSTIC TEMPO & SEQUENCE MEMORY)
// =============================================================================
@Composable
fun FullEchoRhythmSimonScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var isGameOver by remember { mutableStateOf(false) }

    val pads = listOf(NeonCyan, NeonPink, NeonLime, NeonAmber)
    val sequence = remember { mutableStateListOf<Int>() }
    var userIndex by remember { mutableIntStateOf(0) }
    var isPlayingSequence by remember { mutableStateOf(false) }
    var highlightedPad by remember { mutableIntStateOf(-1) }

    suspend fun playSequence() {
        isPlayingSequence = true
        userIndex = 0
        delay(600)
        for (pad in sequence) {
            highlightedPad = pad
            AuraSoundManager.playTapSound(context)
            delay(400)
            highlightedPad = -1
            delay(150)
        }
        isPlayingSequence = false
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            sequence.clear()
            sequence.add(Random.nextInt(4))
            playSequence()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF07141E), Color(0xFF02060A)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonCyan
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🥁 Echo Rhythm Simon", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Acoustic sequence working memory: Watch and listen to the flashing melody, then repeat the exact rhythm sequence!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Start Rhythm Memory 🎵", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Rhythm Memory Completed!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    isGameOver = false
                },
                onExit = onBack,
                accentColor = NeonCyan
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = sequence.size, currentLives = lives, streak = streak, accentColor = NeonCyan)

                Text(
                    text = if (isPlayingSequence) "🎧 Listen to Sequence..." else "🎯 Your Turn: Repeat Rhythm!",
                    color = if (isPlayingSequence) NeonAmber else NeonLime,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                // 2x2 Simon Matrix
                Column(
                    modifier = Modifier.size(260.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    for (row in 0..1) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            for (col in 0..1) {
                                val padIdx = row * 2 + col
                                val padColor = pads[padIdx]
                                val isLit = highlightedPad == padIdx
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(120.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(if (isLit) padColor else padColor.copy(alpha = 0.25f))
                                        .border(2.dp, if (isLit) Color.White else padColor, RoundedCornerShape(20.dp))
                                        .clickable(enabled = !isPlayingSequence) {
                                            AuraSoundManager.playTapSound(context)
                                            if (sequence.getOrNull(userIndex) == padIdx) {
                                                userIndex++
                                                if (userIndex >= sequence.size) {
                                                    score += sequence.size * 20
                                                    streak++
                                                    AuraSoundManager.playSuccessSound()
                                                    sequence.add(Random.nextInt(4))
                                                    kotlinx.coroutines.GlobalScope.let {
                                                        // trigger next round
                                                    }
                                                }
                                            } else {
                                                lives--
                                                streak = 0
                                                if (lives <= 0) {
                                                    isGameOver = true
                                                    GamificationManager.recordGameMatch("g32", "Echo Rhythm Simon", score, 90, 260)
                                                }
                                            }
                                        }
                                )
                            }
                        }
                    }
                }

                Text("Current Length: ${sequence.size}", color = Color.LightGray, fontSize = 14.sp)
            }
        }
    }
}

// =============================================================================
// GAME 33: TIME ESTIMATION PULSE (INTERNAL CIRCADIAN CALIBRATION)
// =============================================================================
@Composable
fun FullTimeEstimationPacerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var attempts by remember { mutableIntStateOf(5) }
    var isGameOver by remember { mutableStateOf(false) }

    val targetSecondsList = listOf(5, 7, 10, 12)
    var currentTargetSec by remember { mutableIntStateOf(5) }
    var timerRunning by remember { mutableStateOf(false) }
    var startTimeMs by remember { mutableStateOf(0L) }
    var lastErrorMs by remember { mutableStateOf<Long?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF141209), Color(0xFF080703)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonAmber
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("⏱️ Time Estimation Pulse", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Internal Biological Clock: Tap 'START', close your eyes or count internally, and tap 'STOP' exactly when target seconds have elapsed!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Calibrate Internal Clock ⚡", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Circadian Pacer Complete!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    attempts = 5
                    isGameOver = false
                    lastErrorMs = null
                },
                onExit = onBack,
                accentColor = NeonAmber
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Rounds Left: $attempts", color = NeonAmber, fontWeight = FontWeight.Bold)
                    Text("Score: $score", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("TARGET DURATION", fontSize = 12.sp, color = Color.Gray)
                    Text("${currentTargetSec}s", fontSize = 64.sp, fontWeight = FontWeight.Black, color = NeonAmber)
                    Spacer(modifier = Modifier.height(10.dp))
                    if (lastErrorMs != null) {
                        Text("Delta: ${lastErrorMs}ms (${if (lastErrorMs!! < 400) "Excellent!" else "Adjust speed"})", color = if (lastErrorMs!! < 400) NeonLime else NeonPink, fontSize = 14.sp)
                    }
                }

                GlassButton(
                    text = if (timerRunning) "TAP TO STOP 🛑" else "START PULSE ⏱️",
                    onClick = {
                        AuraSoundManager.playTapSound(context)
                        if (!timerRunning) {
                            startTimeMs = System.currentTimeMillis()
                            timerRunning = true
                        } else {
                            timerRunning = false
                            val elapsedMs = System.currentTimeMillis() - startTimeMs
                            val targetMs = currentTargetSec * 1000L
                            val delta = abs(elapsedMs - targetMs)
                            lastErrorMs = delta
                            if (delta < 500) {
                                score += 30
                                streak++
                                AuraSoundManager.playSuccessSound()
                            } else if (delta < 1200) {
                                score += 15
                            }
                            attempts--
                            currentTargetSec = targetSecondsList.random()
                            if (attempts <= 0) {
                                isGameOver = true
                                GamificationManager.recordGameMatch("g33", "Time Estimation Pulse", score, 85, 300)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// =============================================================================
// GAME 34: ANAGRAM CHAIN RUSH (VERBAL NEURO-LINGUISTIC FLOW)
// =============================================================================
@Composable
fun FullAnagramChainRushScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(35) }
    var isGameOver by remember { mutableStateOf(false) }

    val wordBank = listOf(
        Pair("FOCUS", listOf("FOCUS", "CLOUDS", "SOLVE", "FROST")),
        Pair("BRAIN", listOf("BRAIN", "BARON", "BRINE", "TRAIN")),
        Pair("ZENITH", listOf("ZENITH", "NEITHER", "HEIGHT", "TENANT")),
        Pair("ENERGY", listOf("ENERGY", "GREEDY", "GENEROUS", "ENTRY")),
        Pair("STREAM", listOf("STREAM", "MASTER", "STAMINA", "REMAIN"))
    )

    var currentWord by remember { mutableStateOf("FOCUS") }
    var scrambledWord by remember { mutableStateOf("COSUF") }
    var options by remember { mutableStateOf(listOf<String>()) }

    fun nextAnagram() {
        val entry = wordBank.random()
        currentWord = entry.first
        scrambledWord = currentWord.toList().shuffled().joinToString("")
        options = entry.second.shuffled()
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            nextAnagram()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g34", "Anagram Chain Rush", score, 90, 240)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF140D1D), Color(0xFF050308)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonViolet
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🔤 Anagram Chain Rush", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Verbal Working Fluency: Rapidly unscramble scrambled letters and choose the correct original cognitive focus word!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Unscramble Anagrams ⚡", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Anagram Sprint Finished!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 35
                    isGameOver = false
                    nextAnagram()
                },
                onExit = onBack,
                accentColor = NeonViolet
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonViolet)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SCRAMBLED ANAGRAM", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(scrambledWord, fontSize = 42.sp, fontWeight = FontWeight.Black, color = NeonViolet)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Score: $score", color = Color.White, fontSize = 16.sp)
                }

                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    options.forEach { opt ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(NeonViolet.copy(alpha = 0.2f))
                                .border(1.dp, NeonViolet, RoundedCornerShape(16.dp))
                                .clickable {
                                    AuraSoundManager.playTapSound(context)
                                    if (opt == currentWord) {
                                        score += 15 + (streak * 2)
                                        streak++
                                        AuraSoundManager.playSuccessSound()
                                    } else {
                                        lives--
                                        streak = 0
                                    }
                                    if (lives <= 0) isGameOver = true else nextAnagram()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(opt, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 35: IMPULSE INHIBIT GO / NO-GO (SUB-SECOND MOTOR SUPPRESSOR)
// =============================================================================
@Composable
fun FullImpulseInhibitionGoNoGoScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var isGoSignal by remember { mutableStateOf(true) }
    var signalColor by remember { mutableStateOf(NeonLime) }
    var signalText by remember { mutableStateOf("GO!") }

    fun nextSignal() {
        isGoSignal = Random.nextFloat() > 0.35f // 65% Go, 35% No-Go
        signalColor = if (isGoSignal) NeonLime else NeonPink
        signalText = if (isGoSignal) "GO! 🟢" else "NO-GO! ⛔"
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            nextSignal()
            while (secondsLeft > 0 && lives > 0) {
                delay(1200)
                nextSignal()
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g35", "Go/No-Go Suppressor", score, 93, 220)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF160F0F), Color(0xFF080404)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonLime
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("⚡ Go / No-Go Impulse Inhibit", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Impulse Control: Tap TRIGGER immediately when green 'GO!' appears. Suppress your motor impulse and DO NOT TAP when red 'NO-GO!' flashes!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Start Impulse Arena 🛑", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Impulse Inhibition Tested!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 30
                    isGameOver = false
                    nextSignal()
                },
                onExit = onBack,
                accentColor = NeonLime
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = signalColor)

                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(signalColor.copy(alpha = 0.25f))
                        .border(3.dp, signalColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(signalText, fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White)
                }

                GlassButton(
                    text = "TRIGGER TAP! 💥",
                    onClick = {
                        AuraSoundManager.playTapSound(context)
                        if (isGoSignal) {
                            score += 10 + (streak * 2)
                            streak++
                            AuraSoundManager.playSuccessSound()
                        } else {
                            lives--
                            streak = 0
                        }
                        if (lives <= 0) isGameOver = true else nextSignal()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )
            }
        }
    }
}

// =============================================================================
// GAME 36: SYMBOL ODYSSEY CIPHER (ANCIENT GLYPHS ASSOCIATIVE MEMORY)
// =============================================================================
@Composable
fun FullSymbolOdysseyCipherScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(35) }
    var isGameOver by remember { mutableStateOf(false) }

    val cipherPairs = listOf(
        Pair("ᛟ", 7),
        Pair("ᚱ", 3),
        Pair("ᚦ", 5),
        Pair("ᚹ", 9),
        Pair("ᛞ", 2)
    )

    var currentGlyph by remember { mutableStateOf(cipherPairs[0].first) }
    var correctDigit by remember { mutableIntStateOf(cipherPairs[0].second) }
    var digitOptions by remember { mutableStateOf(listOf<Int>()) }

    fun nextCipher() {
        val pair = cipherPairs.random()
        currentGlyph = pair.first
        correctDigit = pair.second
        digitOptions = (listOf(correctDigit) + (1..9).filter { it != correctDigit }.shuffled().take(3)).shuffled()
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            nextCipher()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g36", "Symbol Odyssey Cipher", score, 91, 270)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F171E), Color(0xFF05080C)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonCyan
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📜 Symbol Odyssey Cipher", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Associative Working Memory: Remember the ancient rune cipher values (ᛟ=7, ᚱ=3, ᚦ=5, ᚹ=9, ᛞ=2) and tap the matching digit!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Decode Ancient Runes 🗝️", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Cryptanalysis Completed!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 35
                    isGameOver = false
                    nextCipher()
                },
                onExit = onBack,
                accentColor = NeonCyan
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonCyan)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("TRANSLATE SYMBOL GLYPH", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0x20FFFFFF))
                            .border(1.5.dp, NeonCyan, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(currentGlyph, fontSize = 60.sp, color = NeonCyan)
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Score: $score", color = Color.White, fontSize = 16.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    digitOptions.forEach { d ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(NeonCyan.copy(alpha = 0.22f))
                                .border(1.dp, NeonCyan, RoundedCornerShape(16.dp))
                                .clickable {
                                    AuraSoundManager.playTapSound(context)
                                    if (d == correctDigit) {
                                        score += 15 + (streak * 2)
                                        streak++
                                        AuraSoundManager.playSuccessSound()
                                    } else {
                                        lives--
                                        streak = 0
                                    }
                                    if (lives <= 0) isGameOver = true else nextCipher()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("$d", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 37: TACTICAL PERIPHERAL RADAR (CHANGE BLINDNESS ANOMALY HUNTER)
// =============================================================================
@Composable
fun FullTacticalPeripheralRadarScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(35) }
    var isGameOver by remember { mutableStateOf(false) }

    var anomalyIndex by remember { mutableIntStateOf(Random.nextInt(12)) }
    val normalSymbol = "●"
    val anomalySymbol = "▲"

    fun nextRadarSweep() {
        anomalyIndex = Random.nextInt(12)
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            nextRadarSweep()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g37", "Peripheral Radar", score, 92, 230)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF091811), Color(0xFF030A06)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonLime
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📡 Tactical Peripheral Radar", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Overcome Change Blindness: Spot the subtle peripheral anomaly icon hidden inside the radar constellation!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Scan Radar Beacons ⚡", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Peripheral Tracking Evaluated!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 35
                    isGameOver = false
                    nextRadarSweep()
                },
                onExit = onBack,
                accentColor = NeonLime
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonLime)

                Text("Find the Anomaly Shape ($anomalySymbol)", color = Color.LightGray, fontSize = 14.sp)

                // 3x4 Grid
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (row in 0..2) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            for (col in 0..3) {
                                val idx = row * 4 + col
                                val isAnomaly = idx == anomalyIndex
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(70.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color(0x18FFFFFF))
                                        .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(14.dp))
                                        .clickable {
                                            AuraSoundManager.playTapSound(context)
                                            if (isAnomaly) {
                                                score += 15 + (streak * 2)
                                                streak++
                                                AuraSoundManager.playSuccessSound()
                                            } else {
                                                lives--
                                                streak = 0
                                            }
                                            if (lives <= 0) isGameOver = true else nextRadarSweep()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(if (isAnomaly) anomalySymbol else normalSymbol, fontSize = 24.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }

                Text("Score: $score", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// =============================================================================
// GAME 38: SYNAPSE MATH CHAINS (CONTINUOUS OPERATOR FLOW)
// =============================================================================
@Composable
fun FullSynapseMathChainsScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(40) }
    var isGameOver by remember { mutableStateOf(false) }

    var expression by remember { mutableStateOf("3 + 4 × 2") }
    var correctAnswer by remember { mutableIntStateOf(14) }
    var answerOptions by remember { mutableStateOf(listOf<Int>()) }

    fun nextMathChain() {
        val a = Random.nextInt(2, 9)
        val b = Random.nextInt(2, 8)
        val c = Random.nextInt(1, 6)
        val op1 = listOf("+", "-").random()
        val op2 = listOf("+", "×").random()

        var res = if (op1 == "+") a + b else a - b
        res = if (op2 == "+") res + c else res * c
        expression = "($a $op1 $b) $op2 $c"
        correctAnswer = res
        answerOptions = (listOf(res) + listOf(res + 2, res - 3, res + 5).shuffled().take(3)).shuffled()
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            nextMathChain()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g38", "Synapse Math Chains", score, 93, 270)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF09161B), Color(0xFF03080A)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonCyan
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🧮 Synapse Math Chains", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Sequential Working Memory & Arithmetic: Solve compound parenthetical arithmetic chains without using paper!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Calculate Chained Flow ⚡", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Synapse Math Completed!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 40
                    isGameOver = false
                    nextMathChain()
                },
                onExit = onBack,
                accentColor = NeonCyan
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonCyan)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("EQUATION CHAIN", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(expression, fontSize = 38.sp, fontWeight = FontWeight.Black, color = Color.White)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Score: $score", color = NeonCyan, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    answerOptions.chunked(2).forEach { rowPair ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            rowPair.forEach { ans ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(56.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(NeonCyan.copy(alpha = 0.2f))
                                        .border(1.dp, NeonCyan, RoundedCornerShape(16.dp))
                                        .clickable {
                                            AuraSoundManager.playTapSound(context)
                                            if (ans == correctAnswer) {
                                                score += 15 + (streak * 2)
                                                streak++
                                                AuraSoundManager.playSuccessSound()
                                            } else {
                                                lives--
                                                streak = 0
                                            }
                                            if (lives <= 0) isGameOver = true else nextMathChain()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("$ans", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 39: LABYRINTH PATHFINDER (DYNAMIC MAZE NAVIGATION)
// =============================================================================
@Composable
fun FullLabyrinthPathfinderScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(35) }
    var isGameOver by remember { mutableStateOf(false) }

    var playerPos by remember { mutableIntStateOf(0) } // 0..15 grid (4x4)
    val exitPos = 15
    var walls by remember { mutableStateOf(setOf(2, 5, 8, 11)) }

    fun nextMaze() {
        playerPos = 0
        walls = setOf(listOf(1, 4, 6, 9).random(), listOf(2, 5, 8, 10).random(), listOf(3, 7, 12, 13).random())
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            nextMaze()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g39", "Labyrinth Pathfinder", score, 89, 290)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0C161D), Color(0xFF04080A)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonCyan
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🌀 Labyrinth Pathfinder", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Spatial Problem Solving: Move from START (top-left) to GOAL 🏁 (bottom-right). Avoid dynamic barrier blocks!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Enter Labyrinth 🌀", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Labyrinth Run Ended!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 35
                    isGameOver = false
                    nextMaze()
                },
                onExit = onBack,
                accentColor = NeonCyan
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonCyan)

                // 4x4 Maze Grid
                Column(
                    modifier = Modifier
                        .size(260.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x18FFFFFF))
                        .border(1.dp, NeonCyan, RoundedCornerShape(20.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (row in 0..3) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            for (col in 0..3) {
                                val idx = row * 4 + col
                                val isPlayer = idx == playerPos
                                val isGoal = idx == exitPos
                                val isWall = walls.contains(idx)

                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            when {
                                                isPlayer -> NeonCyan.copy(alpha = 0.5f)
                                                isGoal -> NeonLime.copy(alpha = 0.4f)
                                                isWall -> NeonPink.copy(alpha = 0.4f)
                                                else -> Color(0x10FFFFFF)
                                            }
                                        )
                                        .border(
                                            1.dp,
                                            when {
                                                isPlayer -> NeonCyan
                                                isGoal -> NeonLime
                                                isWall -> NeonPink
                                                else -> Color(0x20FFFFFF)
                                            },
                                            RoundedCornerShape(10.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        when {
                                            isPlayer -> "👤"
                                            isGoal -> "🏁"
                                            isWall -> "🧱"
                                            else -> ""
                                        },
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // D-Pad Controls
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0x20FFFFFF))
                            .clickable {
                                AuraSoundManager.playTapSound(context)
                                if (playerPos >= 4 && !walls.contains(playerPos - 4)) playerPos -= 4 else lives--
                            },
                        contentAlignment = Alignment.Center
                    ) { Text("▲", color = Color.White) }

                    Row(horizontalArrangement = Arrangement.spacedBy(40.dp)) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0x20FFFFFF))
                                .clickable {
                                    AuraSoundManager.playTapSound(context)
                                    if (playerPos % 4 > 0 && !walls.contains(playerPos - 1)) playerPos -= 1 else lives--
                                },
                            contentAlignment = Alignment.Center
                        ) { Text("◀", color = Color.White) }

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0x20FFFFFF))
                                .clickable {
                                    AuraSoundManager.playTapSound(context)
                                    if (playerPos % 4 < 3 && !walls.contains(playerPos + 1)) {
                                        playerPos += 1
                                        if (playerPos == exitPos) {
                                            score += 25
                                            streak++
                                            AuraSoundManager.playSuccessSound()
                                            nextMaze()
                                        }
                                    } else lives--
                                },
                            contentAlignment = Alignment.Center
                        ) { Text("▶", color = Color.White) }
                    }

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0x20FFFFFF))
                            .clickable {
                                AuraSoundManager.playTapSound(context)
                                if (playerPos + 4 <= 15 && !walls.contains(playerPos + 4)) {
                                    playerPos += 4
                                    if (playerPos == exitPos) {
                                        score += 25
                                        streak++
                                        AuraSoundManager.playSuccessSound()
                                        nextMaze()
                                    }
                                } else lives--
                            },
                        contentAlignment = Alignment.Center
                    ) { Text("▼", color = Color.White) }
                }
            }
        }
    }
}

// =============================================================================
// GAME 40: CHRONO DIGIT MATRIX (INTERLEAVED ASCENDING / DESCENDING SEARCH)
// =============================================================================
@Composable
fun FullChronoDigitMatrixScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(40) }
    var isGameOver by remember { mutableStateOf(false) }

    var nextTarget by remember { mutableIntStateOf(1) }
    val numbers = remember { mutableStateListOf<Int>() }

    fun initGrid() {
        numbers.clear()
        numbers.addAll((1..16).shuffled())
        nextTarget = 1
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            initGrid()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g40", "Chrono Digit Matrix", score, 94, 210)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF13101C), Color(0xFF06050A)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonViolet
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🔢 Chrono Digit Matrix", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Visual Processing Speed: Tap numbers strictly in ascending order from 1 to 16 as fast as humanly possible!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Start Speed Matrix ⚡", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Processing Speed Tested!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 40
                    isGameOver = false
                    initGrid()
                },
                onExit = onBack,
                accentColor = NeonViolet
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonViolet)

                Text("Next Number Target: $nextTarget", fontSize = 20.sp, fontWeight = FontWeight.Black, color = NeonViolet)

                // 4x4 Grid
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (row in 0..3) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            for (col in 0..3) {
                                val idx = row * 4 + col
                                val num = numbers.getOrElse(idx) { 0 }
                                val isDone = num < nextTarget

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(60.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isDone) Color(0x0AFFFFFF) else NeonViolet.copy(alpha = 0.2f))
                                        .border(1.dp, if (isDone) Color.Transparent else NeonViolet, RoundedCornerShape(12.dp))
                                        .clickable(enabled = !isDone) {
                                            AuraSoundManager.playTapSound(context)
                                            if (num == nextTarget) {
                                                score += 10
                                                streak++
                                                nextTarget++
                                                if (nextTarget > 16) {
                                                    score += 50
                                                    AuraSoundManager.playSuccessSound()
                                                    initGrid()
                                                }
                                            } else {
                                                lives--
                                                streak = 0
                                            }
                                            if (lives <= 0) isGameOver = true
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        if (isDone) "✓" else "$num",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = if (isDone) Color.Gray else Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Text("Score: $score", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

// =============================================================================
// GAME 41: TONE PITCH DISCRIMINATION (MICRO-SEMITONE ACOUSTIC TRAINER)
// =============================================================================
@Composable
fun FullTonePitchDiscriminationScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(35) }
    var isGameOver by remember { mutableStateOf(false) }

    var tone1Hz by remember { mutableIntStateOf(440) }
    var tone2Hz by remember { mutableIntStateOf(448) }

    fun nextPitchTrial() {
        tone1Hz = Random.nextInt(350, 520)
        val diff = listOf(-16, -10, -6, 6, 10, 16).random()
        tone2Hz = tone1Hz + diff
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            nextPitchTrial()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g41", "Pitch Discrimination", score, 88, 280)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF130E1F), Color(0xFF050308)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonPink
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎵 Pitch Discrimination", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Acoustic Ear Training: Tone A vs Tone B. Listen and compare acoustic frequency: Is Tone B HIGHER or LOWER in pitch?",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Test Acoustic Ear 🎧", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score,
                streak = streak,
                title = "Acoustic Hearing Tested!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 35
                    isGameOver = false
                    nextPitchTrial()
                },
                onExit = onBack,
                accentColor = NeonPink
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonPink)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.25f))
                            .border(2.dp, NeonCyan, CircleShape)
                            .clickable {
                                AuraSoundManager.playTapSound(context)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Tone A", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${tone1Hz}Hz", fontSize = 12.sp, color = NeonCyan)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(NeonPink.copy(alpha = 0.25f))
                            .border(2.dp, NeonPink, CircleShape)
                            .clickable {
                                AuraSoundManager.playTapSound(context)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Tone B", fontWeight = FontWeight.Bold, color = Color.White)
                            Text("? Hz", fontSize = 12.sp, color = NeonPink)
                        }
                    }
                }

                Text("Is Tone B Higher or Lower than Tone A?", color = Color.White, fontSize = 14.sp)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    GlassButton(
                        text = "LOWER ⬇️",
                        onClick = {
                            AuraSoundManager.playTapSound(context)
                            if (tone2Hz < tone1Hz) {
                                score += 15 + (streak * 2)
                                streak++
                                AuraSoundManager.playSuccessSound()
                            } else {
                                lives--
                                streak = 0
                            }
                            if (lives <= 0) isGameOver = true else nextPitchTrial()
                        },
                        modifier = Modifier.weight(1f)
                    )

                    GlassButton(
                        text = "HIGHER ⬆️",
                        onClick = {
                            AuraSoundManager.playTapSound(context)
                            if (tone2Hz > tone1Hz) {
                                score += 15 + (streak * 2)
                                streak++
                                AuraSoundManager.playSuccessSound()
                            } else {
                                lives--
                                streak = 0
                            }
                            if (lives <= 0) isGameOver = true else nextPitchTrial()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// =============================================================================
// GAME 42: KINETIC ORB BALANCING (GRAVITY EQUILIBRIUM PRECISION)
// =============================================================================
@Composable
fun FullKineticOrbBalancingScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var orbX by remember { mutableFloatStateOf(0f) } // -1f to 1f

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            while (secondsLeft > 0 && lives > 0) {
                delay(100)
                // Random perturbation
                val drift = (Random.nextFloat() - 0.5f) * 0.12f
                orbX = (orbX + drift).coerceIn(-1.1f, 1.1f)
                if (abs(orbX) > 1.0f) {
                    lives--
                    streak = 0
                    orbX = 0f
                } else {
                    score += 1
                }
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g42", "Kinetic Orb Balancing", score * 2, 90, 200)
        }
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF071714), Color(0xFF020907)))),
        contentAlignment = Alignment.Center
    ) {
        if (!isStarted) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                glowColor = NeonLime
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("⚖️ Kinetic Orb Balancing", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Neuromotor Equilibrium: Keep the drifting energy orb centered on the beam using left & right stabilizer jets!",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    GlassButton(text = "Stabilize Beam ⚖️", onClick = { isStarted = true }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    GlassButton(text = "Exit", onClick = onBack, isPrimary = false, modifier = Modifier.fillMaxWidth())
                }
            }
        } else if (isGameOver) {
            GameOverCard(
                score = score * 2,
                streak = streak,
                title = "Equilibrium Session Done!",
                onPlayAgain = {
                    score = 0
                    streak = 0
                    lives = 3
                    secondsLeft = 30
                    isGameOver = false
                    orbX = 0f
                },
                onExit = onBack,
                accentColor = NeonLime
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonLime)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("BEAM EQUILIBRIUM", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(30.dp))

                    // Beam Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x30FFFFFF))
                            .border(1.dp, NeonLime, RoundedCornerShape(12.dp))
                    ) {
                        // Orb
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .align(
                                    androidx.compose.ui.BiasAlignment(
                                        horizontalBias = orbX,
                                        verticalBias = 0f
                                    )
                                )
                                .clip(CircleShape)
                                .background(NeonCyan)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Score: ${score * 2}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlassButton(
                        text = "◀ TILT LEFT",
                        onClick = {
                            AuraSoundManager.playTapSound(context)
                            orbX = (orbX - 0.2f).coerceIn(-1.1f, 1.1f)
                        },
                        modifier = Modifier.weight(1f)
                    )

                    GlassButton(
                        text = "TILT RIGHT ▶",
                        onClick = {
                            AuraSoundManager.playTapSound(context)
                            orbX = (orbX + 0.2f).coerceIn(-1.1f, 1.1f)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
