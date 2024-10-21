package com.myapplication.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.myapplication.data.TaskDetail
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks
    var taskTitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Open") }

    var expanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("Open", "In Progress", "Completed", "Deferred")

    val currentDateTime = LocalDateTime.now()
    val formattedDateTime = remember {
        currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }


    Column(modifier = Modifier.padding(16.dp)) {
        // Input Field for Task Title
        TextField(
            value = taskTitle,
            onValueChange = {
                taskTitle = it
                viewModel.updateTitle(it)
            },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
                .background(Color.White)
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = description,
            onValueChange = {
                description = it
                viewModel.updateDesc(it)
            },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
                .background(Color.White)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for Task Status
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded } // Control expansion state
        ) {
            TextField(
                value = status,
                onValueChange = {},
                label = { Text("Status") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable { expanded = !expanded }, // Allow click to expand dropdown
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                },
                readOnly = true // Make the TextField non-editable
            )

            DropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = expanded,
                onDismissRequest = { expanded = false } // Collapse when clicked outside
            ) {
                statusOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            status = option
                            expanded = false // Collapse after selection
                            viewModel.updateStatus(status) // Update status in ViewModel
                        }
                    ) {
                        Text(option)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // TextField to display current date and time (read-only)
        TextField(
            value = formattedDateTime,
            onValueChange = {},
            label = { Text("Current Date & Time") },
            modifier = Modifier.fillMaxWidth().background(Color.White),
            readOnly = true // Make it non-editable
        )

        Spacer(modifier = Modifier.height(8.dp))
        // Add / Update Button
        Button(
            onClick = {
                if (taskTitle.isNotEmpty()) {
                    viewModel.upsertSelectedTask()
                    taskTitle = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add / Update Task")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the list of Tasks with Edit and Favourite functionality
        TaskList(
            tasks = tasks,
            onDelete = { viewModel.deleteTask(it) },
            onEdit = {
                viewModel.loadTaskById(it.id)
                taskTitle = it.title // Update UI with the selected task
            },
            onFavouriteToggle = {
                viewModel.updateFavourite(it.id, it.isFavourite.not()) // Toggle the current favourite status
                //viewModel.updateTask(it.copy(isFavourite = !it.isFavourite)) // Save the change in the repository
            }
        )
    }
}


@Composable
fun TaskItem(
    task: TaskDetail,
    onDelete: (TaskDetail) -> Unit,
    onEdit: (TaskDetail) -> Unit,
    onFavouriteToggle: (TaskDetail) -> Unit // Callback to toggle favourite
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(task.title, style = MaterialTheme.typography.bodySmall)
        }

        // Heart (Favourite) button
        IconButton(onClick = { onFavouriteToggle(task) }) {
            Icon(
                if (task.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Toggle Favourite"
            )
        }

        // Edit button (icon)
        IconButton(onClick = { onEdit(task) }) {
            Icon(Icons.Default.Edit, contentDescription = "Edit Task")
        }

        // Delete button (icon)
        IconButton(onClick = { onDelete(task) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Task")
        }
    }
}




