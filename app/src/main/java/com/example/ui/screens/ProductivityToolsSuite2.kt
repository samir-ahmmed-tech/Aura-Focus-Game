package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

@Composable
fun ToolTopNavHeader(
    title: String,
    subtitle: String,
    accentColor: Color,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = accentColor
            )
        }
    }
}

// =============================================================================
// TOOL 21: 5-4-3-2-1 SENSORY GROUNDING (MINDFULNESS ANXIETY DISSOLVER)
// =============================================================================
@Composable
fun FullSensoryGrounding54321Screen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var currentStep by remember { mutableIntStateOf(5) } // 5, 4, 3, 2, 1

    val stepPrompts = mapOf(
        5 to Pair("👀 5 Things You Can SEE", "Look around your space right now. Notice 5 distinct colors, shadows, or textures."),
        4 to Pair("✋ 4 Things You Can FEEL", "Feel your feet on the ground, the texture of your clothes, or the screen in your hands."),
        3 to Pair("👂 3 Things You Can HEAR", "Listen to subtle distant sounds: ambient wind, electronic hums, or your breathing."),
        2 to Pair("👃 2 Things You Can SMELL", "Take a deep breath. Notice the scent of clean air, coffee, or your environment."),
        1 to Pair("👅 1 Thing You Can TASTE", "Notice the current sensation or taste in your mouth. Anchor your presence.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0C191E), Color(0xFF03070A))))
    ) {
        ToolTopNavHeader("5-4-3-2-1 Sensory Grounding", "Anxiety Dissolver & Present Moment Anchor", NeonCyan, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val prompt = stepPrompts[currentStep] ?: Pair("", "")

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonCyan,
                glowRadius = 14.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(prompt.first, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(prompt.second, fontSize = 15.sp, color = Color.White, textAlign = TextAlign.Center, lineHeight = 22.sp)
                }
            }

            Text("Step ${6 - currentStep} of 5", color = Color.LightGray, fontSize = 14.sp)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (currentStep > 1) {
                    GlassButton(
                        text = "Next Sensory Anchor ➔",
                        onClick = {
                            AuraSoundManager.playTapSound(context)
                            currentStep--
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    GlassButton(
                        text = "Complete & Ground Mind 🧘",
                        onClick = {
                            AuraSoundManager.playSuccessSound()
                            GamificationManager.addXp(40, "Sensory Grounding")
                            Toast.makeText(context, "Mind Grounded & Centered! +40 XP", Toast.LENGTH_SHORT).show()
                            onBack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// =============================================================================
// TOOL 22: POMODORO INTERVAL ARCHITECT (CUSTOM FLOW TIMERS)
// =============================================================================
@Composable
fun FullPomodoroIntervalArchitectScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var workMinutes by remember { mutableIntStateOf(25) }
    var breakMinutes by remember { mutableIntStateOf(5) }
    var isRunning by remember { mutableStateOf(false) }
    var isBreakPhase by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(25 * 60) }

    LaunchedEffect(isRunning, secondsLeft) {
        if (isRunning && secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        } else if (isRunning && secondsLeft == 0) {
            AuraSoundManager.playSuccessSound()
            if (!isBreakPhase) {
                isBreakPhase = true
                secondsLeft = breakMinutes * 60
                GamificationManager.recordFocusSession(workMinutes)
                Toast.makeText(context, "Work round done! Short break begins.", Toast.LENGTH_SHORT).show()
            } else {
                isBreakPhase = false
                secondsLeft = workMinutes * 60
                Toast.makeText(context, "Break over! Ready for next sprint?", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A1209), Color(0xFF080502))))
    ) {
        ToolTopNavHeader("Pomodoro Flow Architect", "Customizable Sprint & Rest Intervals", NeonAmber, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                GlassChip(
                    label = if (isBreakPhase) "☕ RECOVERY BREAK" else "⚡ DEEP WORK SPRINT",
                    color = if (isBreakPhase) NeonLime else NeonAmber,
                    selected = true
                )
                Spacer(modifier = Modifier.height(24.dp))
                val m = secondsLeft / 60
                val s = secondsLeft % 60
                Text(String.format("%02d:%02d", m, s), fontSize = 68.sp, fontWeight = FontWeight.Black, color = Color.White)
            }

            if (!isRunning) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Work Duration: $workMinutes mins", color = Color.White, fontWeight = FontWeight.Bold)
                    Slider(
                        value = workMinutes.toFloat(),
                        onValueChange = {
                            workMinutes = it.toInt()
                            secondsLeft = workMinutes * 60
                        },
                        valueRange = 10f..90f,
                        steps = 7,
                        colors = SliderDefaults.colors(thumbColor = NeonAmber, activeTrackColor = NeonAmber)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Break Duration: $breakMinutes mins", color = Color.White, fontWeight = FontWeight.Bold)
                    Slider(
                        value = breakMinutes.toFloat(),
                        onValueChange = { breakMinutes = it.toInt() },
                        valueRange = 3f..25f,
                        steps = 3,
                        colors = SliderDefaults.colors(thumbColor = NeonLime, activeTrackColor = NeonLime)
                    )
                }
            }

            GlassButton(
                text = if (isRunning) "PAUSE SPRINT ⏸️" else "START INTERVAL ⏱️",
                onClick = {
                    AuraSoundManager.playTapSound(context)
                    isRunning = !isRunning
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// =============================================================================
// TOOL 23: CIRCADIAN ENERGY AUDIT (BIOLOGICAL PEAK TRACKER)
// =============================================================================
@Composable
fun FullCircadianEnergyAuditScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var sleepHours by remember { mutableFloatStateOf(7.5f) }
    var morningEnergy by remember { mutableIntStateOf(8) }
    var afternoonEnergy by remember { mutableIntStateOf(6) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F1A1B), Color(0xFF030708))))
    ) {
        ToolTopNavHeader("Circadian Energy Audit", "Bio-Rhythm Peaks & Alertness Log", NeonCyan, onBack)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonCyan) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("🌙 Sleep Duration: ${String.format("%.1f", sleepHours)} Hours", fontWeight = FontWeight.Bold, color = Color.White)
                        Slider(
                            value = sleepHours,
                            onValueChange = { sleepHours = it },
                            valueRange = 4f..11f,
                            colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                        )
                    }
                }
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonAmber) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("☀️ Morning Cortisol Peak: $morningEnergy / 10", fontWeight = FontWeight.Bold, color = Color.White)
                        Slider(
                            value = morningEnergy.toFloat(),
                            onValueChange = { morningEnergy = it.toInt() },
                            valueRange = 1f..10f,
                            colors = SliderDefaults.colors(thumbColor = NeonAmber, activeTrackColor = NeonAmber)
                        )
                    }
                }
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonLime) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("🌆 Afternoon Recovery Alertness: $afternoonEnergy / 10", fontWeight = FontWeight.Bold, color = Color.White)
                        Slider(
                            value = afternoonEnergy.toFloat(),
                            onValueChange = { afternoonEnergy = it.toInt() },
                            valueRange = 1f..10f,
                            colors = SliderDefaults.colors(thumbColor = NeonLime, activeTrackColor = NeonLime)
                        )
                    }
                }
            }

            item {
                GlassButton(
                    text = "Save Circadian Bio-Log 💾",
                    onClick = {
                        AuraSoundManager.playSuccessSound()
                        GamificationManager.addXp(35, "Circadian Audit")
                        Toast.makeText(context, "Circadian bio-curve saved! +35 XP", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// =============================================================================
// TOOL 24: DEEP WORK RATIO TRACKER (DEEP VS SHALLOW AUDITOR)
// =============================================================================
@Composable
fun FullDeepWorkRatioTrackerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var deepHours by remember { mutableFloatStateOf(3.5f) }
    var shallowHours by remember { mutableFloatStateOf(2.0f) }

    val total = deepHours + shallowHours
    val deepPercent = if (total > 0) ((deepHours / total) * 100).toInt() else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF140C1A), Color(0xFF050308))))
    ) {
        ToolTopNavHeader("Deep Work Ratio Tracker", "Cal Newport Deep vs Shallow Hours", NeonPink, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonPink
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Deep Flow Ratio", color = Color.LightGray, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("$deepPercent%", fontSize = 54.sp, fontWeight = FontWeight.Black, color = if (deepPercent >= 60) NeonLime else NeonAmber)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(if (deepPercent >= 60) "🌟 Elite Deep Work Habit" else "⚡ Aim for 60%+ Deep Focus", fontSize = 13.sp, color = Color.White)
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Deep Work: ${String.format("%.1f", deepHours)} hrs", color = Color.White, fontWeight = FontWeight.Bold)
                Slider(
                    value = deepHours,
                    onValueChange = { deepHours = it },
                    valueRange = 0f..8f,
                    colors = SliderDefaults.colors(thumbColor = NeonLime, activeTrackColor = NeonLime)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Shallow Work (Emails/Admin): ${String.format("%.1f", shallowHours)} hrs", color = Color.White, fontWeight = FontWeight.Bold)
                Slider(
                    value = shallowHours,
                    onValueChange = { shallowHours = it },
                    valueRange = 0f..8f,
                    colors = SliderDefaults.colors(thumbColor = NeonPink, activeTrackColor = NeonPink)
                )
            }

            GlassButton(
                text = "Log Daily Ratio 📊",
                onClick = {
                    AuraSoundManager.playSuccessSound()
                    GamificationManager.addXp(40, "Deep Work Log")
                    Toast.makeText(context, "Deep work ratio recorded! +40 XP", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// =============================================================================
// TOOL 25: GRATITUDE PEBBLE JAR (TACTILE ACCUMULATOR)
// =============================================================================
@Composable
fun FullGratitudePebbleJarScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var pebbleCount by remember { mutableIntStateOf(3) }
    var newPebbleText by remember { mutableStateOf("") }
    val pebbles = remember { mutableStateListOf("Morning sunshine & clean air", "Family health and resilience", "Deep coding progress today") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF141A10), Color(0xFF040803))))
    ) {
        ToolTopNavHeader("Gratitude Pebble Jar", "Daily 3-Pebble Mental Fortitude Accumulator", NeonLime, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonLime
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Pebbles in Sanctuary Jar", fontSize = 12.sp, color = Color.LightGray)
                        Text("$pebbleCount Dropped", fontSize = 28.sp, fontWeight = FontWeight.Black, color = NeonLime)
                    }
                    Text("🏺", fontSize = 42.sp)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(pebbles) { text ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0x20FFFFFF))
                            .border(1.dp, NeonLime, RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Text("🪨 $text", color = Color.White, fontSize = 14.sp)
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = newPebbleText,
                    onValueChange = { newPebbleText = it },
                    placeholder = { Text("What are you grateful for today?", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                GlassButton(
                    text = "Drop 🪨",
                    onClick = {
                        if (newPebbleText.isNotBlank()) {
                            pebbles.add(newPebbleText.trim())
                            pebbleCount++
                            newPebbleText = ""
                            AuraSoundManager.playSuccessSound()
                            GamificationManager.addXp(30, "Gratitude Pebble")
                        }
                    }
                )
            }
        }
    }
}

