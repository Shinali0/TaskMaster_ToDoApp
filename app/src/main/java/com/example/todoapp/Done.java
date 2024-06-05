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

import com.example.todoapp.Adapter.ToDoAdapterDone;

import com.example.todoapp.Model.ToDoModelDone;

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
import java.util.List;

public class Done extends AppCompatActivity implements OnDialogCloseListner{

    private RecyclerView recyclerView;
    private com.google.android.material.floatingactionbutton.FloatingActionButton addbutton;
    private FirebaseFirestore firestore;
    private ToDoAdapterDone adapterx1;
    private Query query;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ListenerRegistration listenerRegistration;
    private List<ToDoModelDone> mList;
    private ImageButton backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        recyclerView=findViewById(R.id.recyclerview);
        addbutton=findViewById(R.id.addbutton);
        firestore=FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Done.this));

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewDone.newInstance().show(getSupportFragmentManager(),AddNewDone.TAG);

            }
        });

        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Done.this,Status.class);
                startActivity(intent);
            }
        });

        mList=new ArrayList<>();
        adapterx1= new ToDoAdapterDone(Done.this,mList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelperDone(adapterx1));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        showData();
        recyclerView.setAdapter(adapterx1);
    }

    private void showData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            query = firestore.collection("done")
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
                            ToDoModelDone toDoModelDone = documentChange.getDocument().toObject(ToDoModelDone.class).withId(id);

                            mList.add(toDoModelDone);
                            adapterx1.notifyDataSetChanged();


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
        adapterx1.notifyDataSetChanged();
    }


}