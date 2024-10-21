package com.myapplication.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapplication.data.TaskDetail
import com.myapplication.repo.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel(){

    private val _tasks  = mutableStateListOf<TaskDetail>()
    val tasks : State<List<TaskDetail>> = mutableStateOf(_tasks)

    val emptyTask = TaskDetail(0,"", "", "", isFavourite = false, null, null)
    private val _selectedTask = mutableStateOf(emptyTask)
    val selectedTask : State<TaskDetail> = _selectedTask

    init {
        viewModelScope.launch {
            repository.syncTasksFromFirebase(viewModelScope)
            fetchAll()
        }
    }

    private suspend fun fetchAll() {
        _tasks.clear()
        _tasks.addAll(repository.getAllTasks())
    }


    fun upsertSelectedTask() {
        if (Objects.isNull(_selectedTask.value.createdAt)) {
            insertTask()
        } else updateSelectedTask(_selectedTask.value)
        //_selectedTask.value.createdAt?.let{ updateSelectedTask() }?.run { insertTask() }
    }

    private fun insertTask(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTask(task = _selectedTask.value.copy(createdAt = formattedDataString()))
            fetchAll()
            _selectedTask.value = emptyTask
        }
    }

    private fun updateSelectedTask(task: TaskDetail){
        viewModelScope.launch(Dispatchers.IO) {
            val updated = repository.updateTask( task.copy(updatedAt = formattedDataString()))
            fetchAll()
            _selectedTask.value = emptyTask
        }
    }

    fun updateFavourite(id: Long, newValue: Boolean){
        viewModelScope.launch {
            val task = repository.getTaskById(id = id)
            updateSelectedTask(task.copy(isFavourite = newValue))
        }
    }

    fun deleteTask(task:TaskDetail){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task = task)
            _tasks.remove(task)
            _selectedTask.value = emptyTask
        }
    }

    fun loadTaskById(id: Long){
        viewModelScope.launch {
            _selectedTask.value = repository.getTaskById(id = id)
        }
    }

    fun updateTitle(newValue: String){
        _selectedTask.value = _selectedTask.value.copy(title = newValue)
        //updateSelectedTask()
    }

    fun updateStatus(newValue: String){
        _selectedTask.value = _selectedTask.value.copy(status = newValue)
        //updateSelectedTask()
    }

    fun updateDesc(newValue: String){
        _selectedTask.value = _selectedTask.value.copy(desc = newValue)
        //updateSelectedTask()
    }



    private fun formattedDataString() : String {
        return SimpleDateFormat("dd-MMM-yyy HH:MM:SS").format(Date())
    }
}
