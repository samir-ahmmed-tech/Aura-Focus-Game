package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.ripple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AuraThemeMode
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassWhite12
import com.example.ui.theme.GlassWhite18
import com.example.ui.theme.GlassWhite8
import com.example.ui.theme.LocalAuraThemeMode
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalView
import android.view.SoundEffectConstants
import com.example.ui.theme.CardBackdropStyle
import com.example.ui.theme.LocalCardBackdropStyle
import com.example.ui.theme.LocalTouchSoundEnabled
import com.example.util.AuraSoundManager

/**
 * Reusable Glassmorphism Card surface with customizable frosted glass gradient,
 * background blur simulation, translucent border, glowing highlights, and Material3 surface integration.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    borderBrush: Brush? = null,
    borderWidth: Dp = 1.dp,
    glowColor: Color = Color.Transparent,
    glowRadius: Dp = 0.dp,
    onClick: (() -> Unit)? = null,
    testTag: String = "glass_card",
    content: @Composable BoxScope.() -> Unit
) {
    val themeMode = LocalAuraThemeMode.current
    val view = LocalView.current
    val touchSoundEnabled = LocalTouchSoundEnabled.current
    val backdropStyle = LocalCardBackdropStyle.current
    val isLight = themeMode == AuraThemeMode.SOLAR_LIGHT

    // Theme-specific frosted glass gradient backgrounds
    val baseBgBrush = when (themeMode) {
        AuraThemeMode.SOLAR_LIGHT -> Brush.verticalGradient(
            colors = listOf(
                Color(0xEEFFFFFF),
                Color(0xCCF8FAFC)
            )
        )
        AuraThemeMode.MIDNIGHT_EMERALD -> Brush.verticalGradient(
            colors = listOf(
                Color(0x2800E676),
                Color(0x120A1F1B),
                Color(0x0A061412)
            )
        )
        AuraThemeMode.SUNSET_OBSIDIAN -> Brush.verticalGradient(
            colors = listOf(
                Color(0x2AFFFF80),
                Color(0x18FF6E7F),
                Color(0x0E120B10)
            )
        )
        AuraThemeMode.CYBER_NEON -> Brush.verticalGradient(
            colors = listOf(
                Color(0x2600FF87),
                Color(0x10FF2A85),
                Color(0x0C0C0E14)
            )
        )
        AuraThemeMode.AURORA_DARK -> Brush.verticalGradient(
            colors = listOf(
                Color(0x2400F2FE),
                Color(0x167F00FF),
                Color(0x0D10121D)
            )
        )
    }

    val defaultBorderBrush = when (themeMode) {
        AuraThemeMode.SOLAR_LIGHT -> Brush.verticalGradient(
            listOf(Color(0x400284C7), Color(0x150F172A))
        )
        AuraThemeMode.MIDNIGHT_EMERALD -> Brush.verticalGradient(
            listOf(Color(0x6000E676), Color(0x1800E676), Color(0x3500E5FF))
        )
        AuraThemeMode.SUNSET_OBSIDIAN -> Brush.verticalGradient(
            listOf(Color(0x65FF6E7F), Color(0x20FFAB40), Color(0x30FF6E7F))
        )
        AuraThemeMode.CYBER_NEON -> Brush.verticalGradient(
            listOf(Color(0x6000FF87), Color(0x15FF2A85), Color(0x4000FF87))
        )
        AuraThemeMode.AURORA_DARK -> Brush.verticalGradient(
            listOf(Color(0x5500F2FE), Color(0x18FFFFFF), Color(0x407F00FF))
        )
    }

    val finalBorderBrush = borderBrush ?: defaultBorderBrush

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = true, color = MaterialTheme.colorScheme.primary),
            onClick = {
                if (touchSoundEnabled) {
                    AuraSoundManager.playTapSound(view.context, view)
                }
                onClick()
            }
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            // Simulated frosted background blur depth halo with customizable backdrop style
            .drawBehind {
                val haloColor = if (glowRadius > 0.dp && glowColor != Color.Transparent) glowColor.copy(alpha = 0.28f) else Color(0x0AFFFFFF)
                when (backdropStyle) {
                    CardBackdropStyle.OFF -> {
                        // Completely OFF: do not draw any halo or shape behind!
                    }
                    CardBackdropStyle.CIRCLE_HALO -> {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(haloColor, Color.Transparent),
                                radius = size.maxDimension * 0.75f
                            )
                        )
                    }
                    CardBackdropStyle.CYBER_GRID -> {
                        val step = size.maxDimension * 0.18f
                        var x = 0f
                        while (x <= size.width) {
                            drawLine(
                                color = haloColor.copy(alpha = 0.12f),
                                start = Offset(x, 0f),
                                end = Offset(x, size.height),
                                strokeWidth = 1.5f
                            )
                            x += step
                        }
                        var y = 0f
                        while (y <= size.height) {
                            drawLine(
                                color = haloColor.copy(alpha = 0.12f),
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = 1.5f
                            )
                            y += step
                        }
                    }
                    CardBackdropStyle.HEX_PULSE -> {
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val r = size.maxDimension * 0.6f
                        val hexPath = Path().apply {
                            for (i in 0 until 6) {
                                val angle = (i * 60 - 30) * (Math.PI / 180.0)
                                val x = cx + (r * Math.cos(angle)).toFloat()
                                val y = cy + (r * Math.sin(angle)).toFloat()
                                if (i == 0) moveTo(x, y) else lineTo(x, y)
                            }
                            close()
                        }
                        drawPath(
                            path = hexPath,
                            brush = Brush.radialGradient(
                                colors = listOf(haloColor, Color.Transparent),
                                center = Offset(cx, cy),
                                radius = r
                            )
                        )
                    }
                    CardBackdropStyle.AURORA_BEAM -> {
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    haloColor.copy(alpha = 0.35f),
                                    haloColor.copy(alpha = 0.15f),
                                    Color.Transparent
                                ),
                                start = Offset(0f, size.height * 0.2f),
                                end = Offset(size.width, size.height * 0.8f)
                            )
                        )
                    }
                    CardBackdropStyle.NEBULA_WAVE -> {
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val wavePath = Path().apply {
                            moveTo(0f, cy * 0.5f)
                            cubicTo(cx * 0.5f, 0f, cx * 1.5f, cy, size.width, cy * 0.6f)
                            lineTo(size.width, size.height)
                            cubicTo(cx * 1.5f, size.height * 0.8f, cx * 0.5f, size.height * 1.1f, 0f, size.height * 0.9f)
                            close()
                        }
                        drawPath(
                            path = wavePath,
                            brush = Brush.linearGradient(
                                colors = listOf(haloColor, haloColor.copy(alpha = 0.05f), Color.Transparent),
                                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                end = androidx.compose.ui.geometry.Offset(size.width, size.height)
                            )
                        )
                    }
                }
            }
            .shadow(
                elevation = if (isLight) 6.dp else 4.dp,
                shape = shape,
                ambientColor = if (isLight) Color(0x20000000) else Color(0x40000000),
                spotColor = if (isLight) Color(0x15000000) else Color(0x25000000)
            )
            .clip(shape)
            .background(baseBgBrush)
            .border(borderWidth, finalBorderBrush, shape)
            .then(clickableModifier),
        content = content
    )
}

/**
 * Glassmorphic Pill Button with neon gradient border and touch target >= 48dp.
 */
