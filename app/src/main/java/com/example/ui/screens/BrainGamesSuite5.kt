package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

// =============================================================================
// GAME 53: QUANTUM POLARIZATION (Photon dual-spin angle alignment)
// =============================================================================
@Composable
fun FullQuantumPolarizationScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var angleA by remember { mutableIntStateOf(0) }
    var angleB by remember { mutableIntStateOf(90) }
    var isEntangled by remember { mutableStateOf(false) }

    fun generateRound() {
        val entangled = Random.nextBoolean()
        isEntangled = entangled
        angleA = listOf(0, 45, 90, 135).random()
        angleB = if (entangled) angleA else listOf(0, 45, 90, 135).filter { it != angleA }.random()
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generateRound()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g53", "Quantum Polarization", score, (score * 8).coerceIn(40, 100), 240)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar("Quantum Polarization", score, "Standard", onBack, NeonCyan)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonCyan)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚛️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Quantum Polarization", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Detect whether two photon spin polarizations are entangled (matching angle) or orthogonal!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "ENGAGE POLARITY", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Polarity Test Complete", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonCyan)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "REPLAY", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generateRound() }, isPrimary = true)
                    }
                }
            } else {
                Text("Do both photons share identical polarization angle?", color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    GlassCard(modifier = Modifier.size(120.dp)) {
                        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text("PHOTON α", fontSize = 10.sp, color = NeonCyan)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("$angleA°", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                    }
                    GlassCard(modifier = Modifier.size(120.dp)) {
                        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text("PHOTON β", fontSize = 10.sp, color = NeonPink)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("$angleB°", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlassButton(
                        text = "DIFFERENT ❌",
                        onClick = {
                            if (!isEntangled) { score += 12 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            generateRound()
                        },
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = "ENTANGLED ✨",
                        onClick = {
                            if (isEntangled) { score += 12 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            generateRound()
                        },
                        isPrimary = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// =============================================================================
// GAME 54: SCHULTE TABLE MATRIX SPRINT (Rapid 5x5 peripheral vision number hunt)
// =============================================================================
@Composable
fun FullSchulteTableMatrixScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var currentTarget by remember { mutableIntStateOf(1) }
    var secondsLeft by remember { mutableIntStateOf(45) }
    var isGameOver by remember { mutableStateOf(false) }

    val numbers = remember { mutableStateListOf<Int>() }

    fun generateTable() {
        numbers.clear()
        numbers.addAll((1..25).shuffled())
        currentTarget = 1
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generateTable()
            while (secondsLeft > 0 && currentTarget <= 25) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            val score = (currentTarget * 10) + (secondsLeft * 2)
            GamificationManager.recordGameMatch("g54", "Schulte Table Matrix", score, (currentTarget * 4).coerceIn(40, 100), 220)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar("Schulte Table", currentTarget, "Standard", onBack, NeonLime)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = 1, streak = currentTarget, accentColor = NeonLime)

        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔢", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Schulte Table Matrix", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tap numbers in ascending order from 1 to 25 as fast as possible using peripheral vision!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "START SCAN", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (currentTarget > 25) "🏆 Table Completed!" else "Time Up!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonLime)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Reached: ${currentTarget - 1}/25 | Seconds Left: $secondsLeft", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "RETRY", onClick = { secondsLeft = 45; isGameOver = false; generateTable() }, isPrimary = true)
                    }
                }
            } else {
                Text("TAP NUMBER: $currentTarget", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = NeonLime)
                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (row in 0 until 5) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            for (col in 0 until 5) {
                                val idx = row * 5 + col
                                if (idx < numbers.size) {
                                    val num = numbers[idx]
                                    val isNext = num == currentTarget
                                    val isPassed = num < currentTarget
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isPassed) Color(0x10FFFFFF) else if (isNext) NeonLime.copy(alpha = 0.25f) else Color(0x22FFFFFF))
                                            .border(1.dp, if (isNext) NeonLime else Color(0x33FFFFFF), RoundedCornerShape(8.dp))
                                            .clickable {
                                                if (num == currentTarget) {
                                                    currentTarget++
                                                    AuraSoundManager.playTapSound(context)
                                                    if (currentTarget > 25) {
                                                        AuraSoundManager.playSuccessSound(context)
                                                        isGameOver = true
                                                    }
                                                } else {
                                                    AuraSoundManager.playErrorSound(context)
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("$num", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (isPassed) Color(0x40FFFFFF) else Color.White)
                                    }
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
// GAME 55: TASK-SWITCHING PARITY VS MAGNITUDE (Cognitive task-set switching)
// =============================================================================
@Composable
fun FullTaskSwitchingParityMagnitudeScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var rule by remember { mutableStateOf("PARITY") } // "PARITY" (Odd/Even) or "MAGNITUDE" (>5 or <5)
    var currentNumber by remember { mutableIntStateOf(4) }

    fun nextNumber() {
        rule = if (Random.nextBoolean()) "PARITY" else "MAGNITUDE"
        currentNumber = Random.nextInt(1, 10)
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            nextNumber()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g55", "Task-Switching Arena", score, (score * 8).coerceIn(40, 100), 220)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar("Task Switching", score, "Standard", onBack, NeonAmber)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonAmber)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔄", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Cognitive Task-Switching", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Rule switches dynamically every round between 'ODD/EVEN' and 'GREATER/LESS THAN 5'!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "ENGAGE SWITCH", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Switching Complete", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonAmber)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Max Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "REPLAY", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; nextNumber() }, isPrimary = true)
                    }
                }
            } else {
                Text(
                    text = if (rule == "PARITY") "RULE: IS IT ODD OR EVEN?" else "RULE: IS IT GREATER THAN 5?",
                    color = if (rule == "PARITY") NeonCyan else NeonAmber,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                        .border(2.dp, if (rule == "PARITY") NeonCyan else NeonAmber, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$currentNumber", fontSize = 56.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(30.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    val label1 = if (rule == "PARITY") "ODD" else "≤ 5"
                    val label2 = if (rule == "PARITY") "EVEN" else "> 5"
                    val isOption1Correct = if (rule == "PARITY") (currentNumber % 2 != 0) else (currentNumber <= 5)

                    GlassButton(
                        text = label1,
                        onClick = {
                            if (isOption1Correct) { score += 10 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            nextNumber()
                        },
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = label2,
                        onClick = {
                            if (!isOption1Correct) { score += 10 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            nextNumber()
                        },
                        isPrimary = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// =============================================================================
// GAME 56: SPATIAL ISOMETRIC CUBE COUNT (Mental 3D hidden block tallying)
// =============================================================================
@Composable
fun FullSpatialIsometricCubeScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var baseCubes by remember { mutableIntStateOf(4) }
    var stackHeight by remember { mutableIntStateOf(2) }
    var correctCount by remember { mutableIntStateOf(8) }

    fun generate() {
        val width = Random.nextInt(2, 4)
        val depth = Random.nextInt(2, 4)
        val height = Random.nextInt(2, 4)
        baseCubes = width * depth
        stackHeight = height
        correctCount = width * depth * height
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generate()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g56", "Isometric Cube Count", score, (score * 8).coerceIn(40, 100), 240)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar("Isometric Cube Count", score, "Standard", onBack, NeonViolet)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonViolet)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🧊", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("3D Isometric Cube Count", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Calculate total cubes including hidden internal blocks supporting the upper tiers!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "TALLY CUBES", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Spatial Tally Complete", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonViolet)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "AGAIN", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generate() }, isPrimary = true)
                    }
                }
            } else {
                Text("Base Layer: $baseCubes blocks | Stack Height: $stackHeight tiers", color = Color.White, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Text("How many total cubes in the solid 3D isometric structure?", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(24.dp))

                val choices = remember(correctCount) {
                    listOf(correctCount, correctCount + 2, (correctCount - 2).coerceAtLeast(1), correctCount + 4).shuffled()
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    choices.forEach { choice ->
                        GlassButton(
                            text = "$choice",
                            onClick = {
                                if (choice == correctCount) {
                                    score += 15 + streak * 3
                                    streak++
                                    AuraSoundManager.playSuccessSound(context)
                                } else {
                                    lives--
                                    streak = 0
                                    AuraSoundManager.playErrorSound(context)
                                }
                                generate()
                            },
                            isPrimary = (choice == correctCount),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 57: AUDITORY PITCH MEMORY ECHO (3-tone melodic pitch interval recall)
// =============================================================================
@Composable
fun FullAuditoryPitchEchoScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var melodySequence by remember { mutableStateOf(listOf<String>()) }
    var userEntry by remember { mutableStateOf(listOf<String>()) }

    val notes = listOf("DO", "RE", "MI", "FA")

    fun generate() {
        melodySequence = (0..2).map { notes.random() }
        userEntry = emptyList()
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generate()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g57", "Pitch Memory Echo", score, (score * 8).coerceIn(40, 100), 230)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar("Auditory Pitch Echo", score, "Standard", onBack, NeonPink)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonPink)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎵", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Auditory Pitch Echo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Memorize the 3-tone solfège sequence, then repeat it back in exact acoustic order!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "HEAR MELODY", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Acoustic Memory Concluded", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonPink)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "AGAIN", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generate() }, isPrimary = true)
                    }
                }
            } else {
                Text("Target Melody: ${melodySequence.joinToString(" - ")}", color = NeonCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Your Input: ${userEntry.joinToString(" - ")}", color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    notes.forEach { note ->
                        GlassButton(
                            text = note,
                            onClick = {
                                val next = userEntry + note
                                userEntry = next
                                AuraSoundManager.playTapSound(context)
                                if (next == melodySequence) {
                                    score += 20 + streak * 3
                                    streak++
                                    AuraSoundManager.playSuccessSound(context)
                                    generate()
                                } else if (next.size >= melodySequence.size) {
                                    lives--
                                    streak = 0
                                    AuraSoundManager.playErrorSound(context)
                                    generate()
                                }
                            },
                            isPrimary = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 58: FLANKER ARROW SACCADE SPRINT (Central arrow direction vs flankers)
// =============================================================================
@Composable
fun FullFlankerArrowSaccadeScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var centralArrow by remember { mutableStateOf("←") }
    var flankerArrow by remember { mutableStateOf("←") }

    fun generate() {
        val left = Random.nextBoolean()
        centralArrow = if (left) "←" else "→"
        val congruent = Random.nextBoolean()
        flankerArrow = if (congruent) centralArrow else (if (left) "→" else "←")
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generate()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g58", "Flanker Arrow Saccade", score, (score * 8).coerceIn(40, 100), 220)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar("Flanker Saccade", score, "Standard", onBack, NeonCyan)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonCyan)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏹", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Eriksen Flanker Task", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Focus strictly on the CENTER arrow. Ignore the distracting flanker arrows on the sides!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "FOCUS CENTER", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Inhibition Sprint Complete", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonCyan)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "REPLAY", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generate() }, isPrimary = true)
                    }
                }
            } else {
                Text("WHICH WAY IS THE CENTER ARROW POINTING?", color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(flankerArrow, fontSize = 36.sp, color = Color.White.copy(alpha = 0.4f))
                    Text(flankerArrow, fontSize = 36.sp, color = Color.White.copy(alpha = 0.4f))
                    Text(centralArrow, fontSize = 52.sp, fontWeight = FontWeight.ExtraBold, color = NeonCyan)
                    Text(flankerArrow, fontSize = 36.sp, color = Color.White.copy(alpha = 0.4f))
                    Text(flankerArrow, fontSize = 36.sp, color = Color.White.copy(alpha = 0.4f))
                }

                Spacer(modifier = Modifier.height(34.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlassButton(
                        text = "LEFT ←",
                        onClick = {
                            if (centralArrow == "←") { score += 10 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            generate()
                        },
                        isPrimary = (centralArrow == "←"),
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = "RIGHT →",
                        onClick = {
                            if (centralArrow == "→") { score += 10 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            generate()
                        },
                        isPrimary = (centralArrow == "→"),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// =============================================================================
// GAME 59: MENTAL MATH FRACTION SPRINT (Fraction vs percentage comparison)
// =============================================================================
@Composable
fun FullMathFractionSprintScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var fractionStr by remember { mutableStateOf("1/4") }
    var percentValue by remember { mutableIntStateOf(20) }
    var fractionIsGreater by remember { mutableStateOf(true) }

    fun generate() {
        val frac = listOf("1/2" to 0.5, "1/4" to 0.25, "3/4" to 0.75, "1/5" to 0.20, "2/5" to 0.40, "4/5" to 0.80).random()
        fractionStr = frac.first
        val p = listOf(15, 30, 45, 60, 70, 85).random()
        percentValue = p
        fractionIsGreater = frac.second > (p / 100.0)
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generate()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g59", "Math Fraction Sprint", score, (score * 8).coerceIn(40, 100), 220)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar("Fraction Sprint", score, "Standard", onBack, NeonLime)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonLime)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("➗", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Fraction vs Percentage Sprint", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Determine under sub-second pressure whether the fraction is LARGER than the percentage!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "START CALC", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Sprint Finished", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonLime)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "RETRY", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generate() }, isPrimary = true)
                    }
                }
            } else {
                Text("Is $fractionStr > $percentValue% ?", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(modifier = Modifier.height(30.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlassButton(
                        text = "FALSE (≤) ❌",
                        onClick = {
                            if (!fractionIsGreater) { score += 10 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            generate()
                        },
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = "TRUE (>) ✅",
                        onClick = {
                            if (fractionIsGreater) { score += 10 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            generate()
                        },
                        isPrimary = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// =============================================================================
// GAME 60: LEXICAL RHYME SPEED MATCH (Phonological awareness rhyme classifier)
// =============================================================================
@Composable
fun FullLexicalRhymeSpeedScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var wordA by remember { mutableStateOf("BRIGHT") }
    var wordB by remember { mutableStateOf("NIGHT") }
    var doesRhyme by remember { mutableStateOf(true) }

    val rhymePairs = listOf(
        ("FOCUS" to "HOCUS") to true,
        ("FLOW" to "GLOW") to true,
        ("MIND" to "FIND") to true,
        ("BRAIN" to "TRAIN") to true,
        ("ZONE" to "STONE") to true,
        ("TIME" to "RHYME") to true,
        ("DEEP" to "JUMP") to false,
        ("AURA" to "SPEED") to false,
        ("CODE" to "ROAD") to true,
        ("PEAK" to "BOOK") to false
    )

    fun generate() {
        val item = rhymePairs.random()
        wordA = item.first.first
        wordB = item.first.second
        doesRhyme = item.second
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generate()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g60", "Lexical Rhyme Speed", score, (score * 8).coerceIn(40, 100), 220)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar("Lexical Rhyme", score, "Standard", onBack, NeonAmber)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonAmber)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎤", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Lexical Rhyme Speed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Does Word 1 rhyme acoustically with Word 2? Train phonological processing speed!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "START RHYME", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Rhyme Scan Complete", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonAmber)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "REPLAY", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generate() }, isPrimary = true)
                    }
                }
            } else {
                Text("$wordA   vs   $wordB", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(modifier = Modifier.height(30.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlassButton(
                        text = "NO RHYME ❌",
                        onClick = {
                            if (!doesRhyme) { score += 10 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            generate()
                        },
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = "RHYMES 🎵",
                        onClick = {
                            if (doesRhyme) { score += 10 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            generate()
                        },
                        isPrimary = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// =============================================================================
// GAME 61: COLOR STROOP TRIPLE CONFLICT (Text, Ink, and Frame 3-way clash)
// =============================================================================
@Composable
fun FullColorStroopTripleConflictScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var wordText by remember { mutableStateOf("CYAN") }
    var fontColor by remember { mutableStateOf(NeonPink) }
    var frameColor by remember { mutableStateOf(NeonLime) }
    var targetMatch by remember { mutableStateOf(false) }

    val colors = listOf("CYAN" to NeonCyan, "PINK" to NeonPink, "LIME" to NeonLime, "AMBER" to NeonAmber)

    fun generate() {
        val w = colors.random()
        val f = colors.random()
        val fr = colors.random()
        wordText = w.first
        fontColor = f.second
        frameColor = fr.second
        // Rule: Does the Font Color match the Border Frame Color?
        targetMatch = (f.first == fr.first)
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generate()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g61", "Color Stroop Triple Conflict", score, (score * 8).coerceIn(40, 100), 240)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar("Triple Conflict", score, "Standard", onBack, NeonPink)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonPink)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎨", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Triple Conflict Stroop", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Three levels of conflict: Text Word, Font Color, and Border Frame. Does Font Color equal Frame Color?", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "RESOLVE CONFLICT", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Conflict Resolution Over", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonPink)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "AGAIN", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generate() }, isPrimary = true)
                    }
                }
            } else {
                Text("Does Font Color MATCH Border Frame Color?", color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x20FFFFFF))
                        .border(4.dp, frameColor, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(wordText, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = fontColor)
                }

                Spacer(modifier = Modifier.height(30.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlassButton(
                        text = "DIFFERENT ❌",
                        onClick = {
                            if (!targetMatch) { score += 10 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            generate()
                        },
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = "MATCHES ✅",
                        onClick = {
                            if (targetMatch) { score += 10 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            generate()
                        },
                        isPrimary = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// =============================================================================
// GAME 62: NEURO VELOCITY REFLEX ARENA (Multi-target kinetic moving orb reflex)
// =============================================================================
@Composable
fun FullNeuroVelocityReflexArenaScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var targetPositionIndex by remember { mutableIntStateOf(0) }

    fun shiftTarget() {
        targetPositionIndex = Random.nextInt(9)
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            shiftTarget()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g62", "Velocity Reflex Arena", score, (score * 8).coerceIn(40, 100), 240)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar("Reflex Arena", score, "Standard", onBack, NeonCyan)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonCyan)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚡", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Neuro Velocity Reflex Arena", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Rapid sub-millisecond reaction: Tap the illuminated neon target the instant it materializes!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "ENTER ARENA", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Reflex Session Concluded", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonCyan)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "REPLAY", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; shiftTarget() }, isPrimary = true)
                    }
                }
            } else {
                Text("TAP THE ILLUMINATED NEON ORB RAPIDLY!", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(20.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    for (row in 0 until 3) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            for (col in 0 until 3) {
                                val idx = row * 3 + col
                                val isTarget = (idx == targetPositionIndex)
                                Box(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .clip(CircleShape)
                                        .background(if (isTarget) NeonCyan else Color(0x18FFFFFF))
                                        .border(1.5.dp, if (isTarget) Color.White else Color(0x33FFFFFF), CircleShape)
                                        .clickable {
                                            if (isTarget) {
                                                score += 15 + streak * 3
                                                streak++
                                                AuraSoundManager.playSuccessSound(context)
                                                shiftTarget()
                                            } else {
                                                lives--
                                                streak = 0
                                                AuraSoundManager.playErrorSound(context)
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isTarget) {
                                        Text("⚡", fontSize = 24.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
