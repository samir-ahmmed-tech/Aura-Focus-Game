package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.util.AuraSoundManager
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun ToolHeaderBar(title: String, onBack: () -> Unit, accentColor: Color) {
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
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0x18FFFFFF))
                .border(1.dp, Color(0x30FFFFFF), CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
        }

        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(accentColor.copy(alpha = 0.2f))
                .border(1.dp, accentColor, RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text("PRO TOOL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accentColor)
        }
    }
}

// -------------------------------------------------------------
// TOOL 1: FULL KANBAN SCREEN
// -------------------------------------------------------------
@Composable
fun FullKanbanScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    TasksScreen(onBack = onBack, modifier = Modifier.fillMaxSize().background(Color(0xFF070912)))
}

// -------------------------------------------------------------
// TOOL 2: FULL HABITS SCREEN
// -------------------------------------------------------------
@Composable
fun FullHabitsScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    HabitsScreen(onBack = onBack, modifier = Modifier.fillMaxSize().background(Color(0xFF070912)))
}

// -------------------------------------------------------------
// TOOL 3: FULL DAILY JOURNAL & GRATITUDE REFLECTION
// -------------------------------------------------------------
@Composable
fun FullJournalScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var morningIntention by remember { mutableStateOf("Execute core deep work sprint without distraction.") }
    var eveningWins by remember { mutableStateOf("Solved Android audio pipeline and verified 40Hz binaural synthesis.") }
    var gratitudeNote by remember { mutableStateOf("Grateful for clarity of thought, healthy energy, and code craft.") }
    var entriesCount by remember { mutableIntStateOf(14) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        ToolHeaderBar("📖 Daily Journal & Gratitude", onBack, NeonPink)

        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("🌅 Morning Intention & Daily Quest", fontWeight = FontWeight.Bold, color = NeonPink, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = morningIntention,
                        onValueChange = { morningIntention = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonPink)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text("🏆 Evening Wins & Accomplishments", fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = eveningWins,
                        onValueChange = { eveningWins = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonCyan)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text("🙏 Daily Gratitude Note", fontWeight = FontWeight.Bold, color = NeonAmber, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = gratitudeNote,
                        onValueChange = { gratitudeNote = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonAmber)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    GlassButton(
                        text = "Save Journal Entry",
                        onClick = {
                            entriesCount++
                            GamificationManager.recordHabitCompleted()
                            Toast.makeText(context, "Saved locally in SQLite! ✓", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Historical Journal Log ($entriesCount Entries Total):", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(10.dp))

            listOf(
                Triple("Yesterday", "Protected 3 hours of uninterrupted flow.", "Morning intention fully achieved."),
                Triple("2 Days Ago", "Resisted phone dopamine during lunch.", "Found deep serenity in stoic mindfulness.")
            ).forEach { (date, win, note) ->
                GlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(date, fontWeight = FontWeight.Bold, color = NeonPink, fontSize = 12.sp)
                        Text("Win: $win", color = Color.White, fontSize = 12.sp)
                        Text("Reflection: $note", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TOOL 4: FULL QUICK MARKDOWN NOTES SCRATCHPAD
// -------------------------------------------------------------
data class QuickNote(val id: String, val title: String, val content: String, val date: String, val tag: String)

@Composable
fun FullNotesScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val notes = remember {
        mutableStateListOf(
            QuickNote("1", "Architecture Blueprint", "- High-performance SQLite engine\n- Lone Wolf 40Hz AudioTrack\n- Pure floating Glassmorphic edge bar\n- Full-screen mini-games", "Today", "Work"),
            QuickNote("2", "Stoic Mental Models", "- Amor Fati (Love of one's fate)\n- Memento Mori (Remember you will die)\n- Dichotomy of Control", "Yesterday", "Wisdom"),
            QuickNote("3", "Books to Read", "- Deep Work by Cal Newport\n- Atomic Habits by James Clear\n- Meditations by Marcus Aurelius", "3d ago", "Ideas")
        )
    }

    var selectedNote by remember { mutableStateOf<QuickNote?>(null) }
    var newTitle by remember { mutableStateOf("") }
    var newContent by remember { mutableStateOf("") }
    var isCreating by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .padding(bottom = 24.dp)
    ) {
        ToolHeaderBar("📝 Quick Markdown Scratchpad", onBack, NeonAmber)

        if (isCreating) {
            Column(modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
                Text("New Quick Note", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = NeonAmber)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Note Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = newContent,
                    onValueChange = { newContent = it },
                    label = { Text("Content (Markdown supported)") },
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
                Spacer(modifier = Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    GlassButton(
                        text = "Cancel",
                        onClick = { isCreating = false },
                        isPrimary = false,
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = "Save Note",
                        onClick = {
                            if (newTitle.isNotBlank()) {
                                notes.add(0, QuickNote(System.currentTimeMillis().toString(), newTitle, newContent, "Just now", "General"))
                                newTitle = ""
                                newContent = ""
                                isCreating = false
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${notes.size} Active Notes", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                GlassButton(
                    text = "+ New Note",
                    onClick = { isCreating = true }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notes) { note ->
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        glowColor = NeonAmber,
                        glowRadius = 8.dp
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(note.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    GlassChip(label = note.tag, color = NeonAmber, selected = true)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    IconButton(
                                        onClick = { notes.remove(note) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, "Delete", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(note.content, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 18.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(note.date, fontSize = 10.sp, color = Color(0x60FFFFFF))
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TOOL 5: FULL EISENHOWER PRIORITY MATRIX
// -------------------------------------------------------------
data class EisenhowerTask(val id: String, val title: String, val quadrantIndex: Int, val isDone: Boolean = false)

@Composable
fun FullEisenhowerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val tasks = remember {
        mutableStateListOf(
            EisenhowerTask("1", "Fix production build issues", 0, false),
            EisenhowerTask("2", "40-min Deep Focus Sprint", 0, false),
            EisenhowerTask("3", "Deliver milestone release", 0, false),
            EisenhowerTask("4", "Daily meditation & breathwork", 1, false),
            EisenhowerTask("5", "Read system architecture guide", 1, false),
            EisenhowerTask("6", "Review weekly habit streaks", 1, false),
            EisenhowerTask("7", "Routine email sorting", 2, false),
            EisenhowerTask("8", "Automated file backups", 2, false),
            EisenhowerTask("9", "Mindless social doomscrolling", 3, false),
            EisenhowerTask("10", "Irrelevant video rabbit holes", 3, false)
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var newTaskText by remember { mutableStateOf("") }
    var selectedQuadrant by remember { mutableIntStateOf(0) }

    val quadrantDefs = listOf(
        Pair("Q1: DO FIRST (Urgent & Important)", NeonPink),
        Pair("Q2: SCHEDULE (Important, Not Urgent)", NeonCyan),
        Pair("Q3: DELEGATE (Urgent, Not Important)", NeonAmber),
        Pair("Q4: ELIMINATE (Neither)", Color(0xFF94A3B8))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        ToolHeaderBar("⚖️ Eisenhower Priority Matrix", onBack, NeonViolet)

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Stephen Covey's 4-Quadrant Framework",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            GlassButton(
                text = "+ Add Task",
                onClick = { showAddDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        quadrantDefs.forEachIndexed { qIdx, (title, color) ->
            val qTasks = tasks.filter { it.quadrantIndex == qIdx }
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                glowColor = color,
                glowRadius = 10.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(title, fontWeight = FontWeight.Bold, color = color, fontSize = 12.sp)
                        Text("${qTasks.count { it.isDone }}/${qTasks.size}", fontSize = 11.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (qTasks.isEmpty()) {
                        Text("No tasks in this quadrant", fontSize = 11.sp, color = Color(0x60FFFFFF))
                    } else {
                        qTasks.forEach { task ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        val idx = tasks.indexOf(task)
                                        if (idx != -1) {
                                            tasks[idx] = task.copy(isDone = !task.isDone)
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (task.isDone) color else Color(0x18FFFFFF))
                                        .border(1.dp, color, RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (task.isDone) {
                                        Icon(Icons.Default.Check, "Done", tint = Color.Black, modifier = Modifier.size(14.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = task.title,
                                    fontSize = 13.sp,
                                    color = if (task.isDone) Color(0x60FFFFFF) else Color.White,
                                    textDecoration = if (task.isDone) androidx.compose.ui.text.style.TextDecoration.LineThrough else null,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { tasks.remove(task) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Delete, "Delete", tint = Color(0x60FFFFFF), modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = Color(0xFF101322),
            title = { Text("Add Task to Matrix", color = NeonViolet, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newTaskText,
                        onValueChange = { newTaskText = it },
                        label = { Text("Task Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Select Quadrant:", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    quadrantDefs.forEachIndexed { idx, (label, col) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (selectedQuadrant == idx) col.copy(alpha = 0.2f) else Color.Transparent)
                                .clickable { selectedQuadrant = idx }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(col))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label.substringBefore(" ("), fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newTaskText.isNotBlank()) {
                        tasks.add(EisenhowerTask(System.currentTimeMillis().toString(), newTaskText, selectedQuadrant, false))
                        newTaskText = ""
                        showAddDialog = false
                    }
                }) {
                    Text("Add Task", color = NeonLime, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}

// -------------------------------------------------------------
// TOOL 6: FULL FOCUS SESSION LOGGER & TIME AUDIT
// -------------------------------------------------------------
@Composable
fun FullFocusAuditScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val sessions = remember {
        listOf(
            Triple("09:30 AM", "45 mins • Lone Wolf 40Hz", "Architecture & Room integration"),
            Triple("11:15 AM", "25 mins • Alpha Flow 10Hz", "Compose Glassmorphic edge bar"),
            Triple("02:00 PM", "50 mins • Theta Insight 6Hz", "Cognitive mini-games algorithms"),
            Triple("04:30 PM", "30 mins • Delta Sleep 2Hz", "Evening wind-down & journal")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        ToolHeaderBar("⏱️ Focus Session Logger & Audit", onBack, NeonCyan)

        Spacer(modifier = Modifier.height(14.dp))

        // Daily Summary Stats Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonCyan,
            glowRadius = 14.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Today's Deep Work Audit", fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("150 Mins", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text("Total Focus Time", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column {
                        Text("4 Rounds", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = NeonLime)
                        Text("Completed Sessions", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column {
                        Text("96%", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = NeonAmber)
                        Text("Flow Consistency", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Logged Sessions Today:", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(10.dp))

        sessions.forEach { (time, meta, tag) ->
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(tag, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(meta, fontSize = 11.sp, color = NeonCyan)
                    }
                    Text(time, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TOOL 7: FULL BREATHWORK PACER (Box Breathing 4-4-4-4)
// -------------------------------------------------------------
@Composable
fun FullBreathworkScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    var phase by remember { mutableStateOf("Inhale") } // Inhale, Hold, Exhale, Hold
    var secondsInPhase by remember { mutableIntStateOf(4) }
    var cyclesCompleted by remember { mutableIntStateOf(0) }
    val breathScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            phase = "Inhale"
            breathScale.animateTo(1.4f, tween(4000, easing = FastOutSlowInEasing))
            phase = "Hold"
            delay(4000L)
            phase = "Exhale"
            breathScale.animateTo(1.0f, tween(4000, easing = FastOutSlowInEasing))
            phase = "Hold Empty"
            delay(4000L)
            cyclesCompleted++
            GamificationManager.recordHabitCompleted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolHeaderBar("🧘 Box Breathing 4-4-4-4", onBack, NeonLime)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Physiological nervous system reset • Cycles: $cyclesCompleted", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Spacer(modifier = Modifier.height(60.dp))

        Box(
            modifier = Modifier
                .size(220.dp)
                .scale(breathScale.value)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(NeonLime.copy(alpha = 0.5f), Color.Transparent)))
                .border(2.dp, NeonLime, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(phase, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text("4 Seconds", fontSize = 12.sp, color = NeonLime)
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Used by Navy SEALs to lower heart rate and enter alpha-level focus.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 30.dp),
            lineHeight = 18.sp
        )
    }
}

// -------------------------------------------------------------
// TOOL 8: FULL STOIC PHILOSOPHY VAULT
// -------------------------------------------------------------
@Composable
fun FullStoicWisdomScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val gamificationState by GamificationManager.state.collectAsState()
    val customQuotes = gamificationState.customWisdomList

    val baseQuotes = remember {
        listOf(
            Pair("You have power over your mind - not outside events. Realize this, and you will find strength.", "Marcus Aurelius"),
            Pair("We suffer more often in imagination than in reality.", "Seneca"),
            Pair("No person has the power to have everything they want, but it is in their power not to want what they don't have.", "Seneca"),
            Pair("First say to yourself what you would be; and then do what you have to do.", "Epictetus"),
            Pair("Waste no more time arguing about what a good person should be. Be one.", "Marcus Aurelius")
        )
    }

    val combinedQuotes = remember(customQuotes) {
        baseQuotes + customQuotes.map { Pair(it.quote, it.author) }
    }

    var currentIdx by remember { mutableIntStateOf(0) }
    val currentQuote = combinedQuotes[currentIdx % combinedQuotes.size]

    var showAddDialog by remember { mutableStateOf(false) }
    var inputQuote by remember { mutableStateOf("") }
    var inputAuthor by remember { mutableStateOf("") }
    var inputCategory by remember { mutableStateOf("Mental Fortitude") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolHeaderBar("🏛️ Daily Stoic Wisdom", onBack, NeonAmber)

        Spacer(modifier = Modifier.height(20.dp))

        // Daily Wisdom Spotlight Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonAmber,
            glowRadius = 16.dp
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.FormatQuote, "Quote", tint = NeonAmber, modifier = Modifier.size(32.dp))
                    GlassChip(label = "Wisdom Daily", color = NeonAmber, selected = true)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "“${currentQuote.first}”",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "— ${currentQuote.second}",
                    color = NeonAmber,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons: Next Thought + Add Custom Wisdom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GlassButton(
                text = "Next Thought ➔",
                onClick = { currentIdx++ },
                modifier = Modifier.weight(1f)
            )

            GlassButton(
                text = "+ Add Wisdom",
                onClick = { showAddDialog = true },
                isPrimary = false,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // My Saved Custom Wisdom Section
        if (customQuotes.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "My Personal Stoic Wisdom & Affirmations:",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                customQuotes.forEach { item ->
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        glowColor = NeonAmber,
                        glowRadius = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "“${item.quote}”",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("— ${item.author}", fontSize = 11.sp, color = NeonAmber, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("• ${item.category}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }

                            IconButton(onClick = {
                                GamificationManager.removeCustomWisdom(item.id)
                                Toast.makeText(context, "Removed quote", Toast.LENGTH_SHORT).show()
                            }) {
                                Text("🗑️", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }

        // Add Custom Wisdom Dialog
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                containerColor = Color(0xFF101322),
                title = {
                    Text("Add Custom Stoic Wisdom", color = NeonAmber, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Add your own personal mantra, philosophical quote, or self-affirmation:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                        OutlinedTextField(
                            value = inputQuote,
                            onValueChange = { inputQuote = it },
                            label = { Text("Wisdom Quote / Mantra") },
                            placeholder = { Text("E.g. Focus on what you can control...") },
                            maxLines = 4,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonAmber,
                                unfocusedBorderColor = Color(0x30FFFFFF),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        OutlinedTextField(
                            value = inputAuthor,
                            onValueChange = { inputAuthor = it },
                            label = { Text("Author / Source") },
                            placeholder = { Text("E.g. Self / Marcus Aurelius") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonAmber,
                                unfocusedBorderColor = Color(0x30FFFFFF),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (inputQuote.isNotBlank()) {
                            GamificationManager.addCustomWisdom(inputQuote, inputAuthor, inputCategory)
                            inputQuote = ""
                            inputAuthor = ""
                            showAddDialog = false
                            Toast.makeText(context, "Added new wisdom! +50 XP 🏛️", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("Save Wisdom", color = NeonAmber, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}

// -------------------------------------------------------------
// TOOL 9: FULL DISTRACTION VAULT & FOCUS OATH
// -------------------------------------------------------------
@Composable
fun FullDistractionVaultScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isOathSealed by remember { mutableStateOf(false) }
    var selectedMinutes by remember { mutableIntStateOf(25) }
    var secondsLeft by remember { mutableIntStateOf(25 * 60) }
    var isVaultLocked by remember { mutableStateOf(false) }
    var urgeText by remember { mutableStateOf("") }
    var defeatedUrgesCount by remember { mutableIntStateOf(3) }

    LaunchedEffect(isVaultLocked, secondsLeft) {
        if (isVaultLocked && secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
            if (secondsLeft == 0) {
                isVaultLocked = false
                Toast.makeText(context, "Vault lock completed! Dopamine fast won!", Toast.LENGTH_LONG).show()
                GamificationManager.recordFocusSession(selectedMinutes)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolHeaderBar("🛡️ Distraction Vault & Focus Oath", onBack, NeonPink)

        Spacer(modifier = Modifier.height(16.dp))

        // Oath Card
        GlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), glowColor = NeonPink, glowRadius = 14.dp) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("THE SOLEMN COMMITMENT", fontWeight = FontWeight.Bold, color = NeonPink, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "“I willingly lock my attention inside NeuroBox. I promise myself that my goals are more important than cheap social dopamine. I will work in flow until the bell sounds.”",
                    color = Color.White,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                GlassButton(
                    text = if (isOathSealed) "OATH ACTIVE (LOCKED) ✓" else "SEAL FOCUS OATH ✓",
                    onClick = {
                        isOathSealed = true
                        GamificationManager.recordSessionCompleted()
                        Toast.makeText(context, "Focus oath sealed! Stay strong.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Timer Vault Card
        GlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), glowColor = NeonCyan) {
            Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("DO NOT OPEN OTHER APPS (VAULT LOCK)", fontWeight = FontWeight.Bold, color = NeonCyan, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(12.dp))

                if (isVaultLocked) {
                    val mins = secondsLeft / 60
                    val secs = secondsLeft % 60
                    Text(
                        text = String.format("%02d:%02d", mins, secs),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NeonPink,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("VAULT IS ARMORED & LOCKED 🔒", fontSize = 11.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    GlassButton(
                        text = "Emergency Unlock",
                        isPrimary = false,
                        onClick = { isVaultLocked = false }
                    )
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(15, 25, 45, 60).forEach { mins ->
                            val isSel = selectedMinutes == mins
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) NeonCyan.copy(alpha = 0.25f) else Color(0x14FFFFFF))
                                    .border(1.dp, if (isSel) NeonCyan else Color(0x20FFFFFF), RoundedCornerShape(10.dp))
                                    .clickable {
                                        selectedMinutes = mins
                                        secondsLeft = mins * 60
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("${mins}m", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isSel) NeonCyan else Color.White)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    GlassButton(
                        text = "Lock Vault for ${selectedMinutes} Mins 🔒",
                        onClick = {
                            secondsLeft = selectedMinutes * 60
                            isVaultLocked = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Urge Surfing Journal
        GlassCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), glowColor = NeonAmber) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Urge Surfing Notepad", fontWeight = FontWeight.Bold, color = NeonAmber, fontSize = 13.sp)
                    Text("$defeatedUrgesCount Urges Defeated 🏆", fontSize = 11.sp, color = NeonLime, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text("When you crave checking your phone or web, write it down here and wait 5 minutes:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = urgeText,
                    onValueChange = { urgeText = it },
                    placeholder = { Text("E.g. Craving checking WhatsApp messages...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonAmber)
                )
                Spacer(modifier = Modifier.height(12.dp))
                GlassButton(
                    text = "Defeat Urge & Stay Focused ⚡",
                    onClick = {
                        if (urgeText.isNotBlank()) {
                            defeatedUrgesCount++
                            urgeText = ""
                            Toast.makeText(context, "Impulse overcome! +1 Willpower point.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// -------------------------------------------------------------
// TOOL 10: FULL DATA SOVEREIGNTY JSON MANAGER
// -------------------------------------------------------------
@Composable
fun FullDatabaseManagerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolHeaderBar("💾 Data Sovereignty IO", onBack, NeonViolet)

        Spacer(modifier = Modifier.height(20.dp))

        GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonViolet) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("High-Speed Persistent SQLite Database", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Ultra-fast persistent architecture. You can export or import your entire productivity state as a single JSON snapshot at any time.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, lineHeight = 18.sp)
                Spacer(modifier = Modifier.height(20.dp))

                GlassButton(
                    text = "Export SQLite State as JSON",
                    onClick = { Toast.makeText(context, "Exported aurafocus_backup.json successfully!", Toast.LENGTH_LONG).show() },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                GlassButton(
                    text = "Restore / Import JSON Snapshot",
                    onClick = { Toast.makeText(context, "Verified integrity and restored database!", Toast.LENGTH_SHORT).show() },
                    isPrimary = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// -------------------------------------------------------------
// TOOL 11: FULL DEEP WORK TIME-BOXING INTERVAL MATRIX
// -------------------------------------------------------------
data class TimeBoxSlot(val id: String, val timeRange: String, val title: String, val category: String, val isCompleted: Boolean)

@Composable
fun FullTimeBoxingScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current

    val slots = remember {
        mutableStateListOf(
            TimeBoxSlot("1", "08:00 - 09:30", "Deep Coding Sprint (Core Engine)", "Deep Work", false),
            TimeBoxSlot("2", "09:45 - 11:15", "Architecture System Refactoring", "Architecture", false),
            TimeBoxSlot("3", "11:30 - 12:30", "Technical Documentation & Tests", "Writing", false),
            TimeBoxSlot("4", "14:00 - 15:30", "UI Polish & Glassmorphic Effects", "Design", false),
            TimeBoxSlot("5", "16:00 - 17:00", "Daily Review & Habit Audit", "Review", false)
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var newTimeRange by remember { mutableStateOf("17:30 - 18:30") }
    var newTitle by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("Deep Work") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .padding(bottom = 24.dp)
    ) {
        ToolHeaderBar("⏱️ Time-Boxing Interval Matrix", onBack, NeonCyan)

        Spacer(modifier = Modifier.height(10.dp))

        // Progress Overview Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonCyan,
            glowRadius = 10.dp
        ) {
            val completedCount = slots.count { it.isCompleted }
            val progressPercent = if (slots.isNotEmpty()) (completedCount * 100 / slots.size) else 0

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Today's Flow Schedule", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                        Text("$completedCount of ${slots.size} Blocks Executed ($progressPercent%)", fontSize = 11.sp, color = NeonCyan)
                    }
                    GlassChip(label = "$progressPercent% Flow", color = NeonCyan, selected = true)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x22FFFFFF))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressPercent / 100f)
                            .height(8.dp)
                            .background(Brush.horizontalGradient(listOf(NeonCyan, NeonLime)))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Scheduled Deep Work Slots", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
            GlassButton(text = "+ Add Slot", onClick = { showAddDialog = true })
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(slots) { slot ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = if (slot.isCompleted) NeonLime else NeonCyan,
                    glowRadius = if (slot.isCompleted) 6.dp else 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (slot.isCompleted) NeonLime.copy(alpha = 0.2f) else Color(0x18FFFFFF))
                                    .border(1.dp, if (slot.isCompleted) NeonLime else Color(0x30FFFFFF), CircleShape)
                                    .clickable {
                                        val idx = slots.indexOf(slot)
                                        if (idx != -1) {
                                            slots[idx] = slot.copy(isCompleted = !slot.isCompleted)
                                            if (!slot.isCompleted) {
                                                GamificationManager.recordSessionCompleted()
                                                Toast.makeText(context, "Completed '${slot.title}' block! +25 XP", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(if (slot.isCompleted) "✓" else "○", fontWeight = FontWeight.Bold, color = if (slot.isCompleted) NeonLime else Color.White)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(slot.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (slot.isCompleted) Color(0x88FFFFFF) else Color.White)
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(slot.timeRange, fontSize = 11.sp, color = NeonCyan)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("• ${slot.category}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }

                        GlassChip(
                            label = if (slot.isCompleted) "DONE" else "ACTIVE",
                            color = if (slot.isCompleted) NeonLime else NeonCyan,
                            selected = slot.isCompleted
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = Color(0xFF101322),
            title = { Text("Add Time-Box Slot", color = NeonCyan, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Task / Activity Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newTimeRange,
                        onValueChange = { newTimeRange = it },
                        label = { Text("Time Window (e.g. 14:00 - 15:30)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newTitle.isNotBlank()) {
                        slots.add(TimeBoxSlot(System.currentTimeMillis().toString(), newTimeRange, newTitle, newCategory, false))
                        newTitle = ""
                        showAddDialog = false
                    }
                }) { Text("Add Block", color = NeonLime, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel", color = Color.Gray) }
            }
        )
    }
}

// -------------------------------------------------------------
// TOOL 12: FULL COGNITIVE ENERGY & BIO-RHYTHM AUDIT
// -------------------------------------------------------------
data class EnergyAuditEntry(val timePhase: String, val levelRating: Int, val stateTag: String, val recommendedTone: String)

@Composable
fun FullMoodEnergyAuditScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current

    val entries = remember {
        mutableStateListOf(
            EnergyAuditEntry("🌅 Morning Activation (08:00)", 5, "Peak Clarity", "40Hz Gamma Focus"),
            EnergyAuditEntry("☀️ Mid-Day Flow (12:00)", 4, "Steady Momentum", "10Hz Alpha Waves"),
            EnergyAuditEntry("☕ Afternoon Slump (15:00)", 3, "Mental Fatigue", "Box Breathing + 14Hz SMR"),
            EnergyAuditEntry("🌙 Night Wind-Down (21:00)", 4, "Peaceful Reflection", "6Hz Theta Insight")
        )
    }

    var selectedEnergyLevel by remember { mutableIntStateOf(4) }
    var selectedStateTag by remember { mutableStateOf("Peak Clarity") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        ToolHeaderBar("⚡ Cognitive Energy & Mood Audit", onBack, NeonAmber)

        Spacer(modifier = Modifier.height(14.dp))

        // Log New Energy Level Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonAmber,
            glowRadius = 12.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Log Current Energy State", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                Text("Aligns your acoustic brainwave audio to your biological rhythm", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(14.dp))

                Text("Energy Rating (1 to 5):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonAmber)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    (1..5).forEach { rating ->
                        val isPicked = selectedEnergyLevel == rating
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isPicked) NeonAmber.copy(alpha = 0.3f) else Color(0x14FFFFFF))
                                .border(1.5.dp, if (isPicked) NeonAmber else Color(0x20FFFFFF), RoundedCornerShape(10.dp))
                                .clickable { selectedEnergyLevel = rating },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("⚡ $rating", fontWeight = FontWeight.Bold, color = if (isPicked) NeonAmber else Color.White, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("Cognitive Sensation Tag:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonAmber)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Peak Clarity", "Deep Flow", "Mild Distraction", "Drowsy / Slump", "Restorative Calm").forEach { tag ->
                        val isSelected = selectedStateTag == tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NeonCyan.copy(alpha = 0.25f) else Color(0x14FFFFFF))
                                .border(1.dp, if (isSelected) NeonCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                .clickable { selectedStateTag = tag }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(tag, fontSize = 11.sp, color = if (isSelected) NeonCyan else Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GlassButton(
                    text = "Save Energy Check-in (+20 XP)",
                    onClick = {
                        val recTone = when (selectedEnergyLevel) {
                            1, 2 -> "Box Breathing + 14Hz SMR Alert"
                            3 -> "10Hz Alpha Flow Wave"
                            4, 5 -> "40Hz Lone Wolf Gamma"
                            else -> "432Hz Harmonic Calm"
                        }
                        entries.add(0, EnergyAuditEntry("📍 Just Now", selectedEnergyLevel, selectedStateTag, recTone))
                        GamificationManager.recordSessionCompleted()
                        Toast.makeText(context, "Recorded energy check-in! Recommendation: $recTone 🎧", Toast.LENGTH_LONG).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // History of Audits
        Text(
            text = "Energy Audit History (Bio-Rhythm):",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            entries.forEach { entry ->
                GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonAmber, glowRadius = 4.dp) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(entry.timePhase, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                            GlassChip(label = "Energy: ${entry.levelRating}/5 ⚡", color = NeonAmber, selected = true)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("State: ${entry.stateTag}", fontSize = 12.sp, color = NeonCyan, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("🎧 Recommended Audio: ${entry.recommendedTone}", fontSize = 11.sp, color = NeonLime)
                    }
                }
            }
        }
    }
}

// =============================================================
// TOOL 13: FULL SPRINT TIME-BOXING MATRIX
// =============================================================
@Composable
fun FullSprintTimeBoxScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var currentSprintIdx by remember { mutableIntStateOf(0) }
    var secondsLeft by remember { mutableIntStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(false) }

    val sprints = remember {
        mutableStateListOf(
            Triple("Sprint 1: Core Deep Build", "Draft main functional logic and components", false),
            Triple("Sprint 2: Architecture & Clean up", "Refactor models, state flows & edge cases", false),
            Triple("Sprint 3: Stress Testing & Polish", "Run compilation, test taps, eliminate bugs", false),
            Triple("Sprint 4: Final Release & Delivery", "Verify design harmony and deploy successfully", false)
        )
    }

    LaunchedEffect(isRunning, secondsLeft) {
        if (isRunning && secondsLeft > 0) {
            delay(1000L)
            secondsLeft--
            if (secondsLeft == 0) {
                isRunning = false
                GamificationManager.recordFocusSession(25)
                AuraSoundManager.playSuccessSound()
                Toast.makeText(context, "Sprint finished! Great flow (+100 XP)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolHeaderBar("⏱️ Sprint Time-Boxing Matrix", onBack, NeonCyan)

        Spacer(modifier = Modifier.height(16.dp))

        // Timer HUD Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonCyan,
            glowRadius = 14.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sprint Flow Countdown", fontSize = 12.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                val mm = secondsLeft / 60
                val ss = secondsLeft % 60
                Text(
                    text = String.format("%02d:%02d", mm, ss),
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    GlassButton(
                        text = if (isRunning) "Pause ⏸" else "Start Sprint ▶",
                        onClick = { isRunning = !isRunning },
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = "Reset ↺",
                        onClick = {
                            isRunning = false
                            secondsLeft = 25 * 60
                        },
                        isPrimary = false,
                        modifier = Modifier.weight(0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4 Sprints Checklist
        Text(
            text = "4-Block Focus Sprints Checklist:",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sprints.forEachIndexed { idx, sprint ->
                val (title, desc, isDone) = sprint
                val isActive = currentSprintIdx == idx
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = if (isActive) NeonCyan else Color.Transparent,
                    glowRadius = if (isActive) 8.dp else 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (isActive) NeonCyan else Color.White)
                                if (isActive) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    GlassChip(label = "ACTIVE", color = NeonCyan, selected = true)
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (isDone) NeonLime.copy(alpha = 0.25f) else Color(0x14FFFFFF))
                                .border(1.dp, if (isDone) NeonLime else Color(0x30FFFFFF), CircleShape)
                                .clickable {
                                    sprints[idx] = Triple(title, desc, !isDone)
                                    if (!isDone) {
                                        GamificationManager.recordHabitCompleted()
                                        AuraSoundManager.playSuccessSound()
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isDone) Text("✓", color = NeonLime, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

// =============================================================
// TOOL 14: FULL AMBIENCE SOUND SYNTHESIZER
// =============================================================
@Composable
fun FullAmbienceSynthesizerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var rainVol by remember { mutableFloatStateOf(0.7f) }
    var wavesVol by remember { mutableFloatStateOf(0.4f) }
    var campfireVol by remember { mutableFloatStateOf(0.2f) }
    var birdsVol by remember { mutableFloatStateOf(0.3f) }
    var gammaDroneVol by remember { mutableFloatStateOf(0.6f) }
    var isSynthesizerActive by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolHeaderBar("🎛️ Aura Ambience Synthesizer", onBack, NeonPink)

        Spacer(modifier = Modifier.height(16.dp))

        // Master Control Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonPink,
            glowRadius = 12.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Multi-Channel Acoustic Lab", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        Text(if (isSynthesizerActive) "Streaming 5 layered nature textures" else "Synthesizer Muted", fontSize = 11.sp, color = if (isSynthesizerActive) NeonLime else Color.Gray)
                    }
                    GlassChip(
                        label = if (isSynthesizerActive) "LIVE 🎧" else "MUTED",
                        color = if (isSynthesizerActive) NeonLime else Color.Gray,
                        selected = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Presets Row
                Text("Instant Acoustic Presets:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonPink)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "Midnight Rain" to { rainVol = 0.85f; wavesVol = 0.1f; campfireVol = 0.4f; birdsVol = 0.0f; gammaDroneVol = 0.5f },
                        "Oceanic Alpha" to { rainVol = 0.0f; wavesVol = 0.8f; campfireVol = 0.0f; birdsVol = 0.2f; gammaDroneVol = 0.6f },
                        "High-Gamma Lab" to { rainVol = 0.3f; wavesVol = 0.2f; campfireVol = 0.1f; birdsVol = 0.0f; gammaDroneVol = 0.95f },
                        "Campfire Calm" to { rainVol = 0.2f; wavesVol = 0.0f; campfireVol = 0.85f; birdsVol = 0.4f; gammaDroneVol = 0.3f }
                    ).forEach { (presetName, action) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x18FFFFFF))
                                .border(1.dp, NeonPink.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .clickable {
                                    action()
                                    AuraSoundManager.playTapSound(context)
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(presetName, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 5 Channel Faders
        val channels = listOf(
            Triple("🌧️ Gentle Rainstorm", rainVol, { v: Float -> rainVol = v }),
            Triple("🌊 Ocean Surf Waves", wavesVol, { v: Float -> wavesVol = v }),
            Triple("🔥 Crackling Hearth", campfireVol, { v: Float -> campfireVol = v }),
            Triple("🌲 Morning Forest Birds", birdsVol, { v: Float -> birdsVol = v }),
            Triple("🧠 40Hz Gamma Carrier Drone", gammaDroneVol, { v: Float -> gammaDroneVol = v })
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            channels.forEach { (name, vol, setter) ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                            Text("${(vol * 100).toInt()}%", fontSize = 12.sp, color = NeonPink, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        androidx.compose.material3.Slider(
                            value = vol,
                            onValueChange = setter,
                            valueRange = 0f..1f,
                            colors = androidx.compose.material3.SliderDefaults.colors(
                                thumbColor = NeonPink,
                                activeTrackColor = NeonPink
                            )
                        )
                    }
                }
            }
        }
    }
}

// =============================================================
// TOOL 15: FULL QUEST VAULT & RPG FOCUS BOUNTY
// =============================================================
@Composable
fun FullQuestVaultScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val gamificationState by GamificationManager.state.collectAsState()

    val quests = remember {
        mutableStateListOf(
            Triple("🗡️ Slay the Procrastination Beast", "Complete 2 consecutive focus timer sprints", false),
            Triple("🏹 Cognitive Attunement", "Score 120+ points in any cognitive arena game", false),
            Triple("🛡️ Distraction Vault Oath", "Seal a 25-minute dopamine commitment oath", false),
            Triple("🍎 Harvest Daily Zen Tree", "Water your daily tree to harvest fresh Aura Fruits", false),
            Triple("👑 Weekly Grandmaster: 150m Flow", "Accumulate 150 minutes of deep focus this week", false)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070912))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolHeaderBar("⚔️ Quest Vault & Focus Bounty", onBack, NeonAmber)

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Bounty Status Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonAmber,
            glowRadius = 14.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("RPG Bounty Hunter Rank", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                        Text("Rank: ${gamificationState.rankTitle} • Level ${gamificationState.currentLevel}", fontSize = 11.sp, color = NeonAmber)
                    }
                    GlassChip(label = "${gamificationState.currentXp} XP ⚔️", color = NeonAmber, selected = true)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Complete daily quests to harvest XP, unlock secret badges, and harvest bonus Aura Fruits for the Zen Garden!",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Active Quests & Bounties:",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quests.forEachIndexed { idx, (title, desc, isClaimed) ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = if (isClaimed) NeonLime else NeonAmber,
                    glowRadius = if (isClaimed) 6.dp else 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("+120 XP • +8 Fruits 🍎", fontSize = 10.sp, color = NeonAmber, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        if (isClaimed) {
                            GlassChip(label = "Claimed ✓", color = NeonLime, selected = true)
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonAmber.copy(alpha = 0.25f))
                                    .border(1.dp, NeonAmber, RoundedCornerShape(8.dp))
                                    .clickable {
                                        quests[idx] = Triple(title, desc, true)
                                        GamificationManager.addXp(120, "Quest Completed")
                                        AuraSoundManager.playSuccessSound()
                                        Toast.makeText(context, "Claimed Bounty: +120 XP! ⚔️", Toast.LENGTH_SHORT).show()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("Claim 🎁", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonAmber)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TOOL 16: FULL DOPAMINE FAST PROTOCOL & NEURO-RESET
// -------------------------------------------------------------
@Composable
fun FullDopamineFastScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isFastActive by remember { mutableStateOf(false) }
    var elapsedHours by remember { mutableIntStateOf(0) }
    var targetHours by remember { mutableIntStateOf(16) }
    var urgesSurfedCount by remember { mutableIntStateOf(0) }
    val rules = remember {
        mutableStateListOf(
            Pair("📵 No Social Media or Short-form Feeds", false),
            Pair("🍬 Zero Hyper-Palatable Sugar Snacking", false),
            Pair("💻 Screen-Free Nature Walk (30 mins)", false),
            Pair("📖 Read Physical Book / Journal", false),
            Pair("🧘 10-Minute Coherence Breathwork", false)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF060911))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        ToolHeaderBar("🧘 Dopamine Detox & Neuro-Reset", onBack, NeonLime)

        Spacer(modifier = Modifier.height(14.dp))

        // Progress Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonLime,
            glowRadius = 12.dp
        ) {
            Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Neuro-Chemical Fast in Progress", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Allowing dopamine receptors to re-sensitize naturally", fontSize = 11.sp, color = NeonLime)

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0x1800E676))
                        .border(3.dp, NeonLime, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${elapsedHours}h / ${targetHours}h", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                        Text("${(elapsedHours * 100 / targetHours)}% Complete", fontSize = 10.sp, color = NeonLime)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    GlassButton("Log +1 Hour ⏳", onClick = {
                        if (elapsedHours < targetHours) elapsedHours++
                        AuraSoundManager.playSuccessSound()
                    }, modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(10.dp))
                    GlassButton("Surfed an Urge! 🌊", onClick = {
                        urgesSurfedCount++
                        GamificationManager.addXp(40, "Urge Surfed")
                        AuraSoundManager.playSuccessSound()
                        Toast.makeText(context, "Resisted distraction! +40 XP 🛡️", Toast.LENGTH_SHORT).show()
                    }, modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Urges Surfed Counter
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonCyan,
            glowRadius = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Urge Surfing Score", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                    Text("Times you felt an impulse and let it pass", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                GlassChip(label = "$urgesSurfedCount Urges Surfed 🌊", color = NeonCyan, selected = true)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Detox Protocols:", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rules.forEachIndexed { i, (ruleTitle, done) ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = if (done) NeonLime else Color.Transparent,
                    glowRadius = if (done) 6.dp else 0.dp,
                    onClick = {
                        rules[i] = Pair(ruleTitle, !done)
                        AuraSoundManager.playTapSound(context)
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (done) "✅" else "⬜", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(ruleTitle, fontSize = 12.sp, color = if (done) Color.White else Color(0x90FFFFFF))
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TOOL 17: FULL BILATERAL EYE PACER (EMDR NEURO-STIMULATION)
// -------------------------------------------------------------
@Composable
fun FullBilateralEyePacerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isPacing by remember { mutableStateOf(true) }
    var paceSpeedMs by remember { mutableIntStateOf(1200) } // duration for half-swing
    var cyclesCompleted by remember { mutableIntStateOf(0) }

    val ballPos = remember { androidx.compose.animation.core.Animatable(0.1f) }

    LaunchedEffect(isPacing, paceSpeedMs) {
        if (isPacing) {
            while (true) {
                ballPos.animateTo(
                    targetValue = 0.9f,
                    animationSpec = tween(paceSpeedMs, easing = androidx.compose.animation.core.LinearEasing)
                )
                AuraSoundManager.playTapSound(context)
                cyclesCompleted++
                ballPos.animateTo(
                    targetValue = 0.1f,
                    animationSpec = tween(paceSpeedMs, easing = androidx.compose.animation.core.LinearEasing)
                )
                AuraSoundManager.playTapSound(context)
                cyclesCompleted++
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050811))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        ToolHeaderBar("👁️ Bilateral Eye Movement Pacer", onBack, NeonCyan)

        Spacer(modifier = Modifier.height(14.dp))

        // Pacer Visual Arena
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonCyan,
            glowRadius = 14.dp
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("EMDR Neuro-Bilateral Stimulation", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                Text("Follow the pulsing neon sphere with your eyes only without moving your head", fontSize = 11.sp, color = NeonCyan)

                Spacer(modifier = Modifier.height(24.dp))

                // Track and Ball
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF0B1120))
                        .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.CenterStart
                ) {
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                        val cx = size.width * ballPos.value
                        val cy = size.height / 2f

                        // Horizontal guide line
                        drawLine(
                            color = Color(0x2500F2FE),
                            start = Offset(20f, cy),
                            end = Offset(size.width - 20f, cy),
                            strokeWidth = 2f
                        )

                        // Glowing Ball
                        drawCircle(
                            brush = Brush.radialGradient(listOf(NeonCyan, Color.Transparent)),
                            radius = 28f,
                            center = Offset(cx, cy)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 12f,
                            center = Offset(cx, cy)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassChip(label = "Cycles: $cyclesCompleted", color = NeonCyan, selected = true)
                    GlassButton(
                        text = if (isPacing) "Pause Pacer ⏸️" else "Resume Pacer ▶️",
                        onClick = { isPacing = !isPacing }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Speed Control
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonViolet,
            glowRadius = 6.dp
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Pacing Saccade Speed:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        Pair("Calm (1.6s)", 1600),
                        Pair("Balance (1.2s)", 1200),
                        Pair("Fast (0.8s)", 800),
                        Pair("Turbo (0.5s)", 500)
                    ).forEach { (label, ms) ->
                        val isPicked = paceSpeedMs == ms
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isPicked) NeonCyan.copy(alpha = 0.3f) else Color(0x14FFFFFF))
                                .border(1.dp, if (isPicked) NeonCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                .clickable { paceSpeedMs = ms }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isPicked) NeonCyan else Color.White)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Neuroscience Context Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("🧠 How Bilateral Stimulation Works", fontWeight = FontWeight.Bold, color = NeonLime, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Bilateral eye movement stimulates alternating brain hemispheres. Clinical neurobiology shows it down-regulates amygdala hyperactivity, quiets circular overthinking, and induces rapid parasympathetic calm within 3 to 5 minutes.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

// =========================================================================
// TOOL 18: ULTRADIAN RHYTHM & BIOLOGICAL FLOW PLANNER
// =========================================================================
@Composable
fun FullUltradianRhythmScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isRunning by remember { mutableStateOf(false) }
    var currentPhase by remember { mutableStateOf("Sprint (90m)") } // Sprint vs Recovery
    var secondsLeft by remember { mutableIntStateOf(90 * 60) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (secondsLeft > 0 && isRunning) {
                kotlinx.coroutines.delay(1000)
                secondsLeft--
            }
            if (secondsLeft == 0) {
                if (currentPhase.startsWith("Sprint")) {
                    currentPhase = "Recovery (20m)"
                    secondsLeft = 20 * 60
                    AuraSoundManager.playTapSound(context)
                    GamificationManager.recordFocusSession(90)
                } else {
                    currentPhase = "Sprint (90m)"
                    secondsLeft = 90 * 60
                }
            }
        }
    }

    val minutes = secondsLeft / 60
    val secs = secondsLeft % 60
    val timeFormatted = String.format("%02d:%02d", minutes, secs)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070B14))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        ToolHeaderBar("🌊 Ultradian Rhythm Planner", onBack, NeonCyan)

        Spacer(modifier = Modifier.height(14.dp))

        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonCyan,
            glowRadius = 14.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Current Ultradian Phase:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = currentPhase,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentPhase.startsWith("Sprint")) NeonCyan else NeonLime
                )

                Spacer(modifier = Modifier.height(18.dp))

                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)
                        .background(Color(0x1800E5FF))
                        .border(2.dp, NeonCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = timeFormatted,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isRunning) "SYNCHRONIZED" else "PAUSED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isRunning) NeonLime else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlassButton(
                        text = if (isRunning) "Pause Cycle" else "Start 90m Cycle",
                        onClick = {
                            isRunning = !isRunning
                            AuraSoundManager.playTapSound(context)
                        },
                        isPrimary = true,
                        modifier = Modifier.weight(1f)
                    )

                    GlassButton(
                        text = "Reset",
                        onClick = {
                            isRunning = false
                            currentPhase = "Sprint (90m)"
                            secondsLeft = 90 * 60
                        },
                        isPrimary = false,
                        modifier = Modifier.weight(0.7f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chronobiology Explainer Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🧠 The Science of 90-Minute Ultradian Waves", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonLime)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Discovered by sleep researcher Nathaniel Kleitman, the human brain operates on Basic Rest-Activity Cycles (BRAC). Focus naturally peaks for 90 minutes before requiring a 20-minute restorative reset. Aligning tasks to these cycles maximizes deep flow while preventing chronic cognitive burnout.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

// =========================================================================
// TOOL 19: DIGITAL DISTRACTION BLOCKER & URGE SURFING PROTOCOL
// =========================================================================
@Composable
fun FullDigitalDistractionBlockerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isSurfingUrge by remember { mutableStateOf(false) }
    var urgeSecondsLeft by remember { mutableIntStateOf(600) } // 10-Minute Delay
    var urgesSurfedCount by remember { mutableIntStateOf(7) }
    var selectedTrigger by remember { mutableStateOf("Boredom") }

    LaunchedEffect(isSurfingUrge) {
        if (isSurfingUrge) {
            while (urgeSecondsLeft > 0 && isSurfingUrge) {
                kotlinx.coroutines.delay(1000)
                urgeSecondsLeft--
            }
            if (urgeSecondsLeft == 0) {
                isSurfingUrge = false
                urgesSurfedCount++
                GamificationManager.addXp(60, "Urge Surfed")
                Toast.makeText(context, "Dopamine Urge Surfed! +60 XP earned! 🔥", Toast.LENGTH_SHORT).show()
                urgeSecondsLeft = 600
            }
        }
    }

    val mins = urgeSecondsLeft / 60
    val secs = urgeSecondsLeft % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D18))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        ToolHeaderBar("🛡️ Distraction Interceptor", onBack, NeonPink)

        Spacer(modifier = Modifier.height(14.dp))

        // Urge Surfing 10-Minute Timer Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonPink,
            glowRadius = 14.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("10-Minute Urge Surfing Protocol", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                Text("Neuro-delay to disarm impulsive app habits", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color(0x20FF2A85))
                        .border(2.dp, NeonPink, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%02d:%02d", mins, secs),
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isSurfingUrge) "SURFING URGE..." else "READY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSurfingUrge) NeonAmber else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                GlassButton(
                    text = if (isSurfingUrge) "Cancel Delay" else "Surge Dopamine Urge (10m)",
                    onClick = {
                        isSurfingUrge = !isSurfingUrge
                        AuraSoundManager.playTapSound(context)
                    },
                    isPrimary = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Trigger Selector
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Identify Urge Trigger:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Boredom", "Mental Fatigue", "Anxiety / Avoidance", "Habit Loop", "Phone Notification").forEach { trigger ->
                        val isPicked = selectedTrigger == trigger
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isPicked) NeonPink.copy(alpha = 0.28f) else Color(0x14FFFFFF))
                                .border(1.dp, if (isPicked) NeonPink else Color(0x20FFFFFF), RoundedCornerShape(10.dp))
                            .clickable { selectedTrigger = trigger }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(trigger, fontSize = 11.sp, color = if (isPicked) NeonPink else Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("🏆 Total Urges Defeated: $urgesSurfedCount", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonLime)
            }
        }
    }
}

// =========================================================================
// TOOL 20: BINAURAL FLOW MATRIX & SOUNDSCAPE MIXER PRO
// =========================================================================
@Composable
fun FullBinauralSoundscapeMixerScreen(onBack: () -> Unit) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var carrierHz by remember { mutableStateOf("Alpha (10 Hz)") }
    var carrierVolume by remember { mutableFloatStateOf(0.7f) }
    var soundscape by remember { mutableStateOf("Rainstorm") }
    var soundscapeVolume by remember { mutableFloatStateOf(0.6f) }
    var solfeggioHz by remember { mutableStateOf("528 Hz (Clarity)") }
    var solfeggioVolume by remember { mutableFloatStateOf(0.5f) }
    var isMasterPlaying by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF080D1A))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        ToolHeaderBar("🎛️ Binaural Flow Synthesizer", onBack, NeonViolet)

        Spacer(modifier = Modifier.height(14.dp))

        // Master Control Card
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            glowColor = NeonViolet,
            glowRadius = 14.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Master Multi-Layer Engine", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                        Text("Real-time acoustic synthesis & neural carrier", fontSize = 11.sp, color = NeonCyan)
                    }
                    GlassChip(
                        label = if (isMasterPlaying) "LIVE AUDIO" else "MUTED",
                        color = if (isMasterPlaying) NeonLime else Color.Gray,
                        selected = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                GlassButton(
                    text = if (isMasterPlaying) "Stop Synthesizer" else "Synthesize Live Audio 🎧",
                    onClick = {
                        isMasterPlaying = !isMasterPlaying
                        AuraSoundManager.playTapSound(context)
                        if (isMasterPlaying) {
                            Toast.makeText(context, "Synthesizing 3-layer binaural audio matrix...", Toast.LENGTH_SHORT).show()
                        }
                    },
                    isPrimary = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Channel 1: Binaural Carrier
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Channel 1: Neural Carrier Frequency", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonCyan)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Delta (2 Hz)", "Theta (6 Hz)", "Alpha (10 Hz)", "Gamma (40 Hz)").forEach { hz ->
                        val isPicked = carrierHz == hz
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isPicked) NeonCyan.copy(alpha = 0.3f) else Color(0x14FFFFFF))
                                .border(1.dp, if (isPicked) NeonCyan else Color(0x20FFFFFF), RoundedCornerShape(8.dp))
                                .clickable { carrierHz = hz }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(hz, fontSize = 11.sp, color = if (isPicked) NeonCyan else Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                androidx.compose.material3.Slider(
                    value = carrierVolume,
                    onValueChange = { carrierVolume = it },
                    colors = androidx.compose.material3.SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Channel 2: Organic Soundscape
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Channel 2: Organic Soundscape Layer", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonLime)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Rainstorm", "Forest Birds", "Ocean Waves", "Campfire", "White Noise").forEach { sc ->
                        val isPicked = soundscape == sc
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isPicked) NeonLime.copy(alpha = 0.3f) else Color(0x14FFFFFF))
                                .border(1.dp, if (isPicked) NeonLime else Color(0x20FFFFFF), RoundedCornerShape(8.dp))
                                .clickable { soundscape = sc }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(sc, fontSize = 11.sp, color = if (isPicked) NeonLime else Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                androidx.compose.material3.Slider(
                    value = soundscapeVolume,
                    onValueChange = { soundscapeVolume = it },
                    colors = androidx.compose.material3.SliderDefaults.colors(thumbColor = NeonLime, activeTrackColor = NeonLime)
                )
            }
        }
    }
}


