package com.example.module_3_lesson_1_hw_1_compose

import android.util.Log

class DbThread(private val idItemClicked: Int, private val callback: DbThreadCallback): Runnable {

    init {
        Thread(this).start()
    }



    override fun run() {
        var db = App.instance.database
        var taskDao = db.taskDao


        val tasks = taskDao.allTask

        val itemById = taskDao.getById(idItemClicked)

        itemById.flagged = itemById.flagged == false

        taskDao.update(itemById)



        callback.onTaskUpdated(itemById)


        if (tasks.isNotEmpty()){
            Log.d("MYLOG", "${idItemClicked}\n${itemById.flagged}")
        }


    }
}