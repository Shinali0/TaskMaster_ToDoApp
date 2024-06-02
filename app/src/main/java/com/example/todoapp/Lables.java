package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Lables extends AppCompatActivity {

    androidx.cardview.widget.CardView btnstudy, btnsports, btnwork, btnpersonal, btnhabit;
    private ImageButton backbtn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lables);

        btnstudy = findViewById(R.id.study);
        btnstudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lables.this, Study.class);
                startActivity(intent);
            }
        });

        btnsports = findViewById(R.id.sports);
        btnsports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lables.this, Sports.class);
                startActivity(intent);
            }
        });

        btnwork = findViewById(R.id.work);
        btnwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lables.this, Work.class);
                startActivity(intent);
            }
        });

        btnpersonal = findViewById(R.id.personal);
        btnpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lables.this, Personal.class);
                startActivity(intent);
            }
        });

        btnhabit = findViewById(R.id.habit);
        btnhabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lables.this, Habit.class);
                startActivity(intent);
            }
        });

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Lables.this, ToDoScreenAvtivity.class);
                startActivity(intent);
            }
        });
    }
}