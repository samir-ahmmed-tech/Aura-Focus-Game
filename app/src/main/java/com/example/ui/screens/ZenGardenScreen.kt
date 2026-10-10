package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
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
import com.example.util.AuraSoundManager

@Composable
fun ZenGardenScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val gamificationState by GamificationManager.state.collectAsState()
    val tree = gamificationState.tree

    // Subtle sway animation for leaves
    val swayAnim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        swayAnim.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Top Nav
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "Zen Tree & Daily Garden",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "Daily login streak evolution & fruit harvest",
                        style = MaterialTheme.typography.bodySmall,
                        color = NeonLime
                    )
                }
            }

            // Fruit Counter Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x25FF2A85))
                    .border(1.dp, NeonPink, RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text("🍎 ${tree.auraFruits}", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Penalty Notice Banner if user missed a day
        if (tree.penaltyNotice != null) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glowColor = NeonPink,
                glowRadius = 10.dp
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⚠️", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = tree.penaltyNotice,
                        fontSize = 12.sp,
                        color = Color.White,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // =====================================================================
        // VISUAL TREE & SANCTUARY GARDEN CANVAS CARD
        // =====================================================================
        // =====================================================================
        // VISUAL TREE & SANCTUARY GARDEN CANVAS CARD
        // (Realistic Tree Rendering: Seed -> Sprout -> Tree -> Food -> Garden)
        // =====================================================================
        var previewStage by remember { mutableStateOf<Int?>(null) }
        val activeStage = previewStage ?: tree.stage

        val currentStageTitle = when (activeStage) {
            1 -> "Sacred Seed (বীজ)"
            2 -> "Living Sprout (অঙ্কুর)"
            3 -> "Growing Tree (গাছ)"
            4 -> "Majestic Mature Tree"
            5 -> "Aura Fruit Harvest (Food / ফল)"
            else -> "Zen Sanctuary Garden (বাগান)"
        }

        val currentStageEmoji = when (activeStage) {
            1 -> "🌰"
            2 -> "🌱"
            3 -> "🌿"
            4 -> "🌳"
            5 -> "🍎"
            else -> "🏞️"
        }

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glowColor = NeonLime,
            glowRadius = 14.dp
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header details (Clean - no water buttons inside card)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(currentStageEmoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = currentStageTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = "Stage $activeStage/6 • ${tree.growthDays} Consecutive Days",
                                fontSize = 11.sp,
                                color = NeonLime
                            )
                        }
                    }

                    GlassChip(
                        label = if (activeStage >= 6) "Sanctuary Garden 🏞️" else "Tree Growth Level $activeStage",
                        color = NeonCyan,
                        selected = true
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Realistic Tree Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF061017),
                                    Color(0xFF0B1F1E),
                                    Color(0xFF112B22)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val groundY = h * 0.82f
                        val centerX = w / 2f
                        val sway = (swayAnim.value - 0.5f) * 8f

                        // Draw organic rich soil mound
                        drawOval(
                            brush = Brush.verticalGradient(
                                listOf(Color(0xFF2E1F14), Color(0xFF1A110B))
                            ),
                            topLeft = Offset(0f, groundY - 18f),
                            size = androidx.compose.ui.geometry.Size(w, h * 0.45f)
                        )
                        // Lush green moss on soil
                        drawOval(
                            brush = Brush.radialGradient(
                                listOf(Color(0xFF1B4D3E), Color.Transparent),
                                center = Offset(centerX, groundY),
                                radius = w * 0.4f
                            ),
                            topLeft = Offset(centerX - w * 0.35f, groundY - 14f),
                            size = androidx.compose.ui.geometry.Size(w * 0.7f, 28f)
                        )

                        when (activeStage) {
                            1 -> {
                                // STAGE 1: SEED (বীজ) - Glowing split seed pod in fertile soil with embryo sprout
                                val seedY = groundY - 4f

                                // Glowing golden halo around seed
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        listOf(Color(0x80FFD700), Color.Transparent),
                                        center = Offset(centerX, seedY),
                                        radius = 35f
                                    ),
                                    radius = 35f,
                                    center = Offset(centerX, seedY)
                                )

                                // Seed shell (Chestnut brown)
                                drawOval(
                                    brush = Brush.verticalGradient(
                                        listOf(Color(0xFF8D5524), Color(0xFF4A2810))
                                    ),
                                    topLeft = Offset(centerX - 16f, seedY - 12f),
                                    size = androidx.compose.ui.geometry.Size(32f, 24f)
                                )
                                // Seed split highlight
                                drawArc(
                                    color = Color(0xFFFFD54F),
                                    startAngle = 30f,
                                    sweepAngle = 120f,
                                    useCenter = false,
                                    topLeft = Offset(centerX - 14f, seedY - 10f),
                                    size = androidx.compose.ui.geometry.Size(28f, 20f),
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5f)
                                )

                                // Little embryo rootlets
                                val rootPath = Path().apply {
                                    moveTo(centerX - 2f, seedY + 8f)
                                    quadraticBezierTo(centerX - 8f, seedY + 22f, centerX - 12f, seedY + 30f)
                                    moveTo(centerX + 2f, seedY + 8f)
                                    quadraticBezierTo(centerX + 8f, seedY + 20f, centerX + 10f, seedY + 28f)
                                }
                                drawPath(rootPath, Color(0xCCFFFFFF), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f))

                                // Tender green germinating sprout shoot emerging upward
                                val shootPath = Path().apply {
                                    moveTo(centerX, seedY - 6f)
                                    quadraticBezierTo(centerX + sway, seedY - 26f, centerX + sway, seedY - 40f)
                                }
                                drawPath(shootPath, Color(0xFF69F0AE), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.5f))

                                // Twin tiny cotyledon baby leaves
                                drawOval(
                                    color = Color(0xFF00E676),
                                    topLeft = Offset(centerX + sway - 12f, seedY - 48f),
                                    size = androidx.compose.ui.geometry.Size(12f, 8f)
                                )
                                drawOval(
                                    color = Color(0xFF76FF03),
                                    topLeft = Offset(centerX + sway, seedY - 50f),
                                    size = androidx.compose.ui.geometry.Size(13f, 9f)
                                )
                            }

                            2 -> {
                                // STAGE 2: SPROUT / YOUNG SHOOT (অঙ্কুর / ছোট চারা)
                                val stemPath = Path().apply {
                                    moveTo(centerX, groundY)
                                    cubicTo(
                                        centerX - 8f, groundY - 35f,
                                        centerX + 10f + sway, groundY - 65f,
                                        centerX + sway, groundY - 95f
                                    )
                                }
                                drawPath(
                                    stemPath,
                                    brush = Brush.verticalGradient(listOf(Color(0xFF69F0AE), Color(0xFF2E7D32))),
                                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f)
                                )

                                // Multiple pairs of tender young leaves
                                val leafPositions = listOf(
                                    Triple(centerX - 4f, groundY - 40f, -25f),
                                    Triple(centerX + 6f, groundY - 55f, 25f),
                                    Triple(centerX - 2f, groundY - 75f, -35f),
                                    Triple(centerX + sway, groundY - 95f, 0f)
                                )

                                leafPositions.forEach { (lx, ly, rot) ->
                                    drawOval(
                                        brush = Brush.radialGradient(
                                            listOf(Color(0xFF76FF03), Color(0xFF1B5E20)),
                                            center = Offset(lx, ly),
                                            radius = 16f
                                        ),
                                        topLeft = Offset(lx - 12f, ly - 8f),
                                        size = androidx.compose.ui.geometry.Size(24f, 16f)
                                    )
                                    // Dewdrop on leaf
                                    drawCircle(Color.White, radius = 2.5f, center = Offset(lx + 4f, ly - 2f))
                                }
                            }

                            3 -> {
                                // STAGE 3: GROWING YOUNG TREE (গাছ)
                                val trunkPath = Path().apply {
                                    moveTo(centerX - 10f, groundY)
                                    lineTo(centerX - 6f, groundY - 60f)
                                    lineTo(centerX - 20f + sway, groundY - 95f)
                                    lineTo(centerX - 16f + sway, groundY - 98f)
                                    lineTo(centerX - 2f, groundY - 65f)
                                    lineTo(centerX + 18f + sway, groundY - 92f)
                                    lineTo(centerX + 22f + sway, groundY - 89f)
                                    lineTo(centerX + 4f, groundY - 55f)
                                    lineTo(centerX + 8f, groundY)
                                    close()
                                }
                                drawPath(
                                    trunkPath,
                                    brush = Brush.linearGradient(
                                        listOf(Color(0xFF5D4037), Color(0xFF3E2723)),
                                        start = Offset(centerX, groundY),
                                        end = Offset(centerX, groundY - 100f)
                                    )
                                )

                                // Lush green foliage clouds
                                val foliageClusters = listOf(
                                    Triple(centerX - 20f + sway, groundY - 105f, 26f),
                                    Triple(centerX + 20f + sway, groundY - 102f, 26f),
                                    Triple(centerX + sway, groundY - 125f, 32f)
                                )
                                foliageClusters.forEach { (fx, fy, rad) ->
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            listOf(Color(0xFF00E676), Color(0xFF1B5E20)),
                                            center = Offset(fx, fy),
                                            radius = rad
                                        ),
                                        radius = rad,
                                        center = Offset(fx, fy)
                                    )
                                }
                            }

                            4 -> {
                                // STAGE 4: MAJESTIC MATURE TREE (পরিপূর্ণ বিশাল গাছ)
                                val trunkPath = Path().apply {
                                    moveTo(centerX - 18f, groundY)
                                    // Roots entering ground
                                    quadraticBezierTo(centerX - 28f, groundY + 4f, centerX - 36f, groundY + 8f)
                                    quadraticBezierTo(centerX - 18f, groundY - 2f, centerX - 12f, groundY - 20f)
                                    lineTo(centerX - 10f, groundY - 60f)
                                    // Left spreading major bough
                                    quadraticBezierTo(centerX - 40f + sway, groundY - 90f, centerX - 55f + sway, groundY - 110f)
                                    lineTo(centerX - 50f + sway, groundY - 114f)
                                    quadraticBezierTo(centerX - 30f + sway, groundY - 96f, centerX - 4f, groundY - 75f)
                                    // Right major bough
                                    quadraticBezierTo(centerX + 35f + sway, groundY - 95f, centerX + 55f + sway, groundY - 108f)
                                    lineTo(centerX + 58f + sway, groundY - 104f)
                                    quadraticBezierTo(centerX + 30f + sway, groundY - 88f, centerX + 6f, groundY - 65f)
                                    // Center bough
                                    lineTo(centerX + 3f + sway, groundY - 115f)
                                    lineTo(centerX - 3f + sway, groundY - 115f)
                                    lineTo(centerX + 12f, groundY - 20f)
                                    quadraticBezierTo(centerX + 25f, groundY - 2f, centerX + 34f, groundY + 8f)
                                    close()
                                }
                                drawPath(
                                    trunkPath,
                                    brush = Brush.verticalGradient(
                                        listOf(Color(0xFF6D4C41), Color(0xFF3E2723), Color(0xFF271510))
                                    )
                                )

                                // Dense majestic canopies with depth shading
                                val denseCanopies = listOf(
                                    Triple(centerX - 60f + sway, groundY - 125f, 34f),
                                    Triple(centerX + 60f + sway, groundY - 120f, 34f),
                                    Triple(centerX - 25f + sway, groundY - 145f, 38f),
                                    Triple(centerX + 25f + sway, groundY - 145f, 38f),
                                    Triple(centerX + sway, groundY - 158f, 44f)
                                )

                                denseCanopies.forEach { (cx, cy, r) ->
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            listOf(Color(0xFF00E676), Color(0xFF2E7D32), Color(0xFF0D3319)),
                                            center = Offset(cx, cy - r * 0.2f),
                                            radius = r
                                        ),
                                        radius = r,
                                        center = Offset(cx, cy)
                                    )
                                    // Bright foliage highlight
                                    drawCircle(
                                        color = Color(0x60B9F6CA),
                                        radius = r * 0.45f,
                                        center = Offset(cx - r * 0.2f, cy - r * 0.25f)
                                    )
                                }
                            }

                            5 -> {
                                // STAGE 5: FRUIT / FOOD BEARING TREE (Food / ফল)
                                // Full majestic tree loaded with edible glowing Aura Fruits!
                                val trunkPath = Path().apply {
                                    moveTo(centerX - 16f, groundY)
                                    lineTo(centerX - 10f, groundY - 65f)
                                    quadraticBezierTo(centerX - 45f + sway, groundY - 95f, centerX - 60f + sway, groundY - 115f)
                                    lineTo(centerX - 55f + sway, groundY - 118f)
                                    quadraticBezierTo(centerX - 30f + sway, groundY - 98f, centerX - 4f, groundY - 80f)
                                    quadraticBezierTo(centerX + 35f + sway, groundY - 98f, centerX + 60f + sway, groundY - 115f)
                                    lineTo(centerX + 64f + sway, groundY - 112f)
                                    quadraticBezierTo(centerX + 30f + sway, groundY - 90f, centerX + 6f, groundY - 70f)
                                    lineTo(centerX + 14f, groundY)
                                    close()
                                }
                                drawPath(trunkPath, Brush.verticalGradient(listOf(Color(0xFF795548), Color(0xFF3E2723))))

                                val canopies = listOf(
                                    Triple(centerX - 55f + sway, groundY - 128f, 35f),
                                    Triple(centerX + 55f + sway, groundY - 125f, 35f),
                                    Triple(centerX - 22f + sway, groundY - 148f, 40f),
                                    Triple(centerX + 22f + sway, groundY - 148f, 40f),
                                    Triple(centerX + sway, groundY - 160f, 46f)
                                )

                                canopies.forEach { (cx, cy, r) ->
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            listOf(Color(0xFF00E676), Color(0xFF1B5E20)),
                                            center = Offset(cx, cy),
                                            radius = r
                                        ),
                                        radius = r,
                                        center = Offset(cx, cy)
                                    )
                                }

                                // Glowing ripe Aura Fruits (Food / ফল) hanging from branches!
                                val fruitSpots = listOf(
                                    Offset(centerX - 62f + sway, groundY - 112f),
                                    Offset(centerX - 35f + sway, groundY - 132f),
                                    Offset(centerX - 15f + sway, groundY - 118f),
                                    Offset(centerX + 12f + sway, groundY - 122f),
                                    Offset(centerX + 38f + sway, groundY - 135f),
                                    Offset(centerX + 62f + sway, groundY - 110f),
                                    Offset(centerX - 5f + sway, groundY - 148f),
                                    Offset(centerX + 25f + sway, groundY - 152f)
                                )

                                fruitSpots.forEach { fPos ->
                                    // Stem
                                    drawLine(
                                        color = Color(0xFF4E342E),
                                        start = Offset(fPos.x, fPos.y - 12f),
                                        end = fPos,
                                        strokeWidth = 2f
                                    )
                                    // Fruit glow
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            listOf(Color(0x90FF1744), Color.Transparent),
                                            center = fPos,
                                            radius = 16f
                                        ),
                                        radius = 16f,
                                        center = fPos
                                    )
                                    // Apple/Fruit body
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            listOf(Color(0xFFFF5252), Color(0xFFD50000)),
                                            center = Offset(fPos.x - 2f, fPos.y - 2f),
                                            radius = 9f
                                        ),
                                        radius = 8.5f,
                                        center = fPos
                                    )
                                    // Specular shine
                                    drawCircle(Color.White, radius = 2.2f, center = Offset(fPos.x - 3f, fPos.y - 3f))
                                    // Tiny green fruit leaf
                                    drawOval(
                                        color = Color(0xFF76FF03),
                                        topLeft = Offset(fPos.x, fPos.y - 14f),
                                        size = androidx.compose.ui.geometry.Size(7f, 4f)
                                    )
                                }
                            }

                            else -> {
                                // STAGE 6: FULL ZEN SANCTUARY GARDEN (বাগান)
                                // Multi-tree garden with pond, stepping stones, lanterns & fruit orchards

                                // Background Misty Trees
                                drawCircle(Color(0x55E91E63), radius = 35f, center = Offset(w * 0.18f, groundY - 65f)) // Sakura
                                drawCircle(Color(0x5500E676), radius = 38f, center = Offset(w * 0.82f, groundY - 70f)) // Emerald Pine
                                drawCircle(Color(0x4000E5FF), radius = 30f, center = Offset(w * 0.32f, groundY - 80f)) // Blue Spruce

                                // Reflective Koi Pond
                                drawOval(
                                    brush = Brush.radialGradient(
                                        listOf(Color(0xAA00F2FE), Color(0x600D47A1), Color.Transparent),
                                        center = Offset(centerX + 60f, groundY + 12f),
                                        radius = 65f
                                    ),
                                    topLeft = Offset(centerX + 10f, groundY - 4f),
                                    size = androidx.compose.ui.geometry.Size(110f, 32f)
                                )
                                // Pond Lily Pad
                                drawCircle(Color(0xFF00E676), radius = 6f, center = Offset(centerX + 50f, groundY + 10f))
                                drawCircle(Color(0xFFFF4081), radius = 3.5f, center = Offset(centerX + 52f, groundY + 9f)) // Lotus blossom

                                // Stone Pagoda Lantern on the bank
                                val lx = centerX - 80f
                                val ly = groundY - 20f
                                drawRect(Color(0xFF90A4AE), topLeft = Offset(lx - 4f, ly), size = androidx.compose.ui.geometry.Size(8f, 16f))
                                drawRect(Color(0xFFFFD54F), topLeft = Offset(lx - 6f, ly - 8f), size = androidx.compose.ui.geometry.Size(12f, 8f)) // Lantern light
                                drawOval(Color(0xFF546E7A), topLeft = Offset(lx - 10f, ly - 12f), size = androidx.compose.ui.geometry.Size(20f, 6f)) // Roof

                                // Main Master Bonsai Tree in center
                                val trunkPath = Path().apply {
                                    moveTo(centerX - 14f, groundY)
                                    lineTo(centerX - 8f, groundY - 65f)
                                    quadraticBezierTo(centerX - 40f + sway, groundY - 95f, centerX - 55f + sway, groundY - 110f)
                                    lineTo(centerX - 50f + sway, groundY - 114f)
                                    quadraticBezierTo(centerX - 25f + sway, groundY - 95f, centerX - 2f, groundY - 80f)
                                    quadraticBezierTo(centerX + 30f + sway, groundY - 95f, centerX + 50f + sway, groundY - 110f)
                                    lineTo(centerX + 54f + sway, groundY - 106f)
                                    quadraticBezierTo(centerX + 25f + sway, groundY - 90f, centerX + 6f, groundY - 70f)
                                    lineTo(centerX + 12f, groundY)
                                    close()
                                }
                                drawPath(trunkPath, Brush.verticalGradient(listOf(Color(0xFF6D4C41), Color(0xFF271510))))

                                val gardenCanopies = listOf(
                                    Triple(centerX - 50f + sway, groundY - 120f, 32f),
                                    Triple(centerX + 50f + sway, groundY - 118f, 32f),
                                    Triple(centerX + sway, groundY - 145f, 40f)
                                )
                                gardenCanopies.forEach { (cx, cy, r) ->
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            listOf(Color(0xFF00E676), Color(0xFF1B5E20)),
                                            center = Offset(cx, cy),
                                            radius = r
                                        ),
                                        radius = r,
                                        center = Offset(cx, cy)
                                    )
                                }

                                // Fruits in Garden
                                listOf(
                                    Offset(centerX - 45f + sway, groundY - 110f),
                                    Offset(centerX - 15f + sway, groundY - 130f),
                                    Offset(centerX + 15f + sway, groundY - 130f),
                                    Offset(centerX + 45f + sway, groundY - 110f)
                                ).forEach { fPos ->
                                    drawCircle(Color(0xFFFF1744), radius = 7f, center = fPos)
                                    drawCircle(Color.White, radius = 2f, center = Offset(fPos.x - 2f, fPos.y - 2f))
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // STAGE INSPECTOR CHIPS (Seed -> Sprout -> Tree -> Mature -> Food -> Garden)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                1 to "🌰 1. Seed (বীজ)",
                2 to "🌱 2. Sprout (অঙ্কুর)",
                3 to "🌿 3. Growing (গাছ)",
                4 to "🌳 4. Mature Tree",
                5 to "🍎 5. Food (ফল)",
                6 to "🏞️ 6. Zen Garden (বাগান)"
            ).forEach { (stg, label) ->
                val isSelected = (activeStage == stg)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) NeonLime.copy(alpha = 0.28f) else Color(0x14FFFFFF))
                        .border(1.dp, if (isSelected) NeonLime else Color(0x24FFFFFF), RoundedCornerShape(10.dp))
                        .clickable { previewStage = stg }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) NeonLime else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // WATER STATUS & ACTION BUTTON (EXPLICITLY OUTSIDE AND BELOW TREE CARD PER USER REQUEST)
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
                    Column {
                        Text("💧 Daily Watering & Tree Care", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                        Text(
                            text = if (tree.isWateredToday) "Tree is watered & thriving today!" else "Water today to absorb nutrients & harvest fruits",
                            fontSize = 11.sp,
                            color = if (tree.isWateredToday) NeonLime else NeonCyan
                        )
                    }
                    GlassChip(
                        label = if (tree.isWateredToday) "Nourished ✓" else "Needs Water 💧",
                        color = if (tree.isWateredToday) NeonLime else NeonAmber,
                        selected = true
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                GlassButton(
                    text = "💧 Water Tree & Harvest Fruits (+${15 + activeStage * 5} Aura Fruits)",
                    onClick = {
                        val gained = GamificationManager.waterAndHarvestTree()
                        AuraSoundManager.playHarvestSound()
                        AuraSoundManager.performHaptic(context)
                        Toast.makeText(context, "Harvested +$gained Aura Fruits! 🍎 Tree is thriving!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Psychological Streak & Penalty Rule Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x14FFFFFF))
                .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("⏳ Psychological Habit Protection Rule:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = NeonAmber)
                    Text("Streak: ${gamificationState.currentStreakDays} Days", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = NeonLime)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• Log in each day: Your tree absorbs nourishment and grows continuously from Seed (বীজ) to Tree, Food (ফল), and full Garden (বাগান).\n• Miss a day penalty: The tree loses 2/3 of its growth (retaining only 1/3)! For example, 9 days of growth drops to 3 days if you miss a check-in.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(modifier = Modifier.height(20.dp))

        // =====================================================================
        // REWARD VAULT & AURA FRUIT STORE (UNLOCK EXCLUSIVE MODES)
        // =====================================================================
        Text(
            text = "🛒 Aura Fruit Vault (Unlock Exclusive Rewards)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Text(
            text = "Spend harvested fruits to unlock secret game modes, tools & protections",
            fontSize = 11.sp,
            color = NeonPink
        )

        Spacer(modifier = Modifier.height(10.dp))

        val rewards = listOf(
            Triple("reward_shield", "🛡️ Tree Streak Freeze Shield", "Protects your growing tree from losing 2/3 growth if you miss a day.") to 40,
            Triple("reward_cyber_mode", "⚡ VIP Cyber Hack Matrix Mode", "Unlocks hyper-speed overclocked mode for Cyber Glitch Sequence game.") to 60,
            Triple("reward_synthesizer", "🎛️ Aura Ambience Master Synthesizer", "Unlocks 5-channel nature sound mixing with binaural crossfades.") to 75,
            Triple("reward_golden_shader", "✨ Mythic Golden Obsidian Shader", "Exclusive radiant neon shader theme for all glassmorphic cards.") to 100
        )

        rewards.forEach { (item, cost) ->
            val (id, title, desc) = item
            val isUnlocked = tree.unlockedRewards.contains(id)

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                glowColor = if (isUnlocked) NeonLime else NeonViolet,
                glowRadius = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    if (isUnlocked) {
                        GlassChip(label = "Unlocked ✓", color = NeonLime, selected = true)
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (tree.auraFruits >= cost) NeonPink.copy(alpha = 0.25f) else Color(0x18FFFFFF))
                                .border(1.dp, if (tree.auraFruits >= cost) NeonPink else Color(0x30FFFFFF), RoundedCornerShape(10.dp))
                                .clickable {
                                    if (GamificationManager.spendAuraFruits(cost, id)) {
                                        AuraSoundManager.playSuccessSound()
                                        Toast.makeText(context, "Unlocked $title!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Need $cost fruits! Water your tree daily to harvest more.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🍎 $cost Fruits",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (tree.auraFruits >= cost) NeonPink else Color(0x80FFFFFF)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}
