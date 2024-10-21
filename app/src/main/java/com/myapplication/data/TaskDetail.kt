package com.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Long =0,
    val title: String = "",
    val desc: String ="",
    val status: String = "Open",
    val isFavourite: Boolean = false,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

enum class TaskStatus {
    OPEN, COMPLETED, CANCELLED, IN_PROGRESS
}