@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    testTag: String = "glass_button"
) {
    val themeMode = LocalAuraThemeMode.current
    val view = LocalView.current
    val touchSoundEnabled = LocalTouchSoundEnabled.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    val buttonBrush = if (isPrimary) {
        Brush.horizontalGradient(
            colors = listOf(primaryColor.copy(alpha = 0.28f), secondaryColor.copy(alpha = 0.28f))
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(Color(0x18FFFFFF), Color(0x10FFFFFF))
        )
    }

    val borderBrush = if (isPrimary) {
        Brush.horizontalGradient(
            colors = listOf(primaryColor, secondaryColor)
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(Color(0x35FFFFFF), Color(0x15FFFFFF))
        )
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(buttonBrush)
            .border(1.5.dp, borderBrush, RoundedCornerShape(25.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = primaryColor),
                onClick = {
                    if (touchSoundEnabled) {
                        AuraSoundManager.playTapSound(view.context, view)
                    }
                    onClick()
                }
            )
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 15.sp,
                    letterSpacing = 0.5.sp
                ),
                color = if (isPrimary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }
}

/**
 * Status/Priority Chip with glowing translucent glass background
 */
@Composable
fun GlassChip(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    testTag: String = "glass_chip"
) {
    val bgAlpha = if (selected) 0.35f else 0.12f
    val borderAlpha = if (selected) 0.9f else 0.35f

    val clickableMod = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = true, color = color),
            onClick = onClick
        )
    } else Modifier

    Box(
        modifier = modifier
            .testTag(testTag)
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = bgAlpha))
            .border(1.dp, color.copy(alpha = borderAlpha), RoundedCornerShape(16.dp))
            .then(clickableMod)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
            color = if (selected) Color.White else color
        )
    }
}

