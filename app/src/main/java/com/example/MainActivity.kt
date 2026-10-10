package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.data.GamificationManager
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.compose.runtime.mutableFloatStateOf
import com.example.audio.LocalMusicPlayerEngine
import com.example.audio.MediaStoreAudioScanner
import com.example.audio.MusicTrack
import com.example.ui.screens.BrainGamesScreen
import com.example.ui.screens.FocusScreen
import com.example.ui.screens.FullBreathworkScreen
import com.example.ui.screens.FullDatabaseManagerScreen
import com.example.ui.screens.FullDigitSpanScreen
import com.example.ui.screens.FullDistractionVaultScreen
import com.example.ui.screens.FullDualNBackScreen
import com.example.ui.screens.FullEisenhowerScreen
import com.example.ui.screens.FullFocusAuditScreen
import com.example.ui.screens.FullHabitsScreen
import com.example.ui.screens.FullJournalScreen
import com.example.ui.screens.FullKanbanScreen
import com.example.ui.screens.FullMemoryMatrixScreen
import com.example.ui.screens.FullMentalMathScreen
import com.example.ui.screens.FullMicroReactionScreen
import com.example.ui.screens.FullNotesScreen
import com.example.ui.screens.FullSpatialMatchScreen
import com.example.ui.screens.FullSpeedSequenceScreen
import com.example.ui.screens.FullStoicWisdomScreen
import com.example.ui.screens.FullStroopScreen
import com.example.ui.screens.FullWordUnscrambleScreen
import com.example.ui.screens.FullFlankerScreen
import com.example.ui.screens.FullChimpMemoryScreen
import com.example.ui.screens.FullRhythmBeatScreen
import com.example.ui.screens.FullCognitiveFlexDuelScreen
import com.example.ui.screens.FullTimeBoxingScreen
import com.example.ui.screens.FullMoodEnergyAuditScreen
import com.example.ui.screens.FullCyberGlitchScreen
import com.example.ui.screens.FullChromaClashScreen
import com.example.ui.screens.FullTargetLockRadarScreen
import com.example.ui.screens.FullPitchMatchScreen
import com.example.ui.screens.FullCodebreakerCipherScreen
import com.example.ui.screens.FullQuantumFlowMazeScreen
import com.example.ui.screens.FullNeuroBalanceTiltScreen
import com.example.ui.screens.FullSprintTimeBoxScreen
import com.example.ui.screens.FullAmbienceSynthesizerScreen
import com.example.ui.screens.FullQuestVaultScreen
import com.example.ui.screens.FullDopamineFastScreen
import com.example.ui.screens.FullBilateralEyePacerScreen
import com.example.ui.screens.ZenGardenScreen
import com.example.ui.screens.AnalyticsAndHistoryScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearOutSlowInEasing
import com.example.util.AuraNotificationHelper
import com.example.util.AuraSoundManager
import androidx.compose.ui.platform.LocalView
import com.example.ui.screens.HubScreen
import com.example.ui.screens.MusicPlayerScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.theme.AuraFocusTheme
import com.example.ui.theme.AuraThemeMode
import com.example.ui.theme.CardBackdropStyle
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import kotlinx.coroutines.launch

enum class AuraNavDestination(val title: String, val icon: ImageVector, val tag: String) {
    FOCUS("Focus", Icons.Default.Timer, "nav_focus"),
    MUSIC("Music", Icons.Default.Headphones, "nav_music"),
    GAMES("Games", Icons.Default.SportsEsports, "nav_games"),
    TOOLS("Tools", Icons.Default.Build, "nav_tools"),
    HUB("Hub", Icons.Default.Settings, "nav_hub")
}

class MainActivity : ComponentActivity() {
    private var musicEngine: LocalMusicPlayerEngine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure transparent status and navigation bars without system black scrims
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        musicEngine = LocalMusicPlayerEngine(applicationContext)

