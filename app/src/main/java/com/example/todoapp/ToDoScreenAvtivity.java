package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class ToDoScreenAvtivity extends AppCompatActivity {

    private ImageButton addbtn, navbtn;
    TextView username;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userId;
    de.hdodenhof.circleimageview.CircleImageView userImage;
    Button btnstudy, btnsports, btnwork, btnpersonal, btnhabit, btntodo, btndoing, btndone;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_screen_avtivity);
        username=findViewById(R.id.username);
        userImage = findViewById(R.id.profile);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userId=firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference=firebaseFirestore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                username.setText(documentSnapshot.getString("userName"));
            }
        });

        StorageReference profileRef = storageReference.child("users/" + userId + "profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(userImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        addbtn=findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ToDoScreenAvtivity.this,AddProjectActivity.class);
                startActivity(intent);
            }
        });

        navbtn=findViewById(R.id.imageButton);
        navbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ToDoScreenAvtivity.this,Navigation.class);
                startActivity(intent);
            }
        });

        btnstudy = findViewById(R.id.study);
        btnstudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToDoScreenAvtivity.this, Study.class);
                startActivity(intent);
            }
        });

        btnsports = findViewById(R.id.sports);
        btnsports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToDoScreenAvtivity.this, Sports.class);
                startActivity(intent);
            }
        });

        btnwork = findViewById(R.id.work);
        btnwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToDoScreenAvtivity.this, Work.class);
                startActivity(intent);
            }
        });

        btnpersonal = findViewById(R.id.personal);
        btnpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToDoScreenAvtivity.this, Personal.class);
                startActivity(intent);
            }
        });

        btnhabit = findViewById(R.id.habit);
        btnhabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToDoScreenAvtivity.this, Habit.class);
                startActivity(intent);
            }
        });

        btntodo = findViewById(R.id.todo);
        btntodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToDoScreenAvtivity.this, ToDo.class);
                startActivity(intent);
            }
        });

        btndoing = findViewById(R.id.doing);
        btndoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToDoScreenAvtivity.this, Doing.class);
                startActivity(intent);
            }
        });

        btndone = findViewById(R.id.done);
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ToDoScreenAvtivity.this, Done.class);
                startActivity(intent);
            }
        });

    }
}