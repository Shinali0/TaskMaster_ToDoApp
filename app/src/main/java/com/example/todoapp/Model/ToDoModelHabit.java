package com.example.todoapp.Model;

public class ToDoModelHabit extends  HabitId{
    private String habit,due;
    private int status;


    public String getHabit() {
        return habit;
    }

    public String getDue() {
        return due;
    }

    public int getStatus() {
        return status;
    }
}
