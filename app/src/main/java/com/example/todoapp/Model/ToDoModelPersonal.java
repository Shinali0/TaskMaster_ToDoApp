package com.example.todoapp.Model;

import com.example.todoapp.Personal;

public class ToDoModelPersonal extends PersonalId{
    private String personal,due;
    private int status;


    public String getPersonal() {
        return personal;
    }

    public String getDue() {
        return due;
    }

    public int getStatus() {
        return status;
    }
}
