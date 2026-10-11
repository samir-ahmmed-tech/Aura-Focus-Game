package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CustomEmptyStateView
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassChip
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet

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
            ProductivityTool("t13", "Sprint Time-Boxing Matrix", "Multi-sprint flow intervals with goal checklists", "⏱️", "Sprint Matrix", NeonCyan),
            ProductivityTool("t14", "Aura Ambience Synthesizer", "5-channel nature sound mixing with binaural crossfades", "🎛️", "Sound Lab", NeonPink),
            ProductivityTool("t15", "Quest Vault & Focus Bounty", "Daily & weekly RPG productivity bounties & XP loot", "⚔️", "Quests", NeonAmber),
            ProductivityTool("t16", "Dopamine Fast Protocol", "Neuro-reset digital detox timeline & urge surfing tracker", "🧘", "Dopamine Reset", NeonLime),
            ProductivityTool("t17", "Bilateral Eye Pacer", "EMDR neuro-bilateral saccade stimulation pacer for calm & focus", "👁️", "Neuro-Pacer", NeonCyan),
            ProductivityTool("t18", "Ultradian Rhythm Waves", "90-minute biological energy cycle & recovery planner", "🌊", "Bio-Rhythm", NeonCyan),
            ProductivityTool("t19", "Distraction Interceptor", "10-minute dopamine urge surfing delay protocol", "🛡️", "Habit Shield", NeonPink),
            ProductivityTool("t20", "Binaural Flow Synthesizer", "Multi-channel acoustic frequency & nature mixer", "🎛️", "Sound Lab", NeonViolet),
            // 15 BRAND NEW POWER PRODUCTIVITY TOOLS (TOTAL 35 TOOLS)
            ProductivityTool("t21", "5-4-3-2-1 Sensory Grounding", "Anxiety dissolver & sensory mindfulness anchor", "👀", "Mindful", NeonCyan),
            ProductivityTool("t22", "Pomodoro Flow Architect", "Customizable sprint & rest interval intervals", "⏱️", "Deep Flow", NeonAmber),
            ProductivityTool("t23", "Circadian Energy Audit", "Bio-rhythm sleep quality & alertness peak tracker", "🌙", "Circadian", NeonCyan),
            ProductivityTool("t24", "Deep Work Ratio Tracker", "Cal Newport deep work vs shallow work auditor", "📊", "Analytics", NeonPink),
            ProductivityTool("t25", "Gratitude Pebble Jar", "Daily 3-pebble mental fortitude accumulator", "🏺", "Gratitude", NeonLime),
            ProductivityTool("t26", "Vision Manifesto Board", "Personal core values & daily focus anchors", "📜", "Vision", NeonViolet),
            ProductivityTool("t27", "Mental Clarity Brain Dump", "Working memory unloader with instant categorization", "🧠", "Clarity", NeonCyan),
            ProductivityTool("t28", "Ivy Lee 6 Priorities", "Legendary daily discipline: exactly 6 tasks", "📋", "Discipline", NeonAmber),
            ProductivityTool("t29", "20-20-20 Ocular Relief Pacer", "Ocular fatigue reliever & distance gaze pacer", "👁️", "Wellness", NeonLime),
            ProductivityTool("t30", "Noise Waves Synthesizer", "White, Pink & Deep Brown acoustic sound lab", "🔊", "Sound Lab", NeonViolet),
            ProductivityTool("t31", "CBT Cognitive Reframing", "Identify cognitive distortions & reframe thoughts", "💡", "Psychology", NeonPink),
            ProductivityTool("t32", "Hydration Metabolism Pacer", "Daily cognitive water intake & interval pacer", "💧", "Hydration", NeonCyan),
            ProductivityTool("t33", "Weekly Agile Retrospective", "Sprint wins, stumbles & lessons learned review", "🏆", "Agile", NeonLime),
            ProductivityTool("t34", "Focus Commitment Contract", "Pre-commitment pact with streak stake forfeiture", "🔒", "Focus Lock", NeonPink),
            ProductivityTool("t35", "Desk Ergonomics & Stretch", "Guided 3-minute spinal and wrist posture reset", "🧘", "Posture", NeonLime),
            // 10 BRAND NEW POWER PRODUCTIVITY TOOLS (TOTAL 45 TOOLS)
            ProductivityTool("t36", "Parkinson's Law Sprint", "10-minute micro-sprint countdown enforcer", "⏳", "Sprint Matrix", NeonCyan),
            ProductivityTool("t37", "Rubber Duck Debugger", "Socratic lateral blocker solver canvas", "🦆", "Clarity", NeonAmber),
            ProductivityTool("t38", "Burnout Radar Audit", "5-factor vitality friction & recovery index", "⚡", "Energy", NeonPink),
            ProductivityTool("t39", "Mental Rehearsal Visualizer", "Pre-performance flow state imagery script", "🧘", "Mindful", NeonViolet),
            ProductivityTool("t40", "Interstitial Journaling", "Zero-lag micro-journaling between tasks", "📝", "Notes", NeonCyan),
            ProductivityTool("t41", "Feynman Technique Clarifier", "Plain-English deep mastery concept breakdown", "🔬", "Learning", NeonAmber),
            ProductivityTool("t42", "Decision Scorer Matrix", "Weighted criteria prioritization grid", "⚖️", "Strategy", NeonLime),
            ProductivityTool("t43", "Digital Sunset Wind-Down", "4-step circadian evening cognitive shutdown", "🌙", "Recovery", NeonAmber),
            ProductivityTool("t44", "5-Minute Kickstart", "Friction-free momentum initiation protocol", "🚀", "Discipline", NeonLime),
            ProductivityTool("t45", "Weekly OKR Matrix", "Objectives & Key Results weekly alignment", "🎯", "High Leverage", NeonViolet)
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

    // LazyColumn virtualization: 120 FPS ultra-smooth lazy loading
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Top Header
        item(key = "header_item") {
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
                        text = "45 Power Tools • Zero Lag Decoupled Engine",
                        style = MaterialTheme.typography.bodySmall,
                        color = NeonLime
                    )
                }
                GlassChip(
                    label = "${filteredTools.size} Tools ⚡",
                    color = NeonLime,
                    selected = true
                )
            }
        }

        // Search Bar Item
        item(key = "search_item") {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tools_search_bar"),
                placeholder = { Text("Search 35 tools, kanban, habits, notes, pomodoro...", color = Color(0x70FFFFFF), fontSize = 13.sp) },
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
        }

        // Zen Tree Sanctuary Shortcut Item
        item(key = "zen_tree_shortcut") {
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
        }

        if (filteredTools.isEmpty()) {
            item(key = "empty_tools_state") {
                CustomEmptyStateView(
                    iconEmoji = "🧰",
                    title = "No Productivity Tools Found",
                    description = "No tools matched \"$searchQuery\". Clear search to explore all 35 power tools.",
                    actionButtonText = "Clear Search",
                    onActionClick = {
                        searchQuery = ""
                    }
                )
            }
        }

        // Virtualized Tool Cards: loads lazily on-screen, instant page switch!
        items(
            items = filteredTools,
            key = { it.id }
        ) { tool ->
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = tool.accentColor,
                glowRadius = 8.dp,
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
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(tool.accentColor.copy(alpha = 0.22f))
                                .border(1.5.dp, tool.accentColor, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tool.iconEmoji, fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = tool.title,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = tool.desc,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(tool.accentColor.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = tool.badge,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = tool.accentColor
                                )
                            }
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open",
                        tint = tool.accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
