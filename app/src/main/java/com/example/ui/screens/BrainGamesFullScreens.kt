package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.example.util.AuraSoundManager
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Reusable pre-game setup screen that lets user customize difficulty, duration, lives/hearts, and game-specific modes.
 */
@Composable
fun GamePreStartScreen(
    title: String,
    emoji: String,
    description: String,
    difficulty: String,
    onDifficultyChange: (String) -> Unit,
    timeLimit: Int,
    onTimeLimitChange: (Int) -> Unit,
    lives: Int,
    onLivesChange: (Int) -> Unit,
    customOptionLabel: String? = null,
    customOptions: List<String> = emptyList(),
    selectedCustomOption: String = "",
    onCustomOptionChange: (String) -> Unit = {},
    accentColor: Color,
    onStartGame: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Top Back Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0x18FFFFFF))
                    .border(1.dp, Color(0x30FFFFFF), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Game Setup & Customize",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Hero Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = accentColor,
            glowRadius = 14.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.2f))
                        .border(1.5.dp, accentColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 32.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // 1. Difficulty Selector
        Text("1. Select Difficulty:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Easy", "Medium", "Hard", "Master").forEach { diff ->
                val isSelected = difficulty == diff
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) accentColor.copy(alpha = 0.25f) else Color(0x14FFFFFF))
                        .border(1.5.dp, if (isSelected) accentColor else Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                        .clickable { onDifficultyChange(diff) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        diff,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) accentColor else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 2. Time Option (Duration or Unlimited)
        Text("2. Time Limit (Timer):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                30 to "30s",
                60 to "60s",
                120 to "120s",
                -1 to "Unlimited ∞"
            ).forEach { (secs, label) ->
                val isSelected = timeLimit == secs
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) NeonCyan.copy(alpha = 0.25f) else Color(0x14FFFFFF))
                        .border(1.5.dp, if (isSelected) NeonCyan else Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                        .clickable { onTimeLimitChange(secs) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) NeonCyan else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 3. Lives / Hearts (Love)
        Text("3. Hearts / Lives (Love):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                3 to "3 Hearts ❤️",
                5 to "5 Hearts ❤️",
                -1 to "Unlimited ∞"
            ).forEach { (cnt, label) ->
                val isSelected = lives == cnt
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) NeonPink.copy(alpha = 0.25f) else Color(0x14FFFFFF))
                        .border(1.5.dp, if (isSelected) NeonPink else Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                        .clickable { onLivesChange(cnt) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) NeonPink else Color.White
                    )
                }
            }
        }

        // 4. Custom Game-Specific Option
        if (customOptionLabel != null && customOptions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(18.dp))
            Text("4. $customOptionLabel:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                customOptions.forEach { opt ->
                    val isSelected = selectedCustomOption == opt
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NeonLime.copy(alpha = 0.25f) else Color(0x14FFFFFF))
                            .border(1.5.dp, if (isSelected) NeonLime else Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                        .clickable { onCustomOptionChange(opt) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            opt,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) NeonLime else Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Start Game Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.horizontalGradient(listOf(accentColor, NeonLime)))
                .clickable { onStartGame() },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PlayArrow, "Start", tint = Color.Black, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("START GAME ⚡", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * Top bar for active full screen game session
 */
@Composable
fun GameHeaderBar(
    title: String,
    score: Int,
    difficulty: String,
    onBack: () -> Unit,
    accentColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0x18FFFFFF))
                .border(1.dp, Color(0x30FFFFFF), CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Text(
                text = "$difficulty Mode",
                fontSize = 10.sp,
                color = accentColor,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(accentColor.copy(alpha = 0.2f))
                .border(1.dp, accentColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text("Score: $score", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accentColor)
        }
    }
}

/**
 * Reusable Game Over / Victory Modal
 */
@Composable
fun GameOverResultsCard(
    score: Int,
    streak: Int,
    elapsedSeconds: Int,
    difficulty: String,
    accentColor: Color,
    onPlayAgain: () -> Unit,
    onReconfigure: () -> Unit,
    onExit: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        glowColor = accentColor,
        glowRadius = 18.dp
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Session Complete! 🏆", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Final Score", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("$score", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = accentColor)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Best Streak", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("$streak", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = NeonLime)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Time", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${elapsedSeconds}s", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = NeonCyan)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Played on $difficulty Mode", fontSize = 11.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(22.dp))

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GlassButton(
                    text = "Play Again (Same Settings) 🔄",
                    onClick = onPlayAgain
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x18FFFFFF))
                        .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(14.dp))
                        .clickable { onReconfigure() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Settings, "Config", tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Customize / Change Settings", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Transparent)
                        .clickable { onExit() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Exit to Games Menu", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// =============================================================
// GAME 1: FULL SCREEN MENTAL MATH SPRINT
// =============================================================
@Composable
fun FullMentalMathScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    // Pre-start customization state
    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }
    var opType by remember { mutableStateOf("Add & Sub") }

    // In-game state
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    val rangeMax = when (difficulty) {
        "Easy" -> 25
        "Hard" -> 99
        "Master" -> 250
        else -> 50
    }

    var numA by remember { mutableIntStateOf(Random.nextInt(10, rangeMax)) }
    var numB by remember { mutableIntStateOf(Random.nextInt(5, rangeMax / 2 + 2)) }
    var operator by remember { mutableStateOf("+") }

    val correctAnswer = when (operator) {
        "+" -> numA + numB
        "-" -> numA - numB
        "×" -> numA * (numB % 10 + 2)
        else -> numA + numB
    }

    val options = remember(numA, numB, operator) {
        val wrong1 = correctAnswer + Random.nextInt(2, 6)
        val wrong2 = maxOf(1, correctAnswer - Random.nextInt(2, 7))
        val wrong3 = correctAnswer + 10
        listOf(correctAnswer, wrong1, wrong2, wrong3).shuffled()
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) isGameOver = true
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Mental Math Sprint",
            emoji = "🧮",
            description = "Train rapid numerical agility and arithmetic speed. Solve dynamic equations under pressure.",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            customOptionLabel = "Operations",
            customOptions = listOf("Add & Sub", "Multiplication", "All Mixed"),
            selectedCustomOption = opType,
            onCustomOptionChange = { opType = it },
            accentColor = NeonCyan,
            onStartGame = {
                score = 0
                streak = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                elapsedSeconds = 0
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
            .background(Color(0xFF070912)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("🧮 Mental Math", score, difficulty, onBack, NeonCyan)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = streak,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonCyan,
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            // HUD
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, "Timer", tint = NeonCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "${secondsLeft}s",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                glowColor = NeonCyan,
                glowRadius = 14.dp
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(28.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "$numA $operator $numB = ?",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                options.forEach { opt ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x18FFFFFF))
                            .border(1.5.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .clickable {
                                if (opt == correctAnswer) {
                                    score++
                                    streak++
                                    GamificationManager.recordGamePlayed(1)
                                } else {
                                    streak = 0
                                    if (maxLives != -1) {
                                        currentLives--
                                        if (currentLives <= 0) isGameOver = true
                                    }
                                }
                                numA = Random.nextInt(10, rangeMax)
                                numB = Random.nextInt(5, rangeMax / 2 + 2)
                                operator = when (opType) {
                                    "Multiplication" -> "×"
                                    "All Mixed" -> listOf("+", "-", "×").random()
                                    else -> listOf("+", "-").random()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$opt", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

// =============================================================
// GAME 2: FULL SCREEN STROOP COLOR CLASH
// =============================================================
@Composable
fun FullStroopScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(45) }
    var maxLives by remember { mutableIntStateOf(3) }
    var stroopMode by remember { mutableStateOf("Ink Color") }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(45) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    val colorPairs = listOf(
        "RED" to NeonPink,
        "BLUE" to NeonCyan,
        "GREEN" to NeonLime,
        "YELLOW" to NeonAmber,
        "PURPLE" to NeonViolet
    )

    var currentWord by remember { mutableStateOf("RED") }
    var currentColor by remember { mutableStateOf(NeonCyan) }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) isGameOver = true
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Stroop Color Clash",
            emoji = "⚡",
            description = "Test cognitive inhibition & mental flexibility. Tap the color of the ink, not the text word!",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            customOptionLabel = "Target Mode",
            customOptions = listOf("Ink Color", "Word Meaning"),
            selectedCustomOption = stroopMode,
            onCustomOptionChange = { stroopMode = it },
            accentColor = NeonPink,
            onStartGame = {
                score = 0
                streak = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                elapsedSeconds = 0
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
            .background(Color(0xFF070912)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("⚡ Stroop Clash", score, difficulty, onBack, NeonPink)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = streak,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonPink,
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonPink
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                if (stroopMode == "Ink Color") "TAP THE INK COLOR! 👇" else "TAP THE TEXT WORD! 👇",
                fontSize = 13.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                glowColor = currentColor,
                glowRadius = 16.dp
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = currentWord,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = currentColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                colorPairs.forEach { (name, col) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x18FFFFFF))
                            .border(1.5.dp, col, RoundedCornerShape(14.dp))
                            .clickable {
                                val isCorrect = if (stroopMode == "Ink Color") {
                                    col == currentColor
                                } else {
                                    name == currentWord
                                }

                                if (isCorrect) {
                                    score++
                                    streak++
                                    GamificationManager.recordGamePlayed(1)
                                } else {
                                    streak = 0
                                    if (maxLives != -1) {
                                        currentLives--
                                        if (currentLives <= 0) isGameOver = true
                                    }
                                }

                                val pairWord = colorPairs.random()
                                val pairColor = colorPairs.random()
                                currentWord = pairWord.first
                                currentColor = pairColor.second
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = col)
                    }
                }
            }
        }
    }
}

