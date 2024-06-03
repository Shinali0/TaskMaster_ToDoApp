package com.example.todoapp.Model;

import androidx.annotation.NonNull;

import com.example.todoapp.Habit;
import com.google.firebase.database.Exclude;

public class HabitId {
    @Exclude
    public String HabitId;

    public <H extends HabitId> H withId(@NonNull final String id){
        this.HabitId=id;
        return (H) this;
    }
}
