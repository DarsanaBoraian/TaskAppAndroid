package com.myapplication.repo

import com.google.firebase.database.*

import com.myapplication.data.TaskDetail
import com.myapplication.data.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TaskRepository(
    private val dao: TaskDao,

) {
    private val tasksReference = FirebaseDatabase.getInstance().getReference("tasks")


    suspend fun syncTasksFromFirebase(coroutineScope: CoroutineScope) {
        // For MVP Either Sync Room with data from Firebase or Listen to Changes in Firebase
        val tasksFromFirebase = fetchTasksFromFirebase()

        println("Refreshing the internal caches")
        dao.deleteAll();
        dao.insertTasks(tasksFromFirebase)

        // Now attach the listener
        listenToTaskChanges(coroutineScope)
    }

    private suspend fun listenToTaskChanges(coroutineScope: CoroutineScope) {
        println("Setting up the Firebase event listener")

        tasksReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val task = snapshot.getValue(TaskDetail::class.java)
                task?.let {
                    // Task added to Firebase, insert it into Room
                    println("Event - Task Id: " + task.id + " added in Firebase")
                    //coroutineScope.launch { dao.insertTask(task); }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val task = snapshot.getValue(TaskDetail::class.java)
                task?.let {
                    // Task updated in Firebase, update it in Room
                    println("Event - Task Id: " + task.id + " modified in Firebase")
                    coroutineScope.launch {// dao.updateTask(task);
                     }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val task = snapshot.getValue(TaskDetail::class.java)
                task?.let {
                    // Task removed from Firebase, delete it from Room
                    println("Event - Task Id: " + task.id + " deleted in Firebase")
                    coroutineScope.launch {//dao.deleteTask(task);
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Optional: Handle task reordering, if required
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                println("Firebase listener cancelled: ${error.message}")
            }
        })
    }

    suspend fun deleteTask(task: TaskDetail) {
        dao.deleteTask(task = task)
        tasksReference.child(task.id.toString()).removeValue()
            .addOnCompleteListener { taskResult ->
                if (taskResult.isSuccessful) {
                    println("Successfully deleted task in Firebase")
                } else {
                    println("Failed to delete task in Firebase")
                }
            }
    }

    suspend fun insertTask(task: TaskDetail) : TaskDetail {
        val generatedId = dao.insertTask(task)
        println("Inserted task into Room")
        val taskWithId = task.copy(id = generatedId)  // Copy the task with the auto-generated ID
        tasksReference.child(generatedId.toString()).setValue(taskWithId)
        println("Inserted task into Firebase")
        return taskWithId
    }

    suspend fun updateTask(task: TaskDetail) : TaskDetail {
        dao.updateTask(task)
        tasksReference.child(task.id.toString()).setValue(task)
            .addOnCompleteListener { taskResult ->
                if (taskResult.isSuccessful) {
                    println("Successfully updated task in Firebase")
                } else {
                    println("Failed to update task in Firebase")
                }
            }
        return task
    }

    suspend fun getTaskById(id: Long) : TaskDetail  {
        return dao.getTaskById(id = id)
    }

    suspend fun getAllTasks(): List<TaskDetail> = dao.getAllTasks()



    private suspend fun fetchTasksFromFirebase() : List<TaskDetail> {
        println("Fetching Tasks from Firebase")
        val taskList = mutableListOf<TaskDetail>()
        val querySnapshot = tasksReference.get().await()

        for (document in querySnapshot.children) {
            val task = document.getValue(TaskDetail::class.java)
            task?.let {taskList.add(it)}
        }
        return taskList
    }

    

}