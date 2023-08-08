package com.example.module_3_lesson_1_hw_1_compose.threads

import com.example.module_3_lesson_1_hw_1_compose.App
import com.example.module_3_lesson_1_hw_1_compose.data.Task

class DbThreadAddNewTask(private val callback: DbThreadAddNewTaskCallback): Runnable {
    init {
        Thread(this).start()
    }


    override fun run() {
        var db = App.instance.database
        var taskDao =db.taskDao


        val lastTask = taskDao.allTask.lastOrNull()
        val newId = lastTask?.id?.plus(1) ?: 1


        val newTask = Task(
            newId,
            "Task $newId",
            "Description $newId",
            3,
            33,
            false
        )

        taskDao.insert(newTask)

        callback.onTaskAdded(newTask)

//        taskStateValue = taskDao.allTask

    }
}