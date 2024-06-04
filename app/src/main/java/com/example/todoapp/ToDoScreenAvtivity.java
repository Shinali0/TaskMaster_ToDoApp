package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.todoapp.Adapter.ToDoScreenAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ToDoScreenAvtivity extends AppCompatActivity implements OnDialogCloseListner{



    private ImageButton addbtn, navbtn, labelnext, statusnext;
    private RecyclerView recyclerView;
    private Query query;
    private ListenerRegistration listenerRegistration;
    TextView username;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private ToDoScreenAdapter adapter7;
    private List<ToDoModel> mList;

    String userId;
    de.hdodenhof.circleimageview.CircleImageView userImage;
    Button btnstudy, btnsports, btnwork, btnpersonal, btnhabit, btntodo, btndoing, btndone;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_screen_avtivity);

        recyclerView= findViewById(R.id.recyclerview1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        username=findViewById(R.id.username);
        userImage = findViewById(R.id.profile);

        firebaseAuth=FirebaseAuth.getInstance();

        firebaseFirestore=FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userId=firebaseAuth.getCurrentUser().getUid();

        mList=new ArrayList<>();
        adapter7=new ToDoScreenAdapter(ToDoScreenAvtivity.this,mList);


        showData();
        recyclerView.setAdapter(adapter7);


        DocumentReference documentReference=firebaseFirestore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    username.setText(documentSnapshot.getString("userName"));
                } else {
                    Log.d("tag", "onEvent: Document do not exists.");
                }
            }
        });

        StorageReference profileRef=storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(userImage);

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

        labelnext=findViewById(R.id.labelnext);
        labelnext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ToDoScreenAvtivity.this,Lables.class);
                startActivity(intent);
            }
        });

        statusnext=findViewById(R.id.statusnext);
        statusnext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ToDoScreenAvtivity.this,Status.class);
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
                Intent intent = new Intent(ToDoScreenAvtivity.this, Todo.class);
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


    private void showData(){
        query=firebaseFirestore.collection("task").orderBy("time", Query.Direction.DESCENDING);

        listenerRegistration=query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange:value.getDocumentChanges()){
                    if(documentChange.getType()==DocumentChange.Type.ADDED){
                        String id=documentChange.getDocument().getId();
                        ToDoModel toDoModel=documentChange.getDocument().toObject(ToDoModel.class).withId(id);

                        mList.add(toDoModel);
                        adapter7.notifyDataSetChanged();


                    }
                }
                listenerRegistration.remove();
            }
        });
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        adapter7.notifyDataSetChanged();
    }
}