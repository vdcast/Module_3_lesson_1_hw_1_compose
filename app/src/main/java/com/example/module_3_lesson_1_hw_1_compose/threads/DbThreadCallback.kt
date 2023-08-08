package com.example.module_3_lesson_1_hw_1_compose.threads

import com.example.module_3_lesson_1_hw_1_compose.data.Task

interface DbThreadCallback {
    fun onTaskUpdated(task: Task)
}