// =============================================================================
// TOOL 26: VISION MANIFESTO BOARD (CORE VALUES & GOAL ANCHORS)
// =============================================================================
@Composable
fun FullVisionManifestoBoardScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var manifestoText by remember {
        mutableStateOf(
            "I cultivate deep clarity, uninterrupted focus, and ruthless consistency.\n" +
            "External chaos cannot shake my internal discipline.\n" +
            "Daily micro-wins build my compounding sovereign future."
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF14131C), Color(0xFF050408))))
    ) {
        ToolTopNavHeader("Vision Manifesto Board", "Personal Core Values & Daily Focus Anchors", NeonViolet, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                glowColor = NeonViolet
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("MY SOVEREIGN MANIFESTO", fontWeight = FontWeight.Bold, color = NeonViolet, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = manifestoText,
                        onValueChange = { manifestoText = it },
                        modifier = Modifier.fillMaxSize(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonViolet
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            GlassButton(
                text = "Save Manifesto Anchors 📜",
                onClick = {
                    AuraSoundManager.playSuccessSound()
                    GamificationManager.addXp(35, "Manifesto Update")
                    Toast.makeText(context, "Manifesto saved to local database! +35 XP", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// =============================================================================
// TOOL 27: MENTAL CLARITY BRAIN DUMP (WORKING MEMORY UNLOADER)
// =============================================================================
@Composable
fun FullMentalClarityBrainDumpScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val thoughts = remember { mutableStateListOf("Refactor database indexing", "Review audio binaural frequencies", "Book eye checkup appointment") }
    var inputThought by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F171A), Color(0xFF030708))))
    ) {
        ToolTopNavHeader("Mental Clarity Brain Dump", "Unclutter Working Memory Fast", NeonCyan, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(thoughts) { thought ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x18FFFFFF))
                            .border(1.dp, NeonCyan, RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(thought, color = Color.White, modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            thoughts.remove(thought)
                            AuraSoundManager.playTapSound(context)
                        }) {
                            Icon(Icons.Default.Delete, "Remove", tint = NeonPink)
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = inputThought,
                    onValueChange = { inputThought = it },
                    placeholder = { Text("Unload an open loop thought...", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                GlassButton(
                    text = "Add 📥",
                    onClick = {
                        if (inputThought.isNotBlank()) {
                            thoughts.add(0, inputThought.trim())
                            inputThought = ""
                            AuraSoundManager.playTapSound(context)
                        }
                    }
                )
            }
        }
    }
}

// =============================================================================
// TOOL 28: IVY LEE 6 PRIORITIES DISCIPLINE (MAX 6 TASKS DAILY)
// =============================================================================
@Composable
fun FullIvyLeeSixPrioritiesScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val priorities = remember {
        mutableStateListOf(
            Pair("Complete 2 Pomodoro focus sessions", true),
            Pair("Drink 2L fresh water", true),
            Pair("Clean inbox open loops", false),
            Pair("Play 15 mins brain training math", false),
            Pair("Evening 20-min posture walk", false),
            Pair("Read 10 pages stoic philosophy", false)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF16150D), Color(0xFF080703))))
    ) {
        ToolTopNavHeader("Ivy Lee 6 Priorities", "Legendary Rule: Exactly 6 Daily Tasks in Order", NeonAmber, onBack)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(priorities.size) { idx ->
                val item = priorities[idx]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (item.second) NeonLime.copy(alpha = 0.2f) else Color(0x18FFFFFF))
                        .border(1.dp, if (item.second) NeonLime else NeonAmber, RoundedCornerShape(14.dp))
                        .clickable {
                            AuraSoundManager.playTapSound(context)
                            priorities[idx] = item.copy(second = !item.second)
                            if (priorities[idx].second) {
                                AuraSoundManager.playSuccessSound()
                                GamificationManager.addXp(25, "Ivy Lee Task")
                            }
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("#${idx + 1}", fontWeight = FontWeight.Bold, color = NeonAmber, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(item.first, color = Color.White, modifier = Modifier.weight(1f), fontSize = 14.sp)
                    if (item.second) {
                        Text("✓ DONE", color = NeonLime, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 29: 20-20-20 OCULAR RELIEF PACER (EYE STRAIN RELIEVER)
// =============================================================================
@Composable
fun FullOcularRelief202020Screen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var secondsLeft by remember { mutableIntStateOf(20) }
    var isPacing by remember { mutableStateOf(false) }

    LaunchedEffect(isPacing, secondsLeft) {
        if (isPacing && secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        } else if (isPacing && secondsLeft == 0) {
            AuraSoundManager.playSuccessSound()
            isPacing = false
            Toast.makeText(context, "Eye muscles relaxed! +20 XP", Toast.LENGTH_SHORT).show()
            GamificationManager.addXp(20, "Eye Strain Relief")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0C1917), Color(0xFF030807))))
    ) {
        ToolTopNavHeader("20-20-20 Ocular Relief Pacer", "Ocular Fatigue Reliever & Distance Gaze", NeonLime, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonLime
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("The 20-20-20 Golden Rule", fontWeight = FontWeight.Bold, color = NeonLime, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Every 20 minutes of screen time, gaze at an object at least 20 feet (6 meters) away for 20 seconds to relax ciliary ocular muscles.",
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${secondsLeft}s",
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isPacing) NeonLime else Color.White
                )
                Text(if (isPacing) "Look 20ft into the distance now..." else "Ready for ocular reset", color = Color.Gray, fontSize = 14.sp)
            }

            GlassButton(
                text = if (isPacing) "CANCEL PACER" else "START 20s EYE RESET 👁️",
                onClick = {
                    AuraSoundManager.playTapSound(context)
                    if (!isPacing) {
                        secondsLeft = 20
                        isPacing = true
                    } else {
                        isPacing = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// =============================================================================
// TOOL 30: MULTI-SPECTRUM NOISE GENERATOR (WHITE / PINK / BROWN SYNTHESIZER)
// =============================================================================
@Composable
fun FullMultiSpectrumNoiseGenScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var selectedSpectrum by remember { mutableStateOf("BROWN") } // WHITE, PINK, BROWN
    var volume by remember { mutableFloatStateOf(0.7f) }
    var isPlaying by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF14101A), Color(0xFF060408))))
    ) {
        ToolTopNavHeader("Acoustic Noise Synthesizer", "White, Pink & Deep Brown Noise Waves", NeonViolet, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("WHITE", "PINK", "BROWN").forEach { spectrum ->
                    GlassChip(
                        label = "$spectrum NOISE",
                        color = when (spectrum) {
                            "WHITE" -> Color.White
                            "PINK" -> NeonPink
                            else -> NeonAmber
                        },
                        selected = selectedSpectrum == spectrum,
                        onClick = {
                            AuraSoundManager.playTapSound(context)
                            selectedSpectrum = spectrum
                        }
                    )
                }
            }

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonViolet
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        when (selectedSpectrum) {
                            "WHITE" -> "⚪ Pure High-Frequency Masking (Equal across all frequencies)"
                            "PINK" -> "🌸 Deeper Harmonic Flow (1/f natural waterfall power curve)"
                            else -> "🍂 Deep Brown Noise (Heavy low rumbling waves for deep sleep & ADHD focus)"
                        },
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Synthesizer Volume: ${(volume * 100).toInt()}%", color = Color.White, fontWeight = FontWeight.Bold)
                Slider(
                    value = volume,
                    onValueChange = { volume = it },
                    colors = SliderDefaults.colors(thumbColor = NeonViolet, activeTrackColor = NeonViolet)
                )
            }

            GlassButton(
                text = if (isPlaying) "STOP SYNTHESIZER ⏹️" else "PLAY ${selectedSpectrum} NOISE ▶️",
                onClick = {
                    AuraSoundManager.playTapSound(context)
                    isPlaying = !isPlaying
                    if (isPlaying) AuraSoundManager.playSuccessSound()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// =============================================================================
// TOOL 31: CBT COGNITIVE REFRAMING MATRIX (THOUGHT CHALLENGER)
// =============================================================================
@Composable
fun FullCbtCognitiveReframingScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var automaticThought by remember { mutableStateOf("") }
    var distortionType by remember { mutableStateOf("Catastrophizing") }
    var rationalReframe by remember { mutableStateOf("") }

    val distortions = listOf("Catastrophizing", "All-or-Nothing", "Mind Reading", "Overgeneralization", "Emotional Reasoning")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A0E18), Color(0xFF070306))))
    ) {
        ToolTopNavHeader("CBT Cognitive Reframing", "Challenge Negative Distortions & Reframe", NeonPink, onBack)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("1. Negative Automatic Thought", color = Color.LightGray, fontSize = 12.sp)
                OutlinedTextField(
                    value = automaticThought,
                    onValueChange = { automaticThought = it },
                    placeholder = { Text("e.g., I missed my study goal, everything is ruined.") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
            }

            item {
                Text("2. Select Cognitive Distortion Pattern", color = Color.LightGray, fontSize = 12.sp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    distortions.take(3).forEach { dist ->
                        GlassChip(label = dist, color = NeonPink, selected = distortionType == dist, onClick = { distortionType = dist })
                    }
                }
            }

            item {
                Text("3. Objective Rational Reframe", color = Color.LightGray, fontSize = 12.sp)
                OutlinedTextField(
                    value = rationalReframe,
                    onValueChange = { rationalReframe = it },
                    placeholder = { Text("e.g., One missed session does not erase my compounding streak.") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
            }

            item {
                GlassButton(
                    text = "Commit Cognitive Reframe 🧠",
                    onClick = {
                        AuraSoundManager.playSuccessSound()
                        GamificationManager.addXp(45, "CBT Reframe")
                        Toast.makeText(context, "Perspective reframed! +45 XP", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// =============================================================================
// TOOL 32: BIO-HYDRATION METABOLISM PACER (WATER INTAKE LOG)
// =============================================================================
@Composable
fun FullHydrationMetabolismPacerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var glassesDrunk by remember { mutableIntStateOf(5) }
    val targetGlasses = 8

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0B1924), Color(0xFF03070A))))
    ) {
        ToolTopNavHeader("Hydration Metabolism Pacer", "Optimal Cognitive Hydration Tracker", NeonCyan, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonCyan
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Daily Water Intake", color = Color.LightGray, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("$glassesDrunk / $targetGlasses Glasses", fontSize = 38.sp, fontWeight = FontWeight.Black, color = NeonCyan)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("${glassesDrunk * 250} ml / 2000 ml", color = Color.White, fontSize = 14.sp)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (i in 1..targetGlasses) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (i <= glassesDrunk) NeonCyan.copy(alpha = 0.5f) else Color(0x18FFFFFF))
                            .border(1.dp, if (i <= glassesDrunk) NeonCyan else Color.Gray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (i <= glassesDrunk) "💧" else "⚪", fontSize = 14.sp)
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                GlassButton(
                    text = "Drink Glass +1 💧",
                    onClick = {
                        AuraSoundManager.playTapSound(context)
                        if (glassesDrunk < 12) glassesDrunk++
                        if (glassesDrunk == targetGlasses) AuraSoundManager.playSuccessSound()
                    },
                    modifier = Modifier.weight(1f)
                )

                GlassButton(
                    text = "Reset 🔄",
                    onClick = { glassesDrunk = 0 },
                    isPrimary = false,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

// =============================================================================
// TOOL 33: WEEKLY AGILE RETROSPECTIVE (SPRINT RETRO)
// =============================================================================
@Composable
fun FullWeeklyReviewRetrospectiveScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var winsText by remember { mutableStateOf("Completed 12 Pomodoro sprints\nMaintained 7-day login streak") }
    var learningsText by remember { mutableStateOf("Do not check emails before 11:00 AM\nTake 20s eye rest every 20m") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF13170F), Color(0xFF040603))))
    ) {
        ToolTopNavHeader("Weekly Agile Retrospective", "4-Quadrant Sprint Review & Growth Log", NeonLime, onBack)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("🌟 Weekly Wins & Accomplishments", color = NeonLime, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                OutlinedTextField(
                    value = winsText,
                    onValueChange = { winsText = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
            }

            item {
                Text("💡 Lessons Learned & Adjustments", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                OutlinedTextField(
                    value = learningsText,
                    onValueChange = { learningsText = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
            }

            item {
                GlassButton(
                    text = "Finalize Weekly Retrospective 🏆",
                    onClick = {
                        AuraSoundManager.playSuccessSound()
                        GamificationManager.addXp(60, "Weekly Retrospective")
                        Toast.makeText(context, "Sprint retrospective logged! +60 XP", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// =============================================================================
// TOOL 34: FOCUS COMMITMENT CONTRACT (ANTI-PROCRASTINATION OATH)
// =============================================================================
@Composable
fun FullFocusCommitmentContractScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isContractSigned by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF160F11), Color(0xFF060304))))
    ) {
        ToolTopNavHeader("Focus Commitment Contract", "Pre-Commitment Accountability Contract", NeonPink, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonPink
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📜 SOVEREIGN FOCUS PACT", fontWeight = FontWeight.Black, color = NeonPink, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "I formally commit to 60 minutes of unbroken high-cognitive work today.\n\n" +
                        "I will not open social media, short-form reels, or entertainment distractions.\n\n" +
                        "If I break this pact, my habit streak shield will be forfeit.",
                        color = Color.White,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            GlassButton(
                text = if (isContractSigned) "CONTRACT SEALED & ACTIVE 🔒" else "SIGN & SEAL CONTRACT ✍️",
                onClick = {
                    AuraSoundManager.playSuccessSound()
                    isContractSigned = true
                    GamificationManager.addXp(40, "Commitment Contract")
                    Toast.makeText(context, "Pact Sealed! +40 XP", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// =============================================================================
// TOOL 35: DESK POSTURE & ERGONOMICS PACER (3-MIN POSTURE RESET)
// =============================================================================
@Composable
fun FullDeskErgonomicsStretchPacerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var currentExercise by remember { mutableIntStateOf(0) }
    val exercises = listOf(
        Pair("1. Chin Tucks & Neck Retraction", "Gently pull your head backward like creating a double chin. Hold 5 seconds. Repeat 5 times."),
        Pair("2. Shoulder Blade Squeeze", "Pull shoulders down and squeeze shoulder blades together behind your back. Hold 8 seconds."),
        Pair("3. Seated Spinal Twist", "Place left hand on right knee. Gently twist your torso to the right and look over shoulder.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF101914), Color(0xFF030705))))
    ) {
        ToolTopNavHeader("Desk Posture & Ergonomics Pacer", "Guided 3-Minute Spinal & Wrist Reset", NeonLime, onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val item = exercises[currentExercise]
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonLime
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(item.first, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(item.second, fontSize = 14.sp, color = Color.White, textAlign = TextAlign.Center, lineHeight = 22.sp)
                }
            }

            Text("Routine ${currentExercise + 1} of ${exercises.size}", color = Color.LightGray)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (currentExercise < exercises.size - 1) {
                    GlassButton(
                        text = "Next Stretch ➔",
                        onClick = {
                            AuraSoundManager.playTapSound(context)
                            currentExercise++
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    GlassButton(
                        text = "Complete Posture Reset 🧘",
                        onClick = {
                            AuraSoundManager.playSuccessSound()
                            GamificationManager.addXp(30, "Desk Ergonomics")
                            Toast.makeText(context, "Spine & neck reset complete! +30 XP", Toast.LENGTH_SHORT).show()
                            onBack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
