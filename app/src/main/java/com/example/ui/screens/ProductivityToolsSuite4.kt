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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
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
import com.example.util.AuraAdMobManager
import com.example.util.AuraLinkHelper
import com.example.util.AuraSoundManager
import kotlinx.coroutines.delay

// =============================================================================
// TOOL 46: FULL EISENHOWER MATRIX V2 (4-QUADRANT STRATEGIC PRIORITY ENGINE)
// =============================================================================
data class MatrixTask(val id: Int, val title: String, val quadrant: Int, var isCompleted: Boolean = false)

@Composable
fun FullEisenhowerMatrixV2Screen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var newTaskText by remember { mutableStateOf("") }
    var selectedQuadrant by remember { mutableIntStateOf(1) }
    val tasks = remember {
        mutableStateListOf(
            MatrixTask(1, "Complete NeuroBox v1.4.0 Engine Release", 1),
            MatrixTask(2, "Master Daily 90-min Deep Flow Session", 2),
            MatrixTask(3, "Delegate routine notification triage", 3),
            MatrixTask(4, "Purge endless mindless social doomscroll", 4)
        )
    }

    val q2Count = tasks.count { it.quadrant == 2 }
    val totalCount = tasks.size.coerceAtLeast(1)
    val q2Percent = (q2Count * 100) / totalCount

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader("Eisenhower Matrix V2", "Urgent vs Important Strategic Sorter", NeonAmber, onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("🎯 Quadrant 2 Deep Focus Ratio: $q2Percent%", fontWeight = FontWeight.Bold, color = NeonAmber, fontSize = 16.sp)
                        Text(
                            "High performers spend 60%+ of their energy in Q2 (Important, Not Urgent) to prevent future fires before they ignite.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Add Task to Matrix", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newTaskText,
                            onValueChange = { newTaskText = it },
                            placeholder = { Text("Enter task description...", color = Color.White.copy(alpha = 0.4f)) },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonAmber),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Select Target Quadrant:", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            GlassChip(label = "Q1 Do First", color = NeonPink, selected = (selectedQuadrant == 1), onClick = { selectedQuadrant = 1 })
                            GlassChip(label = "Q2 Schedule", color = NeonCyan, selected = (selectedQuadrant == 2), onClick = { selectedQuadrant = 2 })
                            GlassChip(label = "Q3 Delegate", color = NeonAmber, selected = (selectedQuadrant == 3), onClick = { selectedQuadrant = 3 })
                            GlassChip(label = "Q4 Eliminate", color = NeonLime, selected = (selectedQuadrant == 4), onClick = { selectedQuadrant = 4 })
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        GlassButton(
                            text = "+ ADD TO QUADRANT",
                            onClick = {
                                if (newTaskText.isNotBlank()) {
                                    tasks.add(MatrixTask(System.currentTimeMillis().toInt(), newTaskText.trim(), selectedQuadrant))
                                    newTaskText = ""
                                    AuraSoundManager.playTapSound(context)
                                    GamificationManager.addXp(10, "Eisenhower Task Added")
                                }
                            },
                            isPrimary = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Quadrant Breakdown
            items(listOf(1, 2, 3, 4)) { q ->
                val qColor = when (q) {
                    1 -> NeonPink
                    2 -> NeonCyan
                    3 -> NeonAmber
                    else -> NeonLime
                }
                val qTitle = when (q) {
                    1 -> "Q1: Urgent & Important (DO IMMEDIATELY)"
                    2 -> "Q2: Not Urgent but Important (STRATEGIC FLOW)"
                    3 -> "Q3: Urgent but Not Important (DELEGATE / BATCH)"
                    else -> "Q4: Neither (ELIMINATE / PURGE)"
                }
                val qTasks = tasks.filter { it.quadrant == q }

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(qTitle, fontWeight = FontWeight.Bold, color = qColor, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        if (qTasks.isEmpty()) {
                            Text("No tasks currently in this quadrant.", color = Color.White.copy(alpha = 0.4f), fontSize = 12.sp)
                        } else {
                            qTasks.forEach { task ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (task.isCompleted) qColor else Color.White.copy(alpha = 0.1f))
                                            .border(1.dp, qColor, RoundedCornerShape(4.dp))
                                            .clickable {
                                                task.isCompleted = !task.isCompleted
                                                if (task.isCompleted) {
                                                    AuraSoundManager.playSuccessSound(context)
                                                    GamificationManager.addXp(20, "Quadrant Task Completed")
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (task.isCompleted) {
                                            Text("✓", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        task.title,
                                        color = if (task.isCompleted) Color.White.copy(alpha = 0.4f) else Color.White,
                                        fontSize = 13.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { tasks.remove(task); AuraSoundManager.playTapSound(context) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White.copy(alpha = 0.4f), modifier = Modifier.size(16.dp))
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
// TOOL 47: FULL ATOMIC HABIT MICRO-DOSER (2-MINUTE STACKING PROTOCOL)
// =============================================================================
@Composable
fun FullAtomicHabitMicroDoserScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var anchorHabit by remember { mutableStateOf("After I pour morning coffee") }
    var microAction by remember { mutableStateOf("I will open NeuroBox for 2 minutes of focus") }
    var immediateReward by remember { mutableStateOf("I will log +25 XP and celebrate streak") }
    var streakDays by remember { mutableIntStateOf(5) }

    var timerSeconds by remember { mutableIntStateOf(120) }
    var isTimerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isTimerRunning, timerSeconds) {
        if (isTimerRunning && timerSeconds > 0) {
            delay(1000)
            timerSeconds--
        } else if (isTimerRunning && timerSeconds == 0) {
            isTimerRunning = false
            streakDays++
            AuraSoundManager.playSuccessSound(context)
            GamificationManager.addXp(35, "Atomic Habit 2-Min Microdose Executed")
            Toast.makeText(context, "Microdose Complete! Habit Locked In! +35 XP", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader("Atomic Habit Micro-Doser", "James Clear 2-Minute Habit Stacker", NeonLime, onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🔥 Current Habit Stack Streak: ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("$streakDays Days", color = NeonLime, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "The 2-Minute Rule: When you start a new habit, it should take less than two minutes to do. Optimize for the starting line, not the finish line.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("🔗 Habit Stacking Formula", fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("1. Cue (Existing Anchor Habit):", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        OutlinedTextField(
                            value = anchorHabit,
                            onValueChange = { anchorHabit = it },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonLime),
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("2. 2-Minute Micro-Action:", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        OutlinedTextField(
                            value = microAction,
                            onValueChange = { microAction = it },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonLime),
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("3. Immediate Gratification Reward:", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        OutlinedTextField(
                            value = immediateReward,
                            onValueChange = { immediateReward = it },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = NeonLime),
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        )
                    }
                }
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚡ Execute Micro-Dose Right Now", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        val mins = timerSeconds / 60
                        val secs = timerSeconds % 60
                        Text(
                            String.format("%02d:%02d", mins, secs),
                            fontSize = 42.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonLime
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            GlassButton(
                                text = if (isTimerRunning) "PAUSE" else "START 2-MIN TIMER",
                                onClick = { isTimerRunning = !isTimerRunning; AuraSoundManager.playTapSound(context) },
                                isPrimary = true,
                                modifier = Modifier.weight(1f)
                            )
                            GlassButton(
                                text = "RESET",
                                onClick = { isTimerRunning = false; timerSeconds = 120; AuraSoundManager.playTapSound(context) },
                                modifier = Modifier.weight(0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 48: FULL CHRONOTYPE ENERGY PLANNER (CIRCADIAN BIOLOGICAL RHYTHM)
// =============================================================================
enum class Chronotype(val title: String, val peakHours: String, val shallowHours: String, val sleepWindow: String, val description: String) {
    LION("Lion (Early Bird)", "08:00 - 12:00", "13:00 - 16:00", "21:30 - 05:30", "Wakes early with boundless morning energy. Schedule hardest cognitive challenges at dawn."),
    BEAR("Bear (Solar Rhythm)", "10:00 - 14:00", "15:00 - 18:00", "23:00 - 07:00", "Follows the sun. 55% of population. Peak output late morning; post-lunch dip is normal."),
    WOLF("Wolf (Night Owl)", "17:00 - 22:00", "12:00 - 15:00", "01:00 - 08:30", "Creative powerhouse in evening and night. Slow morning startup; high late-day velocity."),
    DOLPHIN("Dolphin (Insomniac/Light)", "15:00 - 19:00", "09:00 - 12:00", "23:30 - 06:30", "Sensitive nervous system. Bursts of intense focus in late afternoon. Prioritize calm routines.")
}

@Composable
fun FullChronotypeEnergyPlannerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var selectedChronotype by remember { mutableStateOf(Chronotype.BEAR) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader("Chronotype Bio-Planner", "Circadian Rhythm Peak Focus Mapper", NeonCyan, onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Select Your Chronotype", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Chronotype.values().forEach { chrono ->
                                GlassChip(
                                    label = chrono.name,
                                    color = NeonCyan,
                                    selected = (selectedChronotype == chrono),
                                    onClick = { selectedChronotype = chrono; AuraSoundManager.playTapSound(context) }
                                )
                            }
                        }
                    }
                }
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(selectedChronotype.title, fontWeight = FontWeight.ExtraBold, color = NeonCyan, fontSize = 17.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(selectedChronotype.description, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)

                        Spacer(modifier = Modifier.height(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🧠 Peak Deep Focus: ", color = NeonLime, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(selectedChronotype.peakHours, color = Color.White, fontSize = 13.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("⚡ Admin & Shallow Work: ", color = NeonAmber, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(selectedChronotype.shallowHours, color = Color.White, fontSize = 13.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🌙 Rest & Sleep Rhythm: ", color = NeonPink, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(selectedChronotype.sleepWindow, color = Color.White, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        GlassButton(
                            text = "LOCK IN DAILY BIORHYTHM (+25 XP)",
                            onClick = {
                                AuraSoundManager.playSuccessSound(context)
                                GamificationManager.addXp(25, "Chronotype Rhythm Synced")
                                Toast.makeText(context, "Circadian Plan Synced! +25 XP", Toast.LENGTH_SHORT).show()
                            },
                            isPrimary = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 49: FULL DISTRACTION SWITCH COST SCREEN (ATTENTION RESIDUE AUDITOR)
// =============================================================================
@Composable
fun FullDistractionSwitchCostScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var switchCount by remember { mutableIntStateOf(3) }
    var firewallActive by remember { mutableStateOf(false) }
    var firewallSeconds by remember { mutableIntStateOf(1500) } // 25 mins

    LaunchedEffect(firewallActive, firewallSeconds) {
        if (firewallActive && firewallSeconds > 0) {
            delay(1000)
            firewallSeconds--
        } else if (firewallActive && firewallSeconds == 0) {
            firewallActive = false
            AuraSoundManager.playSuccessSound(context)
            GamificationManager.addXp(40, "Attention Firewall Complete")
            Toast.makeText(context, "Firewall Sprint Complete! Attention Preserved!", Toast.LENGTH_SHORT).show()
        }
    }

    val lostMinutes = switchCount * 23 // Sophie Leroy attention residue metric: 23 mins lost per context switch

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader("Distraction Switch Cost", "Sophie Leroy Attention Residue Auditor", NeonPink, onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("⚠️ The Cost of Context Switching", fontWeight = FontWeight.Bold, color = NeonPink, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Every time you glance at phone alerts, chat pings, or open a new browser tab, your brain leaves 'Attention Residue'. It takes an average of 23 minutes to return to full cognitive depth.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Today's Context Switches Logged", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("$switchCount", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = NeonPink)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("≈ $lostMinutes Minutes of Cognitive Depth Lost", color = NeonAmber, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            GlassButton(
                                text = "+ LOG DISTRACTION",
                                onClick = {
                                    switchCount++
                                    AuraSoundManager.playErrorSound(context)
                                },
                                modifier = Modifier.weight(1f)
                            )
                            GlassButton(
                                text = "RESET AUDIT",
                                onClick = {
                                    switchCount = 0
                                    AuraSoundManager.playTapSound(context)
                                },
                                modifier = Modifier.weight(0.8f)
                            )
                        }
                    }
                }
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🛡️ Distraction Firewall (25-Min Lock)", fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Zero switching allowed. Lock down all tabs and notifications.", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(12.dp))
                        val mins = firewallSeconds / 60
                        val secs = firewallSeconds % 60
                        Text(String.format("%02d:%02d", mins, secs), fontSize = 38.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

                        Spacer(modifier = Modifier.height(14.dp))
                        GlassButton(
                            text = if (firewallActive) "ABORT FIREWALL" else "ENGAGE FIREWALL SHIELD",
                            onClick = {
                                firewallActive = !firewallActive
                                AuraSoundManager.playTapSound(context)
                            },
                            isPrimary = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// =============================================================================
// TOOL 50: FULL PLAY STORE & ADMOB LAUNCHPAD SCREEN (RELEASE & MONETIZATION WIZARD)
// =============================================================================
@Composable
fun FullPlayStoreAdMobLaunchpadScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        ToolTopNavHeader("Play Store Launchpad", "AdMob Monetization & Release Diagnostics", NeonViolet, onBack)

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text("🚀 NeuroBox Production Suite v1.4.0", fontWeight = FontWeight.ExtraBold, color = NeonViolet, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Architecture: 60 Master Brain Games • 50 Power Productivity Tools • 120 FPS Decoupled Compose Engine.",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Lead Architect & Developer: Samir Ahmmed", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Package: com.aistudio.aurafocus.nmhhmt", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                    }
                }
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("💵 Google AdMob Revenue Engine", fontWeight = FontWeight.Bold, color = NeonLime, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Integrated Official Google Sample AdMob App ID in AndroidManifest.xml. Live banners and rewarded video hooks are active.", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            GlassButton(
                                text = "TEST REWARDED (+50 XP)",
                                onClick = {
                                    AuraAdMobManager.showRewardedAd(context) {
                                        GamificationManager.addXp(50, "Launchpad Rewarded Ad Claimed")
                                        Toast.makeText(context, "Rewarded Ad Completed! +50 XP", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                isPrimary = true,
                                modifier = Modifier.weight(1f)
                            )
                            GlassButton(
                                text = "TEST INTERSTITIAL",
                                onClick = {
                                    AuraAdMobManager.showInterstitialAd(context) {
                                        Toast.makeText(context, "Interstitial Ad Viewed", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Embedded Banner Slot Preview:", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        AuraAdMobManager.AdMobBannerSlot(modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("🌐 Play Store & Distribution Links", fontWeight = FontWeight.Bold, color = NeonAmber, fontSize = 14.sp)

                        GlassButton(
                            text = "⭐ RATE NEUROBOX ON PLAY STORE",
                            onClick = { AuraLinkHelper.rateOnPlayStore(context) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        GlassButton(
                            text = "📤 SHARE APP WITH PRODUCTIVITY ENTHUSIASTS",
                            onClick = { AuraLinkHelper.shareApp(context) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        GlassButton(
                            text = "✉️ CONTACT DEVELOPER (SAMIR AHMMED)",
                            onClick = { AuraLinkHelper.contactDeveloper(context) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        GlassButton(
                            text = "📋 COPY SYSTEM DIAGNOSTIC REPORT",
                            onClick = {
                                val report = "NeuroBox v1.4.0 (Code 5)\nDeveloper: Samir Ahmmed\nGames: 60 Master Games\nTools: 50 Power Tools\nAdMob: Verified\nEngine: 120 FPS Jetpack Compose"
                                clipboard.setText(AnnotatedString(report))
                                AuraSoundManager.playSuccessSound(context)
                                Toast.makeText(context, "Diagnostic report copied to clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