// =============================================================
// GAME 3: FULL SCREEN MEMORY MATRIX GRID
// =============================================================
@Composable
fun FullMemoryMatrixScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }
    var gridSizeChoice by remember { mutableStateOf("3x3 Grid") }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    val gridSize = if (gridSizeChoice == "4x4 Grid") 4 else 3
    val totalTiles = gridSize * gridSize
    val activeTileCount = when (difficulty) {
        "Easy" -> 3
        "Hard" -> 5
        "Master" -> 6
        else -> 4
    }

    var activePattern by remember { mutableStateOf(setOf<Int>()) }
    var userTapped by remember { mutableStateOf(setOf<Int>()) }
    var isShowingPattern by remember { mutableStateOf(true) }

    fun nextRound() {
        val pattern = mutableSetOf<Int>()
        while (pattern.size < activeTileCount) {
            pattern.add(Random.nextInt(0, totalTiles))
        }
        activePattern = pattern
        userTapped = emptySet()
        isShowingPattern = true
    }

    LaunchedEffect(isShowingPattern, isStarted) {
        if (isStarted && isShowingPattern) {
            val flashTime = when (difficulty) {
                "Easy" -> 1800L
                "Hard" -> 900L
                "Master" -> 600L
                else -> 1200L
            }
            delay(flashTime)
            isShowingPattern = false
        }
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) isGameOver = true
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Memory Matrix",
            emoji = "🧩",
            description = "Train visual working memory and spatial encoding. Memorize highlighted grid tiles before they disappear.",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            customOptionLabel = "Grid Size",
            customOptions = listOf("3x3 Grid", "4x4 Grid"),
            selectedCustomOption = gridSizeChoice,
            onCustomOptionChange = { gridSizeChoice = it },
            accentColor = NeonLime,
            onStartGame = {
                score = 0
                streak = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                elapsedSeconds = 0
                isGameOver = false
                isStarted = true
                nextRound()
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
        GameHeaderBar("🧩 Memory Matrix", score, difficulty, onBack, NeonLime)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = streak,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonLime,
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                    nextRound()
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonLime
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                if (isShowingPattern) "MEMORIZE PATTERN..." else "RECALL & TAP TILES! (${userTapped.size}/${activePattern.size})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isShowingPattern) NeonAmber else NeonLime
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (row in 0 until gridSize) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        for (col in 0 until gridSize) {
                            val tileIdx = row * gridSize + col
                            val isHighlighted = (isShowingPattern && activePattern.contains(tileIdx)) || userTapped.contains(tileIdx)

                            Box(
                                modifier = Modifier
                                    .size(if (gridSize == 4) 65.dp else 85.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        if (isHighlighted) NeonLime else Color(0x18FFFFFF)
                                    )
                                    .border(
                                        1.5.dp,
                                        if (isHighlighted) NeonLime else Color(0x30FFFFFF),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable(enabled = !isShowingPattern && !userTapped.contains(tileIdx)) {
                                        if (activePattern.contains(tileIdx)) {
                                            val newTapped = userTapped + tileIdx
                                            userTapped = newTapped
                                            if (newTapped.size == activePattern.size) {
                                                score += 5
                                                streak++
                                                GamificationManager.recordGamePlayed(1)
                                                nextRound()
                                            }
                                        } else {
                                            streak = 0
                                            if (maxLives != -1) {
                                                currentLives--
                                                if (currentLives <= 0) isGameOver = true
                                            }
                                            nextRound()
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================================
// GAME 4: FULL SCREEN SPEED SEQUENCE FLASHER
// =============================================================
@Composable
fun FullSpeedSequenceScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }
    var playbackSpeed by remember { mutableStateOf("Normal") }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    val colors = listOf(NeonCyan, NeonPink, NeonLime, NeonAmber)
    val colorNames = listOf("Cyan", "Pink", "Lime", "Amber")
    val sequence = remember { mutableStateListOf<Int>() }
    var userIndex by remember { mutableIntStateOf(0) }
    var activeFlashIndex by remember { mutableIntStateOf(-1) }
    var isPlayingSequence by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    suspend fun playSequence() {
        isPlayingSequence = true
        userIndex = 0
        activeFlashIndex = -1
        val delayTime = if (playbackSpeed == "Fast Turbo") 300L else 550L
        delay(400L)
        for (item in sequence) {
            activeFlashIndex = item
            delay(delayTime)
            activeFlashIndex = -1
            delay(150L)
        }
        isPlayingSequence = false
    }

    fun handleColorTap(idx: Int) {
        if (isPlayingSequence || isGameOver || sequence.isEmpty() || userIndex >= sequence.size) return

        if (sequence[userIndex] == idx) {
            // Flash tapped button briefly
            activeFlashIndex = idx
            coroutineScope.launch {
                delay(180L)
                if (activeFlashIndex == idx) activeFlashIndex = -1
            }
            userIndex++
            if (userIndex == sequence.size) {
                score += sequence.size * 2
                streak++
                GamificationManager.recordGamePlayed(1)
                isPlayingSequence = true
                coroutineScope.launch {
                    delay(550L)
                    sequence.add(Random.nextInt(0, 4))
                    playSequence()
                }
            }
        } else {
            // Incorrect tap
            streak = 0
            if (maxLives != -1) {
                currentLives--
                if (currentLives <= 0) {
                    isGameOver = true
                    return
                }
            }
            // Flash red or retry sequence
            isPlayingSequence = true
            userIndex = 0
            coroutineScope.launch {
                delay(600L)
                playSequence()
            }
        }
    }

    LaunchedEffect(isStarted) {
        if (isStarted && sequence.isEmpty()) {
            sequence.add(Random.nextInt(0, 4))
            playSequence()
        }
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) isGameOver = true
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Speed Sequence",
            emoji = "🎵",
            description = "Train auditory-visual sequencing and short-term memory recall. Repeat flashing color patterns.",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            customOptionLabel = "Flasher Tempo",
            customOptions = listOf("Normal", "Fast Turbo"),
            selectedCustomOption = playbackSpeed,
            onCustomOptionChange = { playbackSpeed = it },
            accentColor = NeonAmber,
            onStartGame = {
                score = 0
                streak = 0
                sequence.clear()
                userIndex = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                elapsedSeconds = 0
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
            .background(Color(0xFF070912)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("🎵 Speed Sequence", score, difficulty, onBack, NeonAmber)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = streak,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonAmber,
                onPlayAgain = {
                    score = 0
                    streak = 0
                    sequence.clear()
                    userIndex = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                    coroutineScope.launch {
                        sequence.add(Random.nextInt(0, 4))
                        playSequence()
                    }
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonAmber
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                if (isPlayingSequence) "WATCH FLASHING SEQUENCE..." else "YOUR TURN: REPEAT SEQUENCE (${userIndex}/${sequence.size})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isPlayingSequence) NeonAmber else NeonLime
            )

            Spacer(modifier = Modifier.height(30.dp))

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    listOf(0, 1).forEach { idx ->
                        val isLit = activeFlashIndex == idx
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(if (isLit) colors[idx] else colors[idx].copy(alpha = 0.2f))
                                .border(2.dp, colors[idx], RoundedCornerShape(24.dp))
                                .clickable(enabled = !isPlayingSequence) {
                                    handleColorTap(idx)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                colorNames[idx],
                                color = if (isLit) Color.Black else colors[idx],
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    listOf(2, 3).forEach { idx ->
                        val isLit = activeFlashIndex == idx
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(if (isLit) colors[idx] else colors[idx].copy(alpha = 0.2f))
                                .border(2.dp, colors[idx], RoundedCornerShape(24.dp))
                                .clickable(enabled = !isPlayingSequence) {
                                    handleColorTap(idx)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                colorNames[idx],
                                color = if (isLit) Color.Black else colors[idx],
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================================
// GAME 5: FULL SCREEN MICRO REACTION REFLEX
// =============================================================
@Composable
fun FullMicroReactionScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }
    var reflexTask by remember { mutableStateOf("Green Light") }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    var state by remember { mutableStateOf("WAIT") } // WAIT, TAP, TOO_EARLY
    var reactionTimeMs by remember { mutableLongStateOf(0L) }
    var startTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(state, isStarted) {
        if (isStarted && state == "WAIT") {
            val waitMs = Random.nextLong(1200L, 3500L)
            delay(waitMs)
            state = "TAP"
            startTime = System.currentTimeMillis()
        }
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) isGameOver = true
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Micro-Reaction Reflex",
            emoji = "⚡",
            description = "Measure millisecond neural latency. Tap instantly the moment the red light turns green!",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            customOptionLabel = "Target Signal",
            customOptions = listOf("Green Light", "Visual Flash"),
            selectedCustomOption = reflexTask,
            onCustomOptionChange = { reflexTask = it },
            accentColor = NeonCyan,
            onStartGame = {
                score = 0
                streak = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                elapsedSeconds = 0
                state = "WAIT"
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
            .background(Color(0xFF070912)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("⚡ Reflex Latency", score, difficulty, onBack, NeonCyan)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = streak,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonCyan,
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    state = "WAIT"
                    isGameOver = false
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            val bgCol = when (state) {
                "TAP" -> NeonLime
                "TOO_EARLY" -> NeonPink
                else -> Color(0xFFC53030)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(bgCol)
                    .clickable {
                        if (state == "TAP") {
                            reactionTimeMs = System.currentTimeMillis() - startTime
                            score++
                            streak++
                            GamificationManager.recordGamePlayed(1)
                            state = "WAIT"
                        } else if (state == "WAIT") {
                            state = "TOO_EARLY"
                            streak = 0
                            if (maxLives != -1) {
                                currentLives--
                                if (currentLives <= 0) isGameOver = true
                            }
                        } else if (state == "TOO_EARLY") {
                            state = "WAIT"
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when (state) {
                            "TAP" -> "TAP NOW! ⚡"
                            "TOO_EARLY" -> "TOO EARLY! (-1 ❤️)"
                            else -> "WAIT FOR GREEN..."
                        },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                    if (reactionTimeMs > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Last: ${reactionTimeMs}ms", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }
        }
    }
}

// =============================================================
// GAME 16: FULL SCREEN CYBER GLITCH SEQUENCE (MATRIX HACK IQ)
// =============================================================
@Composable
fun FullCyberGlitchScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    var sequenceLength by remember { mutableIntStateOf(3) }
    val sequenceNodes = remember { mutableStateListOf<Int>() }
    val playerInput = remember { mutableStateListOf<Int>() }
    var isShowingSequence by remember { mutableStateOf(false) }
    var activeHighlightedCell by remember { mutableStateOf<Int?>(null) }

    fun generateSequence() {
        sequenceNodes.clear()
        playerInput.clear()
        repeat(sequenceLength) {
            sequenceNodes.add(Random.nextInt(0, 16))
        }
        isShowingSequence = true
    }

    LaunchedEffect(isShowingSequence) {
        if (isShowingSequence) {
            delay(500L)
            for (cell in sequenceNodes) {
                activeHighlightedCell = cell
                AuraSoundManager.playTapSound(context)
                delay(450L)
                activeHighlightedCell = null
                delay(200L)
            }
            isShowingSequence = false
        }
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) {
                        isGameOver = true
                        GamificationManager.recordGameMatch("g16", "Cyber Glitch Sequence", score, 92, 215)
                    }
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Cyber Glitch Sequence",
            emoji = "⚡",
            description = "Overclocked cyberpunk memory challenge. Memorize the glowing matrix pulse sequence and replay it with flawless accuracy.",
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
                elapsedSeconds = 0
                sequenceLength = if (difficulty == "Hard") 4 else 3
                isGameOver = false
                isStarted = true
                generateSequence()
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
        GameHeaderBar("⚡ Cyber Glitch Sequence", score, difficulty, onBack, NeonCyan)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = streak,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonCyan,
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    sequenceLength = if (difficulty == "Hard") 4 else 3
                    isGameOver = false
                    generateSequence()
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
                Text(
                    if (isShowingSequence) "👀 WATCH GLITCH PULSE..." else "REPLAY: ${playerInput.size}/${sequenceNodes.size}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isShowingSequence) NeonPink else NeonLime
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4x4 Cyber Node Grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(4) { r ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        repeat(4) { c ->
                            val cellIndex = r * 4 + c
                            val isPulseActive = activeHighlightedCell == cellIndex
                            val isSelectedByPlayer = playerInput.contains(cellIndex)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        when {
                                            isPulseActive -> NeonCyan
                                            isSelectedByPlayer -> NeonLime.copy(alpha = 0.4f)
                                            else -> Color(0x18FFFFFF)
                                        }
                                    )
                                    .border(
                                        1.5.dp,
                                        if (isPulseActive) Color.White else NeonCyan.copy(alpha = 0.35f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable(enabled = !isShowingSequence && !isGameOver) {
                                        AuraSoundManager.playTapSound(context)
                                        playerInput.add(cellIndex)

                                        val step = playerInput.size - 1
                                        if (playerInput[step] != sequenceNodes[step]) {
                                            // Wrong node!
                                            streak = 0
                                            if (maxLives != -1) {
                                                currentLives--
                                                if (currentLives <= 0) {
                                                    isGameOver = true
                                                    GamificationManager.recordGameMatch("g16", "Cyber Glitch Sequence", score, 85, 240)
                                                }
                                            }
                                            generateSequence()
                                        } else if (playerInput.size == sequenceNodes.size) {
                                            // Sequence Complete!
                                            score += sequenceNodes.size * 15
                                            streak++
                                            AuraSoundManager.playSuccessSound()
                                            if (sequenceLength < 8) sequenceLength++
                                            generateSequence()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isPulseActive) "⚡" else "$cellIndex",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPulseActive) Color.Black else Color(0x60FFFFFF)
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
// GAME 7: FULL SCREEN DIGIT SPAN MEMORY
// =============================================================
@Composable
fun FullDigitSpanScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }
    var spanOrder by remember { mutableStateOf("Forward") }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    var spanLength by remember { mutableIntStateOf(4) }
    var sequenceDigits by remember { mutableStateOf("") }
    var userInput by remember { mutableStateOf("") }
    var isShowingDigits by remember { mutableStateOf(true) }

    fun generateNewSpan() {
        val sb = StringBuilder()
        repeat(spanLength) { sb.append(Random.nextInt(1, 10)) }
        sequenceDigits = sb.toString()
        userInput = ""
        isShowingDigits = true
    }

    LaunchedEffect(isStarted, isShowingDigits) {
        if (isStarted && isShowingDigits) {
            delay(2200L)
            isShowingDigits = false
        }
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) isGameOver = true
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Digit Span Memory",
            emoji = "🔢",
            description = "Gold standard working memory index. Memorize the flashed number sequence and type it back accurately.",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            customOptionLabel = "Recall Order",
            customOptions = listOf("Forward", "Reverse"),
            selectedCustomOption = spanOrder,
            onCustomOptionChange = { spanOrder = it },
            accentColor = NeonViolet,
            onStartGame = {
                score = 0
                streak = 0
                spanLength = if (difficulty == "Easy") 3 else if (difficulty == "Hard") 5 else 4
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                elapsedSeconds = 0
                isGameOver = false
                isStarted = true
                generateNewSpan()
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
        GameHeaderBar("🔢 Digit Span", score, difficulty, onBack, NeonViolet)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = streak,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonViolet,
                onPlayAgain = {
                    score = 0
                    streak = 0
                    spanLength = 4
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                    generateNewSpan()
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonViolet
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                if (isShowingDigits) "MEMORIZE DIGITS! ($spanLength DIGITS)" else "ENTER DIGITS (${spanOrder.uppercase()}): $userInput",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isShowingDigits) NeonAmber else NeonViolet
            )

            Spacer(modifier = Modifier.height(16.dp))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                glowColor = NeonViolet
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(28.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isShowingDigits) sequenceDigits else (userInput.ifEmpty { "• • • •" }),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // NumPad
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
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
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x18FFFFFF))
                                    .border(1.dp, NeonViolet.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                    .clickable(enabled = !isShowingDigits) {
                                        when (key) {
                                            "C" -> userInput = ""
                                            "OK" -> {
                                                val expected = if (spanOrder == "Reverse") sequenceDigits.reversed() else sequenceDigits
                                                if (userInput == expected) {
                                                    score += spanLength * 2
                                                    streak++
                                                    GamificationManager.recordGamePlayed(1)
                                                    spanLength++
                                                    generateNewSpan()
                                                } else {
                                                    streak = 0
                                                    if (maxLives != -1) {
                                                        currentLives--
                                                        if (currentLives <= 0) isGameOver = true
                                                    }
                                                    generateNewSpan()
                                                }
                                            }
                                            else -> {
                                                if (userInput.length < spanLength) userInput += key
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(key, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

// =============================================================
// GAME 8: FULL SCREEN ANAGRAM WORD UNSCRAMBLE
// =============================================================
@Composable
fun FullWordUnscrambleScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }
    var categoryChoice by remember { mutableStateOf("Focus & Tech") }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    val words = listOf(
        "FOCUS", "AUDIO", "BRAIN", "WAVES", "GAMMA", "ALPHA", "THETA", "PULSE", "ZENITH", "CLARITY"
    )

    var currentTarget by remember { mutableStateOf("FOCUS") }
    var scrambled by remember { mutableStateOf("SCOUF") }
    var userLetters by remember { mutableStateOf("") }

    fun nextWord() {
        val word = words.random()
        currentTarget = word
        scrambled = word.toList().shuffled().joinToString("")
        userLetters = ""
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) isGameOver = true
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Word Unscramble",
            emoji = "🔤",
            description = "Cognitive verbal fluency & pattern unscrambling. Reconstruct mixed letters into mindfulness words.",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            customOptionLabel = "Vocabulary",
            customOptions = listOf("Focus & Tech", "Mind & Zen"),
            selectedCustomOption = categoryChoice,
            onCustomOptionChange = { categoryChoice = it },
            accentColor = NeonLime,
            onStartGame = {
                score = 0
                streak = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                elapsedSeconds = 0
                isGameOver = false
                isStarted = true
                nextWord()
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
        GameHeaderBar("🔤 Word Anagram", score, difficulty, onBack, NeonLime)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = streak,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonLime,
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                    nextWord()
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonLime
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                glowColor = NeonLime
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("UNSCRAMBLE THIS WORD:", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(scrambled, fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = NeonLime, letterSpacing = 4.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Your Input: ${userLetters.ifEmpty { "..." }}", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Letter Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                scrambled.forEach { char ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x20FFFFFF))
                            .border(1.dp, NeonLime, RoundedCornerShape(12.dp))
                            .clickable {
                                userLetters += char
                                if (userLetters.length == currentTarget.length) {
                                    if (userLetters == currentTarget) {
                                        score += 3
                                        streak++
                                        GamificationManager.recordGamePlayed(1)
                                        nextWord()
                                    } else {
                                        streak = 0
                                        if (maxLives != -1) {
                                            currentLives--
                                            if (currentLives <= 0) isGameOver = true
                                        }
                                        userLetters = ""
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$char", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .height(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x18FFFFFF))
                    .padding(horizontal = 16.dp)
                    .clickable { userLetters = "" },
                contentAlignment = Alignment.Center
            ) {
                Text("Clear Input", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

// =============================================================
// GAME 9: FULL SCREEN SPATIAL MATCH 2D/3D
// =============================================================
@Composable
fun FullSpatialMatchScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }
    var angleChoice by remember { mutableStateOf("90° Rotation") }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    var isMatch by remember { mutableStateOf(true) }

    fun nextSpatial() {
        isMatch = Random.nextBoolean()
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) isGameOver = true
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Spatial IQ Match",
            emoji = "📐",
            description = "Parietal lobe mental rotation exercise. Determine whether two geometric shapes are identical after rotation.",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            customOptionLabel = "Rotation Style",
            customOptions = listOf("90° Rotation", "180° Flip"),
            selectedCustomOption = angleChoice,
            onCustomOptionChange = { angleChoice = it },
            accentColor = NeonAmber,
            onStartGame = {
                score = 0
                streak = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                elapsedSeconds = 0
                isGameOver = false
                isStarted = true
                nextSpatial()
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
        GameHeaderBar("📐 Spatial Match", score, difficulty, onBack, NeonAmber)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = streak,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonAmber,
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                    nextSpatial()
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonAmber
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                glowColor = NeonAmber
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("ARE THESE TWO SHAPES IDENTICAL?", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text("🔷", fontSize = 48.sp)
                        Text(if (isMatch) "🔷" else "🔶", fontSize = 48.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeonLime.copy(alpha = 0.2f))
                        .border(1.5.dp, NeonLime, RoundedCornerShape(16.dp))
                        .clickable {
                            if (isMatch) {
                                score += 2
                                streak++
                                GamificationManager.recordGamePlayed(1)
                            } else {
                                streak = 0
                                if (maxLives != -1) {
                                    currentLives--
                                    if (currentLives <= 0) isGameOver = true
                                }
                            }
                            nextSpatial()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("MATCH ✓", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(NeonPink.copy(alpha = 0.2f))
                        .border(1.5.dp, NeonPink, RoundedCornerShape(16.dp))
                        .clickable {
                            if (!isMatch) {
                                score += 2
                                streak++
                                GamificationManager.recordGamePlayed(1)
                            } else {
                                streak = 0
                                if (maxLives != -1) {
                                    currentLives--
                                    if (currentLives <= 0) isGameOver = true
                                }
                            }
                            nextSpatial()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("DIFFERENT ✗", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NeonPink)
                }
            }
        }
    }
}

// =============================================================
// GAME 10: FULL SCREEN DUAL N-BACK WORKING MEMORY
// =============================================================
@Composable
fun FullDualNBackScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }
    var nLevelChoice by remember { mutableStateOf("1-Back") }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    val n = if (nLevelChoice == "2-Back") 2 else 1
    val history = remember { mutableStateListOf<Int>() }
    var currentGridPos by remember { mutableIntStateOf(4) } // 0..8
    var matchClaimedThisTurn by remember { mutableStateOf(false) }

    LaunchedEffect(isStarted) {
        if (isStarted) {
            while (!isGameOver) {
                delay(2000L)
                if (history.isNotEmpty() && !matchClaimedThisTurn && history.size >= n + 1) {
                    val nAgo = history[history.size - 1 - n]
                    val curr = history.last()
                    // missed match logic could apply
                }
                matchClaimedThisTurn = false
                val nextPos = Random.nextInt(0, 9)
                currentGridPos = nextPos
                history.add(nextPos)
            }
        }
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) isGameOver = true
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Dual N-Back Training",
            emoji = "🧠",
            description = "Clinically validated fluid intelligence (IQ) training. Detect if current square matches position from N steps ago.",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            customOptionLabel = "N-Back Depth",
            customOptions = listOf("1-Back", "2-Back"),
            selectedCustomOption = nLevelChoice,
            onCustomOptionChange = { nLevelChoice = it },
            accentColor = NeonCyan,
            onStartGame = {
                score = 0
                streak = 0
                history.clear()
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                elapsedSeconds = 0
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
            .background(Color(0xFF070912)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("🧠 Dual N-Back ($n-Back)", score, difficulty, onBack, NeonCyan)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = streak,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonCyan,
                onPlayAgain = {
                    score = 0
                    streak = 0
                    history.clear()
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "DOES THIS TILE MATCH $n STEP(S) AGO?",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3x3 Grid
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (r in 0..2) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        for (c in 0..2) {
                            val pos = r * 3 + c
                            val isActive = currentGridPos == pos
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isActive) NeonCyan else Color(0x18FFFFFF))
                                    .border(1.5.dp, if (isActive) NeonCyan else Color(0x30FFFFFF), RoundedCornerShape(16.dp))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(horizontal = 32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(NeonLime.copy(alpha = 0.25f))
                    .border(1.5.dp, NeonLime, RoundedCornerShape(16.dp))
                    .clickable(enabled = !matchClaimedThisTurn) {
                        matchClaimedThisTurn = true
                        if (history.size > n) {
                            val nAgo = history[history.size - 1 - n]
                            val curr = currentGridPos
                            if (nAgo == curr) {
                                score += 5
                                streak++
                                GamificationManager.recordGamePlayed(1)
                            } else {
                                streak = 0
                                if (maxLives != -1) {
                                    currentLives--
                                    if (currentLives <= 0) isGameOver = true
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("MATCH POSITION ($n-BACK) ⚡", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = NeonLime)
            }
        }
    }
}

// -------------------------------------------------------------
// GAME 11: PATTERN FLANKER SELECTIVE ATTENTION TEST
// -------------------------------------------------------------
@Composable
fun FullFlankerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableIntStateOf(45) }
    var maxLives by remember { mutableIntStateOf(3) }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var isGameOver by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(45) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }

    // Sequence generator: 5 arrows (e.g., <<><< or >>>>>)
    var currentArrows by remember { mutableStateOf(">>>>>") }
    var targetDirectionIsRight by remember { mutableStateOf(true) }
    var feedbackFlash by remember { mutableStateOf<Boolean?>(null) } // true=green, false=red

    fun nextStimulus() {
        val isTargetRight = Random.nextBoolean()
        targetDirectionIsRight = isTargetRight
        val center = if (isTargetRight) ">" else "<"

        val incongruentChance = when (difficulty) {
            "Easy" -> 0.25f
            "Medium" -> 0.50f
            "Hard" -> 0.75f
            else -> 0.85f
        }
        val isCongruent = Random.nextFloat() > incongruentChance
        val flanker = if (isCongruent) center else (if (isTargetRight) "<" else ">")

        currentArrows = "$flanker$flanker$center$flanker$flanker"
        feedbackFlash = null
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            while (true) {
                delay(1000L)
                elapsedSeconds++
                if (timeLimit != -1) {
                    if (secondsLeft > 0) {
                        secondsLeft--
                        if (secondsLeft == 0) isGameOver = true
                    }
                }
            }
        }
    }

    fun handleResponse(pressedRight: Boolean) {
        if (isGameOver) return
        val isCorrect = pressedRight == targetDirectionIsRight
        feedbackFlash = isCorrect

        if (isCorrect) {
            score += 10 + (streak * 2)
            streak++
            GamificationManager.recordGamePlayed(1)
        } else {
            streak = 0
            if (maxLives != -1) {
                currentLives--
                if (currentLives <= 0) isGameOver = true
            }
        }
        nextStimulus()
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Pattern Flanker Attention",
            emoji = "🏹",
            description = "Train selective attention and inhibitory control. Focus only on the central arrow and ignore misleading flankers under reaction pressure.",
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
                elapsedSeconds = 0
                isGameOver = false
                isStarted = true
                nextStimulus()
            },
            onBack = onBack
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFF070912))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("🏹 Flanker Attention", score, difficulty, onBack, NeonCyan)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = streak,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonCyan,
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                    nextStimulus()
                },
                onReconfigure = {
                    isStarted = false
                },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))

            // Stats Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Score: $score", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "INDICATE THE MIDDLE ARROW'S DIRECTION:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main Arrow Display Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        when (feedbackFlash) {
                            true -> NeonLime.copy(alpha = 0.2f)
                            false -> Color(0x35FF4D4D)
                            else -> Color(0x18FFFFFF)
                        }
                    )
                    .border(
                        2.dp,
                        when (feedbackFlash) {
                            true -> NeonLime
                            false -> Color(0xFFFF4D4D)
                            else -> NeonCyan.copy(alpha = 0.6f)
                        },
                        RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    currentArrows.forEachIndexed { index, char ->
                        val isCenter = index == 2
                        Text(
                            text = char.toString(),
                            fontSize = if (isCenter) 48.sp else 36.sp,
                            fontWeight = if (isCenter) FontWeight.Black else FontWeight.Light,
                            color = if (isCenter) NeonCyan else Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Left / Right Response Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(NeonCyan.copy(alpha = 0.2f))
                        .border(2.dp, NeonCyan, RoundedCornerShape(20.dp))
                        .clickable { handleResponse(false) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("⬅️ LEFT", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(NeonCyan.copy(alpha = 0.2f))
                        .border(2.dp, NeonCyan, RoundedCornerShape(20.dp))
                        .clickable { handleResponse(true) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("RIGHT ➡️", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Streak Multiplier: ${streak}x 🔥",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = NeonAmber
            )
        }
    }
}

// -------------------------------------------------------------
// GAME 12: CHIMP MEMORY MATRIX (KYOTO EXPERIMENT)
// -------------------------------------------------------------
@Composable
fun FullChimpMemoryScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableIntStateOf(60) }
    var maxLives by remember { mutableIntStateOf(3) }

    val gridCols = 4
    val gridRows = 4
    val totalCells = gridCols * gridRows

    val maxNumberForDifficulty = when (difficulty) {
        "Easy" -> 4
        "Medium" -> 6
        "Hard" -> 8
        else -> 9
    }

    var score by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var isGameOver by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }

    var cellNumbers by remember { mutableStateOf(mapOf<Int, Int>()) }
    var nextExpectedNumber by remember { mutableIntStateOf(1) }
    var tilesHidden by remember { mutableStateOf(false) }
    val solvedCells = remember { mutableStateListOf<Int>() }

    fun startNewRound() {
        val numbersCount = maxNumberForDifficulty
        val chosenPositions = (0 until totalCells).shuffled().take(numbersCount)
        val newMap = mutableMapOf<Int, Int>()
        chosenPositions.forEachIndexed { index, pos ->
            newMap[pos] = index + 1
        }
        cellNumbers = newMap
        nextExpectedNumber = 1
        tilesHidden = false
        solvedCells.clear()
    }

    // Auto-hide delay
    LaunchedEffect(cellNumbers, isStarted) {
        if (isStarted && cellNumbers.isNotEmpty()) {
            val hideDelay = when (difficulty) {
                "Easy" -> 2200L
                "Medium" -> 1600L
                "Hard" -> 1100L
                else -> 900L
            }
            delay(hideDelay)
            tilesHidden = true
        }
    }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            while (true) {
                delay(1000L)
                elapsedSeconds++
                if (timeLimit != -1) {
                    if (secondsLeft > 0) {
                        secondsLeft--
                        if (secondsLeft == 0) isGameOver = true
                    }
                }
            }
        }
    }

    fun onCellClicked(cellIndex: Int) {
        if (isGameOver) return
        val targetNumber = cellNumbers[cellIndex] ?: return
        if (solvedCells.contains(cellIndex)) return

        tilesHidden = true

        if (targetNumber == nextExpectedNumber) {
            solvedCells.add(cellIndex)
            nextExpectedNumber++
            score += 15

            if (nextExpectedNumber > cellNumbers.size) {
                score += 50
                GamificationManager.recordGamePlayed(2)
                startNewRound()
            }
        } else {
            if (maxLives != -1) {
                currentLives--
                if (currentLives <= 0) isGameOver = true
            }
            startNewRound()
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Chimp Memory Matrix",
            emoji = "🐵",
            description = "Train instantaneous spatial working memory inspired by the Kyoto Primate Research Institute. Memorize number tile positions before they turn blank, then tap in order 1..N!",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            accentColor = NeonLime,
            onStartGame = {
                score = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                elapsedSeconds = 0
                isGameOver = false
                isStarted = true
                startNewRound()
            },
            onBack = onBack
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color(0xFF070912))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("🐵 Chimp Memory Matrix", score, difficulty, onBack, NeonLime)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = 0,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonLime,
                onPlayAgain = {
                    score = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                    startNewRound()
                },
                onReconfigure = {
                    isStarted = false
                },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))

            // Stats Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Score: $score", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonLime
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (tilesHidden) "TAP IN ORDER: 1, 2, 3..." else "MEMORIZE NUMBER POSITIONS!",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (tilesHidden) NeonCyan else NeonLime
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4x4 Chimp Matrix Grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (row in 0 until gridRows) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (col in 0 until gridCols) {
                            val cellIndex = row * gridCols + col
                            val number = cellNumbers[cellIndex]
                            val isSolved = solvedCells.contains(cellIndex)

                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        when {
                                            isSolved -> NeonLime.copy(alpha = 0.2f)
                                            number != null && !tilesHidden -> NeonLime.copy(alpha = 0.35f)
                                            number != null && tilesHidden -> Color(0x35FFFFFF)
                                            else -> Color(0x10FFFFFF)
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        when {
                                            isSolved -> NeonLime
                                            number != null -> NeonLime.copy(alpha = 0.6f)
                                            else -> Color(0x20FFFFFF)
                                        },
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable(enabled = number != null && !isSolved) {
                                        onCellClicked(cellIndex)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (number != null) {
                                    if (!tilesHidden && !isSolved) {
                                        Text(
                                            text = number.toString(),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
                                        )
                                    } else if (isSolved) {
                                        Text(
                                            text = "✓",
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NeonLime
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Next Target: #$nextExpectedNumber",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NeonCyan
            )
        }
    }
}

// =============================================================
// GAME 13: FULL SCREEN RHYTHM BEAT TAP (TEMPO & TIMING IQ)
// =============================================================
@Composable
fun FullRhythmBeatScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(45) }
    var maxLives by remember { mutableIntStateOf(3) }
    var targetBpm by remember { mutableStateOf("90 BPM") }

    var score by remember { mutableIntStateOf(0) }
    var combo by remember { mutableIntStateOf(0) }
    var bestCombo by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(45) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var feedbackText by remember { mutableStateOf("TAP ON BEAT!") }
    var feedbackColor by remember { mutableStateOf(Color.White) }

    val bpmValue = when (targetBpm) {
        "60 BPM" -> 60
        "120 BPM" -> 120
        else -> 90
    }
    val beatIntervalMs = (60000L / bpmValue)

    // Animated beat pulse expanding from 0.2f to 1.0f
    val pulseAnim = remember { androidx.compose.animation.core.Animatable(0.2f) }

    LaunchedEffect(isStarted, isGameOver) {
        if (isStarted && !isGameOver) {
            while (true) {
                pulseAnim.snapTo(0.2f)
                pulseAnim.animateTo(
                    targetValue = 1.0f,
                    animationSpec = androidx.compose.animation.core.tween(
                        durationMillis = beatIntervalMs.toInt(),
                        easing = androidx.compose.animation.core.LinearEasing
                    )
                )
            }
        }
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) isGameOver = true
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Rhythm Beat Tap",
            emoji = "🥁",
            description = "Train cognitive rhythm, acoustic timing prediction, and motor latency. Tap precisely when the expanding wave hits the green target perimeter ring.",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            customOptionLabel = "Tempo (BPM)",
            customOptions = listOf("60 BPM", "90 BPM", "120 BPM"),
            selectedCustomOption = targetBpm,
            onCustomOptionChange = { targetBpm = it },
            accentColor = NeonPink,
            onStartGame = {
                score = 0
                combo = 0
                bestCombo = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                elapsedSeconds = 0
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
            .background(Color(0xFF070912)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameHeaderBar("🥁 Rhythm Beat", score, difficulty, onBack, NeonPink)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = bestCombo,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonPink,
                onPlayAgain = {
                    score = 0
                    combo = 0
                    bestCombo = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            // HUD
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonPink
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Feedback Display
            Text(
                text = feedbackText,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = feedbackColor
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Combo: ${combo}x (Max: ${bestCombo}x)",
                fontSize = 13.sp,
                color = NeonCyan,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            // RHYTHM TARGET ARENA
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
                    .background(Color(0x10FFFFFF))
                    .border(2.dp, NeonPink, CircleShape)
                    .clickable {
                        val currentFraction = pulseAnim.value
                        // Target hit zone is around 0.85f .. 1.0f or right after reset <= 0.15f
                        val isHit = currentFraction >= 0.82f || currentFraction <= 0.15f
                        val isPerfect = currentFraction in 0.90f..0.99f

                        if (isPerfect) {
                            score += 15
                            combo++
                            if (combo > bestCombo) bestCombo = combo
                            feedbackText = "PERFECT! 🔥 (+15)"
                            feedbackColor = NeonLime
                            GamificationManager.recordGamePlayed(2)
                        } else if (isHit) {
                            score += 8
                            combo++
                            if (combo > bestCombo) bestCombo = combo
                            feedbackText = "GREAT! ⚡ (+8)"
                            feedbackColor = NeonCyan
                            GamificationManager.recordGamePlayed(1)
                        } else {
                            combo = 0
                            feedbackText = if (currentFraction < 0.82f) "TOO EARLY! ❌" else "MISSED! ❌"
                            feedbackColor = Color(0xFFFF4D4D)
                            if (maxLives != -1) {
                                currentLives--
                                if (currentLives <= 0) isGameOver = true
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                // Expanding beat pulse wave
                Box(
                    modifier = Modifier
                        .size(260.dp * pulseAnim.value)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(NeonPink.copy(alpha = 0.45f), Color.Transparent)
                            )
                        )
                        .border(2.5.dp, NeonPink.copy(alpha = 0.8f), CircleShape)
                )

                // Static Center Trigger Bullseye
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(NeonPink, NeonViolet)))
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("TAP", fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Tap the bullseye exactly when the wave hits the ring perimeter!",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 24.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// =============================================================
// GAME 14: FULL SCREEN SPATIAL PATTERN SUDOKU (4x4 LOGIC MATRIX)
// =============================================================
@Composable
fun FullSpatialPatternSudokuScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(90) }
    var maxLives by remember { mutableIntStateOf(3) }

    var score by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(90) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    // 4x4 Sudoku Solution (valid):
    // [1, 2, 3, 4]
    // [3, 4, 1, 2]
    // [2, 1, 4, 3]
    // [4, 3, 2, 1]
    val solution = remember {
        listOf(
            1, 2, 3, 4,
            3, 4, 1, 2,
            2, 1, 4, 3,
            4, 3, 2, 1
        )
    }

    // Initial clues depending on difficulty
    val initialClueIndices = remember(difficulty) {
        when (difficulty) {
            "Easy" -> setOf(0, 1, 3, 4, 7, 8, 11, 12, 14, 15) // 10 clues
            "Hard" -> setOf(0, 3, 5, 10, 12, 15) // 6 clues
            "Master" -> setOf(0, 5, 10, 15) // 4 clues
            else -> setOf(0, 2, 5, 7, 8, 10, 13, 15) // 8 clues
        }
    }

    val gridState = remember { mutableStateListOf<Int?>().apply { repeat(16) { add(null) } } }
    var selectedCellIdx by remember { mutableStateOf<Int?>(null) }

    fun resetGrid() {
        gridState.clear()
        for (i in 0 until 16) {
            if (initialClueIndices.contains(i)) {
                gridState.add(solution[i])
            } else {
                gridState.add(null)
            }
        }
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) isGameOver = true
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Spatial Pattern Sudoku",
            emoji = "🔢",
            description = "Train deductive spatial logic and working memory. Fill the 4x4 matrix so that numbers 1 to 4 appear exactly once in each row, column, and 2x2 box.",
            difficulty = difficulty,
            onDifficultyChange = { difficulty = it },
            timeLimit = timeLimit,
            onTimeLimitChange = { timeLimit = it },
            lives = maxLives,
            onLivesChange = { maxLives = it },
            accentColor = NeonCyan,
            onStartGame = {
                score = 0
                currentLives = if (maxLives == -1) 999 else maxLives
                secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                elapsedSeconds = 0
                isGameOver = false
                resetGrid()
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
        GameHeaderBar("🔢 4x4 Sudoku Matrix", score, difficulty, onBack, NeonCyan)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = 1,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonCyan,
                onPlayAgain = {
                    score = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                    resetGrid()
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Select empty cell, then tap 1, 2, 3, or 4! 👇",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 4x4 Grid Board
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (row in 0 until 4) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (col in 0 until 4) {
                            val idx = row * 4 + col
                            val value = gridState[idx]
                            val isClue = initialClueIndices.contains(idx)
                            val isSelected = selectedCellIdx == idx

                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        when {
                                            isSelected -> NeonCyan.copy(alpha = 0.35f)
                                            isClue -> Color(0x28FFFFFF)
                                            value != null -> NeonLime.copy(alpha = 0.2f)
                                            else -> Color(0x12FFFFFF)
                                        }
                                    )
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = when {
                                            isSelected -> NeonCyan
                                            isClue -> Color(0x40FFFFFF)
                                            value != null -> NeonLime
                                            else -> Color(0x20FFFFFF)
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable(enabled = !isClue) {
                                        selectedCellIdx = idx
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = value?.toString() ?: "",
                                    fontSize = 24.sp,
                                    fontWeight = if (isClue) FontWeight.Black else FontWeight.Bold,
                                    color = if (isClue) Color.White else NeonLime
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Number Input Selector (1..4)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(1, 2, 3, 4).forEach { num ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.horizontalGradient(listOf(NeonCyan.copy(alpha = 0.3f), NeonViolet.copy(alpha = 0.3f))))
                            .border(1.5.dp, NeonCyan, RoundedCornerShape(14.dp))
                            .clickable(enabled = selectedCellIdx != null) {
                                selectedCellIdx?.let { cell ->
                                    if (solution[cell] == num) {
                                        gridState[cell] = num
                                        score += 10
                                        GamificationManager.recordGamePlayed(1)
                                        // Check if whole puzzle is solved
                                        if (gridState.indices.all { gridState[it] == solution[it] }) {
                                            score += 50
                                            GamificationManager.recordGamePlayed(5)
                                            isGameOver = true
                                        }
                                    } else {
                                        if (maxLives != -1) {
                                            currentLives--
                                            if (currentLives <= 0) isGameOver = true
                                        }
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$num", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
        }
    }
}

// =============================================================
// GAME 15: FULL SCREEN COGNITIVE FLEX DUEL (DYNAMIC TASK SWITCHING)
// =============================================================
@Composable
fun FullCognitiveFlexDuelScreen(onBack: () -> Unit) {
    BackHandler { onBack() }

    var isStarted by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf("Medium") }
    var timeLimit by remember { mutableStateOf(45) }
    var maxLives by remember { mutableIntStateOf(3) }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var currentLives by remember { mutableIntStateOf(3) }
    var secondsLeft by remember { mutableIntStateOf(45) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }

    // Active Rule can be: "MATCH COLOR" or "MATCH SHAPE"
    var currentRule by remember { mutableStateOf("MATCH COLOR") }
    var roundCount by remember { mutableIntStateOf(0) }

    // Shapes: Triangle (▲), Square (■), Circle (●), Diamond (◆)
    val shapes = listOf("▲", "■", "●", "◆")
    val colors = listOf(NeonCyan, NeonPink, NeonLime, NeonAmber)

    var targetShape by remember { mutableStateOf("▲") }
    var targetColor by remember { mutableStateOf(NeonCyan) }

    val options = remember(roundCount) {
        listOf(
            Pair("▲", NeonCyan),
            Pair("■", NeonPink),
            Pair("●", NeonLime),
            Pair("◆", NeonAmber)
        ).shuffled()
    }

    fun nextRound() {
        roundCount++
        // Dynamic rule switching every 3 rounds!
        currentRule = if (roundCount % 3 == 0) "MATCH SHAPE" else "MATCH COLOR"
        targetShape = shapes.random()
        targetColor = colors.random()
    }

    LaunchedEffect(isStarted, isGameOver, secondsLeft) {
        if (isStarted && !isGameOver) {
            delay(1000L)
            elapsedSeconds++
            if (timeLimit != -1) {
                if (secondsLeft > 0) {
                    secondsLeft--
                    if (secondsLeft == 0) isGameOver = true
                }
            }
        }
    }

    if (!isStarted) {
        GamePreStartScreen(
            title = "Cognitive Flex Duel",
            emoji = "🔄",
            description = "Train cognitive flexibility and executive shifting. The rule unpredictably changes between matching the SHAPE and matching the COLOR!",
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
                elapsedSeconds = 0
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
        GameHeaderBar("🔄 Cognitive Flex", score, difficulty, onBack, NeonLime)

        if (isGameOver) {
            GameOverResultsCard(
                score = score,
                streak = streak,
                elapsedSeconds = elapsedSeconds,
                difficulty = difficulty,
                accentColor = NeonLime,
                onPlayAgain = {
                    score = 0
                    streak = 0
                    currentLives = if (maxLives == -1) 999 else maxLives
                    secondsLeft = if (timeLimit == -1) 9999 else timeLimit
                    elapsedSeconds = 0
                    isGameOver = false
                    nextRound()
                },
                onReconfigure = { isStarted = false },
                onExit = onBack
            )
        } else {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (timeLimit == -1) "⏱️ ${elapsedSeconds}s" else "⏳ ${secondsLeft}s",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonLime
                )
                Row {
                    if (maxLives == -1) {
                        Text("❤️ ∞ Invincible", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    } else {
                        repeat(maxLives) { idx ->
                            Icon(
                                Icons.Default.Favorite,
                                "Life",
                                tint = if (idx < currentLives) NeonPink else Color(0x30FFFFFF),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Prominent Dynamic Rule Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (currentRule == "MATCH COLOR") NeonCyan.copy(alpha = 0.25f) else NeonPink.copy(alpha = 0.25f))
                    .border(1.5.dp, if (currentRule == "MATCH COLOR") NeonCyan else NeonPink, RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "ACTIVE RULE: $currentRule! ⚡",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (currentRule == "MATCH COLOR") NeonCyan else NeonPink
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Target Stimulus
            GlassCard(
                modifier = Modifier
                    .size(160.dp),
                glowColor = targetColor,
                glowRadius = 14.dp
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = targetShape,
                        fontSize = 72.sp,
                        color = targetColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 4 Choices
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                options.forEach { (shape, col) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x18FFFFFF))
                            .border(1.5.dp, col, RoundedCornerShape(14.dp))
                            .clickable {
                                val isCorrect = if (currentRule == "MATCH COLOR") {
                                    col == targetColor
                                } else {
                                    shape == targetShape
                                }

                                if (isCorrect) {
                                    score += 10
                                    streak++
                                    GamificationManager.recordGamePlayed(1)
                                    nextRound()
                                } else {
                                    streak = 0
                                    if (maxLives != -1) {
                                        currentLives--
                                        if (currentLives <= 0) isGameOver = true
                                    }
                                    nextRound()
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(shape, fontSize = 28.sp, color = col)
                    }
                }
            }
        }
    }
}

