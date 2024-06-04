package com.example.todoapp.Model;

import androidx.annotation.NonNull;

import com.example.todoapp.Doing;

import com.google.firebase.database.Exclude;

public class DoingId {

    @Exclude
    public String DoingId;

    public <D extends DoingId> D withId(@NonNull final String id){
        this.DoingId=id;
        return (D) this;
    }
}
