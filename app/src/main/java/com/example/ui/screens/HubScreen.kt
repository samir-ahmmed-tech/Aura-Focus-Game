package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GamificationManager
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassChip
import com.example.ui.theme.AuraThemeMode
import com.example.ui.theme.CardBackdropStyle
import com.example.ui.theme.EmeraldMint
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.RoseGold
import com.example.util.AuraSoundManager
import com.example.util.AuraUpdateChecker
import com.example.util.NeuroBoxBackupEngine
import com.example.util.UpdateCheckResult
import kotlinx.coroutines.launch

data class SettingItem(
    val name: String,
    val explanation: String,
    val defaultValue: Boolean
)

@Composable
fun HubScreen(
    currentTheme: AuraThemeMode,
    onThemeChange: (AuraThemeMode) -> Unit,
    uiScale: Float = 1.0f,
    onUiScaleChange: (Float) -> Unit = {},
    touchSoundEnabled: Boolean = true,
    onTouchSoundToggle: (Boolean) -> Unit = {},
    cardBackdropStyle: CardBackdropStyle = CardBackdropStyle.CIRCLE_HALO,
    onCardBackdropStyleChange: (CardBackdropStyle) -> Unit = {},
    onOpenAnalytics: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    var selectedSection by remember { mutableStateOf(0) } // 0: Focus (30), 1: Music (20), 2: Developer & Updates, 3: Play Store & Privacy, 4: Guide
    val gamificationState by GamificationManager.state.collectAsState()
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showWipeConfirmDialog by remember { mutableStateOf(false) }

    // Live GitHub Release Update Checker State
    var updateResult by remember { mutableStateOf<UpdateCheckResult?>(null) }
    var isCheckingUpdate by remember { mutableStateOf(false) }

    // 30 Focus & Productivity Settings with Plain English Descriptions
    val focusSettingsList = remember {
        listOf(
            SettingItem("Auto-Start Break", "Automatically begins short break when focus session finishes.", true),
            SettingItem("Auto-Start Focus", "Starts next deep work round automatically after break ends.", false),
            SettingItem("Keep Screen Awake", "Prevents display from sleeping during active focus mode.", true),
            SettingItem("Session Finish Chime", "Plays an acoustic bell sound when countdown hits zero.", true),
            SettingItem("Tick Pulse (Haptic)", "Gentle tactile vibration every second to maintain rhythm.", true),
            SettingItem("Vibration Alerts", "Strong pulse alert when timers start or complete.", true),
            SettingItem("Auto-Pause on Unplug", "Stops brainwave audio instantly if headphones disconnect.", true),
            SettingItem("Harmonic Carrier (216 Hz)", "Plays mathematical golden-ratio pitch behind brainwaves.", true),
            SettingItem("Volume Equalizer", "Automatically balances ambient rain against binaural waves.", true),
            SettingItem("Live Lockscreen Timer", "Shows running countdown on lockscreen notification bar.", true),
            SettingItem("Distraction Vault Oath", "Prompts focus commitment confirmation before starting.", true),
            SettingItem("Streak Shield Auto-Use", "Saves your daily habit streak automatically if you miss a day.", true),
            SettingItem("120-Min Deep Work Goal", "Sets daily personal target of 2 hours pure focus.", true),
            SettingItem("Midnight Habit Reset", "Cleans daily habit checkmarks at 00:00 automatically.", true),
            SettingItem("Post-Session Stoic Quote", "Displays uplifting philosophy quote upon timer completion.", true),
            SettingItem("Radial Glow Atmosphere", "Adds breathing ambient neon halos behind interactive cards.", true),
            SettingItem("Habit Fanfare Sound", "Plays celebratory sound effect when checking off a habit.", true),
            SettingItem("XP Level Celebration", "Triggers level-up screen when hitting next XP milestone.", true),
            SettingItem("Status Bar Focus Minutes", "Displays total minutes worked in top navigation pill.", true),
            SettingItem("Auto-Save Journal Entries", "Saves thoughts automatically to SQLite without extra taps.", true),
            SettingItem("Prevent Accidental Skip", "Asks confirmation before skipping an active focus session.", true),
            SettingItem("Micro-Break Reminders", "Suggests standing up and stretching every 20 minutes.", false),
            SettingItem("Hydration & Eye Alerts", "Prompts 20-20-20 eye rest and drinking water periodically.", true),
            SettingItem("Daily Recap at 21:00", "Summarizes total focus minutes and habits achieved at night.", true),
            SettingItem("Math Game Dynamic Level", "Increases arithmetic difficulty as your math score grows.", true),
            SettingItem("Strict Focus Lock (15m)", "Disables pause button for first 15 mins to defeat procrastination.", false),
            SettingItem("Infinite Ambient Sound Loop", "Keeps ambient rain and forest sounds looping continuously.", true),
            SettingItem("Auto-Mute Notifications", "Blocks system sound interruptions during deep flow.", true),
            SettingItem("Milestone Badges Popups", "Celebrates unlocking new achievements like Lone Wolf Initiate.", true),
            SettingItem("Weekly Auto-Backup", "Creates a local database snapshot file once every 7 days.", true)
        )
    }

    // 20 Normal Music Player Settings with Plain English Descriptions
    val musicSettingsList = remember {
        listOf(
            SettingItem("Player Mode Switcher", "Allows toggling between normal music player and brainwave studio.", true),
            SettingItem("Auto-Scan Phone Songs", "Automatically finds and indexes all local MP3 files on startup.", true),
            SettingItem("Crossfade Tracks (3s)", "Smoothly blends audio between ending and starting tracks.", true),
            SettingItem("Gapless Audio Playback", "Removes silence between continuous live or classical albums.", true),
            SettingItem("Volume Button Track Skip", "Long press volume keys to skip tracks when screen is locked.", true),
            SettingItem("Bass Boost Resonator", "Amplifies low-end bass frequencies for deep acoustic punch.", true),
            SettingItem("3D Virtualizer Spatial Audio", "Creates wide multi-speaker acoustic soundstage in earphones.", true),
            SettingItem("ReplayGain Normalization", "Adjusts loud and quiet songs to uniform hearing volume.", false),
            SettingItem("High-Res 24-Bit / 96kHz", "Enables maximum lossless audio output on supported DACs.", true),
            SettingItem("Remember Last Track Position", "Resumes playback exactly where you left off when reopening.", true),
            SettingItem("Audio Focus Auto-Pause", "Pauses music automatically when incoming phone call arrives.", true),
            SettingItem("Headset Plug Auto-Resume", "Starts playing automatically when plugging in headphones.", false),
            SettingItem("Shake to Shuffle", "Gently shake phone to jump to a random queued song.", false),
            SettingItem("Filter Short Files (< 30s)", "Hides short voice clips, ringtones, and notification sounds.", true),
            SettingItem("Ignore WhatsApp Voice Folders", "Prevents voice chat audio from cluttering music playlists.", true),
            SettingItem("Lockscreen Artwork Display", "Shows album cover artwork on phone lockscreen background.", true),
            SettingItem("Low-Latency Audio Buffer", "Minimizes playback delay for instant seek and pause response.", true),
            SettingItem("Sleep Timer Gradual Fade", "Lowers volume slowly over final 2 minutes before shutoff.", true),
            SettingItem("Stacker Sound Balance", "Saves custom fader levels for Music vs Brainwaves vs Rain.", true),
            SettingItem("Auto-Favorites Smart Playlist", "Creates dynamic playlist of your most played and liked tracks.", true)
        )
    }

    val toggleStates = remember {
        mutableStateMapOf<String, Boolean>().apply {
            focusSettingsList.forEach { put(it.name, it.defaultValue) }
            musicSettingsList.forEach { put(it.name, it.defaultValue) }
        }
    }

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var editName by remember(gamificationState.userName) { mutableStateOf(gamificationState.userName) }
    var editBio by remember(gamificationState.userBio) { mutableStateOf(gamificationState.userBio) }
    var editAvatar by remember(gamificationState.userAvatarEmoji) { mutableStateOf(gamificationState.userAvatarEmoji) }

    // LazyColumn provides zero-lag, instant 120 FPS virtualization
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        text = "NeuroBox Hub & Settings",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Samir Ahmmed • 50+ Settings • Update Engine",
                        style = MaterialTheme.typography.bodySmall,
                        color = NeonCyan
                    )
                }
                GlassChip(label = "Level ${gamificationState.currentLevel}", color = NeonLime, selected = true)
            }
        }

        // 1. FLOW MASTERY & TELEMETRY PROFILE DASHBOARD
        item(key = "profile_dashboard_item") {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonCyan,
                glowRadius = 14.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan.copy(alpha = 0.22f))
                                    .border(1.5.dp, NeonCyan, CircleShape)
                                    .clickable { showEditProfileDialog = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(gamificationState.userAvatarEmoji, fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = gamificationState.userName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(NeonLime.copy(alpha = 0.2f))
                                            .border(1.dp, NeonLime, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = gamificationState.rankTitle,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NeonLime
                                        )
                                    }
                                }
                                Text(
                                    text = gamificationState.userBio,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        GlassButton(
                            text = "Edit ✏️",
                            onClick = { showEditProfileDialog = true },
                            isPrimary = false,
                            modifier = Modifier.height(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // XP Progress Bar
                    val xpProgress = (gamificationState.currentXp.toFloat() / gamificationState.xpForNextLevel.toFloat()).coerceIn(0f, 1f)
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Flow Mastery XP", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                "${gamificationState.currentXp} / ${gamificationState.xpForNextLevel} XP",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Color(0x20FFFFFF))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(xpProgress)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(NeonCyan)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⏱️ ${gamificationState.totalFocusMinutes}m", fontWeight = FontWeight.Black, fontSize = 15.sp, color = Color.White)
                            Text("Focus Time", fontSize = 10.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔥 ${gamificationState.currentStreakDays}d", fontWeight = FontWeight.Black, fontSize = 15.sp, color = NeonAmber)
                            Text("Day Streak", fontSize = 10.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎮 ${gamificationState.gamesPlayedTotal}", fontWeight = FontWeight.Black, fontSize = 15.sp, color = NeonLime)
                            Text("Matches", fontSize = 10.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🍎 ${gamificationState.tree.auraFruits}", fontWeight = FontWeight.Black, fontSize = 15.sp, color = NeonPink)
                            Text("Harvest", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        // COGNITIVE ANALYTICS SHORTCUT
        item(key = "analytics_shortcut_item") {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonCyan,
                glowRadius = 8.dp,
                onClick = onOpenAnalytics
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NeonCyan.copy(alpha = 0.22f))
                                .border(1.5.dp, NeonCyan, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📊", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Cognitive IQ & Performance Analytics", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                            Text("IQ, EX, SQ, ESQ, MQ, RQ (1-day, 7-day & 1-month trends)", fontSize = 10.sp, color = NeonCyan)
                        }
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = "Open", tint = NeonCyan)
                }
            }
        }

        // THEME & BACKDROP CONFIGURATOR
        item(key = "theme_config_item") {
            GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonCyan, glowRadius = 8.dp) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("🎨 Aura Theme Mode", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Triple(AuraThemeMode.AURORA_DARK, "🌌 Aurora Dark", NeonCyan),
                            Triple(AuraThemeMode.CYBER_NEON, "⚡ Cyber Neon", NeonLime),
                            Triple(AuraThemeMode.SOLAR_LIGHT, "☀️ Solar Light", Color(0xFF0284C7)),
                            Triple(AuraThemeMode.MIDNIGHT_EMERALD, "🌿 Midnight Emerald", EmeraldMint),
                            Triple(AuraThemeMode.SUNSET_OBSIDIAN, "🌅 Sunset Obsidian", RoseGold)
                        ).forEach { (mode, title, color) ->
                            val isPicked = currentTheme == mode
                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isPicked) color.copy(alpha = 0.25f) else Color(0x12FFFFFF))
                                    .border(1.5.dp, if (isPicked) color else Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                                    .clickable { onThemeChange(mode) }
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(title, color = if (isPicked) color else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 5-WAY NAVIGATION TABS
        item(key = "section_tabs_item") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x14FFFFFF))
                    .padding(4.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    Pair(0, "⏱️ 30 Focus"),
                    Pair(1, "🎵 20 Music"),
                    Pair(2, "👨‍💻 Developer & Updates"),
                    Pair(3, "📜 Play Store & Privacy"),
                    Pair(4, "📖 Guide")
                ).forEach { (secIdx, label) ->
                    val isSelected = selectedSection == secIdx
                    Box(
                        modifier = Modifier
                            .height(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NeonCyan.copy(alpha = 0.25f) else Color.Transparent)
                            .clickable { selectedSection = secIdx }
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) NeonCyan else Color.White
                        )
                    }
                }
            }
        }

        // SECTION CONTENT ACCORDING TO TAB
        when (selectedSection) {
            0 -> {
                // 30 FOCUS SETTINGS
                item(key = "focus_settings_card") {
                    Text("Focus & Brainwave Controls (30 Options):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                    Spacer(modifier = Modifier.height(8.dp))
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            focusSettingsList.forEachIndexed { idx, item ->
                                val isChecked = toggleStates[item.name] ?: item.defaultValue
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("${idx + 1}. ${item.name}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(item.explanation, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Switch(
                                        checked = isChecked,
                                        onCheckedChange = { toggleStates[item.name] = it },
                                        colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = NeonCyan.copy(alpha = 0.4f))
                                    )
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // 20 MUSIC SETTINGS
                item(key = "music_settings_card") {
                    Text("Music Player & Audio Controls (20 Options):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NeonPink)
                    Spacer(modifier = Modifier.height(8.dp))
                    GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonPink) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            musicSettingsList.forEachIndexed { idx, item ->
                                val isChecked = toggleStates[item.name] ?: item.defaultValue
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("${idx + 1}. ${item.name}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(item.explanation, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Switch(
                                        checked = isChecked,
                                        onCheckedChange = { toggleStates[item.name] = it },
                                        colors = SwitchDefaults.colors(checkedThumbColor = NeonPink, checkedTrackColor = NeonPink.copy(alpha = 0.4f))
                                    )
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // DEVELOPER INFO & LIVE UPDATE CHECKER
                item(key = "developer_showcase_card") {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        glowColor = NeonCyan,
                        glowRadius = 12.dp
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(NeonCyan.copy(alpha = 0.22f))
                                            .border(1.5.dp, NeonCyan, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("👨‍💻", fontSize = 26.sp)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Samir Ahmmed",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Lead Architect & Developer",
                                            fontSize = 11.sp,
                                            color = NeonCyan
                                        )
                                    }
                                }
                                GlassChip(label = "Verified Creator ⭐", color = NeonLime, selected = true)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "NeuroBox was conceived and crafted by Samir Ahmmed to deliver a high-octane, zero-lag cognitive conditioning laboratory with complete user privacy sovereignty.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                GlassButton(
                                    text = "GitHub Repo 🌐",
                                    onClick = {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/samir-ahmmed-tech/Aura-Focus-Game"))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                )

                                GlassButton(
                                    text = "Copy Repo URL 📋",
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Repository URL", "https://github.com/samir-ahmmed-tech/Aura-Focus-Game")
                                        clipboard.setPrimaryClip(clip)
                                        AuraSoundManager.playSuccessSound()
                                        Toast.makeText(context, "GitHub URL copied!", Toast.LENGTH_SHORT).show()
                                    },
                                    isPrimary = false,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                item(key = "update_checker_card") {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        glowColor = NeonAmber,
                        glowRadius = 10.dp
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "🚀 In-App Update Engine",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Current: NeuroBox v1.1.0 (Build 2)",
                                        fontSize = 11.sp,
                                        color = NeonAmber
                                    )
                                }
                                GlassChip(
                                    label = if (updateResult?.hasUpdate == true) "Update Ready 🚀" else "Release Active",
                                    color = if (updateResult?.hasUpdate == true) NeonPink else NeonLime,
                                    selected = true
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            if (updateResult != null) {
                                if (updateResult!!.hasUpdate) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(NeonPink.copy(alpha = 0.18f))
                                            .border(1.dp, NeonPink, RoundedCornerShape(12.dp))
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            Text(
                                                text = "🎉 New Release: ${updateResult!!.releaseName} (${updateResult!!.latestVersion})",
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                fontSize = 13.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = updateResult!!.releaseNotes,
                                                fontSize = 11.sp,
                                                color = Color(0xDDFFFFFF),
                                                maxLines = 4
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            GlassButton(
                                                text = "Download Release APK 📥",
                                                onClick = {
                                                    try {
                                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateResult!!.downloadUrl))
                                                        context.startActivity(intent)
                                                    } catch (_: Exception) {}
                                                },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                } else if (updateResult!!.errorMessage != null) {
                                    Text(
                                        text = "Status: ${updateResult!!.errorMessage}",
                                        fontSize = 12.sp,
                                        color = NeonAmber
                                    )
                                } else {
                                    Text(
                                        text = "✓ You are running the newest official release of NeuroBox!",
                                        fontSize = 12.sp,
                                        color = NeonLime
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            GlassButton(
                                text = if (isCheckingUpdate) "Checking GitHub Releases... ⏳" else "Check for Updates Now 🔄",
                                onClick = {
                                    if (!isCheckingUpdate) {
                                        isCheckingUpdate = true
                                        AuraSoundManager.playTapSound(context)
                                        scope.launch {
                                            val result = AuraUpdateChecker.checkForUpdates("1.1.0")
                                            updateResult = result
                                            isCheckingUpdate = false
                                            if (result.hasUpdate) {
                                                AuraSoundManager.playSuccessSound()
                                                Toast.makeText(context, "New update available: ${result.latestVersion}!", Toast.LENGTH_LONG).show()
                                            } else {
                                                Toast.makeText(context, "App is up to date!", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                },
                                isPrimary = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            3 -> {
                // PLAY STORE & PRIVACY SOVEREIGNTY
                item(key = "play_store_legal_card") {
                    GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonCyan) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text("🛡️ Google Play Store Compliance & Privacy", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "NeuroBox adheres strictly to Google Play Store User Data Safety guidelines: 100% offline-first, zero tracking cookies, zero cloud telemetry, hardware AES-256 local database encryption, and instant right-to-erasure data wipes.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                GlassButton(
                                    text = "📄 Privacy Policy",
                                    onClick = { showPrivacyPolicyDialog = true },
                                    modifier = Modifier.weight(1f)
                                )
                                GlassButton(
                                    text = "📜 Terms of Service",
                                    onClick = { showTermsDialog = true },
                                    isPrimary = false,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                item(key = "backup_and_wipe_card") {
                    GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonPink) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text("💾 Data Sovereignty & 1-Tap Erasure", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                GlassButton(
                                    text = "📤 Export .nbx",
                                    onClick = { NeuroBoxBackupEngine.shareBackup(context) },
                                    modifier = Modifier.weight(1f)
                                )
                                GlassButton(
                                    text = "📥 Restore .nbx",
                                    onClick = { NeuroBoxBackupEngine.restoreFromClipboard(context) },
                                    isPrimary = false,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            GlassButton(
                                text = "🗑️ Erase All User Data (Reset to Zero)",
                                onClick = { showWipeConfirmDialog = true },
                                isPrimary = false,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            4 -> {
                // USER GUIDE & DOCUMENTATION
                item(key = "user_guide_card") {
                    Text("📖 NeuroBox User Manual & Feature Guide:", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Read this complete guide to understand every part of the app in simple, clear English.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(10.dp))

                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "1. How Binaural Beats Work (Acoustic Science)\n• Put on stereo headphones (earphones). This is required!\n• The app plays a 216Hz carrier sound in your left ear and 256Hz in your right ear.\n• Your brain automatically detects the difference (40Hz) and synchronizes your brainwaves.\n• 'Lone Wolf' mode (40Hz Gamma) puts you into intense, uninterrupted deep focus.\n• 'Alpha Flow' (10Hz) brings relaxed creativity and easy reading.\n• 'Theta Insight' (6Hz) enables deep intuition and calm daydreaming.\n• 'Delta Rest' (2Hz) helps with power naps and restorative sleep.",
                                fontSize = 12.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                            Text(
                                text = "2. 40 Master Brain Games\n• 40 distinct cognitive arenas covering logic, memory, reflex, tempo, and spatial rotation.\n• High scores and accuracy are saved to local personal best records automatically.\n• Break records to earn XP and level up from Zen Apprentice to Lone Wolf Grandmaster.",
                                fontSize = 12.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                            Text(
                                text = "3. 35 Productivity Power Tools\n• Complete agile suite: Kanban, 16-week Habit Heatmaps, Eisenhower Matrix, Pomodoro Architect, Sensory Grounding, and Stoic Wisdom.",
                                fontSize = 12.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            containerColor = Color(0xFF101322),
            title = {
                Text("Edit Focus Profile", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Personalize your name, bio, and avatar:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Display Name") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonCyan
                        )
                    )

                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Focus Bio / Mantra") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonCyan
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    GamificationManager.updateProfile(editName, editBio, editAvatar)
                    showEditProfileDialog = false
                    Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Save", color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    // Wipe Confirmation Dialog
    if (showWipeConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showWipeConfirmDialog = false },
            containerColor = Color(0xFF141926),
            title = { Text("⚠️ Erase All Data?", color = NeonPink, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "This will wipe all your tree growth, focus minutes, habits, XP, level, and personal bests, resetting everything to ZERO (0). This action is permanent and compliant with Google Play data deletion rights.",
                    color = Color.White,
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    NeuroBoxBackupEngine.eraseAllUserData(context)
                    showWipeConfirmDialog = false
                    Toast.makeText(context, "All user data reset to zero.", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Erase Everything", color = NeonPink, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWipeConfirmDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    // Privacy Policy Dialog
    if (showPrivacyPolicyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicyDialog = false },
            containerColor = Color(0xFF141926),
            title = { Text("📄 Privacy Policy", color = NeonCyan, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "NeuroBox Privacy Policy (Last Updated: October 2026)\n\n" +
                            "Developer: Samir Ahmmed (https://github.com/samir-ahmmed-tech/Aura-Focus-Game)\n\n" +
                            "1. Zero Personal Data Collection: NeuroBox is an offline-first application. We do NOT collect, transmit, store, or sell any of your personal information, focus logs, habits, or biometric check-ins.\n\n" +
                            "2. Local Storage Sovereignty: All data generated inside NeuroBox is saved locally on your physical device. No account creation or remote servers are required.\n\n" +
                            "3. AES-256 Hardware Encryption: Backups exported by the user are encrypted locally using AES-256-GCM. You have 100% control over your files.\n\n" +
                            "4. Media & Audio Storage: The READ_MEDIA_AUDIO permission is exclusively utilized locally by the media player to scan and play audio tracks stored on your own device.\n\n" +
                            "5. Right to Erasure: You have the unconditional right to erase all data at any moment via the 'Erase All Data' button in Settings.",
                        color = Color(0xEEFFFFFF),
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyPolicyDialog = false }) {
                    Text("Close", color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Terms of Service Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            containerColor = Color(0xFF141926),
            title = { Text("📜 Terms of Service", color = NeonAmber, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "NeuroBox Terms of Service (Last Updated: October 2026)\n\n" +
                            "Developer: Samir Ahmmed\n\n" +
                            "1. Acceptance of Terms: By downloading or using NeuroBox, you agree to these Terms of Service.\n\n" +
                            "2. Wellness & Focus Disclaimer: NeuroBox provides cognitive exercises, ambient soundscapes, and binaural frequencies for focus and productivity. It is NOT a medical device and is not intended to diagnose, treat, cure, or prevent any neurological or psychological condition.\n\n" +
                            "3. Safe Audio Practices: Users should listen to binaural beats and ambient sounds at safe, comfortable volume levels. Do not use audio features while driving or operating heavy machinery.\n\n" +
                            "4. Offline Software License: NeuroBox is licensed to you for personal, non-commercial use on your mobile devices.",
                        color = Color(0xEEFFFFFF),
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("Understood", color = NeonAmber, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
