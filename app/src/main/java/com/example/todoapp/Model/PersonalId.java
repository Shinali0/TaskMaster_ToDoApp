package com.example.todoapp.Model;

import androidx.annotation.NonNull;

import com.example.todoapp.Personal;
import com.google.firebase.database.Exclude;

public class PersonalId {
    @Exclude
    public String PersonalId;

    public <P extends PersonalId> P withId(@NonNull final String id){
        this.PersonalId=id;
        return (P) this;
    }
}
