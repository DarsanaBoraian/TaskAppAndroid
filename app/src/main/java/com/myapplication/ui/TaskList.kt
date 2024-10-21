package com.myapplication.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.myapplication.data.TaskDetail


@Composable
fun TaskList(
    tasks: List<TaskDetail>,
    onDelete: (TaskDetail) -> Unit,
    onEdit: (TaskDetail) -> Unit,
    onFavouriteToggle: (TaskDetail) -> Unit // Callback to toggle favourite
) {
    LazyColumn {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onDelete = onDelete,
                onEdit = onEdit,
                onFavouriteToggle = onFavouriteToggle // Pass favourite toggle action
            )
        }
    }
}