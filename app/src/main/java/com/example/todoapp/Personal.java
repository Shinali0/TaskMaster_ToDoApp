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

import com.example.todoapp.Adapter.ToDoAdapterPersonal;
import com.example.todoapp.Model.ToDoModelPersonal;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Personal extends AppCompatActivity implements OnDialogCloseListner{

    private RecyclerView recyclerView;
    private com.google.android.material.floatingactionbutton.FloatingActionButton addbutton;
    private FirebaseFirestore firestore;
    private ToDoAdapterPersonal adapter4;
    private Query query;
    private ListenerRegistration listenerRegistration;
    private List<ToDoModelPersonal> mList;
    private ImageButton backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        recyclerView=findViewById(R.id.recyclerview);
        addbutton=findViewById(R.id.addbutton);
        firestore=FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Personal.this));

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewPersonal.newInstance().show(getSupportFragmentManager(),AddNewPersonal.TAG);

            }
        });

        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Personal.this,Lables.class);
                startActivity(intent);
            }
        });

        mList=new ArrayList<>();
        adapter4= new ToDoAdapterPersonal(Personal.this,mList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperPersonal(adapter4));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        showData();
        recyclerView.setAdapter(adapter4);
    }

    private void showData(){
        query=firestore.collection("personal").orderBy("time", Query.Direction.DESCENDING);

        listenerRegistration=query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange:value.getDocumentChanges()){
                    if(documentChange.getType()==DocumentChange.Type.ADDED){
                        String id=documentChange.getDocument().getId();
                        ToDoModelPersonal toDoModelPersonal=documentChange.getDocument().toObject(ToDoModelPersonal.class).withId(id);

                        mList.add(toDoModelPersonal);
                        adapter4.notifyDataSetChanged();


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
        adapter4.notifyDataSetChanged();
    }
}