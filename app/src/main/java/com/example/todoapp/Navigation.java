package com.example.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class Navigation extends AppCompatActivity {


    private androidx.appcompat.widget.AppCompatButton home, projects, userprofile, lables, status, devinfo;
    private ImageButton backbtn1;
    TextView username, email;
    String rtvusername, rtvemail, loggedemail;
    FirebaseAuth auth;
    private Button logout;
    private FirebaseFirestore fStore;
    String userId;
    private ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        logout = findViewById(R.id.logoutbtn);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = auth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        registration = documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    username.setText(documentSnapshot.getString("userName"));
                    email.setText(documentSnapshot.getString("email"));
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the listener before logging out
                if (registration != null) {
                    registration.remove();
                }

                auth.signOut();
                Toast.makeText(Navigation.this, "Logout Successful...", Toast.LENGTH_SHORT).show();

                // Adding a slight delay before starting the new activity
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Navigation.this, SignUpActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1000); // 1-second delay
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Navigation.this, ToDoScreenAvtivity.class);
                startActivity(intent);
            }
        });

        backbtn1 = findViewById(R.id.backbtn1);
        backbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Navigation.this, ToDoScreenAvtivity.class);
                startActivity(intent);
            }
        });

        projects = findViewById(R.id.projects);
        projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Navigation.this, AddProjectActivity.class);
                startActivity(intent);
            }
        });

        userprofile= findViewById(R.id.userprofile);
        userprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Navigation.this, UserProfile.class);
                startActivity(intent);
            }
        });

        lables = findViewById(R.id.labels);
        lables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Navigation.this, Lables.class);
                startActivity(intent);
            }
        });

        status = findViewById(R.id.status);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Navigation.this, Status.class);
                startActivity(intent);
            }
        });

        devinfo = findViewById(R.id.devdetails);
        devinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Navigation.this, DevInfo.class);
                startActivity(intent);
            }
        });
    }
}