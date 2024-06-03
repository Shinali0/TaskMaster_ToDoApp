package com.example.todoapp.Model;

import androidx.annotation.NonNull;

import com.example.todoapp.Sports;
import com.google.firebase.database.Exclude;
public class SportsId {
   @Exclude
    public String SportsId;

   public <Sp extends SportsId> Sp withId(@NonNull final String id){
        this.SportsId=id;
        return (Sp) this;
    }
}
