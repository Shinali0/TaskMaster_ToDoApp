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

import com.example.todoapp.Adapter.ToDoAdapterSports;
import com.example.todoapp.Adapter.ToDoAdapterStudy;
import com.example.todoapp.Model.TaskId;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.ToDoModelSports;
import com.example.todoapp.Model.ToDoModelStudy;
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

public class Sports extends AppCompatActivity implements OnDialogCloseListner{

    private RecyclerView recyclerView;
    private com.google.android.material.floatingactionbutton.FloatingActionButton addbutton;
    private FirebaseFirestore firestore;
    private ToDoAdapterSports adapter2;
    private Query query;
    private ListenerRegistration listenerRegistration;
    private List<ToDoModelSports> mList;
    private ImageButton backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);

        recyclerView=findViewById(R.id.recyclerview);
        addbutton=findViewById(R.id.addbutton);
        firestore=FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Sports.this));

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewSports.newInstance().show(getSupportFragmentManager(),AddNewSports.TAG);

            }
        });

        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Sports.this,Lables.class);
                startActivity(intent);
            }
        });

        mList=new ArrayList<>();
        adapter2= new ToDoAdapterSports(Sports.this,mList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperSports(adapter2));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        showData();
        recyclerView.setAdapter(adapter2);
    }

    private void showData(){
        query=firestore.collection("sports").orderBy("time", Query.Direction.DESCENDING);

        listenerRegistration=query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange:value.getDocumentChanges()){
                    if(documentChange.getType()==DocumentChange.Type.ADDED){
                        String id=documentChange.getDocument().getId();
                        ToDoModelSports toDoModelSports=documentChange.getDocument().toObject(ToDoModelSports.class).withId(id);

                        mList.add(toDoModelSports);
                        adapter2.notifyDataSetChanged();


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
        adapter2.notifyDataSetChanged();
    }
}