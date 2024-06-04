package com.example.todoapp.Model;

import androidx.annotation.NonNull;
import com.example.todoapp.Done;

import com.google.firebase.database.Exclude;

public class DoneId {
    @Exclude
    public String DoneId;

    public <Di extends DoneId> Di withId(@NonNull final String id){
        this.DoneId=id;
        return (Di) this;
    }
}
