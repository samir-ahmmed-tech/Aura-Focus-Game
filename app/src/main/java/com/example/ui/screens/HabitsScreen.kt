package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GamificationManager
import com.example.data.DailyMoodCheckIn
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassChip
import com.example.ui.components.GlassDialog
import com.example.ui.components.GlassLinearProgressBar
import com.example.ui.components.GlassSlider
import com.example.ui.components.GlassTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet

enum class HabitTimeOfDay(val label: String, val icon: String) {
    ALL("All Day", "🌟"),
    MORNING("Morning", "🌅"),
    AFTERNOON("Afternoon", "☀️"),
    EVENING("Evening", "🌆"),
    NIGHT("Night", "🌙")
}

data class AdvancedHabitItem(
    val id: String,
    val title: String,
    val category: String,
    val timeOfDay: HabitTimeOfDay,
    val streakDays: Int,
    val bestStreak: Int,
    val targetDaysPerWeek: Int,
    val difficulty: String, // Easy, Medium, Hard
    val completedToday: Boolean,
    val streakShieldActive: Boolean = false,
    val completionNote: String = "",
    val totalCheckins: Int = 0,
    val accentColor: Color
)

@Composable
fun HabitsScreen(
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (onBack != null) {
        androidx.activity.compose.BackHandler { onBack() }
    }
    val context = LocalContext.current
    val gamificationState by GamificationManager.state.collectAsState()
    val todayDateStr = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) }
    val isCheckedInToday = gamificationState.lastMoodCheckInDate == todayDateStr
    var showMoodCheckInDialog by remember { mutableStateOf(!isCheckedInToday) }
    var selectedFilter by remember { mutableStateOf(HabitTimeOfDay.ALL) }
    var streakFreezesRemaining by remember { mutableIntStateOf(0) }

    val habits = remember {
        mutableStateListOf(
            AdvancedHabitItem("1", "4 Focus Sessions Daily", "Deep Work", HabitTimeOfDay.MORNING, 0, 0, 7, "Hard", false, false, "", 0, NeonCyan),
            AdvancedHabitItem("2", "Read Architecture & Tech Docs", "Knowledge", HabitTimeOfDay.AFTERNOON, 0, 0, 5, "Medium", false, false, "", 0, NeonViolet),
            AdvancedHabitItem("3", "Hydrate & Stand Every Hour", "Wellness", HabitTimeOfDay.ALL, 0, 0, 7, "Easy", false, false, "", 0, NeonLime),
            AdvancedHabitItem("4", "No Social Media Before Noon", "Mindfulness", HabitTimeOfDay.MORNING, 0, 0, 7, "Medium", false, false, "", 0, NeonPink),
            AdvancedHabitItem("5", "Evening Stoic Reflection Journal", "Mindfulness", HabitTimeOfDay.EVENING, 0, 0, 7, "Easy", false, false, "", 0, NeonAmber),
            AdvancedHabitItem("6", "30-Min Cardio / Cold Shower", "Fitness", HabitTimeOfDay.MORNING, 0, 0, 5, "Hard", false, false, "", 0, NeonLime)
        )
    }

    var showAddHabitDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Header with Streak Shields & Milestone
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onBack != null) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0x18FFFFFF))
                            .border(1.dp, Color(0x30FFFFFF), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Column {
                    Text(
                        text = "Habit Mastery & Streaks",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "20+ Pro Habit Systems • Heatmap",
                        style = MaterialTheme.typography.bodySmall,
                        color = NeonCyan
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Streak Freeze Shield Indicator
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeonCyan.copy(alpha = 0.2f))
                        .border(1.dp, NeonCyan, RoundedCornerShape(12.dp))
                        .clickable {
                            Toast.makeText(context, "$streakFreezesRemaining Streak Shields active (Streak protected!)", Toast.LENGTH_SHORT).show()
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, "Shield", tint = NeonCyan, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("$streakFreezesRemaining Shields", fontSize = 11.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // DAILY MORNING MOOD & PRODUCTIVITY CHECK-IN BANNER
        val todayMood = gamificationState.moodCheckInHistory.firstOrNull { it.date == todayDateStr }
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = if (isCheckedInToday) NeonLime else NeonCyan,
            glowRadius = 12.dp,
            onClick = { showMoodCheckInDialog = true }
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isCheckedInToday) NeonLime.copy(alpha = 0.25f) else NeonCyan.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(todayMood?.moodEmoji ?: "🌅", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isCheckedInToday) "Morning Mindset Logged ✓" else "Daily Morning Check-in Pending",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = if (isCheckedInToday) "Mood: ${todayMood?.moodLabel} • Energy: ${todayMood?.energyLevel}/10" else "Set mindset & earn +60 XP streak reward",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = if (isCheckedInToday) NeonLime else NeonCyan
                            )
                        }
                    }

                    GlassChip(
                        label = if (isCheckedInToday) "Edit ✏️" else "Check In ⚡",
                        color = if (isCheckedInToday) NeonLime else NeonCyan,
                        selected = true
                    )
                }

                if (todayMood != null && todayMood.primaryIntent.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "🎯 Today's Target: “${todayMood.primaryIntent}” (${todayMood.plannedFocusMinutes}m deep work)",
                        fontSize = 11.sp,
                        color = Color(0xCCFFFFFF)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // FEATURE #1: 16-Week GitHub-Style Contribution Heatmap
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = NeonCyan,
            glowRadius = 12.dp,
            testTag = "habit_heatmap_card"
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "16-Week Consistency Matrix",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "🔥 21 Days Best Streak",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = NeonAmber
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    Canvas(modifier = Modifier.size(width = 380.dp, height = 84.dp)) {
                        val cellSize = 12.dp.toPx()
                        val cellGap = 3.dp.toPx()
                        val cols = 18
                        val rows = 5

                        for (c in 0 until cols) {
                            for (r in 0 until rows) {
                                val x = c * (cellSize + cellGap)
                                val y = r * (cellSize + cellGap)
                                val intensity = ((c * 4 + r * 6) % 5)
                                val color = when (intensity) {
                                    0 -> Color(0x18FFFFFF)
                                    1 -> Color(0x3300F2FE)
                                    2 -> Color(0x6600F2FE)
                                    3 -> Color(0xAA00F2FE)
                                    else -> NeonCyan
                                }

                                drawRoundRect(
                                    color = color,
                                    topLeft = Offset(x, y),
                                    size = Size(cellSize, cellSize),
                                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // FEATURE #5 & 12: Time-of-Day Filter Pills & Batch Action
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HabitTimeOfDay.values().forEach { tod ->
                val isSelected = selectedFilter == tod
                GlassChip(
                    label = "${tod.icon} ${tod.label}",
                    color = if (isSelected) NeonCyan else Color(0x40FFFFFF),
                    selected = isSelected,
                    onClick = { selectedFilter = tod }
                )
            }

            // Batch Action: Complete all morning habits
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(NeonLime.copy(alpha = 0.2f))
                    .border(1.dp, NeonLime, RoundedCornerShape(16.dp))
                    .clickable {
                        habits.forEachIndexed { i, h ->
                            if (h.timeOfDay == HabitTimeOfDay.MORNING && !h.completedToday) {
                                habits[i] = h.copy(completedToday = true, streakDays = h.streakDays + 1)
                                GamificationManager.recordHabitCompleted()
                            }
                        }
                        Toast.makeText(context, "All Morning habits checked! ✓", Toast.LENGTH_SHORT).show()
                    }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DoneAll, "Batch", tint = NeonLime, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Check Morning", fontSize = 11.sp, color = NeonLime, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Filtered Habits List
        val filteredHabits = habits.filter {
            selectedFilter == HabitTimeOfDay.ALL || it.timeOfDay == selectedFilter
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredHabits, key = { it.id }) { habit ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = habit.accentColor,
                    glowRadius = 10.dp,
                    testTag = "habit_card_${habit.id}"
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                        .background(habit.accentColor.copy(alpha = 0.2f))
                                        .border(1.dp, habit.accentColor.copy(alpha = 0.5f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${habit.streakDays}d",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = habit.accentColor
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = habit.title,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${habit.timeOfDay.icon} ${habit.category} • ${habit.difficulty}",
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                            color = habit.accentColor
                                        )
                                        if (habit.streakShieldActive) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(Icons.Default.Shield, "Shield", tint = NeonCyan, modifier = Modifier.size(12.dp))
                                        }
                                    }
                                }
                            }

                            // Check-in toggle with XP trigger
                            IconButton(
                                onClick = {
                                    val idx = habits.indexOf(habit)
                                    if (idx >= 0) {
                                        val newDone = !habit.completedToday
                                        val newStreak = if (newDone) habit.streakDays + 1 else maxOf(0, habit.streakDays - 1)
                                        if (newDone) {
                                            GamificationManager.recordHabitCompleted()
                                        }
                                        habits[idx] = habit.copy(
                                            completedToday = newDone,
                                            streakDays = newStreak,
                                            totalCheckins = if (newDone) habit.totalCheckins + 1 else habit.totalCheckins
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (habit.completedToday) NeonLime.copy(alpha = 0.25f) else Color(0x14FFFFFF))
                                    .border(1.dp, if (habit.completedToday) NeonLime else Color(0x25FFFFFF), CircleShape)
                                    .testTag("habit_checkin_btn_${habit.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Check-in",
                                    tint = if (habit.completedToday) NeonLime else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Feature #11: Reflection note
                        if (habit.completionNote.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "💬 “${habit.completionNote}”",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Best: ${habit.bestStreak}d • Total: ${habit.totalCheckins} check-ins", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Daily Habit Streak", fontSize = 10.sp, color = NeonLime, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Daily Mood & Productivity Check-in Dialog
        if (showMoodCheckInDialog) {
            DailyMoodCheckInDialog(
                onDismiss = { showMoodCheckInDialog = false },
                onCheckInComplete = {
                    showMoodCheckInDialog = false
                    Toast.makeText(context, "Morning check-in saved! +60 XP earned ⚡", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
fun DailyMoodCheckInDialog(
    onDismiss: () -> Unit,
    onCheckInComplete: () -> Unit
) {
    var selectedMoodIndex by remember { mutableIntStateOf(0) }
    var energyLevel by remember { mutableFloatStateOf(8f) }
    var primaryIntent by remember { mutableStateOf("Deep flow work on core cognitive tasks") }
    var plannedMinutes by remember { mutableIntStateOf(120) }

    val moodOptions = listOf(
        Triple("⚡", "High Energy", NeonCyan),
        Triple("🧘", "Deep Focus", NeonViolet),
        Triple("🌊", "Calm Zen", NeonLime),
        Triple("😴", "Rest / Recharge", NeonAmber),
        Triple("🔥", "Unstoppable Drive", NeonPink)
    )

    GlassDialog(
        onDismissRequest = onDismiss,
        title = "🌅 Morning Mood & Productivity Check-in",
        glowColor = NeonCyan,
        confirmButton = {
            GlassButton(
                text = "Log Mindset (+60 XP)",
                onClick = {
                    val mood = moodOptions[selectedMoodIndex]
                    GamificationManager.recordDailyCheckIn(
                        moodEmoji = mood.first,
                        moodLabel = mood.second,
                        energyLevel = energyLevel.toInt(),
                        primaryIntent = primaryIntent.trim(),
                        plannedFocusMinutes = plannedMinutes
                    )
                    onCheckInComplete()
                },
                isPrimary = true
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Later", color = Color(0x88FFFFFF))
            }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            // 1. Mood Selector
            Text("How is your mind feeling this morning?", fontSize = 12.sp, color = Color(0xCCFFFFFF), fontWeight = FontWeight.SemiBold)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                moodOptions.forEachIndexed { index, (emoji, label, color) ->
                    val isSelected = selectedMoodIndex == index
                    GlassChip(
                        label = "$emoji $label",
                        color = color,
                        selected = isSelected,
                        onClick = { selectedMoodIndex = index }
                    )
                }
            }

            // 2. Energy Level Slider (1 - 10)
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Bio-Energy Level:", fontSize = 12.sp, color = Color(0xCCFFFFFF))
                    Text("${energyLevel.toInt()} / 10 ⚡", fontSize = 12.sp, color = NeonLime, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                GlassSlider(
                    value = energyLevel,
                    onValueChange = { energyLevel = it },
                    valueRange = 1f..10f,
                    activeColor = NeonLime
                )
            }

            // 3. Primary Morning Focus Intent
            Column {
                Text("Today's Core Intention / Focus Goal:", fontSize = 12.sp, color = Color(0xCCFFFFFF))
                Spacer(modifier = Modifier.height(6.dp))
                GlassTextField(
                    value = primaryIntent,
                    onValueChange = { primaryIntent = it },
                    placeholder = "e.g. Complete math sprints and stay in deep flow..."
                )
            }

            // 4. Target Deep Work Minutes
            Column {
                Text("Target Focus Time Today:", fontSize = 12.sp, color = Color(0xCCFFFFFF))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(60, 90, 120, 180, 240).forEach { mins ->
                        val isSelected = plannedMinutes == mins
                        GlassChip(
                            label = "${mins}m",
                            color = NeonCyan,
                            selected = isSelected,
                            onClick = { plannedMinutes = mins }
                        )
                    }
                }
            }
        }
    }
}

