package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GamificationManager
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassChip
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CognitiveQuotientInfo(
    val code: String, // "IQ", "EX", "SQ", "ESQ", "MQ", "RQ"
    val fullName: String,
    val description: String,
    val color: Color,
    val baseScore: Int,
    val dayDeltaPercent: Float,
    val weekDeltaPercent: Float,
    val monthDeltaPercent: Float,
    val trainedByGames: List<String>
)

@Composable
fun AnalyticsAndHistoryScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val gamificationState by GamificationManager.state.collectAsState()
    var selectedMainTab by remember { mutableIntStateOf(0) } // 0: Cognitive Trends, 1: Match History, 2: Personal Bests
    var selectedTimeframe by remember { mutableIntStateOf(1) } // 0: 1 Day, 1: 7 Days, 2: 1 Month (30 Days)
    var selectedChartMode by remember { mutableIntStateOf(0) } // 0: Radar Web, 1: Growth Columns, 2: Trend Curves

    val matches = gamificationState.matchHistory
    val personalBests = gamificationState.personalBests

    // Calculate aggregated metrics
    val avgReactionMs = if (matches.isNotEmpty()) matches.map { it.reactionTimeMs }.average().toInt() else 215
    val avgAccuracy = if (matches.isNotEmpty()) matches.map { it.accuracyPercent }.average().toInt() else 94
    val overallBrainIndex = (avgAccuracy * 0.7f + (1000 - avgReactionMs).coerceIn(0, 1000) * 0.05f).toInt().coerceIn(100, 145)

    // The 6 Fundamental Cognitive Quotients mapped to games
    val quotients = remember {
        listOf(
            CognitiveQuotientInfo(
                code = "IQ",
                fullName = "Fluid & Quantitative Logic (IQ)",
                description = "Rapid mathematical deduction, numeral sequence processing & pattern extrapolation.",
                color = NeonCyan,
                baseScore = 126,
                dayDeltaPercent = +3.8f,
                weekDeltaPercent = +12.4f,
                monthDeltaPercent = +24.8f,
                trainedByGames = listOf("Mental Math Sprint", "Speed Number Sequence", "Dual N-Back Mini", "Codebreaker Cipher")
            ),
            CognitiveQuotientInfo(
                code = "EX",
                fullName = "Executive Control & Inhibition (EX)",
                description = "Selective impulse filtering, cognitive conflict resolution & dynamic rule switching.",
                color = NeonPink,
                baseScore = 118,
                dayDeltaPercent = +4.1f,
                weekDeltaPercent = +14.6f,
                monthDeltaPercent = +28.2f,
                trainedByGames = listOf("Stroop Color Clash", "Chroma Clash Inversion", "Cognitive Flex Duel", "Pattern Flanker")
            ),
            CognitiveQuotientInfo(
                code = "SQ",
                fullName = "Spatial & Visualization Quotient (SQ)",
                description = "Mental shape rotation, complex dynamic trajectories & labyrinth navigation.",
                color = NeonLime,
                baseScore = 122,
                dayDeltaPercent = +3.2f,
                weekDeltaPercent = +11.8f,
                monthDeltaPercent = +22.5f,
                trainedByGames = listOf("Spatial Angle Match", "Target Lock Radar", "Quantum Flow Maze", "Memory Matrix")
            ),
            CognitiveQuotientInfo(
                code = "ESQ",
                fullName = "Emotional Stability & Rhythm (ESQ)",
                description = "Parasympathetic equilibrium, tempo synchrony & motor precision under stress.",
                color = NeonViolet,
                baseScore = 115,
                dayDeltaPercent = +2.6f,
                weekDeltaPercent = +9.5f,
                monthDeltaPercent = +18.9f,
                trainedByGames = listOf("Rhythm Beat Tap", "Neuro Balance Tilt", "Pitch Match Ear Trainer", "Box Breathing")
            ),
            CognitiveQuotientInfo(
                code = "MQ",
                fullName = "Working Memory Quotient (MQ)",
                description = "Multi-item working memory buffers, backward digit span & visual pattern recall.",
                color = NeonAmber,
                baseScore = 130,
                dayDeltaPercent = +4.5f,
                weekDeltaPercent = +15.2f,
                monthDeltaPercent = +31.0f,
                trainedByGames = listOf("Chimp Memory Matrix", "Digit Span Recall", "Cyber Glitch Sequence")
            ),
            CognitiveQuotientInfo(
                code = "RQ",
                fullName = "Reflex Latency & Reaction Agility (RQ)",
                description = "Micro-millisecond perceptual reaction speed & stimulus-response latency.",
                color = Color(0xFF00E5FF),
                baseScore = 124,
                dayDeltaPercent = +5.4f,
                weekDeltaPercent = +16.8f,
                monthDeltaPercent = +33.4f,
                trainedByGames = listOf("Micro-Reaction Tap", "Speed Sequence", "Chroma Clash Inversion")
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
        // Top Nav Bar
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
                        text = "Cognitive Intelligence Studio",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "IQ, EX, SQ, ESQ, MQ, RQ longitudinal telemetry",
                        style = MaterialTheme.typography.bodySmall,
                        color = NeonCyan
                    )
                }
            }

            GlassChip(label = "Cognitive Index $overallBrainIndex 🧠", color = NeonCyan, selected = true)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3-Way Sub-Tabs: Cognitive Analytics vs Match History vs Personal Bests
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0x14FFFFFF))
                .padding(4.dp)
        ) {
            listOf("🧠 Cognitive IQs", "📜 History Logs", "🏆 Best Records").forEachIndexed { index, label ->
                val isSelected = selectedMainTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) NeonCyan.copy(alpha = 0.25f) else Color.Transparent)
                        .border(1.dp, if (isSelected) NeonCyan else Color.Transparent, RoundedCornerShape(10.dp))
                        .clickable { selectedMainTab = index },
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

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedMainTab) {
            0 -> {
                // =============================================================
                // TAB 0: COGNITIVE ATTRIBUTE GROWTH (1 DAY / 7 DAYS / 1 MONTH)
                // =============================================================
                // Timeframe Selector: 1 Day vs 7 Days vs 1 Month
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Time Horizon:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("1 Day", "7 Days", "1 Month (30d)").forEachIndexed { idx, periodLabel ->
                            val isChosen = selectedTimeframe == idx
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isChosen) NeonCyan.copy(alpha = 0.25f) else Color(0x14FFFFFF))
                                    .border(1.dp, if (isChosen) NeonCyan else Color(0x20FFFFFF), RoundedCornerShape(8.dp))
                                    .clickable { selectedTimeframe = idx }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = periodLabel,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isChosen) NeonCyan else Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Summary Horizon Badge Banner
                val periodName = when (selectedTimeframe) {
                    0 -> "Past 24 Hours (1 Day)"
                    1 -> "Past 7 Days (Weekly)"
                    else -> "Past 30 Days (Monthly)"
                }
                val avgGrowth = when (selectedTimeframe) {
                    0 -> "+3.9%"
                    1 -> "+13.5%"
                    else -> "+26.5%"
                }

                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = NeonLime,
                    glowRadius = 10.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Cognitive Trajectory ($periodName)", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                            Text("Aggregated neuroplastic gains across 17 brain domains", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        GlassChip(label = "$avgGrowth Growth ▲", color = NeonLime, selected = true)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CHART MODE TOGGLE BAR
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x14FFFFFF))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("🕸️ Radar Profile", "📊 Growth Bars", "📈 Trend Curves").forEachIndexed { cIdx, cLabel ->
                        val isSelected = selectedChartMode == cIdx
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NeonCyan.copy(alpha = 0.28f) else Color.Transparent)
                                .border(1.dp, if (isSelected) NeonCyan else Color.Transparent, RoundedCornerShape(8.dp))
                                .clickable { selectedChartMode = cIdx },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cLabel,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NeonCyan else Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // INTERACTIVE CHART VISUALIZATION CARD
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowColor = NeonCyan,
                    glowRadius = 10.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (selectedChartMode) {
                            0 -> {
                                // MODE 0: Custom 6-Axis Radar Web Canvas
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Multi-Quotient Radar Profile", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text("● Baseline", color = Color(0x60FFFFFF), fontSize = 10.sp)
                                        Text("● Current ($avgGrowth)", color = NeonCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Canvas(
                                    modifier = Modifier.size(220.dp)
                                ) {
                                    val cx = size.width / 2f
                                    val cy = size.height / 2f
                                    val maxR = size.minDimension * 0.42f

                                    // Draw concentric 6-sided webs
                                    for (level in 1..4) {
                                        val r = maxR * (level / 4f)
                                        val webPath = Path().apply {
                                            for (i in 0 until 6) {
                                                val angle = (i * 60 - 30) * (Math.PI / 180.0)
                                                val px = cx + (r * Math.cos(angle)).toFloat()
                                                val py = cy + (r * Math.sin(angle)).toFloat()
                                                if (i == 0) moveTo(px, py) else lineTo(px, py)
                                            }
                                            close()
                                        }
                                        drawPath(webPath, color = Color(0x18FFFFFF), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f))
                                    }

                                    // Draw 6 radial spokes
                                    for (i in 0 until 6) {
                                        val angle = (i * 60 - 30) * (Math.PI / 180.0)
                                        val px = cx + (maxR * Math.cos(angle)).toFloat()
                                        val py = cy + (maxR * Math.sin(angle)).toFloat()
                                        drawLine(color = Color(0x20FFFFFF), start = Offset(cx, cy), end = Offset(px, py), strokeWidth = 1f)
                                    }

                                    // Dynamic current score polygon
                                    val polyPath = Path().apply {
                                        quotients.forEachIndexed { i, q ->
                                            val growthFactor = when (selectedTimeframe) {
                                                0 -> 1f + (q.dayDeltaPercent / 100f)
                                                1 -> 1f + (q.weekDeltaPercent / 100f)
                                                else -> 1f + (q.monthDeltaPercent / 100f)
                                            }
                                            val r = (maxR * 0.72f * growthFactor).coerceAtMost(maxR)
                                            val angle = (i * 60 - 30) * (Math.PI / 180.0)
                                            val px = cx + (r * Math.cos(angle)).toFloat()
                                            val py = cy + (r * Math.sin(angle)).toFloat()
                                            if (i == 0) moveTo(px, py) else lineTo(px, py)
                                        }
                                        close()
                                    }

                                    drawPath(polyPath, brush = Brush.radialGradient(listOf(NeonCyan.copy(alpha = 0.35f), Color.Transparent), center = Offset(cx, cy), radius = maxR))
                                    drawPath(polyPath, color = NeonCyan, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5f))

                                    // Draw vertices dots
                                    quotients.forEachIndexed { i, q ->
                                        val growthFactor = when (selectedTimeframe) {
                                            0 -> 1f + (q.dayDeltaPercent / 100f)
                                            1 -> 1f + (q.weekDeltaPercent / 100f)
                                            else -> 1f + (q.monthDeltaPercent / 100f)
                                        }
                                        val r = (maxR * 0.72f * growthFactor).coerceAtMost(maxR)
                                        val angle = (i * 60 - 30) * (Math.PI / 180.0)
                                        val px = cx + (r * Math.cos(angle)).toFloat()
                                        val py = cy + (r * Math.sin(angle)).toFloat()
                                        drawCircle(color = Color.White, radius = 4f, center = Offset(px, py))
                                        drawCircle(color = q.color, radius = 2.5f, center = Offset(px, py))
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Hexagonal axes: IQ (Logic), EX (Control), SQ (Spatial), ESQ (Calm), MQ (Memory), RQ (Reflex)",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            1 -> {
                                // MODE 1: Comparative Growth Bars (Baseline vs Current Growth)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Growth Bar Telemetry ($periodName)", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text("░ Baseline", color = Color(0x60FFFFFF), fontSize = 10.sp)
                                        Text("█ Gain ($avgGrowth)", color = NeonLime, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                ) {
                                    val w = size.width
                                    val h = size.height
                                    val baseY = h * 0.85f
                                    val colW = (w / quotients.size)

                                    // Baseline guideline
                                    drawLine(
                                        color = Color(0x20FFFFFF),
                                        start = Offset(0f, baseY),
                                        end = Offset(w, baseY),
                                        strokeWidth = 1f
                                    )

                                    quotients.forEachIndexed { i, q ->
                                        val delta = when (selectedTimeframe) {
                                            0 -> q.dayDeltaPercent
                                            1 -> q.weekDeltaPercent
                                            else -> q.monthDeltaPercent
                                        }
                                        val xCenter = i * colW + colW / 2f
                                        val barWidth = colW * 0.42f
                                        val maxScore = 170f
                                        val baseBarH = ((q.baseScore / maxScore) * (h * 0.7f)).coerceIn(20f, h * 0.7f)
                                        val currentBarH = (((q.baseScore * (1f + delta / 100f)) / maxScore) * (h * 0.7f)).coerceIn(20f, h * 0.78f)

                                        // 1. Baseline bar (Ghosted outline)
                                        drawRect(
                                            color = Color(0x20FFFFFF),
                                            topLeft = Offset(xCenter - barWidth / 2f, baseY - baseBarH),
                                            size = androidx.compose.ui.geometry.Size(barWidth, baseBarH)
                                        )

                                        // 2. Growth Column (Vibrant neon gradient with glow)
                                        drawRect(
                                            brush = Brush.verticalGradient(
                                                listOf(q.color, q.color.copy(alpha = 0.45f))
                                            ),
                                            topLeft = Offset(xCenter - barWidth / 2f, baseY - currentBarH),
                                            size = androidx.compose.ui.geometry.Size(barWidth, currentBarH)
                                        )

                                        // Neon cap line
                                        drawLine(
                                            color = Color.White,
                                            start = Offset(xCenter - barWidth / 2f, baseY - currentBarH),
                                            end = Offset(xCenter + barWidth / 2f, baseY - currentBarH),
                                            strokeWidth = 2.5f
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Quotient labels under columns
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    quotients.forEach { q ->
                                        val delta = when (selectedTimeframe) {
                                            0 -> q.dayDeltaPercent
                                            1 -> q.weekDeltaPercent
                                            else -> q.monthDeltaPercent
                                        }
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(q.code, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = q.color)
                                            Text("+${delta.toInt()}%", fontSize = 9.sp, color = NeonLime, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                            else -> {
                                // MODE 2: Longitudinal Trend Spline Curves
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Neuroplastic Spline Trajectory", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                                    GlassChip(label = "Adaptive AI Gain 📈", color = NeonCyan, selected = true)
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                ) {
                                    val w = size.width
                                    val h = size.height
                                    val bottomY = h * 0.85f

                                    // Subtle Grid lines
                                    for (step in 1..3) {
                                        val gy = bottomY * (step / 4f)
                                        drawLine(
                                            color = Color(0x10FFFFFF),
                                            start = Offset(0f, gy),
                                            end = Offset(w, gy),
                                            strokeWidth = 1f
                                        )
                                    }

                                    // Trajectory Curve for top 3 cognitive pillars (IQ, EX, MQ)
                                    val curveTracks = listOf(
                                        quotients[0] to NeonCyan,
                                        quotients[1] to NeonPink,
                                        quotients[4] to NeonAmber
                                    )

                                    curveTracks.forEach { (q, curveCol) ->
                                        val p0 = Offset(0f, bottomY - (q.baseScore / 170f) * (h * 0.65f))
                                        val p1 = Offset(w * 0.33f, bottomY - ((q.baseScore * (1f + q.dayDeltaPercent / 100f)) / 170f) * (h * 0.65f))
                                        val p2 = Offset(w * 0.66f, bottomY - ((q.baseScore * (1f + q.weekDeltaPercent / 100f)) / 170f) * (h * 0.65f))
                                        val p3 = Offset(w, bottomY - ((q.baseScore * (1f + q.monthDeltaPercent / 100f)) / 170f) * (h * 0.65f))

                                        val curvePath = Path().apply {
                                            moveTo(p0.x, p0.y)
                                            cubicTo(w * 0.16f, p0.y, w * 0.20f, p1.y, p1.x, p1.y)
                                            cubicTo(w * 0.48f, p1.y, w * 0.52f, p2.y, p2.x, p2.y)
                                            cubicTo(w * 0.80f, p2.y, w * 0.84f, p3.y, p3.x, p3.y)
                                        }

                                        // Area fill under primary IQ curve
                                        if (q.code == "IQ") {
                                            val fillPath = Path().apply {
                                                addPath(curvePath)
                                                lineTo(w, bottomY)
                                                lineTo(0f, bottomY)
                                                close()
                                            }
                                            drawPath(
                                                fillPath,
                                                brush = Brush.verticalGradient(
                                                    listOf(curveCol.copy(alpha = 0.28f), Color.Transparent),
                                                    startY = 0f,
                                                    endY = bottomY
                                                )
                                            )
                                        }

                                        // Stroke curve
                                        drawPath(curvePath, color = curveCol, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5f))

                                        // Data checkpoint dots
                                        listOf(p0, p1, p2, p3).forEach { pt ->
                                            drawCircle(Color.White, radius = 3.5f, center = pt)
                                            drawCircle(curveCol, radius = 2f, center = pt)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Checkpoint X-Axis labels
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    listOf("Origin", "1 Day (+3.8%)", "7 Days (+12.4%)", "30 Days (+24.8%)").forEach { lbl ->
                                        Text(lbl, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Quotient Progression Breakdown ($periodName):",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                // The 6 Detailed Quotient Progression Cards
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    quotients.forEach { q ->
                        val deltaPercent = when (selectedTimeframe) {
                            0 -> q.dayDeltaPercent
                            1 -> q.weekDeltaPercent
                            else -> q.monthDeltaPercent
                        }
                        val currentScore = (q.baseScore * (1f + deltaPercent / 100f)).toInt()
                        val isPositive = deltaPercent >= 0

                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            glowColor = q.color,
                            glowRadius = 6.dp
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(q.color.copy(alpha = 0.22f))
                                                .border(1.dp, q.color, RoundedCornerShape(8.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(q.code, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = q.color)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(q.fullName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                                            Text("Baseline: ${q.baseScore} ➔ Current: $currentScore", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }

                                    // Delta Badge
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isPositive) NeonLime.copy(alpha = 0.2f) else NeonPink.copy(alpha = 0.2f))
                                            .border(1.dp, if (isPositive) NeonLime else NeonPink, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = if (isPositive) "+${deltaPercent}% ▲" else "${deltaPercent}% ▼",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = if (isPositive) NeonLime else NeonPink
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Progress bar comparing baseline to current
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x20FFFFFF))
                                ) {
                                    val progressFraction = (currentScore / 180f).coerceIn(0.1f, 1f)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(fraction = progressFraction)
                                            .height(6.dp)
                                            .clip(CircleShape)
                                            .background(Brush.horizontalGradient(listOf(q.color, NeonCyan)))
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = q.description,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 15.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Trained by: ", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = q.color)
                                    Text(q.trainedByGames.joinToString(" • "), fontSize = 10.sp, color = Color(0xCCFFFFFF))
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // =============================================================
                // TAB 1: RECENT MATCH HISTORY LOG
                // =============================================================
                Text("Match History (Recent Plays):", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                if (matches.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No match records yet! Play any game to log scores.", color = Color.Gray, fontSize = 12.sp)
                    }
                } else {
                    matches.forEach { match ->
                        val dateFormatted = SimpleDateFormat("h:mm a", Locale.US).format(Date(match.timestamp))
                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            glowColor = if (match.isPersonalBest) NeonAmber else Color.Transparent,
                            glowRadius = if (match.isPersonalBest) 8.dp else 0.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(if (match.isPersonalBest) NeonAmber.copy(alpha = 0.25f) else Color(0x18FFFFFF)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(if (match.isPersonalBest) "🏆" else "🎮", fontSize = 16.sp)
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(match.gameTitle, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                                            if (match.isPersonalBest) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text("RECORD!", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeonAmber)
                                            }
                                        }
                                        Text("$dateFormatted • ${match.accuracyPercent}% Acc • ${match.reactionTimeMs}ms", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text("${match.score} pts", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NeonCyan)
                                    Text("Streak: ${match.streakAtMatch}x 🔥", fontSize = 10.sp, color = NeonLime)
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // =============================================================
                // TAB 2: PERSONAL BESTS HALL OF FAME
                // =============================================================
                Text("🏆 Personal Bests & High Score Records:", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Whenever you break a high score, it updates automatically with bonus rewards!", fontSize = 11.sp, color = NeonAmber)
                Spacer(modifier = Modifier.height(10.dp))

                personalBests.values.forEach { pb ->
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        glowColor = NeonAmber,
                        glowRadius = 8.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(NeonAmber.copy(alpha = 0.25f))
                                        .border(1.dp, NeonAmber, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("👑", fontSize = 18.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(pb.gameTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                                    Text("Best Streak: ${pb.bestStreak}x • Accuracy: ${pb.bestAccuracy}%", fontSize = 11.sp, color = NeonLime)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("${pb.highScore} PTS", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NeonAmber)
                                Text("Date: ${pb.dateAchieved}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}
