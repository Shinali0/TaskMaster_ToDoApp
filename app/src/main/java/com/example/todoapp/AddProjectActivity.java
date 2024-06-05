package com.example.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddProjectActivity extends AppCompatActivity implements OnDialogCloseListner{

    private RecyclerView recyclerView;
    private com.google.android.material.floatingactionbutton.FloatingActionButton addbutton;
    private FirebaseFirestore firestore;
    private ToDoAdapter adapter;
    private List<ToDoModel> mList;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Query query;
    private ListenerRegistration listenerRegistration;
    private ImageButton backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        recyclerView= findViewById(R.id.recyclerview);
        addbutton= findViewById(R.id.addbutton);
        firestore=FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddProjectActivity.this));

        addbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AddNewTask.newInstance().show(getSupportFragmentManager() , AddNewTask.TAG);

            }
        });

        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(AddProjectActivity.this,ToDoScreenAvtivity.class);
                startActivity(intent);
                finish();
            }
        });

        mList=new ArrayList<>();
        adapter=new ToDoAdapter(AddProjectActivity.this,mList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        showData();
        recyclerView.setAdapter(adapter);
    }

    private void showData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            query = firestore.collection("task")
                    .whereEqualTo("userId", userId)
                    .orderBy("time", Query.Direction.DESCENDING);

            listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        return;
                    }

                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            String id = documentChange.getDocument().getId();
                            ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);

                            mList.add(toDoModel);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    listenerRegistration.remove();
                }
            });
        }
    }


    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList.clear();
        showData();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddProjectActivity.this,ToDoScreenAvtivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}