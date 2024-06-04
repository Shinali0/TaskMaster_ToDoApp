package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewDone;
import com.example.todoapp.Done;

import com.example.todoapp.Model.ToDoModelDone;

import com.example.todoapp.R;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapterDone extends RecyclerView.Adapter<ToDoAdapterDone.MyViewHolder> {

    private List<ToDoModelDone> todoList;
    private Done activity;
    private FirebaseFirestore firestore;

    public ToDoAdapterDone(Done done, List<ToDoModelDone> todoList){
        this.todoList=todoList;
        activity=done;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.each_task_done,parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new ToDoAdapterDone.MyViewHolder(view);

    }

    public void deleteTask(int position){
        ToDoModelDone toDoModelDone=todoList.get(position);
        firestore.collection("done").document(toDoModelDone.DoneId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return  activity;
    }
    public void editTask(int position){
        ToDoModelDone toDoModelDone=todoList.get(position);

        Bundle bundle=new Bundle();
        bundle.putString("done",toDoModelDone.getDone());
        bundle.putString("due",toDoModelDone.getDue());
        bundle.putString("id", toDoModelDone.DoneId);

        AddNewDone addNewDone=new AddNewDone();
        addNewDone.setArguments(bundle);
        addNewDone.show(activity.getSupportFragmentManager(), addNewDone.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModelDone toDoModelDone=todoList.get(position);
        holder.mTaskDisplay.setText(toDoModelDone.getDone());
        holder.mDueDateTv.setText("Done on " + toDoModelDone.getDue());

//        holder.mCheckBox.setChecked(toBoolean(toDoModelDone.getStatus()));
//        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    firestore.collection("done").document(toDoModelDone.DoneId).update("status",1);
//                }else{
//                    firestore.collection("done").document(toDoModelDone.DoneId).update("status",0);
//                }
//            }
//        });

    }
//    private boolean toBoolean(int status){
//        return status!=0;
//    }


    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDueDateTv;
        TextView mTaskDisplay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mDueDateTv=itemView.findViewById(R.id.due_date_tv);
            mTaskDisplay=itemView.findViewById(R.id.taskdisplay);
        }
    }
}
