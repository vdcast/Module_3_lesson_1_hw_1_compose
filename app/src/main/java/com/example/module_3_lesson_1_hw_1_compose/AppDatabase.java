package com.example.module_3_lesson_1_hw_1_compose;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoTask getTaskDao();
}
