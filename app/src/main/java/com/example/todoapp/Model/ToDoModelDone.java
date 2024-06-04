package com.example.todoapp.Model;

public class ToDoModelDone extends  DoneId{
    private String done,due;
    private int status;


    public String getDone() {
        return done;
    }

    public String getDue() {
        return due;
    }

    public int getStatus() {
        return status;
    }
}
