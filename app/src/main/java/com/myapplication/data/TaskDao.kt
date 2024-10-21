package com.myapplication.data

import androidx.room.*

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskDetail): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskDetail>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: TaskDetail)

    @Delete
    suspend fun deleteTask(task:TaskDetail)

    @Query("DELETE FROM TaskDetail")
    suspend fun deleteAll()

    @Query("SELECT * FROM TaskDetail where id = :id")
    suspend fun getTaskById(id:Long): TaskDetail

    @Query("SELECT * FROM TaskDetail")
    suspend fun getAllTasks(): List<TaskDetail>
}