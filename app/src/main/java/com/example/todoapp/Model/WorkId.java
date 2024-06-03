package com.example.todoapp.Model;

import androidx.annotation.NonNull;
import com.example.todoapp.Work;

import com.google.firebase.database.Exclude;

public class WorkId {
    @Exclude
    public String WorkId;

    public <W extends WorkId> W withId(@NonNull final String id){
        this.WorkId=id;
        return (W) this;
    }
}
