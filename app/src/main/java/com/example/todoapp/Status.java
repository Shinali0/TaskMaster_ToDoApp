package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Status extends AppCompatActivity {

    androidx.cardview.widget.CardView btntodo, btndoing, btndone;
    private ImageButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        btntodo = findViewById(R.id.todo);
        btntodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Status.this, Todo.class);
                startActivity(intent);
            }
        });

        btndoing = findViewById(R.id.doing);
        btndoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Status.this, Doing.class);
                startActivity(intent);
            }
        });

        btndone = findViewById(R.id.done);
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Status.this, Done.class);
                startActivity(intent);
            }
        });

        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Status.this, ToDoScreenAvtivity.class);
                startActivity(intent);
            }
        });
    }
}