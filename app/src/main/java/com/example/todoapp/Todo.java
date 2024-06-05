package com.example.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.todoapp.Adapter.ToDoAdapterStudy;
import com.example.todoapp.Adapter.ToDoAdapterTodo;
import com.example.todoapp.Model.TaskId;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.ToDoModelStudy;
import com.example.todoapp.Model.ToDoModelTodo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Todo extends AppCompatActivity implements OnDialogCloseListner{

    private RecyclerView recyclerView;
    private com.google.android.material.floatingactionbutton.FloatingActionButton addbutton;
    private FirebaseFirestore firestore;
    private ToDoAdapterTodo adapterl1;
    private Query query;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ListenerRegistration listenerRegistration;
    private List<ToDoModelTodo> mList;
    private ImageButton backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        recyclerView=findViewById(R.id.recyclerview);
        addbutton=findViewById(R.id.addbutton);
        firestore=FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Todo.this));

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTodo.newInstance().show(getSupportFragmentManager(),AddNewTodo.TAG);

            }
        });

        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Todo.this,Status.class);
                startActivity(intent);
            }
        });

        mList=new ArrayList<>();
        adapterl1= new ToDoAdapterTodo(Todo.this,mList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperTodo(adapterl1));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        showData();
        recyclerView.setAdapter(adapterl1);
    }

    private void showData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            query = firestore.collection("todo")
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
                            ToDoModelTodo toDoModelTodo = documentChange.getDocument().toObject(ToDoModelTodo.class).withId(id);

                            mList.add(toDoModelTodo);
                            adapterl1.notifyDataSetChanged();


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
        adapterl1.notifyDataSetChanged();
    }
}