        setContent {
            var currentTheme by remember { mutableStateOf(AuraThemeMode.AURORA_DARK) }
            var uiScale by remember { mutableFloatStateOf(1.0f) }
            var touchSoundEnabled by remember { mutableStateOf(true) }
            var cardBackdropStyle by remember { mutableStateOf(CardBackdropStyle.CIRCLE_HALO) }

            AuraFocusTheme(
                themeMode = currentTheme,
                uiScale = uiScale,
                touchSoundEnabled = touchSoundEnabled,
                cardBackdropStyle = cardBackdropStyle
            ) {
                musicEngine?.let { engine ->
                    AuraFocusApp(
                        musicEngine = engine,
                        currentTheme = currentTheme,
                        onThemeChange = { currentTheme = it },
                        uiScale = uiScale,
                        onUiScaleChange = { uiScale = it },
                        touchSoundEnabled = touchSoundEnabled,
                        onTouchSoundToggle = { touchSoundEnabled = it },
                        cardBackdropStyle = cardBackdropStyle,
                        onCardBackdropStyleChange = { cardBackdropStyle = it }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        musicEngine?.release()
    }
}

@Composable
fun AuraFocusApp(
    musicEngine: LocalMusicPlayerEngine,
    currentTheme: AuraThemeMode,
    onThemeChange: (AuraThemeMode) -> Unit,
    uiScale: Float,
    onUiScaleChange: (Float) -> Unit,
    touchSoundEnabled: Boolean,
    onTouchSoundToggle: (Boolean) -> Unit,
    cardBackdropStyle: CardBackdropStyle,
    onCardBackdropStyleChange: (CardBackdropStyle) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var currentTab by remember { mutableStateOf(AuraNavDestination.FOCUS) }

    // Standalone Full Screen Navigation States
    var activeGameId by remember { mutableStateOf<String?>(null) }
    var activeToolId by remember { mutableStateOf<String?>(null) }
    var showAnalyticsScreen by remember { mutableStateOf(false) }

    // Unified Multi-Permission Launcher for Notifications & Local Audio Auto-Import
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultsMap ->
        val notifGranted = resultsMap[Manifest.permission.POST_NOTIFICATIONS] ?: true
        if (notifGranted) {
            AuraNotificationHelper.createNotificationChannels(context)
        }
        val audioGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            resultsMap[Manifest.permission.READ_MEDIA_AUDIO] ?: false
        } else {
            resultsMap[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
        }
        if (audioGranted) {
            musicEngine.scanDeviceAudio(context)
            Toast.makeText(context, "Scanning phone audio files...", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        AuraNotificationHelper.createNotificationChannels(context)

        val neededPermissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(Manifest.permission.READ_MEDIA_AUDIO)
            } else {
                musicEngine.scanDeviceAudio(context)
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                musicEngine.scanDeviceAudio(context)
            }
        }

        if (neededPermissions.isNotEmpty()) {
            permissionsLauncher.launch(neededPermissions.toTypedArray())
        }
    }

    // SAF Fallback Picker
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            var filename = "Imported Audio Track"
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && nameIndex >= 0) {
                    filename = cursor.getString(nameIndex) ?: filename
                }
            }

