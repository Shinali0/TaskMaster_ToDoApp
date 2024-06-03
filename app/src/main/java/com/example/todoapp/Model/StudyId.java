package com.example.todoapp.Model;

import androidx.annotation.NonNull;

import com.example.todoapp.Study;
import com.google.firebase.database.Exclude;

public class StudyId {
    @Exclude
    public String StudyId;

    public <S extends StudyId> S withId(@NonNull final String id){
        this.StudyId=id;
        return (S) this;
    }
}
