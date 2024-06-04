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

import com.example.todoapp.AddNewDoing;
import com.example.todoapp.AddNewStudy;
import com.example.todoapp.AddNewTask;
import com.example.todoapp.AddProjectActivity;
import com.example.todoapp.Doing;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.ToDoModelDoing;
import com.example.todoapp.Model.ToDoModelStudy;
import com.example.todoapp.R;
import com.example.todoapp.Study;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapterDoing extends RecyclerView.Adapter<ToDoAdapterDoing.MyViewHolder> {

    private List<ToDoModelDoing> todoList;
    private Doing activity;
    private FirebaseFirestore firestore;

    public ToDoAdapterDoing(Doing doing, List<ToDoModelDoing> todoList){
        this.todoList=todoList;
        activity=doing;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.each_study,parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new MyViewHolder(view);

    }

    public void deleteTask(int position){
        ToDoModelDoing toDoModelDoing=todoList.get(position);
        firestore.collection("doing").document(toDoModelDoing.DoingId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return  activity;
    }
    public void editTask(int position){
        ToDoModelDoing toDoModelDoing=todoList.get(position);

        Bundle bundle=new Bundle();
        bundle.putString("doing",toDoModelDoing.getDoing());
        bundle.putString("due",toDoModelDoing.getDue());
        bundle.putString("id", toDoModelDoing.DoingId);

        AddNewDoing addNewDoing=new AddNewDoing();
        addNewDoing.setArguments(bundle);
        addNewDoing.show(activity.getSupportFragmentManager(), addNewDoing.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModelDoing toDoModelDoing=todoList.get(position);
        holder.mCheckBox.setText(toDoModelDoing.getDoing());
        holder.mDueDateTv.setText("Do Before " + toDoModelDoing.getDue());

        holder.mCheckBox.setChecked(toBoolean(toDoModelDoing.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("doing").document(toDoModelDoing.DoingId).update("status",1);
                }else{
                    firestore.collection("doing").document(toDoModelDoing.DoingId).update("status",0);
                }
            }
        });

    }

    private boolean toBoolean(int status){
        return status!=0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mDueDateTv;
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mDueDateTv=itemView.findViewById(R.id.due_date_tv);
            mCheckBox=itemView.findViewById(R.id.mcheckbox);
        }
    }
}