            val newTrack = MusicTrack(
                id = System.currentTimeMillis().toString(),
                title = filename,
                artist = "Phone Storage",
                uri = uri,
                durationMs = 0
            )
            musicEngine.addTrack(newTrack)
            musicEngine.playTrack(newTrack)
            currentTab = AuraNavDestination.MUSIC
            Toast.makeText(context, "Playing: $filename", Toast.LENGTH_SHORT).show()
        }
    }

    // Root full-screen container with continuous seamless wallpaper (no black bar)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        val activeRoute = when {
            showAnalyticsScreen -> "analytics"
            activeGameId != null -> "game"
            activeToolId != null -> "tool"
            else -> "main"
        }

        // Seamless, lag-free smooth transition across sub-screens and main tabs
        Crossfade(
            targetState = activeRoute,
            animationSpec = tween(110, easing = LinearOutSlowInEasing),
            label = "root_navigation_crossfade",
            modifier = Modifier.fillMaxSize()
        ) { route ->
            when (route) {
                "analytics" -> {
                    AnalyticsAndHistoryScreen(onBack = { showAnalyticsScreen = false })
                }

                // Full Screen Games (7 Brand New High-Cognitive Games Included)
                "game" -> {
                    when (activeGameId) {
                        "g1" -> FullMentalMathScreen(onBack = { activeGameId = null })
                        "g2" -> FullStroopScreen(onBack = { activeGameId = null })
                        "g3" -> FullMemoryMatrixScreen(onBack = { activeGameId = null })
                        "g4" -> FullSpeedSequenceScreen(onBack = { activeGameId = null })
                        "g5" -> FullMicroReactionScreen(onBack = { activeGameId = null })
                        "g6" -> FullDigitSpanScreen(onBack = { activeGameId = null })
                        "g7" -> FullWordUnscrambleScreen(onBack = { activeGameId = null })
                        "g8" -> FullSpatialMatchScreen(onBack = { activeGameId = null })
                        "g9" -> FullDualNBackScreen(onBack = { activeGameId = null })
                        "g11" -> FullFlankerScreen(onBack = { activeGameId = null })
                        "g12" -> FullChimpMemoryScreen(onBack = { activeGameId = null })
                        "g13" -> FullRhythmBeatScreen(onBack = { activeGameId = null })
                        "g15" -> FullCognitiveFlexDuelScreen(onBack = { activeGameId = null })
                        "g16" -> FullCyberGlitchScreen(onBack = { activeGameId = null })
                        "g17" -> FullChromaClashScreen(onBack = { activeGameId = null })
                        "g18" -> FullTargetLockRadarScreen(onBack = { activeGameId = null })
                        "g19" -> FullPitchMatchScreen(onBack = { activeGameId = null })
                        "g20" -> FullCodebreakerCipherScreen(onBack = { activeGameId = null })
                        "g21" -> FullQuantumFlowMazeScreen(onBack = { activeGameId = null })
                        "g22" -> FullNeuroBalanceTiltScreen(onBack = { activeGameId = null })
                        "analytics_history" -> AnalyticsAndHistoryScreen(onBack = { activeGameId = null })
                        "zen_garden" -> ZenGardenScreen(onBack = { activeGameId = null })
                        else -> FullMentalMathScreen(onBack = { activeGameId = null })
                    }
                }

                // Full Screen Tools (17 Tools + Zen Garden)
                "tool" -> {
                    when (activeToolId) {
                        "t1" -> FullKanbanScreen(onBack = { activeToolId = null })
                        "t2" -> FullHabitsScreen(onBack = { activeToolId = null })
                        "t3" -> FullJournalScreen(onBack = { activeToolId = null })
                        "t4" -> FullNotesScreen(onBack = { activeToolId = null })
                        "t5" -> FullEisenhowerScreen(onBack = { activeToolId = null })
                        "t6" -> FullFocusAuditScreen(onBack = { activeToolId = null })
                        "t7" -> FullBreathworkScreen(onBack = { activeToolId = null })
                        "t8" -> FullStoicWisdomScreen(onBack = { activeToolId = null })
                        "t9" -> FullDistractionVaultScreen(onBack = { activeToolId = null })
                        "t10" -> FullDatabaseManagerScreen(onBack = { activeToolId = null })
                        "t11" -> FullTimeBoxingScreen(onBack = { activeToolId = null })
                        "t12" -> FullMoodEnergyAuditScreen(onBack = { activeToolId = null })
                        "t13" -> FullSprintTimeBoxScreen(onBack = { activeToolId = null })
                        "t14" -> FullAmbienceSynthesizerScreen(onBack = { activeToolId = null })
                        "t15" -> FullQuestVaultScreen(onBack = { activeToolId = null })
                        "t16" -> FullDopamineFastScreen(onBack = { activeToolId = null })
                        "t17" -> FullBilateralEyePacerScreen(onBack = { activeToolId = null })
                        "zen_garden" -> ZenGardenScreen(onBack = { activeToolId = null })
                        "analytics_history" -> AnalyticsAndHistoryScreen(onBack = { activeToolId = null })
                        else -> FullNotesScreen(onBack = { activeToolId = null })
                    }
                }

                // Main Tab Screens with ultra-smooth lag-free 90ms transition
                else -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Crossfade(
                            targetState = currentTab,
                            label = "main_tab_fade",
                            animationSpec = tween(90, easing = LinearOutSlowInEasing),
                            modifier = Modifier.fillMaxSize()
                        ) { tab ->
                            when (tab) {
                                AuraNavDestination.FOCUS -> FocusScreen(
                                    binauralEngine = musicEngine.binauralEngine,
                                    onOpenMusicStudio = { currentTab = AuraNavDestination.MUSIC }
                                )
                                AuraNavDestination.MUSIC -> MusicPlayerScreen(
                                    musicEngine = musicEngine,
                                    onImportAudioClick = { audioPickerLauncher.launch("audio/*") }
                                )
                                AuraNavDestination.GAMES -> BrainGamesScreen(
                                    onLaunchGame = { gameId -> activeGameId = gameId }
                                )
                                AuraNavDestination.TOOLS -> ToolsScreen(
                                    onLaunchTool = { toolId -> activeToolId = toolId }
                                )
                                AuraNavDestination.HUB -> HubScreen(
                                    currentTheme = currentTheme,
                                    onThemeChange = onThemeChange,
                                    uiScale = uiScale,
                                    onUiScaleChange = onUiScaleChange,
                                    touchSoundEnabled = touchSoundEnabled,
                                    onTouchSoundToggle = onTouchSoundToggle,
                                    cardBackdropStyle = cardBackdropStyle,
                                    onCardBackdropStyleChange = onCardBackdropStyleChange,
                                    onOpenAnalytics = { showAnalyticsScreen = true }
                                )
                            }
                        }

                // Pure Floating Glass iOS-Style Edge Dock (Floating over background with NO black bar)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .shadow(
                                elevation = 20.dp,
                                shape = RoundedCornerShape(29.dp),
                                ambientColor = Color(0x60000000),
                                spotColor = Color(0x40000000)
                            )
                            .clip(RoundedCornerShape(29.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0x35FFFFFF), Color(0x18FFFFFF))
                                )
                            )
                            .border(
                                width = 1.dp,
                                brush = Brush.verticalGradient(
                                    listOf(Color(0x55FFFFFF), Color(0x18FFFFFF))
                                ),
                                shape = RoundedCornerShape(29.dp)
                            )
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AuraNavDestination.values().forEach { destination ->
                                val isSelected = currentTab == destination
                                val accentColor = when (destination) {
                                    AuraNavDestination.FOCUS -> NeonCyan
                                    AuraNavDestination.MUSIC -> NeonPink
                                    AuraNavDestination.GAMES -> NeonLime
                                    AuraNavDestination.TOOLS -> NeonAmber
                                    AuraNavDestination.HUB -> NeonViolet
                                }

                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) accentColor.copy(alpha = 0.24f) else Color.Transparent)
                                        .border(
                                            width = if (isSelected) 1.dp else 0.dp,
                                            color = if (isSelected) accentColor else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = ripple(bounded = true, color = accentColor),
                                            onClick = {
                                                if (touchSoundEnabled) {
                                                    AuraSoundManager.playTapSound(context)
                                                }
                                                currentTab = destination
                                            }
                                        )
                                        .testTag(destination.tag),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = destination.icon,
                                        contentDescription = destination.title,
                                        tint = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(22.dp)
                                    )
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
}




