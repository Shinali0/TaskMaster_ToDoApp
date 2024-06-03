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

import com.example.todoapp.AddNewStudy;
import com.example.todoapp.AddNewTask;
import com.example.todoapp.AddProjectActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.ToDoModelStudy;
import com.example.todoapp.R;
import com.example.todoapp.Study;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapterStudy extends RecyclerView.Adapter<ToDoAdapterStudy.MyViewHolder> {

    private List<ToDoModelStudy> todoList;
    private Study activity;
    private FirebaseFirestore firestore;

    public ToDoAdapterStudy(Study study, List<ToDoModelStudy> todoList){
        this.todoList=todoList;
        activity=study;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.each_study,parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new MyViewHolder(view);

    }

    public void deleteTask(int position){
        ToDoModelStudy toDoModelStudy=todoList.get(position);
        firestore.collection("study").document(toDoModelStudy.StudyId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return  activity;
    }
    public void editTask(int position){
        ToDoModelStudy toDoModelStudy=todoList.get(position);

        Bundle bundle=new Bundle();
        bundle.putString("study",toDoModelStudy.getStudy());
        bundle.putString("due",toDoModelStudy.getDue());
        bundle.putString("id", toDoModelStudy.StudyId);

        AddNewStudy addNewStudy=new AddNewStudy();
        addNewStudy.setArguments(bundle);
        addNewStudy.show(activity.getSupportFragmentManager(), addNewStudy.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModelStudy toDoModelStudy=todoList.get(position);
        holder.mCheckBox.setText(toDoModelStudy.getStudy());
        holder.mDueDateTv.setText("Do Before " + toDoModelStudy.getDue());

        holder.mCheckBox.setChecked(toBoolean(toDoModelStudy.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("study").document(toDoModelStudy.StudyId).update("status",1);
                }else{
                    firestore.collection("study").document(toDoModelStudy.StudyId).update("status",0);
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
