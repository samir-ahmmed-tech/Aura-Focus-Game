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
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
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

        // Dynamic organic growth animation: trunk elongates and leaves unfurl naturally
        val growthAnim = remember { Animatable(1f) }
        LaunchedEffect(activeStage) {
            growthAnim.snapTo(0.2f)
            growthAnim.animateTo(
                targetValue = 1f,
                animationSpec = tween(850, easing = FastOutSlowInEasing)
            )
        }

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
                // Header details
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

                // Realistic Botanical Tree Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF040A10),
                                    Color(0xFF08181A),
                                    Color(0xFF0C241E)
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
                        val sway = (swayAnim.value - 0.5f) * 7f
                        val growth = growthAnim.value

                        // Fertile deep soil mound with organic gradation
                        drawOval(
                            brush = Brush.verticalGradient(
                                listOf(Color(0xFF2B1C12), Color(0xFF140D08))
                            ),
                            topLeft = Offset(0f, groundY - 18f),
                            size = Size(w, h * 0.45f)
                        )
                        // Lush velvet moss layer on soil
                        drawOval(
                            brush = Brush.radialGradient(
                                listOf(Color(0xFF1E523A), Color.Transparent),
                                center = Offset(centerX, groundY - 2f),
                                radius = w * 0.42f
                            ),
                            topLeft = Offset(centerX - w * 0.36f, groundY - 15f),
                            size = Size(w * 0.72f, 30f)
                        )

                        when (activeStage) {
                            1 -> {
                                // STAGE 1: SEED (বীজ) - Cracked seed pod with emerging embryo rootlets & live sprout
                                val seedY = groundY - 6f

                                // Radiant vitality glow
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        listOf(Color(0x70FFD54F), Color.Transparent),
                                        center = Offset(centerX, seedY),
                                        radius = 36f * growth
                                    ),
                                    radius = 36f * growth,
                                    center = Offset(centerX, seedY)
                                )

                                // Seed shell with organic wood grain
                                drawOval(
                                    brush = Brush.verticalGradient(
                                        listOf(Color(0xFF8D5524), Color(0xFF4A2810))
                                    ),
                                    topLeft = Offset(centerX - 16f, seedY - 12f),
                                    size = Size(32f, 24f)
                                )
                                drawArc(
                                    color = Color(0xFFFFD54F),
                                    startAngle = 30f,
                                    sweepAngle = 120f,
                                    useCenter = false,
                                    topLeft = Offset(centerX - 14f, seedY - 10f),
                                    size = Size(28f, 20f),
                                    style = Stroke(width = 2.5f)
                                )

                                // Fine rootlets growing deep into soil
                                val rootPath = Path().apply {
                                    moveTo(centerX - 2f, seedY + 8f)
                                    quadraticBezierTo(centerX - 8f, seedY + 18f * growth, centerX - 14f, seedY + 28f * growth)
                                    moveTo(centerX + 2f, seedY + 8f)
                                    quadraticBezierTo(centerX + 8f, seedY + 16f * growth, centerX + 12f, seedY + 26f * growth)
                                }
                                drawPath(rootPath, Color(0xCCFFFFFF), style = Stroke(width = 1.8f))

                                // Sprouting tender shoot emerging upward
                                val shootTopY = seedY - (42f * growth)
                                val shootPath = Path().apply {
                                    moveTo(centerX, seedY - 4f)
                                    quadraticBezierTo(centerX + sway * 0.5f, seedY - 22f * growth, centerX + sway, shootTopY)
                                }
                                drawPath(shootPath, Color(0xFF69F0AE), style = Stroke(width = 3.2f))

                                // Twin cotyledon baby leaves with veins
                                val babyLeafLen = 14f * growth
                                val babyLeafWid = 7f * growth
                                drawRealisticSmallLeaf(
                                    centerX = centerX + sway - 8f * growth,
                                    centerY = shootTopY - 4f,
                                    angleDeg = 150.0,
                                    length = babyLeafLen,
                                    width = babyLeafWid,
                                    color = Color(0xFF00E676)
                                )
                                drawRealisticSmallLeaf(
                                    centerX = centerX + sway + 8f * growth,
                                    centerY = shootTopY - 5f,
                                    angleDeg = 30.0,
                                    length = babyLeafLen * 1.05f,
                                    width = babyLeafWid * 1.05f,
                                    color = Color(0xFF76FF03)
                                )
                            }

                            2 -> {
                                // STAGE 2: SPROUT / YOUNG SHOOT (অঙ্কুর / ছোট চারা)
                                val stemTopY = groundY - (95f * growth)
                                val stemPath = Path().apply {
                                    moveTo(centerX, groundY)
                                    cubicTo(
                                        centerX - 6f, groundY - (35f * growth),
                                        centerX + 8f + sway, groundY - (65f * growth),
                                        centerX + sway, stemTopY
                                    )
                                }
                                drawPath(
                                    stemPath,
                                    brush = Brush.verticalGradient(listOf(Color(0xFF69F0AE), Color(0xFF2E7D32))),
                                    style = Stroke(width = 4.5f)
                                )

                                // Realistic small leaves along the stem
                                val nodeConfigs = listOf(
                                    Triple(centerX - 4f, groundY - (38f * growth), 150.0),
                                    Triple(centerX + 6f, groundY - (55f * growth), 30.0),
                                    Triple(centerX - 3f, groundY - (75f * growth), 140.0),
                                    Triple(centerX + sway, stemTopY, 80.0)
                                )
                                nodeConfigs.forEachIndexed { i, (lx, ly, ang) ->
                                    val lScale = (0.7f + i * 0.1f) * growth
                                    drawRealisticSmallLeaf(
                                        centerX = lx,
                                        centerY = ly,
                                        angleDeg = ang + sway * 1.2,
                                        length = 16f * lScale,
                                        width = 8f * lScale,
                                        color = if (i % 2 == 0) Color(0xFF2E7D32) else Color(0xFF76FF03)
                                    )
                                    // Sunlight dewdrop
                                    drawCircle(Color.White, radius = 2f * growth, center = Offset(lx + 3f, ly - 2f))
                                }
                            }

                            3 -> {
                                // STAGE 3: GROWING YOUNG TREE (গাছ) - Real wood trunk & small leaf clusters
                                val trunkHeight = 85f * growth
                                val topY = groundY - trunkHeight

                                // Tapered organic trunk with root flaring
                                val trunkPath = Path().apply {
                                    moveTo(centerX - 12f, groundY)
                                    quadraticBezierTo(centerX - 18f, groundY + 3f, centerX - 24f, groundY + 6f)
                                    quadraticBezierTo(centerX - 14f, groundY - 4f, centerX - 8f, groundY - 18f)
                                    lineTo(centerX - 5f, topY + 25f)
                                    // Left bough
                                    quadraticBezierTo(centerX - 16f + sway, topY + 12f, centerX - 26f + sway, topY - 5f)
                                    lineTo(centerX - 22f + sway, topY - 8f)
                                    lineTo(centerX - 2f, topY + 15f)
                                    // Right bough
                                    quadraticBezierTo(centerX + 18f + sway, topY + 10f, centerX + 28f + sway, topY - 6f)
                                    lineTo(centerX + 24f + sway, topY - 9f)
                                    lineTo(centerX + 3f, topY + 10f)
                                    // Center tip
                                    lineTo(centerX + 1f + sway, topY - 16f)
                                    lineTo(centerX - 1f + sway, topY - 16f)
                                    lineTo(centerX + 6f, groundY - 18f)
                                    quadraticBezierTo(centerX + 14f, groundY - 4f, centerX + 22f, groundY + 6f)
                                    close()
                                }
                                drawPath(
                                    trunkPath,
                                    brush = Brush.verticalGradient(
                                        listOf(Color(0xFF6D4C41), Color(0xFF3E2723), Color(0xFF271510))
                                    )
                                )

                                // Clusters of realistic small leaves on branch ends
                                drawRealisticFoliageCluster(
                                    cx = centerX - 26f + sway,
                                    cy = topY - 10f,
                                    clusterRadius = 24f,
                                    leafScale = growth,
                                    sway = sway
                                )
                                drawRealisticFoliageCluster(
                                    cx = centerX + 28f + sway,
                                    cy = topY - 12f,
                                    clusterRadius = 24f,
                                    leafScale = growth,
                                    sway = sway
                                )
                                drawRealisticFoliageCluster(
                                    cx = centerX + sway,
                                    cy = topY - 26f,
                                    clusterRadius = 28f,
                                    leafScale = growth,
                                    sway = sway
                                )
                            }

                            4 -> {
                                // STAGE 4: MAJESTIC MATURE TREE (পরিপূর্ণ বিশাল গাছ)
                                val trunkHeight = 105f * growth
                                val topY = groundY - trunkHeight

                                // Mighty trunk with multiple root buttresses and bark striations
                                val trunkPath = Path().apply {
                                    moveTo(centerX - 20f, groundY)
                                    quadraticBezierTo(centerX - 32f, groundY + 5f, centerX - 42f, groundY + 8f)
                                    quadraticBezierTo(centerX - 22f, groundY - 4f, centerX - 14f, groundY - 25f)
                                    lineTo(centerX - 11f, topY + 45f)
                                    // Low-left major limb
                                    quadraticBezierTo(centerX - 42f + sway, topY + 30f, centerX - 62f + sway, topY + 10f)
                                    lineTo(centerX - 58f + sway, topY + 6f)
                                    quadraticBezierTo(centerX - 35f + sway, topY + 22f, centerX - 6f, topY + 35f)
                                    // High-left limb
                                    quadraticBezierTo(centerX - 28f + sway, topY + 15f, centerX - 40f + sway, topY - 15f)
                                    lineTo(centerX - 36f + sway, topY - 18f)
                                    lineTo(centerX - 2f, topY + 20f)
                                    // High-right limb
                                    quadraticBezierTo(centerX + 26f + sway, topY + 15f, centerX + 38f + sway, topY - 16f)
                                    lineTo(centerX + 34f + sway, topY - 19f)
                                    lineTo(centerX + 3f, topY + 20f)
                                    // Low-right major limb
                                    quadraticBezierTo(centerX + 40f + sway, topY + 32f, centerX + 64f + sway, topY + 8f)
                                    lineTo(centerX + 67f + sway, topY + 12f)
                                    quadraticBezierTo(centerX + 36f + sway, topY + 25f, centerX + 8f, topY + 38f)
                                    // Center trunk
                                    lineTo(centerX + 2f + sway, topY - 24f)
                                    lineTo(centerX - 2f + sway, topY - 24f)
                                    lineTo(centerX + 14f, groundY - 25f)
                                    quadraticBezierTo(centerX + 24f, groundY - 4f, centerX + 40f, groundY + 8f)
                                    close()
                                }
                                drawPath(
                                    trunkPath,
                                    brush = Brush.verticalGradient(
                                        listOf(Color(0xFF795548), Color(0xFF4E342E), Color(0xFF2B1A14))
                                    )
                                )

                                // Bark striation texture lines
                                drawLine(
                                    color = Color(0x35000000),
                                    start = Offset(centerX - 5f, groundY - 15f),
                                    end = Offset(centerX - 3f, topY + 30f),
                                    strokeWidth = 1.5f
                                )
                                drawLine(
                                    color = Color(0x40FFFFFF),
                                    start = Offset(centerX + 4f, groundY - 18f),
                                    end = Offset(centerX + 2f, topY + 25f),
                                    strokeWidth = 1.2f
                                )

                                // Dense layered canopy of realistic small leaves (Over 140 individual leaves across clusters)
                                val clusterSpots = listOf(
                                    Triple(centerX - 68f + sway, topY + 5f, 24f),
                                    Triple(centerX + 68f + sway, topY + 4f, 24f),
                                    Triple(centerX - 46f + sway, topY - 20f, 28f),
                                    Triple(centerX + 44f + sway, topY - 22f, 28f),
                                    Triple(centerX - 22f + sway, topY - 40f, 30f),
                                    Triple(centerX + 22f + sway, topY - 42f, 30f),
                                    Triple(centerX + sway, topY - 52f, 34f)
                                )
                                clusterSpots.forEach { (cx, cy, rad) ->
                                    drawRealisticFoliageCluster(
                                        cx = cx,
                                        cy = cy,
                                        clusterRadius = rad,
                                        leafScale = growth,
                                        sway = sway
                                    )
                                }
                            }

                            5 -> {
                                // STAGE 5: FRUIT / FOOD BEARING TREE (Food / ফল)
                                val trunkHeight = 105f * growth
                                val topY = groundY - trunkHeight

                                val trunkPath = Path().apply {
                                    moveTo(centerX - 18f, groundY)
                                    lineTo(centerX - 10f, groundY - 30f)
                                    lineTo(centerX - 8f, topY + 40f)
                                    quadraticBezierTo(centerX - 45f + sway, topY + 22f, centerX - 62f + sway, topY + 8f)
                                    lineTo(centerX - 58f + sway, topY + 4f)
                                    lineTo(centerX - 4f, topY + 26f)
                                    quadraticBezierTo(centerX + 35f + sway, topY + 24f, centerX + 62f + sway, topY + 6f)
                                    lineTo(centerX + 65f + sway, topY + 10f)
                                    lineTo(centerX + 6f, topY + 30f)
                                    lineTo(centerX + 1f + sway, topY - 22f)
                                    lineTo(centerX - 1f + sway, topY - 22f)
                                    lineTo(centerX + 12f, groundY - 30f)
                                    lineTo(centerX + 16f, groundY)
                                    close()
                                }
                                drawPath(trunkPath, Brush.verticalGradient(listOf(Color(0xFF795548), Color(0xFF3E2723))))

                                // Full realistic foliage clusters
                                val matureClusters = listOf(
                                    Triple(centerX - 62f + sway, topY + 5f, 24f),
                                    Triple(centerX + 62f + sway, topY + 4f, 24f),
                                    Triple(centerX - 42f + sway, topY - 18f, 28f),
                                    Triple(centerX + 40f + sway, topY - 20f, 28f),
                                    Triple(centerX - 20f + sway, topY - 38f, 30f),
                                    Triple(centerX + 20f + sway, topY - 40f, 30f),
                                    Triple(centerX + sway, topY - 50f, 34f)
                                )
                                matureClusters.forEach { (cx, cy, rad) ->
                                    drawRealisticFoliageCluster(cx, cy, rad, growth, sway)
                                }

                                // Glowing ripe Aura Fruits hanging naturally among leaves
                                val fruitSpots = listOf(
                                    Offset(centerX - 60f + sway, topY + 20f),
                                    Offset(centerX - 38f + sway, topY - 4f),
                                    Offset(centerX - 16f + sway, topY - 20f),
                                    Offset(centerX + 14f + sway, topY - 22f),
                                    Offset(centerX + 38f + sway, topY - 6f),
                                    Offset(centerX + 60f + sway, topY + 18f),
                                    Offset(centerX - 4f + sway, topY - 32f),
                                    Offset(centerX + 26f + sway, topY - 34f)
                                )
                                fruitSpots.forEach { fPos ->
                                    // Stem
                                    drawLine(
                                        color = Color(0xFF4E342E),
                                        start = Offset(fPos.x, fPos.y - 10f),
                                        end = fPos,
                                        strokeWidth = 2f
                                    )
                                    // Fruit glow
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            listOf(Color(0x90FF1744), Color.Transparent),
                                            center = fPos,
                                            radius = 16f * growth
                                        ),
                                        radius = 16f * growth,
                                        center = fPos
                                    )
                                    // Apple body
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            listOf(Color(0xFFFF5252), Color(0xFFD50000)),
                                            center = Offset(fPos.x - 2f, fPos.y - 2f),
                                            radius = 8.5f * growth
                                        ),
                                        radius = 8f * growth,
                                        center = fPos
                                    )
                                    // Specular shine
                                    drawCircle(Color.White, radius = 2f * growth, center = Offset(fPos.x - 2.5f, fPos.y - 2.5f))
                                    // Small fruit leaf
                                    drawRealisticSmallLeaf(
                                        centerX = fPos.x + 3f,
                                        centerY = fPos.y - 10f,
                                        angleDeg = 45.0,
                                        length = 8f * growth,
                                        width = 4f * growth,
                                        color = Color(0xFF76FF03)
                                    )
                                }
                            }

                            else -> {
                                // STAGE 6: FULL ZEN SANCTUARY GARDEN (বাগান)
                                // Multi-tree sanctuary: Bonsai master tree + Weeping Sakura + Spruce + Koi Pond + Lantern

                                // Sakura Tree (Left) with delicate small pink leaves
                                val sakuraTrunk = Path().apply {
                                    moveTo(w * 0.16f - 8f, groundY)
                                    quadraticBezierTo(w * 0.14f, groundY - 30f, w * 0.12f, groundY - 60f)
                                    lineTo(w * 0.14f, groundY - 60f)
                                    lineTo(w * 0.16f + 8f, groundY)
                                    close()
                                }
                                drawPath(sakuraTrunk, Brush.verticalGradient(listOf(Color(0xFF5D4037), Color(0xFF2E1C14))))
                                drawRealisticFoliageCluster(
                                    cx = w * 0.12f + sway * 0.6f,
                                    cy = groundY - 70f,
                                    clusterRadius = 26f,
                                    leafScale = growth,
                                    sway = sway,
                                    isSakura = true
                                )

                                // Emerald Spruce Tree (Right)
                                val pineTrunk = Path().apply {
                                    moveTo(w * 0.84f - 6f, groundY)
                                    lineTo(w * 0.84f, groundY - 75f)
                                    lineTo(w * 0.84f + 6f, groundY)
                                    close()
                                }
                                drawPath(pineTrunk, Brush.verticalGradient(listOf(Color(0xFF4E342E), Color(0xFF1B0000))))
                                drawRealisticFoliageCluster(
                                    cx = w * 0.84f + sway * 0.5f,
                                    cy = groundY - 78f,
                                    clusterRadius = 28f,
                                    leafScale = growth,
                                    sway = sway
                                )

                                // Reflective Koi Pond
                                drawOval(
                                    brush = Brush.radialGradient(
                                        listOf(Color(0xBB00F2FE), Color(0x600D47A1), Color.Transparent),
                                        center = Offset(centerX + 62f, groundY + 12f),
                                        radius = 65f
                                    ),
                                    topLeft = Offset(centerX + 12f, groundY - 4f),
                                    size = Size(108f, 32f)
                                )
                                // Pond Lily Pad & Lotus Flower
                                drawCircle(Color(0xFF00E676), radius = 6f, center = Offset(centerX + 50f, groundY + 10f))
                                drawCircle(Color(0xFFFF4081), radius = 3.5f, center = Offset(centerX + 52f, groundY + 9f))

                                // Stone Pagoda Lantern with warm golden glow
                                val lx = centerX - 80f
                                val ly = groundY - 20f
                                drawRect(Color(0xFF90A4AE), topLeft = Offset(lx - 4f, ly), size = Size(8f, 16f))
                                drawRect(Color(0xFFFFD54F), topLeft = Offset(lx - 6f, ly - 8f), size = Size(12f, 8f))
                                drawOval(Color(0xFF546E7A), topLeft = Offset(lx - 10f, ly - 12f), size = Size(20f, 6f))

                                // Main Master Bonsai Tree in center
                                val bonsaiTrunk = Path().apply {
                                    moveTo(centerX - 14f, groundY)
                                    lineTo(centerX - 8f, groundY - 55f)
                                    quadraticBezierTo(centerX - 38f + sway, groundY - 80f, centerX - 50f + sway, groundY - 95f)
                                    lineTo(centerX - 46f + sway, groundY - 98f)
                                    quadraticBezierTo(centerX - 22f + sway, groundY - 80f, centerX - 2f, groundY - 68f)
                                    quadraticBezierTo(centerX + 28f + sway, groundY - 80f, centerX + 48f + sway, groundY - 95f)
                                    lineTo(centerX + 52f + sway, groundY - 92f)
                                    quadraticBezierTo(centerX + 24f + sway, groundY - 76f, centerX + 6f, groundY - 60f)
                                    lineTo(centerX + 12f, groundY)
                                    close()
                                }
                                drawPath(bonsaiTrunk, Brush.verticalGradient(listOf(Color(0xFF6D4C41), Color(0xFF271510))))

                                drawRealisticFoliageCluster(
                                    cx = centerX - 48f + sway,
                                    cy = groundY - 105f,
                                    clusterRadius = 26f,
                                    leafScale = growth,
                                    sway = sway
                                )
                                drawRealisticFoliageCluster(
                                    cx = centerX + 48f + sway,
                                    cy = groundY - 103f,
                                    clusterRadius = 26f,
                                    leafScale = growth,
                                    sway = sway
                                )
                                drawRealisticFoliageCluster(
                                    cx = centerX + sway,
                                    cy = groundY - 122f,
                                    clusterRadius = 30f,
                                    leafScale = growth,
                                    sway = sway
                                )

                                // Aura fruits in Sanctuary Garden
                                listOf(
                                    Offset(centerX - 44f + sway, groundY - 96f),
                                    Offset(centerX + 44f + sway, groundY - 94f)
                                ).forEach { fPos ->
                                    drawCircle(Color(0xFFFF1744), radius = 6.5f, center = fPos)
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

// =============================================================================
// REALISTIC BOTANICAL TREE DRAWING HELPERS (Leaf clusters, fractal limbs & veins)
// =============================================================================

private fun DrawScope.drawRealisticSmallLeaf(
    centerX: Float,
    centerY: Float,
    angleDeg: Double,
    length: Float,
    width: Float,
    color: Color,
    veinColor: Color = Color(0x60FFFFFF)
) {
    val rad = Math.toRadians(angleDeg)
    val cos = Math.cos(rad).toFloat()
    val sin = Math.sin(rad).toFloat()

    val halfLen = length * 0.5f
    val tipX = centerX + cos * halfLen
    val tipY = centerY + sin * halfLen
    val baseX = centerX - cos * halfLen
    val baseY = centerY - sin * halfLen

    val perpX = -sin * (width * 0.5f)
    val perpY = cos * (width * 0.5f)

    val leafPath = Path().apply {
        moveTo(baseX, baseY)
        quadraticBezierTo(centerX + perpX, centerY + perpY, tipX, tipY)
        quadraticBezierTo(centerX - perpX, centerY - perpY, baseX, baseY)
        close()
    }
    drawPath(leafPath, color)
    drawLine(veinColor, Offset(baseX, baseY), Offset(tipX, tipY), strokeWidth = 0.8f)
}

private fun DrawScope.drawRealisticFoliageCluster(
    cx: Float,
    cy: Float,
    clusterRadius: Float,
    leafScale: Float,
    sway: Float,
    isSakura: Boolean = false
) {
    if (leafScale <= 0.05f) return
    val scaledRad = clusterRadius * leafScale
    val leafLen = (9.5f * leafScale).coerceAtLeast(3f)
    val leafWid = (4.8f * leafScale).coerceAtLeast(1.8f)

    // Botanical multi-layer palette: Shadow under-leaves, lush body, vibrant sunlit highlights
    val underColor = if (isSakura) Color(0xFFAD1457) else Color(0xFF1B5E20)
    val midColor1 = if (isSakura) Color(0xFFD81B60) else Color(0xFF2E7D32)
    val midColor2 = if (isSakura) Color(0xFFF06292) else Color(0xFF388E3C)
    val hiColor = if (isSakura) Color(0xFFF8BBD0) else Color(0xFF76FF03)
    val veinCol = if (isSakura) Color(0x40FFFFFF) else Color(0x45FFFFFF)

    val offsets = listOf(
        // Layer 1: Under-layer / Shadow core
        Triple(0f, 0f, 0.0),
        Triple(-scaledRad * 0.40f, -scaledRad * 0.20f, 35.0),
        Triple(scaledRad * 0.40f, -scaledRad * 0.25f, -40.0),
        Triple(0f, -scaledRad * 0.50f, 90.0),
        Triple(-scaledRad * 0.30f, scaledRad * 0.30f, 130.0),
        Triple(scaledRad * 0.30f, scaledRad * 0.28f, -125.0),
        // Layer 2: Main canopy body
        Triple(-scaledRad * 0.60f, 0f, 15.0),
        Triple(scaledRad * 0.60f, 0f, -20.0),
        Triple(-scaledRad * 0.20f, -scaledRad * 0.65f, 75.0),
        Triple(scaledRad * 0.22f, -scaledRad * 0.62f, -80.0),
        Triple(-scaledRad * 0.50f, -scaledRad * 0.42f, 50.0),
        Triple(scaledRad * 0.50f, -scaledRad * 0.42f, -55.0),
        // Layer 3: Sunlit tips & outer budding leaves
        Triple(-scaledRad * 0.78f, -scaledRad * 0.15f, 25.0),
        Triple(scaledRad * 0.78f, -scaledRad * 0.15f, -30.0),
        Triple(0f, -scaledRad * 0.85f, 95.0),
        Triple(-scaledRad * 0.32f, -scaledRad * 0.80f, 65.0),
        Triple(scaledRad * 0.32f, -scaledRad * 0.80f, -70.0)
    )

    offsets.forEachIndexed { idx, (ox, oy, angle) ->
        val swayOffset = sway * (0.35f + (idx % 3) * 0.25f)
        val lx = cx + ox + swayOffset
        val ly = cy + oy
        val col = when {
            idx < 6 -> underColor
            idx < 12 -> if (idx % 2 == 0) midColor1 else midColor2
            else -> hiColor
        }
        drawRealisticSmallLeaf(
            centerX = lx,
            centerY = ly,
            angleDeg = angle + sway * 1.5,
            length = leafLen,
            width = leafWid,
            color = col,
            veinColor = veinCol
        )
    }
}
