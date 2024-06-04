package com.example.todoapp.Model;

import androidx.annotation.NonNull;

import com.example.todoapp.Todo;

import com.google.firebase.database.Exclude;

public class TodoId {

    @Exclude
    public String TodoId;

    public <T1 extends TodoId> T1 withId(@NonNull final String id){
        this.TodoId=id;
        return (T1) this;
    }
}
