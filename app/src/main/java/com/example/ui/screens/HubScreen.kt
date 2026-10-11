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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GamificationManager
import com.example.util.AuraNotificationHelper
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassChip
import com.example.ui.theme.AuraThemeMode
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import android.view.SoundEffectConstants
import androidx.compose.ui.platform.LocalView
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import com.example.ui.theme.EmeraldMint
import com.example.ui.theme.RoseGold
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet

import com.example.ui.theme.CardBackdropStyle
import com.example.ui.theme.LocalCardBackdropStyle
import com.example.util.AuraSoundManager
import com.example.util.NeuroBoxBackupEngine

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
    var selectedSection by remember { mutableStateOf(0) } // 0: Focus (30), 1: Music (20), 2: User Manual / Readme
    val gamificationState by GamificationManager.state.collectAsState()
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showWipeConfirmDialog by remember { mutableStateOf(false) }

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 120.dp)
    ) {
        // Top Header
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
                    text = "Gamification profile, 50+ settings & user guide",
                    style = MaterialTheme.typography.bodySmall,
                    color = NeonCyan
                )
            }
            GlassChip(label = "Level ${gamificationState.currentLevel}", color = NeonLime, selected = true)
        }

        Spacer(modifier = Modifier.height(18.dp))

        var showEditProfileDialog by remember { mutableStateOf(false) }
        var editName by remember(gamificationState.userName) { mutableStateOf(gamificationState.userName) }
        var editBio by remember(gamificationState.userBio) { mutableStateOf(gamificationState.userBio) }
        var editAvatar by remember(gamificationState.userAvatarEmoji) { mutableStateOf(gamificationState.userAvatarEmoji) }

        // 1. COMPREHENSIVE FLOW MASTERY & TELEMETRY PROFILE DASHBOARD
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = NeonCyan,
            glowRadius = 14.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header: Avatar, Name, Rank, Edit
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
                                        text = "Lv.${gamificationState.currentLevel}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonLime
                                    )
                                }
                            }
                            Text(
                                text = gamificationState.rankTitle,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NeonCyan
                            )
                        }
                    }

                    // Streak Pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x20FFAA00))
                            .border(1.dp, NeonAmber, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.LocalFireDepartment, "Streak", tint = NeonAmber, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${gamificationState.currentStreakDays}d Streak", fontWeight = FontWeight.Bold, color = NeonAmber, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bio Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x12FFFFFF))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "\"${gamificationState.userBio}\"",
                        fontSize = 11.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 4-STAT METRIC TELEMETRY GRID (Never empty!)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Stat 1: Focus Minutes
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x18FFFFFF))
                            .border(1.dp, Color(0x28FFFFFF), RoundedCornerShape(10.dp))
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⏱️ ${gamificationState.totalFocusMinutes}m", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonCyan)
                            Text("Focus Time", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Stat 2: Habits Done
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x18FFFFFF))
                            .border(1.dp, Color(0x28FFFFFF), RoundedCornerShape(10.dp))
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎯 ${gamificationState.habitsCompletedTotal}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonLime)
                            Text("Habits Met", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Stat 3: Games Played
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x18FFFFFF))
                            .border(1.dp, Color(0x28FFFFFF), RoundedCornerShape(10.dp))
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎮 ${gamificationState.gamesPlayedTotal}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonPink)
                            Text("Games Won", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Stat 4: Daily Energy
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x18FFFFFF))
                            .border(1.dp, Color(0x28FFFFFF), RoundedCornerShape(10.dp))
                            .padding(vertical = 8.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⚡ 98%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NeonAmber)
                            Text("Flow Index", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // XP Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0x22FFFFFF))
                ) {
                    val progress = (gamificationState.currentXp.toFloat() / gamificationState.xpForNextLevel.toFloat()).coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(8.dp)
                            .background(Brush.horizontalGradient(listOf(NeonCyan, NeonLime)))
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Current: ${gamificationState.currentXp} XP",
                        fontSize = 11.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                    val neededXp = (gamificationState.xpForNextLevel - gamificationState.currentXp).coerceAtLeast(0)
                    Text(
                        text = "$neededXp XP to Level ${gamificationState.currentLevel + 1}",
                        fontSize = 10.sp,
                        color = NeonLime,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons Row (3 Actions: Edit Profile, Claim XP, Test Notif)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Edit Profile Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x18FFFFFF))
                            .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .clickable {
                                editName = gamificationState.userName
                                editBio = gamificationState.userBio
                                editAvatar = gamificationState.userAvatarEmoji
                                showEditProfileDialog = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Edit, "Edit", tint = NeonCyan, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    // Claim Daily Reward Button
                    Box(
                        modifier = Modifier
                            .weight(1.1f)
                            .height(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeonLime.copy(alpha = 0.18f))
                            .border(1.dp, NeonLime.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                            .clickable {
                                GamificationManager.claimDailyBonus()
                                Toast.makeText(context, "Daily flow reward +100 XP claimed! 🔥", Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CardGiftcard, "Claim", tint = NeonLime, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Claim +100 XP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                        }
                    }

                    // Test Notification Alert Button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeonAmber.copy(alpha = 0.15f))
                            .border(1.dp, NeonAmber.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .clickable {
                                AuraNotificationHelper.showBrainGameReminderNotification(context, "Flanker & Chimp Memory")
                                Toast.makeText(context, "Sent Notification Alert! Check your status bar 🔔", Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NotificationsActive, "Alert", tint = NeonAmber, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Send Alert", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonAmber)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Badges Gallery Showcase
                Text(
                    text = "Unlocked Mastery Badges:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    gamificationState.badges.forEach { badge ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (badge.isUnlocked) Color(0x22FFFFFF) else Color(0x0CFFFFFF))
                                .border(
                                    1.dp,
                                    if (badge.isUnlocked) NeonCyan.copy(alpha = 0.6f) else Color(0x18FFFFFF),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(badge.iconEmoji, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = badge.title,
                                    fontSize = 10.sp,
                                    color = if (badge.isUnlocked) Color.White else Color(0x88FFFFFF),
                                    fontWeight = if (badge.isUnlocked) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // COGNITIVE PERFORMANCE ANALYTICS & LONGITUDINAL TELEMETRY
        // (Moved from game & tool screen into settings per user request)
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = NeonCyan,
            glowRadius = 10.dp,
            onClick = onOpenAnalytics
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeonCyan.copy(alpha = 0.22f))
                            .border(1.5.dp, NeonCyan, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📊", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Cognitive IQ & Performance Analytics",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "IQ, EX, SQ, ESQ, MQ, RQ (1-day, 7-day & 1-month growth)",
                            fontSize = 11.sp,
                            color = NeonCyan
                        )
                    }
                }
                Icon(Icons.Default.ChevronRight, contentDescription = "Open Analytics", tint = NeonCyan)
            }
        }

        // EDIT PROFILE DIALOG
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

                        // Avatar Picker Row
                        Text("Avatar Icon:", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf("⚡", "🐺", "🧘", "🚀", "🔮", "🎧", "🧠").forEach { emoji ->
                                val isSelected = editAvatar == emoji
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) NeonCyan.copy(alpha = 0.3f) else Color(0x14FFFFFF))
                                        .border(1.5.dp, if (isSelected) NeonCyan else Color.Transparent, CircleShape)
                                        .clickable { editAvatar = emoji },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(emoji, fontSize = 16.sp)
                                }
                            }
                        }

                        // Name input
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Display Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = Color(0x30FFFFFF),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        // Bio input
                        OutlinedTextField(
                            value = editBio,
                            onValueChange = { editBio = it },
                            label = { Text("Focus Bio / Mantra") },
                            maxLines = 2,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                unfocusedBorderColor = Color(0x30FFFFFF),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        GamificationManager.updateProfile(editName, editBio, editAvatar)
                        showEditProfileDialog = false
                        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Save Profile", color = NeonLime, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditProfileDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Dynamic Themes Card (All 5 Glassmorphic Themes)
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("5 Glassmorphic Visual Themes", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    GlassChip(label = currentTheme.name.replace('_', ' '), color = NeonCyan, selected = true)
                }
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
                                .height(44.dp)
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

        Spacer(modifier = Modifier.height(16.dp))

        // Card Backdrop Glow & Shape Customization Card (Circle, Diamond, Hexagon, Star, Wave, OFF)
        GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonCyan, glowRadius = 8.dp) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🔮 Card Ambient Backdrop Glow & Shape",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Customize or turn off the shape behind each card",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    GlassChip(
                        label = cardBackdropStyle.label,
                        color = if (cardBackdropStyle == CardBackdropStyle.OFF) Color.Gray else NeonCyan,
                        selected = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CardBackdropStyle.values().forEach { style ->
                        val isPicked = cardBackdropStyle == style
                        val chipColor = if (style == CardBackdropStyle.OFF) NeonPink else NeonCyan
                        Box(
                            modifier = Modifier
                                .height(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isPicked) chipColor.copy(alpha = 0.28f) else Color(0x12FFFFFF))
                                .border(1.5.dp, if (isPicked) chipColor else Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                                .clickable { onCardBackdropStyleChange(style) }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(style.iconEmoji, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = style.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPicked) chipColor else Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // UI DENSITY (DP SCALING) & TOUCH SOUND CONTROLS CARD
        val view = LocalView.current
        GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonAmber, glowRadius = 8.dp) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📐 UI Density & Display Scaling (DP)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Scale UI elements, paddings & typography",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    GlassChip(
                        label = "${(uiScale * 100).toInt()}% Scale",
                        color = NeonAmber,
                        selected = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Density preset pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        Pair(0.85f, "Compact (85%)"),
                        Pair(1.0f, "Standard (100%)"),
                        Pair(1.15f, "Spacious (115%)")
                    ).forEach { (scale, label) ->
                        val isSelected = kotlin.math.abs(uiScale - scale) < 0.05f
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) NeonAmber.copy(alpha = 0.28f) else Color(0x14FFFFFF))
                                .border(1.dp, if (isSelected) NeonAmber else Color(0x20FFFFFF), RoundedCornerShape(10.dp))
                                .clickable { onUiScaleChange(scale) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NeonAmber else Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Smooth slider
                Slider(
                    value = uiScale,
                    onValueChange = { onUiScaleChange(it) },
                    valueRange = 0.8f..1.25f,
                    steps = 8,
                    colors = SliderDefaults.colors(
                        thumbColor = NeonAmber,
                        activeTrackColor = NeonAmber,
                        inactiveTrackColor = Color(0x30FFFFFF)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Touch Sound Toggle & Feedback
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x12FFFFFF))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🔔 Interactive Touch Sound & Feedback",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Acoustic click sounds when tapping buttons & cards",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = touchSoundEnabled,
                            onCheckedChange = {
                                onTouchSoundToggle(it)
                                if (it) {
                                    AuraSoundManager.playTapSound(context, view)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonCyan,
                                checkedTrackColor = NeonCyan.copy(alpha = 0.4f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                GlassButton(
                    text = "🔊 Tap Here to Test Click Sound",
                    onClick = { AuraSoundManager.playTapSound(context, view) },
                    isPrimary = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // DEFAULT HOME SCREEN LANDING TAB CUSTOMIZER
        GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonLime, glowRadius = 8.dp) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🏠 Default Home Screen Landing Tab",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Choose which tab opens automatically when launching app",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    GlassChip(
                        label = gamificationState.defaultHomeScreenTab,
                        color = NeonLime,
                        selected = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        Pair("FOCUS", "⏱️ Focus (Default)"),
                        Pair("MUSIC", "🎧 Music Player"),
                        Pair("GAMES", "🎮 Brain Games"),
                        Pair("TOOLS", "🛠️ Productivity Suite"),
                        Pair("HUB", "⚙️ Hub & Settings")
                    ).forEach { (tabKey, title) ->
                        val isSelected = gamificationState.defaultHomeScreenTab == tabKey
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NeonLime.copy(alpha = 0.28f) else Color(0x12FFFFFF))
                                .border(1.2.dp, if (isSelected) NeonLime else Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                                .clickable {
                                    GamificationManager.setDefaultHomeScreenTab(tabKey)
                                    Toast.makeText(context, "Default home screen set to $title", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NeonLime else Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // MUSIC PLAYER 5-LAYOUT DESIGNS CUSTOMIZER
        GlassCard(modifier = Modifier.fillMaxWidth(), glowColor = NeonCyan, glowRadius = 8.dp) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🎵 Music Player Layout (5 Designs)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "2 Circular & 3 Horizontal Hi-Fi Player Layouts",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    GlassChip(
                        label = when (gamificationState.musicPlayerDesign) {
                            0 -> "Vinyl (Circle)"
                            1 -> "Mandala (Circle)"
                            2 -> "Wave (Horiz)"
                            3 -> "Cassette (Horiz)"
                            else -> "Soundbar (Horiz)"
                        },
                        color = NeonCyan,
                        selected = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        0 to "⭕ Vinyl Disc (Circle 1)",
                        1 to "🪐 Sacred Mandala (Circle 2)",
                        2 to "🌊 Cyber Wave Deck (Horiz 1)",
                        3 to "📼 Retro Cassette (Horiz 2)",
                        4 to "🔊 Horizon Soundbar (Horiz 3)"
                    ).forEach { (idx, title) ->
                        val isSelected = gamificationState.musicPlayerDesign == idx
                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NeonCyan.copy(alpha = 0.28f) else Color(0x12FFFFFF))
                                .border(1.2.dp, if (isSelected) NeonCyan else Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                                .clickable {
                                    GamificationManager.setMusicPlayerDesign(idx)
                                    Toast.makeText(context, "Player layout changed to $title", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NeonCyan else Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3-Way Navigation Tabs: 30 Focus vs 20 Music vs Feature Guide
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x14FFFFFF))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selectedSection == 0) NeonCyan.copy(alpha = 0.25f) else Color.Transparent)
                    .clickable { selectedSection = 0 },
                contentAlignment = Alignment.Center
            ) {
                Text("30 Focus", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (selectedSection == 0) NeonCyan else Color.White)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selectedSection == 1) NeonPink.copy(alpha = 0.25f) else Color.Transparent)
                    .clickable { selectedSection = 1 },
                contentAlignment = Alignment.Center
            ) {
                Text("20 Music", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (selectedSection == 1) NeonPink else Color.White)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selectedSection == 2) NeonLime.copy(alpha = 0.25f) else Color.Transparent)
                    .clickable { selectedSection = 2 },
                contentAlignment = Alignment.Center
            ) {
                Text("📖 Guide", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (selectedSection == 2) NeonLime else Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section Content
        when (selectedSection) {
            0 -> {
                // 30 FOCUS SETTINGS WITH PLAIN ENGLISH DETAILS
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
            1 -> {
                // 5 MUSIC PLAYER DESIGNS CUSTOMIZER (3 HORIZONTAL, 2 CIRCULAR)
                Text("Music Player Layout & Theme (5 Styles):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NeonPink)
                Spacer(modifier = Modifier.height(6.dp))
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = NeonPink,
                    glowRadius = 10.dp
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Customize the active visualizer and layout of your music player. Choose between 3 modern horizontal deck styles or 2 hypnotic circular orbit discs.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        val currentDesign = gamificationState.musicPlayerDesign

                        listOf(
                            Triple(0, "⭕ Holographic Vinyl Disc", "Concentric vinyl grooves, 36 reactive outer bars & zero-leak center spindle (Circular 1)"),
                            Triple(1, "🪐 Sacred Mandala Orbit", "8-node multi-orbital gyro mandala with harmonic pulse geometry (Circular 2)"),
                            Triple(2, "🌊 Cyber Wave Deck", "Real-time stereo audio waveform analyzer with dancing spectrum bars and peak dB meters (Horizontal 1)"),
                            Triple(3, "📼 Retro Synth Cassette", "Vintage cyber cassette player with twin spinning spools, tape counter, and Dolby ribbon (Horizontal 2)"),
                            Triple(4, "🔊 Horizon Soundbar", "Modern 32-band rainbow equalizer spectrum with hi-res lossless master audio console (Horizontal 3)")
                        ).forEach { (idx, title, desc) ->
                            val isSelected = currentDesign == idx
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) NeonPink.copy(alpha = 0.2f) else Color(0x0CFFFFFF))
                                    .border(1.dp, if (isSelected) NeonPink else Color(0x18FFFFFF), RoundedCornerShape(12.dp))
                                    .clickable { GamificationManager.setMusicPlayerDesign(idx) }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { GamificationManager.setMusicPlayerDesign(idx) },
                                    colors = RadioButtonDefaults.colors(selectedColor = NeonPink)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text(desc, fontSize = 10.sp, color = if (isSelected) NeonPink else Color(0x80FFFFFF))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 20 MUSIC SETTINGS WITH PLAIN ENGLISH DETAILS
                Text("Normal Music Player Controls (20 Options):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NeonPink)
                Spacer(modifier = Modifier.height(8.dp))
                GlassCard(modifier = Modifier.fillMaxWidth()) {
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
            2 -> {
                // FEATURE GUIDE & README IN SIMPLE, CLEAR ENGLISH
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
                            text = "2. Playing Songs From Your Phone\n• Tap the Music tab in the bottom bar.\n• When prompted, grant media storage permission.\n• NeuroBox automatically searches your internal storage and SD card for all MP3, AAC, and WAV songs.\n• Tap any song to play it immediately with repeat, shuffle, and next/previous controls.",
                            fontSize = 12.sp,
                            color = Color.White,
                            lineHeight = 18.sp
                        )

                        Text(
                            text = "3. Soundscape Stacker Mixer\n• In the Music tab, look at the Soundscape Stacker Mixer card.\n• It allows you to play 3 sound layers at the same time:\n   Layer 1: Your favorite music track from phone storage.\n   Layer 2: 40Hz Lone Wolf binaural brainwaves.\n   Layer 3: Soothing continuous rainfall sound.\n• Use the volume sliders to mix each layer to your preferred hearing level.",
                            fontSize = 12.sp,
                            color = Color.White,
                            lineHeight = 18.sp
                        )

                        Text(
                            text = "4. Focus Timer & Unlimited Flow Mode\n• Deep Focus: Standard 25-minute Pomodoro sprint.\n• Short Break: 5-minute breather between deep work.\n• Long Break: 15-minute restorative rest after 4 sprints.\n• Custom Slider: Adjust your session anywhere from 1 minute up to 180 minutes.\n• ♾️ Unlimited Flow Mode: Tap the top chip to switch from a countdown timer to an infinite stopwatch that counts up with no time limit.",
                            fontSize = 12.sp,
                            color = Color.White,
                            lineHeight = 18.sp
                        )

                        Text(
                            text = "5. Cognitive Training Grounds\nSharpen neuroplasticity, impulse control & fluid memory:\n• 🧮 Mental Math Sprint: Solves arithmetic equations under time pressure to sharpen logic.\n• 🎨 Stroop Color Clash: Tests impulse inhibition by matching ink colors against written words.\n• 🧩 Memory Matrix: Memorizes spatial tiles on a grid to expand visual working memory.\n• 🔢 Speed Sequence: Taps numbers as fast as possible to measure visual scanning.\n• ⚡ Micro-Reaction Tap: Tests your reaction speed down to the exact millisecond when the screen turns green.\n• 🧠 Digit Span Recall: Remembers increasingly long sequences of numbers to expand cognitive storage.\n• 🔤 Word Unscramble: Re-arranges scrambled anagram letters into focus terms.\n• 📐 Spatial Angle Match: Mentally rotates geometric shapes to find matching angles.\n• 🎯 Dual N-Back Mini: The gold standard game for increasing fluid IQ by recalling grid positions.\n• 🏹 Flanker Direction Clash: Tests focus by identifying target direction amidst noisy arrow flankers.\n• 🐵 Chimp Working Memory: Memorizes number locations on a grid before they flip hidden.\n• 🥁 Rhythm Beat Tap: Auditory tempo entrainment test matching BPM pulses.\n• 🔄 Cognitive Flex Duel: Rapid dynamic task switching between colors and shapes.\n• ⚡ Cyber Glitch Sequence: Overclocked cyberpunk memory sequence matrix recall.\n• 💥 Chroma Clash Inversion: Neural conflict inhibition & reaction speed.\n• 🎯 Target Lock Radar: Chaotic tactical drone tracking inside radar scope.\n• 🎵 Pitch Match Ear Trainer: Neuro-acoustic audio frequency pitch discrimination.\n• 🕵️ Codebreaker Cipher: Decrypts alien glyph sequences under high pressure.",
                            fontSize = 12.sp,
                            color = Color.White,
                            lineHeight = 18.sp
                        )

                        Text(
                            text = "6. Productivity Power Suite\nComprehensive deep work productivity system:\n• 📋 Smart Kanban Boards: Agile workflow columns with custom task management.\n• 📈 Deep Habit Tracker: 16-week GitHub heatmap with streaks & shields.\n• 📖 Daily Journal & Gratitude: Morning intentions and evening reflections.\n• 📝 Quick Markdown Notes: Instant scratchpad for ideas and code.\n• ⚖️ Eisenhower Matrix: 4-quadrant urgent/important organizer.\n• ⏱️ Focus Session Logger: Tracks daily focus minutes & time audits.\n• 🧘 Breathwork Pacer: Box breathing 4-4-4-4 to lower heart rate.\n• 🏛️ Daily Stoic Wisdom: Marcus Aurelius & Seneca quotes with user custom additions.\n• 🛡️ Distraction Vault: Digital focus oath locking temptations during deep sessions.\n• 💾 Data Sovereignty IO: 1-tap backup of your database into clean JSON.\n• ⏱️ Time-Boxing Interval Matrix: Visual deep work blocks and sprint scheduling.\n• ⚡ Energy & Bio-Rhythm Audit: Bio-energetic mood tracking with matched binaural audio.\n• ⏱️ Sprint Time-Boxing: Multi-sprint flow intervals with goal checklists.\n• 🎛️ Aura Ambience Synthesizer: Multi-channel nature sound mixing.\n• ⚔️ Quest Vault & Focus Bounty: Daily & weekly RPG productivity bounties.",
                            fontSize = 12.sp,
                            color = Color.White,
                            lineHeight = 18.sp
                        )

                        Text(
                            text = "7. Level & XP System (Stored Cleanly in Settings)\n• Your productivity level, streak days, and unlocked achievement badges are kept exclusively in this Settings screen.\n• Every session, habit check-in, and brain game you complete silently raises your level and streak.\n• Ranks progress from Zen Apprentice → Flow Practitioner → Focus Adept → Cognitive Master.",
                            fontSize = 12.sp,
                            color = Color.White,
                            lineHeight = 18.sp
                        )

                        Text(
                            text = "8. Instant High Performance & Cloud Connectivity\n• NeuroBox provides instant zero-lag response with high-speed local caching while staying fully connected to the network.\n• Keep internet enabled to enjoy continuous streaming audio updates, cloud backup compatibility, and uninterrupted ambient focus.",
                            fontSize = 12.sp,
                            color = Color.White,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        Spacer(modifier = Modifier.height(20.dp))

        // 4. OFFLINE DATA SOVEREIGNTY & AES-256-GCM ENCRYPTED LOCAL BACKUP
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = NeonCyan,
            glowRadius = 10.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🛡️ Offline Data Sovereignty & Encrypted Vault",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "100% Offline • Hardware AES-256-GCM • Zero Server Costs",
                            fontSize = 10.sp,
                            color = NeonCyan
                        )
                    }
                    GlassChip(label = "SOVEREIGN", color = NeonLime, selected = true)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Your data belongs solely to you. All stats, tree progress, habits, and cognitive records are stored locally with zero cloud telemetry. Export or restore your encrypted database anytime.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeonCyan.copy(alpha = 0.2f))
                            .border(1.dp, NeonCyan, RoundedCornerShape(12.dp))
                            .clickable {
                                NeuroBoxBackupEngine.shareBackup(context)
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📤 Export .nbx", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonCyan)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x18FFFFFF))
                            .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(12.dp))
                            .clickable {
                                NeuroBoxBackupEngine.copyBackupToClipboard(context)
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📋 Copy Token", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeonLime.copy(alpha = 0.2f))
                            .border(1.dp, NeonLime, RoundedCornerShape(12.dp))
                            .clickable {
                                NeuroBoxBackupEngine.restoreFromClipboard(context)
                            }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📥 Restore", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonLime)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Play Store Right-to-be-Forgotten Data Wipe
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(NeonPink.copy(alpha = 0.12f))
                        .border(1.dp, NeonPink.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable { showWipeConfirmDialog = true }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🗑️ Erase All User Data (Reset to Zero)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NeonPink)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. LEGAL & PLAY STORE COMPLIANCE (Privacy Policy & Terms)
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = Color(0x30FFFFFF),
            glowRadius = 6.dp
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "⚖️ Legal & Play Store Compliance",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x14FFFFFF))
                            .border(1.dp, Color(0x25FFFFFF), RoundedCornerShape(10.dp))
                            .clickable { showPrivacyPolicyDialog = true }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📄 Privacy Policy", fontSize = 11.sp, color = NeonCyan)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x14FFFFFF))
                            .border(1.dp, Color(0x25FFFFFF), RoundedCornerShape(10.dp))
                            .clickable { showTermsDialog = true }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📜 Terms of Service", fontSize = 11.sp, color = NeonAmber)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }

    // Wipe Confirmation Dialog
    if (showWipeConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showWipeConfirmDialog = false },
            containerColor = Color(0xFF141926),
            title = { Text("⚠️ Erase All Data?", color = NeonPink, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "This will wipe all your tree growth, focus minutes, habits, XP, level, and personal bests, resetting everything to ZERO (0). This action is permanent and compliant with data deletion rights.",
                    color = Color.White,
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    NeuroBoxBackupEngine.eraseAllUserData(context)
                    showWipeConfirmDialog = false
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

}
