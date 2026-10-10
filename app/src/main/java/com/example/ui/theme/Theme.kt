package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class AuraThemeMode {
    AURORA_DARK,
    CYBER_NEON,
    SOLAR_LIGHT,
    MIDNIGHT_EMERALD,
    SUNSET_OBSIDIAN
}

enum class CardBackdropStyle(val label: String, val iconEmoji: String) {
    CIRCLE_HALO("Circular Halo", "⚪"),
    HEX_PULSE("Hexagon Wave", "⬡"),
    CYBER_GRID("Cyber Grid", "🌐"),
    NEBULA_WAVE("Nebula Wave", "🌊"),
    AURORA_BEAM("Aurora Beam", "✨"),
    OFF("Off (No Glow)", "🚫")
}

val LocalAuraThemeMode = staticCompositionLocalOf { AuraThemeMode.AURORA_DARK }
val LocalUiScale = staticCompositionLocalOf { 1.0f }
val LocalTouchSoundEnabled = staticCompositionLocalOf { true }
val LocalCardBackdropStyle = staticCompositionLocalOf { CardBackdropStyle.CIRCLE_HALO }

private val AuroraDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = ObsidianDark,
    primaryContainer = Color(0x3300F2FE),
    onPrimaryContainer = NeonCyan,
    secondary = NeonViolet,
    onSecondary = Color.White,
    secondaryContainer = Color(0x337F00FF),
    onSecondaryContainer = Color(0xFFD6BBFC),
    tertiary = NeonLime,
    onTertiary = ObsidianDark,
    background = ObsidianDark,
    onBackground = TextPrimaryDark,
    surface = ObsidianSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = ObsidianElevated,
    onSurfaceVariant = TextSecondaryDark,
    outline = GlassBorder
)

private val CyberNeonColorScheme = darkColorScheme(
    primary = NeonLime,
    onPrimary = ObsidianDark,
    primaryContainer = Color(0x3300FF87),
    onPrimaryContainer = NeonLime,
    secondary = NeonPink,
    onSecondary = Color.White,
    secondaryContainer = Color(0x33FF2A85),
    onSecondaryContainer = Color(0xFFFFB3D9),
    tertiary = NeonCyan,
    onTertiary = ObsidianDark,
    background = Color(0xFF050608),
    onBackground = TextPrimaryDark,
    surface = Color(0xFF0C0E14),
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF141722),
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0x3300FF87)
)

private val SolarLightColorScheme = lightColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = Color(0xFF7C3AED),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE9FE),
    onSecondaryContainer = Color(0xFF5B21B6),
    tertiary = Color(0xFF059669),
    onTertiary = Color.White,
    background = SolarQuartzBg,
    onBackground = TextPrimaryLight,
    surface = SolarQuartzSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = TextSecondaryLight,
    outline = SolarBorder
)

private val MidnightEmeraldColorScheme = darkColorScheme(
    primary = EmeraldMint,
    onPrimary = EmeraldDark,
    primaryContainer = Color(0x3300E676),
    onPrimaryContainer = EmeraldMint,
    secondary = DeepJade,
    onSecondary = Color.White,
    secondaryContainer = Color(0x3300E5FF),
    onSecondaryContainer = Color(0xFF80DEEA),
    tertiary = NeonAmber,
    onTertiary = EmeraldDark,
    background = EmeraldDark,
    onBackground = TextPrimaryDark,
    surface = EmeraldSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = EmeraldElevated,
    onSurfaceVariant = Color(0xFF80CBC4),
    outline = Color(0x3300E676)
)

private val SunsetObsidianColorScheme = darkColorScheme(
    primary = RoseGold,
    onPrimary = SunsetDark,
    primaryContainer = Color(0x33FF6E7F),
    onPrimaryContainer = RoseGold,
    secondary = SunsetAmber,
    onSecondary = Color.White,
    secondaryContainer = Color(0x33FFAB40),
    onSecondaryContainer = Color(0xFFFFE082),
    tertiary = NeonViolet,
    onTertiary = SunsetDark,
    background = SunsetDark,
    onBackground = TextPrimaryDark,
    surface = SunsetSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = SunsetElevated,
    onSurfaceVariant = Color(0xFFCE93D8),
    outline = Color(0x33FF6E7F)
)

@Composable
fun AuraFocusTheme(
    themeMode: AuraThemeMode = AuraThemeMode.AURORA_DARK,
    uiScale: Float = 1.0f,
    touchSoundEnabled: Boolean = true,
    cardBackdropStyle: CardBackdropStyle = CardBackdropStyle.CIRCLE_HALO,
    content: @Composable () -> Unit
) {
    val standardDensity = androidx.compose.ui.platform.LocalDensity.current
    val scaledDensity = androidx.compose.ui.unit.Density(
        density = standardDensity.density * uiScale,
        fontScale = standardDensity.fontScale * uiScale
    )

    val colorScheme = when (themeMode) {
        AuraThemeMode.AURORA_DARK -> AuroraDarkColorScheme
        AuraThemeMode.CYBER_NEON -> CyberNeonColorScheme
        AuraThemeMode.SOLAR_LIGHT -> SolarLightColorScheme
        AuraThemeMode.MIDNIGHT_EMERALD -> MidnightEmeraldColorScheme
        AuraThemeMode.SUNSET_OBSIDIAN -> SunsetObsidianColorScheme
    }

    val scaledTypography = androidx.compose.runtime.remember(uiScale) {
        androidx.compose.material3.Typography(
            displayLarge = Typography.displayLarge.copy(fontSize = Typography.displayLarge.fontSize * uiScale),
            displayMedium = Typography.displayMedium.copy(fontSize = Typography.displayMedium.fontSize * uiScale),
            displaySmall = Typography.displaySmall.copy(fontSize = Typography.displaySmall.fontSize * uiScale),
            headlineLarge = Typography.headlineLarge.copy(fontSize = Typography.headlineLarge.fontSize * uiScale),
            headlineMedium = Typography.headlineMedium.copy(fontSize = Typography.headlineMedium.fontSize * uiScale),
            headlineSmall = Typography.headlineSmall.copy(fontSize = Typography.headlineSmall.fontSize * uiScale),
            titleLarge = Typography.titleLarge.copy(fontSize = Typography.titleLarge.fontSize * uiScale),
            titleMedium = Typography.titleMedium.copy(fontSize = Typography.titleMedium.fontSize * uiScale),
            titleSmall = Typography.titleSmall.copy(fontSize = Typography.titleSmall.fontSize * uiScale),
            bodyLarge = Typography.bodyLarge.copy(fontSize = Typography.bodyLarge.fontSize * uiScale),
            bodyMedium = Typography.bodyMedium.copy(fontSize = Typography.bodyMedium.fontSize * uiScale),
            bodySmall = Typography.bodySmall.copy(fontSize = Typography.bodySmall.fontSize * uiScale),
            labelLarge = Typography.labelLarge.copy(fontSize = Typography.labelLarge.fontSize * uiScale),
            labelMedium = Typography.labelMedium.copy(fontSize = Typography.labelMedium.fontSize * uiScale),
            labelSmall = Typography.labelSmall.copy(fontSize = Typography.labelSmall.fontSize * uiScale)
        )
    }

    CompositionLocalProvider(
        LocalAuraThemeMode provides themeMode,
        LocalUiScale provides uiScale,
        LocalTouchSoundEnabled provides touchSoundEnabled,
        LocalCardBackdropStyle provides cardBackdropStyle,
        androidx.compose.ui.platform.LocalDensity provides scaledDensity
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = scaledTypography,
            content = content
        )
    }
}
