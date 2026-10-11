package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassButton
import com.example.ui.components.GlassCard
import com.example.ui.components.GlassChip
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet

enum class TaskColumn(val label: String, val color: Color) {
    BACKLOG("Backlog", Color(0xFF94A3B8)),
    IN_PROGRESS("In Progress", NeonCyan),
    REVIEW("Review", NeonViolet),
    DONE("Completed", NeonLime)
}

enum class TaskPriority(val label: String, val color: Color) {
    LOW("Low", Color(0xFF64748B)),
    MEDIUM("Med", NeonAmber),
    HIGH("High", NeonPink),
    URGENT("Urgent", Color(0xFFFF3366))
}

data class TaskItem(
    val id: String,
    val title: String,
    val description: String = "",
    val column: TaskColumn,
    val priority: TaskPriority,
    val subtasksTotal: Int = 0,
    val subtasksDone: Int = 0,
    val isDone: Boolean = false
)

@Composable
fun TasksScreen(
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (onBack != null) {
        androidx.activity.compose.BackHandler { onBack() }
    }
    val tasks = remember {
        mutableStateListOf(
            TaskItem("1", "Draft NeuroBox architecture", "Room SQLite schema and DAO interfaces", TaskColumn.TODO, TaskPriority.HIGH, 3, 0, false),
            TaskItem("2", "Design 3D Glassmorphism Cards", "Layered acrylic surfaces with neon edge shaders", TaskColumn.TODO, TaskPriority.URGENT, 4, 0, false),
            TaskItem("3", "Implement Pomodoro countdown loop", "Precision timekeeper with breathing pulse", TaskColumn.TODO, TaskPriority.HIGH, 2, 0, false),
            TaskItem("4", "Local MP3 audio player SAF hook", "Storage Access Framework integration", TaskColumn.BACKLOG, TaskPriority.MEDIUM, 2, 0, false),
            TaskItem("5", "Weekly habit heatmap matrix", "GitHub contribution grid in Compose canvas", TaskColumn.BACKLOG, TaskPriority.HIGH, 3, 0, false)
        )
    }

    var selectedTab by remember { mutableStateOf(TaskColumn.IN_PROGRESS) }
    var showAddTaskDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onBack != null) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0x18FFFFFF))
                            .border(1.dp, Color(0x30FFFFFF), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Column {
                    Text(
                        text = "Smart Tasks & Kanban",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${tasks.count { !it.isDone }} active • ${tasks.count { it.isDone }} completed",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(
                onClick = { showAddTaskDialog = true },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(NeonCyan.copy(alpha = 0.2f))
                    .border(1.dp, NeonCyan, CircleShape)
                    .testTag("add_task_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task",
                    tint = NeonCyan
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Horizontal Kanban Column Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TaskColumn.values().forEach { column ->
                val count = tasks.count { it.column == column }
                val isSelected = selectedTab == column
                Box(
                    modifier = Modifier
                        .height(38.dp)
                        .clip(RoundedCornerShape(19.dp))
                        .background(if (isSelected) column.color.copy(alpha = 0.25f) else Color(0x10FFFFFF))
                        .border(1.dp, if (isSelected) column.color else Color(0x20FFFFFF), RoundedCornerShape(19.dp))
                        .clickable { selectedTab = column }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(column.color)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${column.label} ($count)",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = if (isSelected) column.color else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Tasks in selected column
        val columnTasks = tasks.filter { it.column == selectedTab }

        if (columnTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No tasks in this lane", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    GlassButton(
                        text = "Create Task",
                        onClick = { showAddTaskDialog = true },
                        isPrimary = false
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(columnTasks, key = { it.id }) { task ->
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        glowColor = task.priority.color,
                        glowRadius = 12.dp,
                        testTag = "task_card_${task.id}"
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GlassChip(
                                    label = task.priority.label,
                                    color = task.priority.color,
                                    selected = true
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Move to next column button
                                    if (task.column != TaskColumn.DONE) {
                                        IconButton(
                                            onClick = {
                                                val nextCol = when (task.column) {
                                                    TaskColumn.BACKLOG -> TaskColumn.IN_PROGRESS
                                                    TaskColumn.IN_PROGRESS -> TaskColumn.REVIEW
                                                    TaskColumn.REVIEW -> TaskColumn.DONE
                                                    TaskColumn.DONE -> TaskColumn.DONE
                                                }
                                                val idx = tasks.indexOf(task)
                                                if (idx >= 0) {
                                                    tasks[idx] = task.copy(
                                                        column = nextCol,
                                                        isDone = nextCol == TaskColumn.DONE
                                                    )
                                                }
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                                contentDescription = "Advance Column",
                                                tint = NeonCyan,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    // Mark Done toggle
                                    IconButton(
                                        onClick = {
                                            val idx = tasks.indexOf(task)
                                            if (idx >= 0) {
                                                val newDone = !task.isDone
                                                tasks[idx] = task.copy(
                                                    isDone = newDone,
                                                    column = if (newDone) TaskColumn.DONE else TaskColumn.IN_PROGRESS
                                                )
                                            }
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (task.isDone) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                                            contentDescription = "Toggle Complete",
                                            tint = if (task.isDone) NeonLime else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
                                ),
                                color = if (task.isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onBackground
                            )

                            if (task.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = task.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (task.subtasksTotal > 0) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(Color(0x20FFFFFF))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(task.subtasksDone.toFloat() / task.subtasksTotal.toFloat())
                                                .height(4.dp)
                                                .background(NeonCyan)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "${task.subtasksDone}/${task.subtasksTotal} subtasks",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Task Dialog
    if (showAddTaskDialog) {
        var newTitle by remember { mutableStateOf("") }
        var newDesc by remember { mutableStateOf("") }
        var newPriority by remember { mutableStateOf(TaskPriority.HIGH) }

        AlertDialog(
            onDismissRequest = { showAddTaskDialog = false },
            title = { Text("New NeuroBox Task", color = MaterialTheme.colorScheme.onBackground) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Task Title") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = Color(0x33FFFFFF)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newDesc,
                        onValueChange = { newDesc = it },
                        label = { Text("Description / Details") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = Color(0x33FFFFFF)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TaskPriority.values().forEach { p ->
                            GlassChip(
                                label = p.label,
                                color = p.color,
                                selected = newPriority == p,
                                onClick = { newPriority = p }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            tasks.add(
                                TaskItem(
                                    id = System.currentTimeMillis().toString(),
                                    title = newTitle,
                                    description = newDesc,
                                    column = selectedTab,
                                    priority = newPriority,
                                    subtasksTotal = 0,
                                    subtasksDone = 0,
                                    isDone = false
                                )
                            )
                            showAddTaskDialog = false
                        }
                    }
                ) {
                    Text("Add Task", color = NeonCyan)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTaskDialog = false }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = Color(0xFF131625)
        )
    }
}
