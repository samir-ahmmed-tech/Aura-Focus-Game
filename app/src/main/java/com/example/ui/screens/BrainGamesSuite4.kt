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
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.util.AuraSoundManager
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

// =============================================================================
// GAME 43: QUANTUM MIRROR ENTANGLEMENT (Instant mirror symmetry detector)
// =============================================================================
@Composable
fun FullQuantumMirrorEntanglementScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    val leftPattern = remember { mutableStateListOf<Boolean>() }
    val rightPattern = remember { mutableStateListOf<Boolean>() }
    var isSymmetric by remember { mutableStateOf(true) }

    fun generateRound() {
        leftPattern.clear()
        rightPattern.clear()
        val symmetric = Random.nextBoolean()
        isSymmetric = symmetric
        for (i in 0 until 9) {
            val cell = Random.nextBoolean()
            leftPattern.add(cell)
        }
        for (row in 0 until 3) {
            for (col in 0 until 3) {
                val leftIndex = row * 3 + col
                if (symmetric) {
                    val rightCol = 2 - col
                    val rightIndex = row * 3 + rightCol
                    while (rightPattern.size <= rightIndex) rightPattern.add(false)
                    rightPattern[rightIndex] = leftPattern[leftIndex]
                } else {
                    rightPattern.add(Random.nextBoolean())
                }
            }
        }
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generateRound()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g43", "Quantum Mirror Entanglement", score, (score * 8).coerceIn(40, 100), 240)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GameHeaderBar(title = "Quantum Mirror Entanglement", subtitle = "Symmetry & Parity IQ", onBack = onBack, accentColor = NeonCyan)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonCyan)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🪞", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Quantum Entanglement", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Determine if the right grid is an exact horizontal mirror reflection of the left grid.", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "ENGAGE SYMMETRY", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (score > 10) "🌟 Quantum Master!" else "⚡ Session Complete", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonCyan)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Max Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "PLAY AGAIN", onClick = {
                            score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generateRound()
                        }, isPrimary = true)
                    }
                }
            } else {
                Text("Are both grids horizontally mirrored?", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Grid
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (r in 0 until 3) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                for (c in 0 until 3) {
                                    val idx = r * 3 + c
                                    val active = if (idx < leftPattern.size) leftPattern[idx] else false
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (active) NeonCyan else Color(0x22FFFFFF))
                                            .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    )
                                }
                            }
                        }
                    }

                    // Mirror Line
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(120.dp)
                            .background(Color.White.copy(alpha = 0.3f))
                    )

                    // Right Grid
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (r in 0 until 3) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                for (c in 0 until 3) {
                                    val idx = r * 3 + c
                                    val active = if (idx < rightPattern.size) rightPattern[idx] else false
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (active) NeonPink else Color(0x22FFFFFF))
                                            .border(1.dp, NeonPink.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GlassButton(
                        text = "MISMATCH ❌",
                        onClick = {
                            if (!isSymmetric) {
                                score += 10 + streak * 2
                                streak++
                                AuraSoundManager.playSuccessSound(context)
                            } else {
                                lives--
                                streak = 0
                                AuraSoundManager.playErrorSound(context)
                            }
                            generateRound()
                        },
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = "MIRRORED ✨",
                        onClick = {
                            if (isSymmetric) {
                                score += 10 + streak * 2
                                streak++
                                AuraSoundManager.playSuccessSound(context)
                            } else {
                                lives--
                                streak = 0
                                AuraSoundManager.playErrorSound(context)
                            }
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
// GAME 44: DUAL CONFLICT COLOR CLASH (Stroop Word vs Background Inversion)
// =============================================================================
@Composable
fun FullStroopDualConflictScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    val colors = listOf("CYAN" to NeonCyan, "PINK" to NeonPink, "LIME" to NeonLime, "AMBER" to NeonAmber)
    var card1Text by remember { mutableStateOf("CYAN") }
    var card1Color by remember { mutableStateOf(NeonPink) }
    var card2Text by remember { mutableStateOf("PINK") }
    var card2Color by remember { mutableStateOf(NeonCyan) }
    var matchTarget by remember { mutableStateOf(true) }

    fun generate() {
        val c1 = colors[Random.nextInt(colors.size)]
        val c2 = colors[Random.nextInt(colors.size)]
        val c3 = colors[Random.nextInt(colors.size)]
        val c4 = colors[Random.nextInt(colors.size)]
        card1Text = c1.first
        card1Color = c2.second
        card2Text = c3.first
        card2Color = c4.second
        // Rule: Does Card 1 Text color match Card 2 Written Word?
        matchTarget = (c2.first == c3.first)
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generate()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g44", "Dual Conflict Color Clash", score, (score * 8).coerceIn(40, 100), 220)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar(title = "Dual Conflict Color Clash", subtitle = "Inhibition Duel", onBack = onBack, accentColor = NeonPink)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonPink)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💥", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Dual Conflict Clash", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Rule: Does Card 1's Ink Color equal Card 2's Written Text? Suppress the conflicting words!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "START DUEL", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚡ Session Finished", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonPink)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "RETRY", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generate() }, isPrimary = true)
                    }
                }
            } else {
                Text("Card 1 Ink Color == Card 2 Word?", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("CARD 1", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(card1Text, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = card1Color)
                        }
                    }
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("CARD 2", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(card2Text, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = card2Color)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlassButton(
                        text = "NO ❌",
                        onClick = {
                            if (!matchTarget) { score += 10 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
                            else { lives--; streak = 0; AuraSoundManager.playErrorSound(context) }
                            generate()
                        },
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = "YES ✅",
                        onClick = {
                            if (matchTarget) { score += 10 + streak * 2; streak++; AuraSoundManager.playSuccessSound(context) }
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
// GAME 45: BACKWARD DIGIT SPAN RECALL (Clinical working memory manipulation)
// =============================================================================
@Composable
fun FullReverseDigitSpanScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var currentLength by remember { mutableIntStateOf(3) }
    var lives by remember { mutableIntStateOf(3) }
    var isShowingSequence by remember { mutableStateOf(true) }
    var currentDisplayDigit by remember { mutableStateOf("") }
    var sequence by remember { mutableStateOf(listOf<Int>()) }
    var userInput by remember { mutableStateOf(listOf<Int>()) }
    var isGameOver by remember { mutableStateOf(false) }

    fun playRound() {
        val newSeq = (0 until currentLength).map { Random.nextInt(1, 10) }
        sequence = newSeq
        userInput = emptyList()
        isShowingSequence = true
    }

    LaunchedEffect(isStarted, currentLength, isGameOver) {
        if (isStarted && !isGameOver) {
            playRound()
            for (d in sequence) {
                currentDisplayDigit = d.toString()
                delay(850)
                currentDisplayDigit = ""
                delay(200)
            }
            isShowingSequence = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar(title = "Reverse Digit Span", subtitle = "WAIS Working Memory", onBack = onBack, accentColor = NeonViolet)
        GameStatHUD(secondsLeft = currentLength, currentLives = lives, streak = score / 10, accentColor = NeonViolet)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🧠", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Backward Digit Span", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Watch the digits appear, then enter them in strict REVERSE order. Length increases with each success!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "TEST MEMORY", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Working Memory Limit Reached", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonViolet)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Max Span: $currentLength Digits | Score: $score", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "TRY AGAIN", onClick = {
                            score = 0; lives = 3; currentLength = 3; isGameOver = false; playRound()
                        }, isPrimary = true)
                    }
                }
            } else {
                if (isShowingSequence) {
                    Text("MEMORIZE DIGITS...", color = NeonViolet, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(NeonViolet.copy(alpha = 0.2f)).border(2.dp, NeonViolet, CircleShape), contentAlignment = Alignment.Center) {
                        Text(currentDisplayDigit, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                } else {
                    Text("ENTER IN REVERSE ORDER (${sequence.size - userInput.size} left)", color = NeonCyan, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(userInput.joinToString(" "), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(20.dp))

                    // 1-9 Keypad
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (row in 0 until 3) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                for (col in 1..3) {
                                    val digit = row * 3 + col
                                    Box(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0x22FFFFFF))
                                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                                            .clickable {
                                                val nextInput = userInput + digit
                                                userInput = nextInput
                                                val expectedDigit = sequence[sequence.size - nextInput.size]
                                                if (digit != expectedDigit) {
                                                    lives--
                                                    AuraSoundManager.playErrorSound(context)
                                                    if (lives <= 0) isGameOver = true
                                                    else playRound()
                                                } else if (nextInput.size == sequence.size) {
                                                    score += currentLength * 15
                                                    currentLength++
                                                    AuraSoundManager.playSuccessSound(context)
                                                    playRound()
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("$digit", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
// GAME 46: MENTAL ORIGAMI HOLE PUNCH (Visuospatial folding prediction)
// =============================================================================
@Composable
fun FullMentalOrigamiScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var foldDirection by remember { mutableStateOf("HORIZONTAL") } // "HORIZONTAL" (left folded to right) or "VERTICAL" (top to bottom)
    var punchQuadrant by remember { mutableIntStateOf(0) } // 0..3 (TL, TR, BL, BR)
    var correctPattern by remember { mutableStateOf(listOf<Int>()) }

    fun generate() {
        foldDirection = if (Random.nextBoolean()) "HORIZONTAL" else "VERTICAL"
        punchQuadrant = Random.nextInt(4)
        correctPattern = if (foldDirection == "HORIZONTAL") {
            // Left folded over right: punching on right punch will mirror horizontally
            when (punchQuadrant) {
                1 -> listOf(0, 1)
                3 -> listOf(2, 3)
                else -> listOf(punchQuadrant)
            }
        } else {
            // Top folded over bottom: mirrors vertically
            when (punchQuadrant) {
                2 -> listOf(0, 2)
                3 -> listOf(1, 3)
                else -> listOf(punchQuadrant)
            }
        }
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generate()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g46", "Mental Origami Hole Punch", score, (score * 8).coerceIn(40, 100), 250)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar(title = "Mental Origami Fold", subtitle = "Visuospatial Projection", onBack = onBack, accentColor = NeonLime)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonLime)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📐", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Mental Origami Fold", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Predict how many holes appear and where they land when the folded paper is unfolded!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "UNFOLD MIND", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Spatial Projection Complete", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonLime)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "AGAIN", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generate() }, isPrimary = true)
                    }
                }
            } else {
                Text("Fold: $foldDirection | Punch at Position #$punchQuadrant", color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Text("How many holes will the unfolded sheet have?", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    listOf(1, 2).forEach { answerCount ->
                        val isCorrect = correctPattern.size == answerCount
                        GlassButton(
                            text = "$answerCount HOLE${if (answerCount > 1) "S" else ""}",
                            onClick = {
                                if (isCorrect) {
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
                            isPrimary = isCorrect,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 47: COLOR PULSE RHYTHM SYNC (Dynamic speed rhythm target reaction)
// =============================================================================
@Composable
fun FullColorPulseRhythmScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var pulseRadius by remember { mutableFloatStateOf(0f) }
    var isExpanding by remember { mutableStateOf(true) }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            while (secondsLeft > 0 && lives > 0) {
                delay(30)
                if (isExpanding) {
                    pulseRadius += 0.04f
                    if (pulseRadius >= 1f) isExpanding = false
                } else {
                    pulseRadius -= 0.04f
                    if (pulseRadius <= 0f) isExpanding = true
                }
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g47", "Color Pulse Rhythm Sync", score, (score * 7).coerceIn(40, 100), 220)
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

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar(title = "Color Pulse Rhythm", subtitle = "Sub-50ms Beat Sync", onBack = onBack, accentColor = NeonAmber)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonAmber)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎯", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Color Pulse Rhythm", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tap the ring precisely when the expanding wave strikes the target boundary circle!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "SYNC PULSE", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Sync Session Ended", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonAmber)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Max Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "REPLAY", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false }, isPrimary = true)
                    }
                }
            } else {
                Text("TAP WHEN THE EXPANDING RING OVERLAPS THE BORDER!", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape)
                        .clickable {
                            val accuracy = abs(pulseRadius - 0.9f)
                            if (accuracy < 0.18f) {
                                score += 20 + streak * 4
                                streak++
                                AuraSoundManager.playSuccessSound(context)
                            } else {
                                lives--
                                streak = 0
                                AuraSoundManager.playErrorSound(context)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(color = Color(0x33FFFFFF), radius = size.minDimension / 2f, style = Stroke(width = 4.dp.toPx()))
                        drawCircle(color = NeonAmber, radius = (size.minDimension / 2f) * pulseRadius.coerceIn(0.1f, 1f), style = Stroke(width = 8.dp.toPx()))
                    }
                    Text("TAP NOW", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

// =============================================================================
// GAME 48: TOWER OF HANOI FLOW (Executive sequence planning)
// =============================================================================
@Composable
fun FullTowerOfHanoiMiniScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var moves by remember { mutableIntStateOf(0) }
    var selectedPeg by remember { mutableStateOf<Int?>(null) }
    var isWon by remember { mutableStateOf(false) }

    val pegs = remember {
        mutableStateListOf(
            mutableListOf(3, 2, 1),
            mutableListOf<Int>(),
            mutableListOf<Int>()
        )
    }

    fun resetGame() {
        pegs[0].clear(); pegs[0].addAll(listOf(3, 2, 1))
        pegs[1].clear()
        pegs[2].clear()
        moves = 0
        selectedPeg = null
        isWon = false
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar(title = "Tower of Hanoi Flow", subtitle = "Executive Sequence Planning", onBack = onBack, accentColor = NeonCyan)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏛️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Tower of Hanoi Flow", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Move all 3 disks from Peg 1 to Peg 3. You can never place a larger disk on top of a smaller one!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "START PUZZLE", onClick = { isStarted = true; resetGame() }, isPrimary = true)
                    }
                }
            } else if (isWon) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏆 Solved in $moves Moves!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonLime)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Optimal is 7 moves. Outstanding cognitive planning!", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "SOLVE AGAIN", onClick = { resetGame() }, isPrimary = true)
                    }
                }
            } else {
                Text("Moves: $moves | Tap source peg then destination peg", color = Color.White, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    for (pegIdx in 0..2) {
                        val isSelected = selectedPeg == pegIdx
                        Column(
                            modifier = Modifier
                                .width(80.dp)
                                .height(160.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NeonCyan.copy(alpha = 0.25f) else Color(0x18FFFFFF))
                                .clickable {
                                    if (selectedPeg == null) {
                                        if (pegs[pegIdx].isNotEmpty()) selectedPeg = pegIdx
                                    } else {
                                        val from = selectedPeg!!
                                        if (from != pegIdx) {
                                            val sourceTop = pegs[from].lastOrNull()
                                            val destTop = pegs[pegIdx].lastOrNull()
                                            if (sourceTop != null && (destTop == null || sourceTop < destTop)) {
                                                pegs[from].removeAt(pegs[from].size - 1)
                                                pegs[pegIdx].add(sourceTop)
                                                moves++
                                                AuraSoundManager.playTapSound(context)
                                                if (pegs[2].size == 3) {
                                                    isWon = true
                                                    GamificationManager.recordGameMatch("g48", "Tower of Hanoi Flow", 100 - moves * 5, 100, 260)
                                                }
                                            } else {
                                                AuraSoundManager.playErrorSound(context)
                                            }
                                        }
                                        selectedPeg = null
                                    }
                                }
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            pegs[pegIdx].forEach { diskSize ->
                                val diskWidth = (diskSize * 22).dp
                                Box(
                                    modifier = Modifier
                                        .width(diskWidth)
                                        .height(18.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (diskSize == 1) NeonLime else if (diskSize == 2) NeonAmber else NeonPink)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            Text("Peg ${pegIdx + 1}", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 49: LEXICAL ANOMALY HUNT (High-speed word anomaly spotting)
// =============================================================================
@Composable
fun FullLexicalSpeedHuntScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var wordOptions by remember { mutableStateOf(listOf<String>()) }
    var anomalyWord by remember { mutableStateOf("") }

    val focusClusters = listOf(
        listOf("FOCUS", "ATTENTION", "CONCENTRATE", "FLOW", "ZONE", "MIND", "AWARE", "DISCIPLINE") to "BURGER",
        listOf("QUANTUM", "NEURON", "SYNAPSE", "CORTEX", "DENDRITE", "AXON", "BRAIN", "LOBE") to "TRACTOR",
        listOf("TEMPO", "RHYTHM", "HARMONY", "CADENCE", "CHORD", "MELODY", "BEAT", "OCTAVE") to "HAMMER",
        listOf("ZEN", "CALM", "SERENE", "TRANQUIL", "STILL", "PEACE", "BREATH", "REST") to "TRAFFIC"
    )

    fun generate() {
        val cluster = focusClusters[Random.nextInt(focusClusters.size)]
        anomalyWord = cluster.second
        val list = (cluster.first.shuffled().take(7) + cluster.second).shuffled()
        wordOptions = list
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generate()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g49", "Lexical Anomaly Hunt", score, (score * 8).coerceIn(40, 100), 220)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar(title = "Lexical Anomaly Hunt", subtitle = "Verbal Anomaly Spotter", onBack = onBack, accentColor = NeonViolet)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonViolet)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔤", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Lexical Anomaly Hunt", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Spot the 1 intruder word that does NOT belong to the semantic focus cluster!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "SPOT ANOMALIES", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Lexical Scan Complete", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonViolet)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "REPLAY", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generate() }, isPrimary = true)
                    }
                }
            } else {
                Text("TAP THE INTRUDER WORD!", color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (row in 0 until 4) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            for (col in 0 until 2) {
                                val idx = row * 2 + col
                                if (idx < wordOptions.size) {
                                    val word = wordOptions[idx]
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0x22FFFFFF))
                                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(10.dp))
                                            .clickable {
                                                if (word == anomalyWord) {
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
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(word, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
// GAME 50: SYNAPSE CIRCUIT ROUTER (Grid wire routing speed challenge)
// =============================================================================
@Composable
fun FullSynapseCircuitConnectScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    // 4 tiles with rotation angles (0, 90, 180, 270)
    val tileRotations = remember { mutableStateListOf(0, 90, 180, 270) }
    var targetRotations = listOf(90, 180, 0, 90)

    fun generate() {
        for (i in 0 until 4) {
            tileRotations[i] = (Random.nextInt(4) * 90)
        }
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generate()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g50", "Synapse Circuit Router", score, (score * 8).coerceIn(40, 100), 240)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar(title = "Synapse Circuit Router", subtitle = "Neural Conduit Aligner", onBack = onBack, accentColor = NeonCyan)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonCyan)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚡", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Synapse Circuit Router", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tap tiles to rotate them into a continuous circuit line and conduct the signal!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "BOOT CIRCUIT", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Circuit Online", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonCyan)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "AGAIN", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generate() }, isPrimary = true)
                    }
                }
            } else {
                Text("Align tiles horizontally to transmit signal!", color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    for (i in 0 until 4) {
                        val rot = tileRotations[i]
                        val isAligned = (rot % 180 == 0)
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .padding(4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isAligned) NeonCyan.copy(alpha = 0.25f) else Color(0x22FFFFFF))
                                .border(1.dp, if (isAligned) NeonCyan else Color(0x33FFFFFF), RoundedCornerShape(8.dp))
                                .clickable {
                                    tileRotations[i] = (rot + 90) % 360
                                    AuraSoundManager.playTapSound(context)
                                    if (tileRotations.all { it % 180 == 0 }) {
                                        score += 20 + streak * 4
                                        streak++
                                        AuraSoundManager.playSuccessSound(context)
                                        generate()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(if (rot % 180 == 0) "━" else "┃", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = if (isAligned) NeonCyan else Color.White)
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// GAME 51: MICROTONE INTERVAL CALIBRATOR (Acoustic pitch ear calibrator)
// =============================================================================
@Composable
fun FullAcousticIntervalScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var toneFreqA by remember { mutableIntStateOf(440) }
    var toneFreqB by remember { mutableIntStateOf(440) }

    fun generate() {
        toneFreqA = Random.nextInt(300, 600)
        val diff = if (Random.nextBoolean()) Random.nextInt(10, 40) else -Random.nextInt(10, 40)
        toneFreqB = toneFreqA + diff
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generate()
            while (secondsLeft > 0 && lives > 0) {
                delay(1000)
                secondsLeft--
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g51", "Microtone Interval Calibrator", score, (score * 8).coerceIn(40, 100), 230)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar(title = "Microtone Interval", subtitle = "Acoustic Ear Discriminator", onBack = onBack, accentColor = NeonPink)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonPink)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎵", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Microtone Calibrator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Compare the wave frequencies between Tone A and Tone B. Is Tone B HIGHER or LOWER in pitch?", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "TUNE EAR", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Acoustic Calibration Over", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonPink)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "RECALIBRATE", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generate() }, isPrimary = true)
                    }
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("TONE A", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("$toneFreqA Hz", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                        }
                    }
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("TONE B", fontSize = 11.sp, color = Color.White.copy(alpha = 0.6f))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("$toneFreqB Hz", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = NeonPink)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    GlassButton(
                        text = "B IS LOWER 📉",
                        onClick = {
                            if (toneFreqB < toneFreqA) {
                                score += 10 + streak * 2
                                streak++
                                AuraSoundManager.playSuccessSound(context)
                            } else {
                                lives--
                                streak = 0
                                AuraSoundManager.playErrorSound(context)
                            }
                            generate()
                        },
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = "B IS HIGHER 📈",
                        onClick = {
                            if (toneFreqB > toneFreqA) {
                                score += 10 + streak * 2
                                streak++
                                AuraSoundManager.playSuccessSound(context)
                            } else {
                                lives--
                                streak = 0
                                AuraSoundManager.playErrorSound(context)
                            }
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
// GAME 52: PERIPHERAL ANOMALY SCANNER (Anti-change blindness trainer)
// =============================================================================
@Composable
fun FullPeripheralChangeBlindnessScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isStarted by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(30) }
    var isGameOver by remember { mutableStateOf(false) }

    var changingIndex by remember { mutableIntStateOf(0) }
    var flashState by remember { mutableStateOf(false) }

    fun generate() {
        changingIndex = Random.nextInt(9)
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            generate()
            while (secondsLeft > 0 && lives > 0) {
                delay(400)
                flashState = !flashState
            }
            isGameOver = true
            GamificationManager.recordGameMatch("g52", "Peripheral Anomaly Scanner", score, (score * 8).coerceIn(40, 100), 240)
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

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        GameHeaderBar(title = "Peripheral Anomaly Scanner", subtitle = "Change Blindness Radar", onBack = onBack, accentColor = NeonLime)
        GameStatHUD(secondsLeft = secondsLeft, currentLives = lives, streak = streak, accentColor = NeonLime)

        Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            if (!isStarted) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📡", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Peripheral Anomaly Scanner", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("One cell in the 3x3 matrix is secretly flickering color between frames. Tap the changing cell!", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "SCAN SCENE", onClick = { isStarted = true }, isPrimary = true)
                    }
                }
            } else if (isGameOver) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Scan Finished", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = NeonLime)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Score: $score | Streak: $streak", color = Color.White)
                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(text = "AGAIN", onClick = { score = 0; streak = 0; lives = 3; secondsLeft = 30; isGameOver = false; generate() }, isPrimary = true)
                    }
                }
            } else {
                Text("TAP THE CELL THAT IS FLICKERING IN COLOR!", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(20.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (row in 0 until 3) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            for (col in 0 until 3) {
                                val idx = row * 3 + col
                                val isTheChangingCell = (idx == changingIndex)
                                val cellColor = if (isTheChangingCell && flashState) NeonPink else NeonLime
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(cellColor.copy(alpha = 0.25f))
                                        .border(1.5.dp, cellColor, RoundedCornerShape(12.dp))
                                        .clickable {
                                            if (isTheChangingCell) {
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
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✦", fontSize = 20.sp, color = cellColor)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
