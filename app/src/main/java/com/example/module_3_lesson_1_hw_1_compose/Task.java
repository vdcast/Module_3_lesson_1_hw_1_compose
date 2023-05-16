package com.example.module_3_lesson_1_hw_1_compose;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Написать органайзер. На главном экране список дел (у каждого дела: тема, описание, время, и
// возможность сделать важным и оно выделится в списке более другим цветом). Можно
// создавать дела, редактировать, удалять.
// Дела сохраняются в БД, т.е. не пропадают при новом запуске приложения

@Entity
public class Task {
    @PrimaryKey
    public int id;
    public String title;
    public String description;
    public int timeHours;
    public int timeMinutes;
    public Boolean flagged;

    public Task(int id, String title, String description, int timeHours, int timeMinutes, Boolean flagged) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timeHours = timeHours;
        this.timeMinutes = timeMinutes;
        this.flagged = flagged;
    }
}
