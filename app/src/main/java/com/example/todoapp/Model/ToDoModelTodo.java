package com.example.todoapp.Model;

public class ToDoModelTodo extends  TodoId{
    private String todo,due;
    private int status;


    public String getTodo() {
        return todo;
    }

    public String getDue() {
        return due;
    }

    public int getStatus() {
        return status;
    }
}
