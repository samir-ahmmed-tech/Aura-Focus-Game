package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

// =============================================================================
// TOOL 36: PARKINSON'S LAW SPRINT ENFORCER
// =============================================================================
@Composable
fun FullParkinsonsLawSprintScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var taskTitle by remember { mutableStateOf("") }
    var sprintMinutes by remember { mutableIntStateOf(10) }
    var secondsLeft by remember { mutableIntStateOf(600) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, secondsLeft) {
        if (isRunning && secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        } else if (isRunning && secondsLeft == 0) {
            isRunning = false
            AuraSoundManager.playSuccessSound(context)
            GamificationManager.recordFocusSession(sprintMinutes)
            Toast.makeText(context, "Sprint Complete! Task Shrunk Successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader(title = "Parkinson's Law Sprint", subtitle = "Compress Work to Prevent Expansion", accentColor = NeonCyan, onBack = onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("⏳ Work Expands to Fill Time Allowed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Parkinson's Law states that if you give yourself 2 hours, it takes 2 hours. Give yourself 10 minutes, and focus spikes instantly.", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = taskTitle,
                            onValueChange = { taskTitle = it },
                            label = { Text("Single Micro-Task Objective", color = Color.White.copy(alpha = 0.7f)) },
                            placeholder = { Text("e.g. Write intro email, Review 5 PR lines", color = Color.White.copy(alpha = 0.4f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonCyan),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Sprint Target: $sprintMinutes Minutes", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(5, 10, 15, 20).forEach { mins ->
                                GlassChip(
                                    label = "$mins Mins",
                                    color = NeonCyan,
                                    selected = (sprintMinutes == mins),
                                    onClick = { sprintMinutes = mins; secondsLeft = mins * 60; isRunning = false }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        val m = secondsLeft / 60
                        val s = secondsLeft % 60
                        Text(String.format("%02d:%02d", m, s), fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            GlassButton(
                                text = if (isRunning) "PAUSE" else "START SPRINT",
                                onClick = { isRunning = !isRunning; AuraSoundManager.playTapSound(context) },
                                isPrimary = true,
                                modifier = Modifier.weight(1f)
                            )
                            GlassButton(
                                text = "DONE! ⚡",
                                onClick = {
                                    isRunning = false
                                    GamificationManager.addXp(30, "Parkinson Task Smashed")
                                    AuraSoundManager.playSuccessSound(context)
                                    Toast.makeText(context, "Task Done! +30 XP", Toast.LENGTH_SHORT).show()
                                },
                                isPrimary = false,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 37: RUBBER DUCK COGNITIVE DEBUGGER
// =============================================================================
@Composable
fun FullRubberDuckDebuggerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var problemState by remember { mutableStateOf("") }
    var expectedBehavior by remember { mutableStateOf("") }
    var actualBehavior by remember { mutableStateOf("") }
    var breakthroughInsight by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader(title = "Rubber Duck Debugger", subtitle = "Socratic Problem Unpacker", accentColor = NeonAmber, onBack = onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🦆", fontSize = 32.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Explain it to the Duck", fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Speaking or writing forced clarity triggers lateral solutions in 85% of blockers.", fontSize = 11.sp, color = NeonAmber)
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = problemState,
                            onValueChange = { problemState = it },
                            label = { Text("1. What problem are you stuck on?", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonAmber),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = expectedBehavior,
                            onValueChange = { expectedBehavior = it },
                            label = { Text("2. What did you expect to happen?", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonAmber),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = actualBehavior,
                            onValueChange = { actualBehavior = it },
                            label = { Text("3. What is actually happening?", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonAmber),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = breakthroughInsight,
                            onValueChange = { breakthroughInsight = it },
                            label = { Text("4. Breakthrough Insight / Root Cause", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonLime),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GlassButton(
                            text = "SAVE DUCK LOG (+25 XP)",
                            onClick = {
                                GamificationManager.addXp(25, "Rubber Duck Insight Logged")
                                AuraSoundManager.playSuccessSound(context)
                                Toast.makeText(context, "Duck Insight Saved! +25 XP", Toast.LENGTH_SHORT).show()
                            },
                            isPrimary = true
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 38: ENERGY DRAINER & BURNOUT RADAR
// =============================================================================
@Composable
fun FullBurnoutRadarAuditScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var cognitiveFatigue by remember { mutableFloatStateOf(3f) }
    var physicalTension by remember { mutableFloatStateOf(2f) }
    var emotionalReserve by remember { mutableFloatStateOf(4f) }
    var taskFriction by remember { mutableFloatStateOf(3f) }

    val burnoutRisk = remember(cognitiveFatigue, physicalTension, emotionalReserve, taskFriction) {
        val total = (cognitiveFatigue + physicalTension + (6f - emotionalReserve) + taskFriction) / 20f
        (total * 100).toInt()
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader(title = "Burnout Radar Audit", subtitle = "5-Factor Vitality & Friction Index", accentColor = NeonPink, onBack = onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Burnout Friction Score", fontWeight = FontWeight.Bold, color = Color.White)
                            GlassChip(
                                label = "$burnoutRisk% ${if (burnoutRisk > 60) "HIGH RISK ⚠️" else if (burnoutRisk > 35) "MODERATE ⚡" else "OPTIMAL FLOW 🌿"}",
                                color = if (burnoutRisk > 60) NeonPink else if (burnoutRisk > 35) NeonAmber else NeonLime,
                                selected = true
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("1. Mental / Cognitive Fatigue (1..5): ${cognitiveFatigue.toInt()}", color = Color.White, fontSize = 12.sp)
                        Slider(value = cognitiveFatigue, onValueChange = { cognitiveFatigue = it }, valueRange = 1f..5f, steps = 3, colors = SliderDefaults.colors(thumbColor = NeonPink, activeTrackColor = NeonPink))

                        Text("2. Physical Shoulder/Eye Tension (1..5): ${physicalTension.toInt()}", color = Color.White, fontSize = 12.sp)
                        Slider(value = physicalTension, onValueChange = { physicalTension = it }, valueRange = 1f..5f, steps = 3, colors = SliderDefaults.colors(thumbColor = NeonPink, activeTrackColor = NeonPink))

                        Text("3. Emotional Reserve & Patience (1..5): ${emotionalReserve.toInt()}", color = Color.White, fontSize = 12.sp)
                        Slider(value = emotionalReserve, onValueChange = { emotionalReserve = it }, valueRange = 1f..5f, steps = 3, colors = SliderDefaults.colors(thumbColor = NeonLime, activeTrackColor = NeonLime))

                        Text("4. Current Task Friction (1..5): ${taskFriction.toInt()}", color = Color.White, fontSize = 12.sp)
                        Slider(value = taskFriction, onValueChange = { taskFriction = it }, valueRange = 1f..5f, steps = 3, colors = SliderDefaults.colors(thumbColor = NeonAmber, activeTrackColor = NeonAmber))

                        Spacer(modifier = Modifier.height(14.dp))
                        GlassButton(
                            text = "RECORD ENERGY AUDIT",
                            onClick = {
                                GamificationManager.addXp(20, "Burnout Audit Logged")
                                AuraSoundManager.playSuccessSound(context)
                                Toast.makeText(context, "Vitality Recorded! Recovery protocol updated.", Toast.LENGTH_SHORT).show()
                            },
                            isPrimary = true
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 39: MENTAL REHEARSAL VISUALIZER
// =============================================================================
@Composable
fun FullMentalRehearsalScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var currentStep by remember { mutableIntStateOf(0) }
    val steps = listOf(
        "Phase 1: Sensory Grounding" to "Close eyes. Take 3 deep diaphragmatic breaths. Feel your feet flat on the floor.",
        "Phase 2: Environment Simulation" to "Envision your screen, tools, and workspace in vivid detail. Feel the posture of authority.",
        "Phase 3: Obstacle Defusal" to "Anticipate the first moment of distraction or urge to check messages. Rehearse acknowledging the urge and returning calmly to the work.",
        "Phase 4: Flow State Lock-in" to "Envision the exact finished state of your work. Feel the satisfaction of completion."
    )

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader(title = "Mental Rehearsal Visualizer", subtitle = "Pre-Performance Neural Scripting", accentColor = NeonViolet, onBack = onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🧘‍♂️ Step ${currentStep + 1} / 4", color = NeonViolet, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(steps[currentStep].first, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(steps[currentStep].second, color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp, textAlign = TextAlign.Center, lineHeight = 20.sp)

                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            if (currentStep > 0) {
                                GlassButton(text = "PREVIOUS", onClick = { currentStep-- }, isPrimary = false, modifier = Modifier.weight(1f))
                            }
                            if (currentStep < 3) {
                                GlassButton(text = "NEXT PHASE", onClick = { currentStep++; AuraSoundManager.playTapSound(context) }, isPrimary = true, modifier = Modifier.weight(1f))
                            } else {
                                GlassButton(text = "ENTER FLOW (+30 XP)", onClick = {
                                    GamificationManager.addXp(30, "Mental Rehearsal Done")
                                    AuraSoundManager.playSuccessSound(context)
                                    onBack()
                                }, isPrimary = true, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 40: INTERSTITIAL JOURNALING TIMER
// =============================================================================
@Composable
fun FullInterstitialJournalScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var justFinished by remember { mutableStateOf("") }
    var nextObjective by remember { mutableStateOf("") }
    var frictionNote by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader(title = "Interstitial Journaling", subtitle = "Zero-Lag Task Transition Bridge", accentColor = NeonCyan, onBack = onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("Bridge Between Tasks", fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Writing 2 sentences between tasks unloads attention residue so you begin the next task with 100% bandwidth.", color = Color.White.copy(alpha = 0.75f), fontSize = 11.sp)

                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = justFinished,
                            onValueChange = { justFinished = it },
                            label = { Text("1. What exact micro-task did you just complete?", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonCyan),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = nextObjective,
                            onValueChange = { nextObjective = it },
                            label = { Text("2. What is the single next immediate action?", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonLime),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = frictionNote,
                            onValueChange = { frictionNote = it },
                            label = { Text("3. Any mental baggage or lingering thoughts to drop?", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonPink),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(18.dp))
                        GlassButton(
                            text = "TRANSITION & ENGAGE NEXT (+20 XP)",
                            onClick = {
                                GamificationManager.addXp(20, "Interstitial Transition Logged")
                                AuraSoundManager.playSuccessSound(context)
                                Toast.makeText(context, "Transition logged! Next task activated.", Toast.LENGTH_SHORT).show()
                            },
                            isPrimary = true
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 41: FEYNMAN TECHNIQUE CONCEPT CLARIFIER
// =============================================================================
@Composable
fun FullFeynmanTechniqueScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var conceptName by remember { mutableStateOf("") }
    var simpleExplanation by remember { mutableStateOf("") }
    var realWorldAnalogy by remember { mutableStateOf("") }
    var blindspots by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader(title = "Feynman Technique Clarifier", subtitle = "Explain It Like I'm 10 Years Old", accentColor = NeonAmber, onBack = onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("🔬 Deep Mastery Protocol", fontWeight = FontWeight.Bold, color = Color.White)
                        Text("If you cannot explain a concept without jargon, you do not understand it deeply.", color = Color.White.copy(alpha = 0.75f), fontSize = 11.sp)

                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = conceptName,
                            onValueChange = { conceptName = it },
                            label = { Text("Target Complex Concept", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonAmber),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = simpleExplanation,
                            onValueChange = { simpleExplanation = it },
                            label = { Text("Simple Plain-English Explanation (No buzzwords)", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonCyan),
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = realWorldAnalogy,
                            onValueChange = { realWorldAnalogy = it },
                            label = { Text("Real-World Physical Analogy", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonLime),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = blindspots,
                            onValueChange = { blindspots = it },
                            label = { Text("Identified Gaps & Confusions to Research", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonPink),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        GlassButton(
                            text = "CRYSTALLIZE MASTERY (+30 XP)",
                            onClick = {
                                GamificationManager.addXp(30, "Feynman Concept Clarified")
                                AuraSoundManager.playSuccessSound(context)
                                Toast.makeText(context, "Concept Crystallized! +30 XP", Toast.LENGTH_SHORT).show()
                            },
                            isPrimary = true
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 42: DECISION MATRIX & WEIGHTED SCORER
// =============================================================================
@Composable
fun FullDecisionMatrixScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var optionA by remember { mutableStateOf("Option A: Refactor Architecture") }
    var optionB by remember { mutableStateOf("Option B: Ship Quick MVP") }
    var impactA by remember { mutableFloatStateOf(4f) }
    var easeA by remember { mutableFloatStateOf(2f) }
    var impactB by remember { mutableFloatStateOf(3f) }
    var easeB by remember { mutableFloatStateOf(4f) }

    val scoreA = remember(impactA, easeA) { (impactA * 2 + easeA * 1.5f).toInt() }
    val scoreB = remember(impactB, easeB) { (impactB * 2 + easeB * 1.5f).toInt() }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader(title = "Decision Scorer Matrix", subtitle = "Weighted Clarity Grid", accentColor = NeonLime, onBack = onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("⚖️ Weighted Evaluation", fontWeight = FontWeight.Bold, color = Color.White)

                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = optionA,
                            onValueChange = { optionA = it },
                            label = { Text("Path A Name", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonCyan),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text("Path A Impact: ${impactA.toInt()} | Ease: ${easeA.toInt()} -> Score: $scoreA", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Slider(value = impactA, onValueChange = { impactA = it }, valueRange = 1f..5f, steps = 3)

                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = optionB,
                            onValueChange = { optionB = it },
                            label = { Text("Path B Name", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonPink),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text("Path B Impact: ${impactB.toInt()} | Ease: ${easeB.toInt()} -> Score: $scoreB", color = NeonPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Slider(value = impactB, onValueChange = { impactB = it }, valueRange = 1f..5f, steps = 3)

                        Spacer(modifier = Modifier.height(16.dp))
                        val winner = if (scoreA > scoreB) optionA else optionB
                        Text("Recommended Direction: $winner", color = NeonLime, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                        Spacer(modifier = Modifier.height(14.dp))
                        GlassButton(
                            text = "DECIDE & COMMIT (+25 XP)",
                            onClick = {
                                GamificationManager.addXp(25, "Strategic Decision Committed")
                                AuraSoundManager.playSuccessSound(context)
                                Toast.makeText(context, "Commitment locked in! Move forward boldly.", Toast.LENGTH_SHORT).show()
                            },
                            isPrimary = true
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 43: DIGITAL SUNSET & WIND-DOWN PROTOCOL
// =============================================================================
@Composable
fun FullDigitalSunsetScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val checklist = remember {
        mutableStateListOf(
            "Close all work browser tabs & save git commits" to false,
            "Identify exactly ONE #1 priority task for tomorrow" to false,
            "Dim room lighting & enable blue light night filter" to false,
            "10 slow 4-7-8 calming recovery breaths" to false
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader(title = "Digital Sunset Wind-Down", subtitle = "Circadian Recovery Protocol", accentColor = NeonAmber, onBack = onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("🌙 Evening Cognitive Shutdown", fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Preventing revenge bedtime procrastination through a 4-step closing ritual.", color = Color.White.copy(alpha = 0.75f), fontSize = 11.sp)

                        Spacer(modifier = Modifier.height(16.dp))
                        checklist.forEachIndexed { index, pair ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (pair.second) NeonAmber.copy(alpha = 0.2f) else Color(0x18FFFFFF))
                                    .clickable {
                                        checklist[index] = pair.first to !pair.second
                                        AuraSoundManager.playTapSound(context)
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(if (pair.second) "✅ " else "⭕ ", fontSize = 16.sp)
                                Text(pair.first, color = if (pair.second) NeonAmber else Color.White, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))
                        GlassButton(
                            text = "COMPLETE DIGITAL SUNSET (+30 XP)",
                            onClick = {
                                GamificationManager.addXp(30, "Digital Sunset Completed")
                                AuraSoundManager.playSuccessSound(context)
                                Toast.makeText(context, "Sunset Protocol Complete! Sleep well.", Toast.LENGTH_SHORT).show()
                            },
                            isPrimary = true
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 44: ANTI-PROCRASTINATION 5-MIN KICKSTART
// =============================================================================
@Composable
fun FullAntiProcrastination5MinScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var secondsLeft by remember { mutableIntStateOf(300) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning, secondsLeft) {
        if (isRunning && secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        } else if (isRunning && secondsLeft == 0) {
            isRunning = false
            GamificationManager.addXp(30, "5-Min Barrier Broken")
            AuraSoundManager.playSuccessSound(context)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader(title = "5-Minute Kickstart", subtitle = "Friction-Free Momentum Trigger", accentColor = NeonLime, onBack = onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🚀 Zero-Resistance Start", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("You don't have to finish the project. You only have to sit and type for 5 minutes. If you want to stop after 5 minutes, you have full permission.", textAlign = TextAlign.Center, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)

                        Spacer(modifier = Modifier.height(24.dp))
                        val m = secondsLeft / 60
                        val s = secondsLeft % 60
                        Text(String.format("%02d:%02d", m, s), fontSize = 54.sp, fontWeight = FontWeight.ExtraBold, color = NeonLime)

                        Spacer(modifier = Modifier.height(20.dp))
                        GlassButton(
                            text = if (isRunning) "PAUSE" else "START 5 MINUTES",
                            onClick = { isRunning = !isRunning; AuraSoundManager.playTapSound(context) },
                            isPrimary = true
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 45: WEEKLY OKR & RETROSPECTIVE MATRIX
// =============================================================================
@Composable
fun FullWeeklyOkrMatrixScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var objectiveTitle by remember { mutableStateOf("Scale Focus Output to 12 Hours") }
    var kr1Title by remember { mutableStateOf("Complete 10 Pomodoro Sprints") }
    var kr1Progress by remember { mutableFloatStateOf(70f) }
    var kr2Title by remember { mutableStateOf("0 Phone checks during deep work") }
    var kr2Progress by remember { mutableFloatStateOf(85f) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader(title = "Weekly OKR Matrix", subtitle = "Objectives & Key Results Tracker", accentColor = NeonViolet, onBack = onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("🎯 Weekly North Star Objective", fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = objectiveTitle,
                            onValueChange = { objectiveTitle = it },
                            label = { Text("Objective", color = Color.White.copy(alpha = 0.7f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonViolet),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Key Result 1: $kr1Title (${kr1Progress.toInt()}%)", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Slider(value = kr1Progress, onValueChange = { kr1Progress = it }, valueRange = 0f..100f, colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan))

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Key Result 2: $kr2Title (${kr2Progress.toInt()}%)", color = NeonLime, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Slider(value = kr2Progress, onValueChange = { kr2Progress = it }, valueRange = 0f..100f, colors = SliderDefaults.colors(thumbColor = NeonLime, activeTrackColor = NeonLime))

                        Spacer(modifier = Modifier.height(16.dp))
                        GlassButton(
                            text = "UPDATE OKR STATUS (+25 XP)",
                            onClick = {
                                GamificationManager.addXp(25, "OKR Progress Updated")
                                AuraSoundManager.playSuccessSound(context)
                                Toast.makeText(context, "OKR Updated! +25 XP", Toast.LENGTH_SHORT).show()
                            },
                            isPrimary = true
                        )
                    }
                }
            }
        }
    }
}
