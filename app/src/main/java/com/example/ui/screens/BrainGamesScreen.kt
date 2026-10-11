package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
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
import com.example.ui.components.GlassCard
import com.example.ui.components.CustomEmptyStateView
import com.example.ui.components.GlassChip
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet

data class BrainGameInfo(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val categoryGroup: String, // "All", "Math & Logic", "Memory", "Reflex & Rhythm", "Executive Focus"
    val iconEmoji: String,
    val xpReward: Int,
    val accentColor: Color
)

@Composable
fun BrainGamesScreen(
    onLaunchGame: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val games = remember {
        listOf(
            BrainGameInfo("g1", "Mental Math Sprint", "Rapid arithmetic under pressure", "Math & Logic", "Math & Logic", "🧮", 30, NeonCyan),
            BrainGameInfo("g2", "Stroop Color Clash", "Inhibition & impulse control", "Cognitive Reflex", "Reflex & Rhythm", "🎨", 35, NeonPink),
            BrainGameInfo("g3", "Memory Matrix", "Recall pattern of illuminated tiles", "Visual Memory", "Memory", "🧩", 40, NeonViolet),
            BrainGameInfo("g4", "Speed Number Sequence", "Tap ascending numbers (1..16)", "Processing Speed", "Reflex & Rhythm", "🔢", 25, NeonLime),
            BrainGameInfo("g5", "Micro-Reaction Tap", "Sub-second reaction latency test", "Reflex", "Reflex & Rhythm", "⚡", 20, NeonAmber),
            BrainGameInfo("g6", "Digit Span Recall", "Working memory sequence builder", "Working Memory", "Memory", "🧠", 35, NeonCyan),
            BrainGameInfo("g7", "Word Unscramble", "Solve focus & cognitive anagrams", "Verbal Fluency", "Executive Focus", "🔤", 30, NeonViolet),
            BrainGameInfo("g8", "Spatial Angle Match", "Rotate 2D/3D shapes mentally", "Spatial IQ", "Executive Focus", "📐", 40, NeonPink),
            BrainGameInfo("g9", "Dual N-Back Mini", "Auditory & visual position tracker", "Fluid IQ", "Memory", "🎯", 50, NeonLime),
            BrainGameInfo("g11", "Pattern Flanker Test", "Selective visual attention & inhibition", "Attention Focus", "Executive Focus", "🏹", 45, NeonCyan),
            BrainGameInfo("g12", "Chimp Memory Matrix", "Spatial sequence recall faster than chimpanzee", "Primate IQ", "Memory", "🐵", 50, NeonLime),
            BrainGameInfo("g13", "Rhythm Beat Tap", "Sub-millisecond tempo synchronizer & rhythm IQ", "Rhythm & Tempo", "Reflex & Rhythm", "🥁", 45, NeonPink),
            BrainGameInfo("g15", "Cognitive Flex Duel", "Rapid dynamic rule switching (Color vs Shape)", "Executive Control", "Executive Focus", "🔄", 50, NeonLime),
            BrainGameInfo("g16", "Cyber Glitch Sequence", "Overclocked memory matrix hack sequence recall", "Cyber Memory", "Memory", "⚡", 50, NeonCyan),
            BrainGameInfo("g17", "Chroma Clash Inversion", "Neural conflict inhibition & reaction speed", "Inhibition IQ", "Reflex & Rhythm", "💥", 45, NeonPink),
            BrainGameInfo("g18", "Target Lock Radar", "Chaotic orbiting radar target tracking", "Visual Tracking", "Executive Focus", "🎯", 55, NeonLime),
            BrainGameInfo("g19", "Pitch Match Ear Trainer", "Acoustic audio frequency pitch discrimination", "Acoustic IQ", "Reflex & Rhythm", "🎵", 45, NeonViolet),
            BrainGameInfo("g20", "Codebreaker Cipher", "Rapid symbol-to-digit cryptanalysis under timer", "Cryptanalysis", "Math & Logic", "🕵️", 55, NeonAmber),
            BrainGameInfo("g21", "Quantum Flow Maze", "Dynamic rotating labyrinth pathfinding", "Spatial Maze", "Math & Logic", "🌀", 55, NeonCyan),
            BrainGameInfo("g22", "Neuro Balance Tilt", "Kinetic gravity equilibrium & precision stabilizer", "Equilibrium", "Reflex & Rhythm", "⚖️", 50, NeonLime),
            BrainGameInfo("g23", "Synapse Speed Sort", "Overclocked rapid categorization under shifting rules", "Executive Control", "Reflex & Rhythm", "⚡", 45, NeonCyan),
            BrainGameInfo("g24", "Spatial Path Trace", "Recall celestial constellation sequences", "Visuospatial IQ", "Memory", "✨", 50, NeonLime),
            BrainGameInfo("g25", "N-Back Audio Echo", "2-back acoustic frequency memory tracker", "Acoustic Memory", "Memory", "🎧", 55, NeonViolet),
            BrainGameInfo("g26", "Focus Radar Saccade", "Anti-saccadic eye movement target inhibitor", "Inhibition IQ", "Executive Focus", "🎯", 50, NeonCyan),
            // 16 BRAND NEW HIGH-COGNITIVE GAMES (TOTAL 40 GAMES)
            BrainGameInfo("g27", "Quantum Color Clash", "Inverted Stroop & chromatic quantum rule flip", "Cognitive Inversion", "Reflex & Rhythm", "🎨", 50, NeonPink),
            BrainGameInfo("g28", "Pattern Logic Matrix", "Progressive 3x3 Raven's inductive matrix reasoning", "Fluid Matrix IQ", "Math & Logic", "🧩", 55, NeonCyan),
            BrainGameInfo("g29", "Visual Saccade Sprint", "Sub-100ms anti-saccadic peripheral target jump", "Peripheral Reflex", "Executive Focus", "🎯", 45, NeonAmber),
            BrainGameInfo("g30", "Dual Switch Odd/Even", "Rapid parity vs magnitude task-set switching", "Cognitive Switch", "Executive Focus", "🔄", 50, NeonLime),
            BrainGameInfo("g31", "Spatial 3D Rotation", "Mental polyhedral angle rotation & reflection IQ", "3D Spatial IQ", "Executive Focus", "📐", 55, NeonViolet),
            BrainGameInfo("g32", "Echo Rhythm Simon", "Acoustic melody sequence echo memory", "Audio Memory", "Memory", "🥁", 50, NeonCyan),
            BrainGameInfo("g33", "Time Estimation Pulse", "Internal biological clock interval calibration", "Circadian Timing", "Reflex & Rhythm", "⏱️", 45, NeonAmber),
            BrainGameInfo("g34", "Anagram Chain Rush", "Verbal neuro-linguistic anagram unscramble", "Verbal Fluency", "Executive Focus", "🔤", 50, NeonViolet),
            BrainGameInfo("g35", "Go / No-Go Inhibit", "Sub-second motor impulse suppression arena", "Impulse Inhibit", "Reflex & Rhythm", "🛑", 45, NeonLime),
            BrainGameInfo("g36", "Symbol Odyssey Cipher", "Ancient rune glyphs associative working memory", "Symbolic Memory", "Memory", "📜", 55, NeonCyan),
            BrainGameInfo("g37", "Peripheral Radar", "Tactile change blindness anomaly scanner", "Peripheral Tracking", "Executive Focus", "📡", 50, NeonLime),
            BrainGameInfo("g38", "Synapse Math Chains", "Chained parenthetical mental arithmetic flow", "Mental Math", "Math & Logic", "🧮", 50, NeonCyan),
            BrainGameInfo("g39", "Labyrinth Pathfinder", "Dynamic barrier rotation maze navigation", "Spatial Planning", "Math & Logic", "🌀", 55, NeonCyan),
            BrainGameInfo("g40", "Chrono Digit Matrix", "Interleaved ascending order processing speed", "Processing Speed", "Reflex & Rhythm", "🔢", 45, NeonViolet),
            BrainGameInfo("g41", "Pitch Discrimination", "Micro-semitone acoustic ear discrimination", "Acoustic IQ", "Reflex & Rhythm", "🎵", 50, NeonPink),
            BrainGameInfo("g42", "Kinetic Orb Balancing", "Kinetic gravity equilibrium beam precision", "Equilibrium", "Reflex & Rhythm", "⚖️", 50, NeonLime),
            // 10 BRAND NEW HIGH-COGNITIVE GAMES (TOTAL 50 GAMES)
            BrainGameInfo("g43", "Quantum Mirror Entanglement", "Instant mirror-spin pattern symmetry detector", "Quantum Symmetry", "Math & Logic", "🪞", 55, NeonCyan),
            BrainGameInfo("g44", "Dual Conflict Color Clash", "Double-conflict text vs ink decision test", "Cognitive Inversion", "Reflex & Rhythm", "💥", 50, NeonPink),
            BrainGameInfo("g45", "Reverse Digit Span Recall", "Working memory sequence backward manipulation", "Working Memory", "Memory", "🧠", 50, NeonViolet),
            BrainGameInfo("g46", "Mental Origami Hole Punch", "Predict punched holes on unfolded 2D/3D sheets", "Visuospatial IQ", "Executive Focus", "📐", 55, NeonLime),
            BrainGameInfo("g47", "Color Pulse Rhythm Sync", "Sub-50ms concentric beat expansion synchronizer", "Rhythm & Reflex", "Reflex & Rhythm", "🎯", 45, NeonAmber),
            BrainGameInfo("g48", "Tower of Hanoi Flow", "Executive 3-peg disk sequencing optimizer", "Executive Planning", "Math & Logic", "🏛️", 60, NeonCyan),
            BrainGameInfo("g49", "Lexical Anomaly Hunt", "High-speed semantic anomaly spotting in word grid", "Verbal Processing", "Executive Focus", "🔤", 45, NeonViolet),
            BrainGameInfo("g50", "Synapse Circuit Router", "Rotate 3x3 tiles to transmit neural signals", "Spatial Circuit", "Math & Logic", "⚡", 55, NeonCyan),
            BrainGameInfo("g51", "Microtone Interval Calibrator", "Micro-tonal acoustic pitch ear calibrator", "Acoustic Ear", "Reflex & Rhythm", "🎵", 50, NeonPink),
            BrainGameInfo("g52", "Peripheral Anomaly Scanner", "Subtle visual scene change blindness scanner", "Visual Saccade", "Executive Focus", "📡", 50, NeonLime)
        )
    }

    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    val categories = remember { listOf("All", "Math & Logic", "Memory", "Reflex & Rhythm", "Executive Focus") }

    val filteredGames = remember(selectedCategory, searchQuery) {
        games.filter { game ->
            val matchCat = selectedCategory == "All" || game.categoryGroup == selectedCategory
            val matchQuery = searchQuery.isBlank() ||
                game.title.contains(searchQuery, ignoreCase = true) ||
                game.subtitle.contains(searchQuery, ignoreCase = true) ||
                game.category.contains(searchQuery, ignoreCase = true)
            matchCat && matchQuery
        }
    }

    // LazyColumn provides butter-smooth, 120 FPS virtualization: only renders visible items!
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Top Header Item
        item(key = "header_item") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Cognitive Training Ground",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "50 Master Brain Games • 120 FPS Decoupled Engine",
                        style = MaterialTheme.typography.bodySmall,
                        color = NeonCyan
                    )
                }
                GlassChip(
                    label = "${filteredGames.size} Games ⚡",
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
                    .testTag("games_search_bar"),
                placeholder = { Text("Search 40 brain games, memory, reflex, math...", color = Color(0x70FFFFFF), fontSize = 13.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = NeonCyan, modifier = Modifier.size(20.dp))
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
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = Color(0x28FFFFFF),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
        }

        // Zen Tree Shortcut Item
        item(key = "zen_tree_shortcut") {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonLime,
                glowRadius = 6.dp,
                onClick = { onLaunchGame("zen_garden") }
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
                            Text("Realistic tree growth, daily watering & fruit garden", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NeonLime)
                }
            }
        }

        // Category Filter Chips Item
        item(key = "category_chips_item") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    val chipColor = when (cat) {
                        "Math & Logic" -> NeonCyan
                        "Memory" -> NeonViolet
                        "Reflex & Rhythm" -> NeonPink
                        "Executive Focus" -> NeonLime
                        else -> NeonAmber
                    }

                    GlassChip(
                        label = cat,
                        color = chipColor,
                        selected = isSelected,
                        onClick = { selectedCategory = cat }
                    )
                }
            }
        }

        if (filteredGames.isEmpty()) {
            item(key = "empty_games_state") {
                CustomEmptyStateView(
                    iconEmoji = "🧩",
                    title = "No Cognitive Games Found",
                    description = "No games matched \"$searchQuery\" in category \"$selectedCategory\". Reset filters to explore all 40 cognitive games.",
                    actionButtonText = "Reset All Filters",
                    onActionClick = {
                        searchQuery = ""
                        selectedCategory = "All"
                    }
                )
            }
        }

        // Virtualized Game Cards: Only renders visible items, zero lag!
        items(
            items = filteredGames,
            key = { it.id }
        ) { game ->
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = game.accentColor,
                glowRadius = 8.dp,
                onClick = { onLaunchGame(game.id) },
                testTag = "game_card_${game.id}"
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
                                .background(game.accentColor.copy(alpha = 0.22f))
                                .border(1.5.dp, game.accentColor, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(game.iconEmoji, fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = game.title,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = game.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(game.accentColor.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = game.category,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = game.accentColor
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "+${game.xpReward} XP",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonAmber
                                )
                            }
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Play",
                        tint = game.accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
