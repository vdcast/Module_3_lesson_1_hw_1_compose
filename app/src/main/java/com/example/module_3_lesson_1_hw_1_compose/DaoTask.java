package com.example.module_3_lesson_1_hw_1_compose;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoTask {
    @Query("SELECT * FROM Task")
    List<Task> getAllTask();

    @Query("SELECT * FROM Task WHERE id = :id")
    Task getById(int id);

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);
}