/**
 * Reusable Glassmorphism Modal Dialog with frosted backdrop, glowing border, and animated entrance
 */
@Composable
fun GlassDialog(
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    glowColor: Color = NeonCyan,
    confirmButton: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            GlassCard(
                modifier = modifier.fillMaxWidth(),
                glowColor = glowColor,
                glowRadius = 14.dp,
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    content()

                    Spacer(modifier = Modifier.height(18.dp))

                    if (confirmButton != null || dismissButton != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (dismissButton != null) {
                                dismissButton()
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                            if (confirmButton != null) {
                                confirmButton()
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Reusable Glass Linear Progress Bar with glowing gradient fill & rounded caps
 */
@Composable
fun GlassLinearProgressBar(
    progress: Float, // 0f to 1f
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    trackColor: Color = Color(0x22FFFFFF),
    progressBrush: Brush = Brush.horizontalGradient(listOf(NeonCyan, NeonPink)),
    glowColor: Color = NeonCyan
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        label = "glass_progress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(trackColor)
            .border(0.8.dp, Color(0x33FFFFFF), RoundedCornerShape(height / 2))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .background(progressBrush)
        )
    }
}

/**
 * Reusable Glass Circular Progress Bar with smooth animated arc & center content
 */
@Composable
fun GlassCircularProgressBar(
    progress: Float, // 0f to 1f
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    strokeWidth: Dp = 8.dp,
    trackColor: Color = Color(0x20FFFFFF),
    progressColor: Color = NeonCyan,
    centerContent: (@Composable () -> Unit)? = null
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        label = "glass_circular_progress"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokePx = strokeWidth.toPx()
            val radius = (this.size.minDimension - strokePx) / 2f
            val center = Offset(this.size.width / 2f, this.size.height / 2f)

            // Background Track
            drawCircle(
                color = trackColor,
                radius = radius,
                center = center,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokePx)
            )

            // Active Progress Arc
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(progressColor.copy(alpha = 0.6f), progressColor, progressColor.copy(alpha = 0.8f)),
                    center = center
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    width = strokePx,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            )
        }

        if (centerContent != null) {
            centerContent()
        }
    }
}

/**
 * Reusable Glassmorphism Text Field with glowing focus state
 */
@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    accentColor: Color = NeonCyan,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, color = Color(0x70FFFFFF), fontSize = 13.sp) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0x18FFFFFF),
            unfocusedContainerColor = Color(0x0EFFFFFF),
            focusedBorderColor = accentColor,
            unfocusedBorderColor = Color(0x28FFFFFF),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}

/**
 * Reusable Glassmorphism Slider for audio volume, frequencies & timers
 */
@Composable
fun GlassSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    activeColor: Color = NeonCyan,
    inactiveColor: Color = Color(0x22FFFFFF)
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        modifier = modifier,
        colors = SliderDefaults.colors(
            thumbColor = activeColor,
            activeTrackColor = activeColor,
            inactiveTrackColor = inactiveColor
        )
    )
}

/**
 * Reusable Glassmorphism Notification / Rationale Banner with icon and optional action button.
 */
@Composable
fun GlassBanner(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    iconEmoji: String = "✨",
    accentColor: Color = NeonCyan,
    actionButtonText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        glowColor = accentColor,
        glowRadius = 10.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f))
                    .border(1.dp, accentColor.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(iconEmoji, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp, fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (actionButtonText != null && onActionClick != null) {
                Spacer(modifier = Modifier.width(8.dp))
                GlassChip(
                    label = actionButtonText,
                    color = accentColor,
                    selected = true,
                    onClick = onActionClick
                )
            }
        }
    }
}

/**
 * Reusable Glassmorphism Metric Pill for live telemetry, scores & stat counters.
 */
@Composable
fun GlassMetricPill(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    iconEmoji: String? = null,
    color: Color = NeonCyan
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (iconEmoji != null) {
                Text(iconEmoji, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(5.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                    color = color
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

