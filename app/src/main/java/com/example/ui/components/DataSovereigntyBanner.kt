package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime

/**
 * Mobile Data Sovereignty & Privacy Consent Banner.
 * Item 5 Checklist: Mobile GDPR/Privacy Consent Banner.
 * Informs the user on first launch that NeuroBox stores 100% of data locally on device.
 */
@Composable
fun DataSovereigntyBanner(
    visible: Boolean,
    onAcknowledge: () -> Unit,
    onViewPolicy: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .shadow(24.dp, RoundedCornerShape(22.dp))
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xE6141A29), Color(0xF20B0F19))
                    )
                )
                .border(
                    1.dp,
                    Brush.verticalGradient(
                        listOf(NeonCyan.copy(alpha = 0.5f), Color(0x20FFFFFF))
                    ),
                    RoundedCornerShape(22.dp)
                )
                .padding(18.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeonCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Data Sovereignty",
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "100% Local Data Sovereignty",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "Zero Cloud Tracking • Your Focus Data Stays on Device",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = NeonLime
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "NeuroBox is committed to absolute privacy. All biometric timers, journal reflections, game scores, and personal settings are strictly stored in local offline SQLite. No trackers, telemetry SDKs, or cloud uploads are used.",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp),
                    color = Color(0xCCFFFFFF)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlassButton(
                        text = "Privacy Policy",
                        onClick = onViewPolicy,
                        accentColor = Color(0x99FFFFFF),
                        modifier = Modifier.weight(1f)
                    )
                    GlassButton(
                        text = "I Understand",
                        onClick = onAcknowledge,
                        accentColor = NeonCyan,
                        modifier = Modifier.weight(1.2f)
                    )
                }
            }
        }
    }
}
