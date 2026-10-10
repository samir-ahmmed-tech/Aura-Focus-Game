package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Grid4x4
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.ViewKanban
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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

import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton

data class ProductivityTool(
    val id: String,
    val title: String,
    val desc: String,
    val iconEmoji: String,
    val badge: String,
    val accentColor: Color
)

@Composable
fun ToolsScreen(
    onLaunchTool: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val tools = remember {
        listOf(
            ProductivityTool("t1", "Smart Kanban Boards", "4-lane agile workflow board", "📋", "Core", NeonCyan),
            ProductivityTool("t2", "Deep Habit Tracker", "16-week GitHub heatmap & streaks", "📈", "Streaks", NeonLime),
            ProductivityTool("t3", "Daily Journal & Gratitude", "Morning intentions & evening reflection", "📖", "Reflect", NeonPink),
            ProductivityTool("t4", "Quick Markdown Notes", "Instant idea scratchpad", "📝", "Notes", NeonAmber),
            ProductivityTool("t5", "Eisenhower Matrix", "4-quadrant urgent/important organizer", "⚖️", "Priority", NeonViolet),
            ProductivityTool("t6", "Focus Session Logger", "Daily deep work logs & time audit", "⏱️", "Analytics", NeonCyan),
            ProductivityTool("t7", "Breathwork Pacer", "Box breathing 4-4-4-4 mental coherence", "🧘", "Mindful", NeonLime),
            ProductivityTool("t8", "Daily Stoic Philosophy", "Quotes from Marcus Aurelius & Seneca", "🏛️", "Wisdom", NeonAmber),
            ProductivityTool("t9", "Distraction Vault", "Focus oath & commitment lock device", "🛡️", "Focus Lock", NeonPink),
            ProductivityTool("t10", "Data Sovereignty IO", "1-tap SQLite JSON database export & restore", "💾", "Database", NeonViolet),
            ProductivityTool("t11", "Time-Boxing Interval Matrix", "Visual 30-min deep work blocks & schedule", "⏱️", "Deep Flow", NeonCyan),
            ProductivityTool("t12", "Energy & Bio-Rhythm Audit", "Bio-energetic mood tracker & audio matcher", "⚡", "Energy", NeonAmber),
            // 5 BRAND NEW HIGH-UTILITY POWER TOOLS
            ProductivityTool("t13", "Sprint Time-Boxing Matrix", "Multi-sprint flow intervals with goal checklists", "⏱️", "Sprint Matrix", NeonCyan),
            ProductivityTool("t14", "Aura Ambience Synthesizer", "5-channel nature sound mixing with binaural crossfades", "🎛️", "Sound Lab", NeonPink),
            ProductivityTool("t15", "Quest Vault & Focus Bounty", "Daily & weekly RPG productivity bounties & XP loot", "⚔️", "Quests", NeonAmber),
            ProductivityTool("t16", "Dopamine Fast Protocol", "Neuro-reset digital detox timeline & urge surfing tracker", "🧘", "Dopamine Reset", NeonLime),
            ProductivityTool("t17", "Bilateral Eye Pacer", "EMDR neuro-bilateral saccade stimulation pacer for calm & focus", "👁️", "Neuro-Pacer", NeonCyan),
            ProductivityTool("t18", "Ultradian Rhythm Waves", "90-minute biological energy cycle & recovery planner", "🌊", "Bio-Rhythm", NeonCyan),
            ProductivityTool("t19", "Distraction Interceptor", "10-minute dopamine urge surfing delay protocol", "🛡️", "Habit Shield", NeonPink),
            ProductivityTool("t20", "Binaural Flow Synthesizer", "Multi-channel acoustic frequency & nature mixer", "🎛️", "Sound Lab", NeonViolet)
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    val filteredTools = remember(searchQuery) {
        if (searchQuery.isBlank()) tools
        else tools.filter { tool ->
            tool.title.contains(searchQuery, ignoreCase = true) ||
            tool.desc.contains(searchQuery, ignoreCase = true) ||
            tool.badge.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 120.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Productivity Power Suite",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Deep work execution, mindfulness & habit systems",
                    style = MaterialTheme.typography.bodySmall,
                    color = NeonLime
                )
            }
            GlassChip(
                label = "Power Suite ⚡",
                color = NeonLime,
                selected = true
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar for Productivity Tools
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("tools_search_bar"),
            placeholder = { Text("Search tools, kanban, habits, notes...", color = Color(0x70FFFFFF), fontSize = 13.sp) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = NeonLime, modifier = Modifier.size(20.dp))
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0x16FFFFFF),
                unfocusedContainerColor = Color(0x0EFFFFFF),
                focusedBorderColor = NeonLime,
                unfocusedBorderColor = Color(0x28FFFFFF),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Zen Tree Sanctuary Shortcut (Analytics removed per user request)
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = NeonLime,
            glowRadius = 6.dp,
            onClick = { onLaunchTool("zen_garden") }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NeonLime.copy(alpha = 0.22f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Eco, "Tree", tint = NeonLime, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Zen Tree Sanctuary", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                        Text("Daily habit streaks & fruit harvest garden", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NeonLime)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tools List
        filteredTools.forEach { tool ->
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                glowColor = tool.accentColor,
                glowRadius = 10.dp,
                onClick = { onLaunchTool(tool.id) },
                testTag = "tool_card_${tool.id}"
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(tool.accentColor.copy(alpha = 0.2f))
                                .border(1.dp, tool.accentColor.copy(alpha = 0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tool.iconEmoji, fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = tool.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = tool.desc,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    GlassChip(
                        label = tool.badge,
                        color = tool.accentColor,
                        selected = true
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun JournalDialog(onDismiss: () -> Unit) {
    var intention by remember { mutableStateOf("Complete core Android build today with zero distractions.") }
    var gratitude by remember { mutableStateOf("Grateful for clean code, deep focus, and high battery life.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("📖 Daily Reflection Journal", color = MaterialTheme.colorScheme.onBackground) },
        text = {
            Column {
                Text("Morning Intention:", color = NeonPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = intention,
                    onValueChange = { intention = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonPink)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("Evening Gratitude:", color = NeonPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = gratitude,
                    onValueChange = { gratitude = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonPink)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                GamificationManager.recordSessionCompleted()
                onDismiss()
            }) {
                Text("Save Entry", color = NeonLime)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        },
        containerColor = Color(0xFF10121D)
    )
}

@Composable
fun QuickNotesDialog(onDismiss: () -> Unit) {
    var noteContent by remember { mutableStateOf("- Review Room SQLite DAO\n- Check binaural 40Hz output\n- Verify floating glass bottom bar") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("📝 Quick Scratchpad", color = MaterialTheme.colorScheme.onBackground) },
        text = {
            OutlinedTextField(
                value = noteContent,
                onValueChange = { noteContent = it },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = NeonAmber)
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Save Note", color = NeonCyan) }
        },
        containerColor = Color(0xFF10121D)
    )
}

@Composable
fun EisenhowerDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("⚖️ Eisenhower Priority Matrix", color = MaterialTheme.colorScheme.onBackground) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.fillMaxWidth().background(NeonPink.copy(alpha = 0.2f)).padding(10.dp)) {
                    Text("1. DO FIRST (Urgent & Important): Core Architecture & Focus Audio", color = NeonPink, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth().background(NeonCyan.copy(alpha = 0.2f)).padding(10.dp)) {
                    Text("2. SCHEDULE (Important, Not Urgent): 16-Week Habit Matrix & Games", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth().background(NeonAmber.copy(alpha = 0.2f)).padding(10.dp)) {
                    Text("3. DELEGATE (Urgent, Not Important): Social notifications & noisy emails", color = NeonAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.fillMaxWidth().background(Color(0x20FFFFFF)).padding(10.dp)) {
                    Text("4. ELIMINATE: Doomscrolling & non-deep work distractions", color = Color.White, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Got It", color = NeonCyan) }
        },
        containerColor = Color(0xFF10121D)
    )
}

@Composable
fun StoicQuoteDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🏛️ Stoic Wisdom", color = MaterialTheme.colorScheme.onBackground) },
        text = {
            Column {
                Text(
                    text = "“You have power over your mind - not outside events. Realize this, and you will find strength.”",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = NeonAmber
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "— Marcus Aurelius, Meditations", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close", color = NeonAmber) }
        },
        containerColor = Color(0xFF10121D)
    )
}

@Composable
fun DistractionVaultDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🛡️ Distraction Vault Oath", color = MaterialTheme.colorScheme.onBackground) },
        text = {
            Text(
                text = "“I commit to staying in AuraFocus for this session. I will not break flow for trivial notifications or social feeds until the timer expires.”",
                color = NeonPink,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = {
                GamificationManager.recordSessionCompleted()
                onDismiss()
            }) { Text("I Commit ✓", color = NeonLime) }
        },
        containerColor = Color(0xFF10121D)
    )
}

@Composable
fun DatabaseExportDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("💾 Data Sovereignty JSON", color = MaterialTheme.colorScheme.onBackground) },
        text = {
            Text(
                text = "All your habits, tasks, journal entries, and focus minutes are securely managed in high-speed SQLite. You can export a snapshot JSON anytime.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Export Backup", color = NeonViolet) }
        },
        containerColor = Color(0xFF10121D)
    )
}

@Composable
fun GenericToolDialog(tool: ProductivityTool, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("${tool.iconEmoji} ${tool.title}", color = MaterialTheme.colorScheme.onBackground) },
        text = {
            Text(tool.desc, color = MaterialTheme.colorScheme.onSurfaceVariant)
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close", color = tool.accentColor) }
        },
        containerColor = Color(0xFF10121D)
    )
}
