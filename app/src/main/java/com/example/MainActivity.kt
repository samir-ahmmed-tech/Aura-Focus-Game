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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.draw.scale
import com.example.ui.components.DataSovereigntyBanner
import com.example.util.AuraGlobalCrashHandler
import com.example.util.AuraLocalAnalytics
import com.example.util.DebounceHelper
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.screens.FullSynapseSpeedSortScreen
import com.example.ui.screens.FullSpatialPathTraceScreen
import com.example.ui.screens.FullNBackAudioEchoScreen
import com.example.ui.screens.FullFocusRadarSaccadeScreen
import com.example.ui.screens.FullUltradianRhythmScreen
import com.example.ui.screens.FullDigitalDistractionBlockerScreen
import com.example.ui.screens.FullBinauralSoundscapeMixerScreen
import com.example.ui.screens.ZenGardenScreen
import com.example.ui.screens.AnalyticsAndHistoryScreen
import com.example.ui.screens.FullQuantumColorClashScreen
import com.example.ui.screens.FullPatternLogicMatrixScreen
import com.example.ui.screens.FullVisualSaccadeSprintScreen
import com.example.ui.screens.FullDualSwitchOddevenScreen
import com.example.ui.screens.FullSpatialRotation3DScreen
import com.example.ui.screens.FullEchoRhythmSimonScreen
import com.example.ui.screens.FullTimeEstimationPacerScreen
import com.example.ui.screens.FullAnagramChainRushScreen
import com.example.ui.screens.FullImpulseInhibitionGoNoGoScreen
import com.example.ui.screens.FullSymbolOdysseyCipherScreen
import com.example.ui.screens.FullTacticalPeripheralRadarScreen
import com.example.ui.screens.FullSynapseMathChainsScreen
import com.example.ui.screens.FullLabyrinthPathfinderScreen
import com.example.ui.screens.FullChronoDigitMatrixScreen
import com.example.ui.screens.FullTonePitchDiscriminationScreen
import com.example.ui.screens.FullKineticOrbBalancingScreen
import com.example.ui.screens.FullSensoryGrounding54321Screen
import com.example.ui.screens.FullPomodoroIntervalArchitectScreen
import com.example.ui.screens.FullCircadianEnergyAuditScreen
import com.example.ui.screens.FullDeepWorkRatioTrackerScreen
import com.example.ui.screens.FullGratitudePebbleJarScreen
import com.example.ui.screens.FullVisionManifestoBoardScreen
import com.example.ui.screens.FullMentalClarityBrainDumpScreen
import com.example.ui.screens.FullIvyLeeSixPrioritiesScreen
import com.example.ui.screens.FullOcularRelief202020Screen
import com.example.ui.screens.FullMultiSpectrumNoiseGenScreen
import com.example.ui.screens.FullCbtCognitiveReframingScreen
import com.example.ui.screens.FullHydrationMetabolismPacerScreen
import com.example.ui.screens.FullWeeklyReviewRetrospectiveScreen
import com.example.ui.screens.FullFocusCommitmentContractScreen
import com.example.ui.screens.FullDeskErgonomicsStretchPacerScreen
import com.example.ui.screens.FullQuantumMirrorEntanglementScreen
import com.example.ui.screens.FullStroopDualConflictScreen
import com.example.ui.screens.FullReverseDigitSpanScreen
import com.example.ui.screens.FullMentalOrigamiScreen
import com.example.ui.screens.FullColorPulseRhythmScreen
import com.example.ui.screens.FullTowerOfHanoiMiniScreen
import com.example.ui.screens.FullLexicalSpeedHuntScreen
import com.example.ui.screens.FullSynapseCircuitConnectScreen
import com.example.ui.screens.FullAcousticIntervalScreen
import com.example.ui.screens.FullPeripheralChangeBlindnessScreen
import com.example.ui.screens.FullParkinsonsLawSprintScreen
import com.example.ui.screens.FullRubberDuckDebuggerScreen
import com.example.ui.screens.FullBurnoutRadarAuditScreen
import com.example.ui.screens.FullMentalRehearsalScreen
import com.example.ui.screens.FullInterstitialJournalScreen
import com.example.ui.screens.FullFeynmanTechniqueScreen
import com.example.ui.screens.FullDecisionMatrixScreen
import com.example.ui.screens.FullDigitalSunsetScreen
import com.example.ui.screens.FullAntiProcrastination5MinScreen
import com.example.ui.screens.FullWeeklyOkrMatrixScreen
import com.example.ui.screens.FullQuantumPolarizationScreen
import com.example.ui.screens.FullSchulteTableMatrixScreen
import com.example.ui.screens.FullTaskSwitchingParityMagnitudeScreen
import com.example.ui.screens.FullSpatialIsometricCubeScreen
import com.example.ui.screens.FullAuditoryPitchEchoScreen
import com.example.ui.screens.FullFlankerArrowSaccadeScreen
import com.example.ui.screens.FullMathFractionSprintScreen
import com.example.ui.screens.FullLexicalRhymeSpeedScreen
import com.example.ui.screens.FullColorStroopTripleConflictScreen
import com.example.ui.screens.FullNeuroVelocityReflexArenaScreen
import com.example.ui.screens.FullEisenhowerMatrixV2Screen
import com.example.ui.screens.FullAtomicHabitMicroDoserScreen
import com.example.ui.screens.FullChronotypeEnergyPlannerScreen
import com.example.ui.screens.FullDistractionSwitchCostScreen
import com.example.ui.screens.FullPlayStoreAdMobLaunchpadScreen
import com.example.ui.components.GlassBanner
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassChip
import com.example.ui.components.GlassDialog
import com.example.ui.components.GlassSlider
import com.example.ui.components.GlassTextField
import android.content.Intent
import android.provider.Settings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
        // Install Global Crash Boundary & Diagnostics Logger (Item 20)
        AuraGlobalCrashHandler.install(applicationContext)
        // Initialize 100% Offline Local Analytics Engine (Item 15)
        AuraLocalAnalytics.init(applicationContext)

        // Ensure transparent status and navigation bars without system black scrims
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        musicEngine = LocalMusicPlayerEngine(applicationContext)

        setContent {
            var currentTheme by remember { mutableStateOf(AuraThemeMode.AURORA_DARK) }
            var uiScale by remember { mutableFloatStateOf(0.85f) }
            var touchSoundEnabled by remember { mutableStateOf(true) }
            var cardBackdropStyle by remember { mutableStateOf(CardBackdropStyle.OFF) }

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
    val gamificationState by GamificationManager.state.collectAsState()
    var currentTab by remember {
        mutableStateOf(
            try {
                AuraNavDestination.valueOf(gamificationState.defaultHomeScreenTab)
            } catch (_: Exception) {
                AuraNavDestination.FOCUS
            }
        )
    }

    // Standalone Full Screen Navigation States
    var activeGameId by remember { mutableStateOf<String?>(null) }
    var activeToolId by remember { mutableStateOf<String?>(null) }
    var showAnalyticsScreen by remember { mutableStateOf(false) }

    // Unified Multi-Permission Launcher for Notifications & Local Audio Auto-Import
    var hasNotifPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }
    var hasAudioPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
            } else {
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultsMap ->
        val notifGranted = resultsMap[Manifest.permission.POST_NOTIFICATIONS] ?: true
        if (notifGranted) {
            hasNotifPermission = true
            AuraNotificationHelper.createNotificationChannels(context)
        }
        val audioGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            resultsMap[Manifest.permission.READ_MEDIA_AUDIO] ?: false
        } else {
            resultsMap[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
        }
        if (audioGranted) {
            hasAudioPermission = true
            musicEngine.scanDeviceAudio(context)
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
                        "g23" -> FullSynapseSpeedSortScreen(onBack = { activeGameId = null })
                        "g24" -> FullSpatialPathTraceScreen(onBack = { activeGameId = null })
                        "g25" -> FullNBackAudioEchoScreen(onBack = { activeGameId = null })
                        "g26" -> FullFocusRadarSaccadeScreen(onBack = { activeGameId = null })
                        "g27" -> FullQuantumColorClashScreen(onBack = { activeGameId = null })
                        "g28" -> FullPatternLogicMatrixScreen(onBack = { activeGameId = null })
                        "g29" -> FullVisualSaccadeSprintScreen(onBack = { activeGameId = null })
                        "g30" -> FullDualSwitchOddevenScreen(onBack = { activeGameId = null })
                        "g31" -> FullSpatialRotation3DScreen(onBack = { activeGameId = null })
                        "g32" -> FullEchoRhythmSimonScreen(onBack = { activeGameId = null })
                        "g33" -> FullTimeEstimationPacerScreen(onBack = { activeGameId = null })
                        "g34" -> FullAnagramChainRushScreen(onBack = { activeGameId = null })
                        "g35" -> FullImpulseInhibitionGoNoGoScreen(onBack = { activeGameId = null })
                        "g36" -> FullSymbolOdysseyCipherScreen(onBack = { activeGameId = null })
                        "g37" -> FullTacticalPeripheralRadarScreen(onBack = { activeGameId = null })
                        "g38" -> FullSynapseMathChainsScreen(onBack = { activeGameId = null })
                        "g39" -> FullLabyrinthPathfinderScreen(onBack = { activeGameId = null })
                        "g40" -> FullChronoDigitMatrixScreen(onBack = { activeGameId = null })
                        "g41" -> FullTonePitchDiscriminationScreen(onBack = { activeGameId = null })
                        "g42" -> FullKineticOrbBalancingScreen(onBack = { activeGameId = null })
                        "g43" -> FullQuantumMirrorEntanglementScreen(onBack = { activeGameId = null })
                        "g44" -> FullStroopDualConflictScreen(onBack = { activeGameId = null })
                        "g45" -> FullReverseDigitSpanScreen(onBack = { activeGameId = null })
                        "g46" -> FullMentalOrigamiScreen(onBack = { activeGameId = null })
                        "g47" -> FullColorPulseRhythmScreen(onBack = { activeGameId = null })
                        "g48" -> FullTowerOfHanoiMiniScreen(onBack = { activeGameId = null })
                        "g49" -> FullLexicalSpeedHuntScreen(onBack = { activeGameId = null })
                        "g50" -> FullSynapseCircuitConnectScreen(onBack = { activeGameId = null })
                        "g51" -> FullAcousticIntervalScreen(onBack = { activeGameId = null })
                        "g52" -> FullPeripheralChangeBlindnessScreen(onBack = { activeGameId = null })
                        "g53" -> FullQuantumPolarizationScreen(onBack = { activeGameId = null })
                        "g54" -> FullSchulteTableMatrixScreen(onBack = { activeGameId = null })
                        "g55" -> FullTaskSwitchingParityMagnitudeScreen(onBack = { activeGameId = null })
                        "g56" -> FullSpatialIsometricCubeScreen(onBack = { activeGameId = null })
                        "g57" -> FullAuditoryPitchEchoScreen(onBack = { activeGameId = null })
                        "g58" -> FullFlankerArrowSaccadeScreen(onBack = { activeGameId = null })
                        "g59" -> FullMathFractionSprintScreen(onBack = { activeGameId = null })
                        "g60" -> FullLexicalRhymeSpeedScreen(onBack = { activeGameId = null })
                        "g61" -> FullColorStroopTripleConflictScreen(onBack = { activeGameId = null })
                        "g62" -> FullNeuroVelocityReflexArenaScreen(onBack = { activeGameId = null })
                        "analytics_history" -> AnalyticsAndHistoryScreen(onBack = { activeGameId = null })
                        "zen_garden" -> ZenGardenScreen(onBack = { activeGameId = null })
                        else -> FullMentalMathScreen(onBack = { activeGameId = null })
                    }
                }

                // Full Screen Tools (50 Power Tools + Zen Garden)
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
                        "t18" -> FullUltradianRhythmScreen(onBack = { activeToolId = null })
                        "t19" -> FullDigitalDistractionBlockerScreen(onBack = { activeToolId = null })
                        "t20" -> FullBinauralSoundscapeMixerScreen(onBack = { activeToolId = null })
                        "t21" -> FullSensoryGrounding54321Screen(onBack = { activeToolId = null })
                        "t22" -> FullPomodoroIntervalArchitectScreen(onBack = { activeToolId = null })
                        "t23" -> FullCircadianEnergyAuditScreen(onBack = { activeToolId = null })
                        "t24" -> FullDeepWorkRatioTrackerScreen(onBack = { activeToolId = null })
                        "t25" -> FullGratitudePebbleJarScreen(onBack = { activeToolId = null })
                        "t26" -> FullVisionManifestoBoardScreen(onBack = { activeToolId = null })
                        "t27" -> FullMentalClarityBrainDumpScreen(onBack = { activeToolId = null })
                        "t28" -> FullIvyLeeSixPrioritiesScreen(onBack = { activeToolId = null })
                        "t29" -> FullOcularRelief202020Screen(onBack = { activeToolId = null })
                        "t30" -> FullMultiSpectrumNoiseGenScreen(onBack = { activeToolId = null })
                        "t31" -> FullCbtCognitiveReframingScreen(onBack = { activeToolId = null })
                        "t32" -> FullHydrationMetabolismPacerScreen(onBack = { activeToolId = null })
                        "t33" -> FullWeeklyReviewRetrospectiveScreen(onBack = { activeToolId = null })
                        "t34" -> FullFocusCommitmentContractScreen(onBack = { activeToolId = null })
                        "t35" -> FullDeskErgonomicsStretchPacerScreen(onBack = { activeToolId = null })
                        "t36" -> FullParkinsonsLawSprintScreen(onBack = { activeToolId = null })
                        "t37" -> FullRubberDuckDebuggerScreen(onBack = { activeToolId = null })
                        "t38" -> FullBurnoutRadarAuditScreen(onBack = { activeToolId = null })
                        "t39" -> FullMentalRehearsalScreen(onBack = { activeToolId = null })
                        "t40" -> FullInterstitialJournalScreen(onBack = { activeToolId = null })
                        "t41" -> FullFeynmanTechniqueScreen(onBack = { activeToolId = null })
                        "t42" -> FullDecisionMatrixScreen(onBack = { activeToolId = null })
                        "t43" -> FullDigitalSunsetScreen(onBack = { activeToolId = null })
                        "t44" -> FullAntiProcrastination5MinScreen(onBack = { activeToolId = null })
                        "t45" -> FullWeeklyOkrMatrixScreen(onBack = { activeToolId = null })
                        "t46" -> FullEisenhowerMatrixV2Screen(onBack = { activeToolId = null })
                        "t47" -> FullAtomicHabitMicroDoserScreen(onBack = { activeToolId = null })
                        "t48" -> FullChronotypeEnergyPlannerScreen(onBack = { activeToolId = null })
                        "t49" -> FullDistractionSwitchCostScreen(onBack = { activeToolId = null })
                        "t50" -> FullPlayStoreAdMobLaunchpadScreen(onBack = { activeToolId = null })
                        "zen_garden" -> ZenGardenScreen(onBack = { activeToolId = null })
                        "analytics_history" -> AnalyticsAndHistoryScreen(onBack = { activeToolId = null })
                        else -> FullNotesScreen(onBack = { activeToolId = null })
                    }
                }

                // Main Tab Screens with ultra-smooth lag-free instant state-preserved transition
                else -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        val tabStateHolder = rememberSaveableStateHolder()
                        tabStateHolder.SaveableStateProvider(key = currentTab) {
                            when (currentTab) {
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

                        // Mobile Data Sovereignty & Privacy Consent Banner (Item 5)
                        DataSovereigntyBanner(
                            visible = !gamificationState.hasAcknowledgedPrivacy,
                            onAcknowledge = { GamificationManager.acknowledgePrivacy() },
                            onViewPolicy = {
                                currentTab = AuraNavDestination.HUB
                            },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .navigationBarsPadding()
                                .padding(bottom = 80.dp)
                        )

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

                                        val animatedTabScale by animateFloatAsState(
                                            targetValue = if (isSelected) 1.15f else 1.0f,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessMedium
                                            ),
                                            label = "tab_scale_${destination.name}"
                                        )

                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .scale(animatedTabScale)
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
                                                        if (currentTab != destination && DebounceHelper.canClick(100L)) {
                                                            if (touchSoundEnabled) {
                                                                AuraSoundManager.playTapSound(context)
                                                            }
                                                            currentTab = destination
                                                        }
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

