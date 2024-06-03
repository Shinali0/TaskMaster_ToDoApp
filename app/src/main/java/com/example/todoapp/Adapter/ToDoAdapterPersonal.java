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

import com.example.todoapp.AddNewPersonal;
import com.example.todoapp.AddNewStudy;
import com.example.todoapp.AddNewTask;
import com.example.todoapp.AddProjectActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.ToDoModelPersonal;
import com.example.todoapp.Model.ToDoModelStudy;
import com.example.todoapp.Personal;
import com.example.todoapp.R;
import com.example.todoapp.Study;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapterPersonal extends RecyclerView.Adapter<ToDoAdapterPersonal.MyViewHolder> {

    private List<ToDoModelPersonal> todoList;
    private Personal activity;
    private FirebaseFirestore firestore;

    public ToDoAdapterPersonal(Personal personal, List<ToDoModelPersonal> todoList){
        this.todoList=todoList;
        activity=personal;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.each_study,parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new MyViewHolder(view);

    }

    public void deleteTask(int position){
        ToDoModelPersonal toDoModelPersonal=todoList.get(position);
        firestore.collection("personal").document(toDoModelPersonal.PersonalId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return  activity;
    }
    public void editTask(int position){
        ToDoModelPersonal toDoModelPersonal=todoList.get(position);

        Bundle bundle=new Bundle();
        bundle.putString("personal",toDoModelPersonal.getPersonal());
        bundle.putString("due",toDoModelPersonal.getDue());
        bundle.putString("id", toDoModelPersonal.PersonalId);

        AddNewPersonal addNewPersonal=new AddNewPersonal();
        addNewPersonal.setArguments(bundle);
        addNewPersonal.show(activity.getSupportFragmentManager(), addNewPersonal.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModelPersonal toDoModelPersonal=todoList.get(position);
        holder.mCheckBox.setText(toDoModelPersonal.getPersonal());
        holder.mDueDateTv.setText("Do Before " + toDoModelPersonal.getDue());

        holder.mCheckBox.setChecked(toBoolean(toDoModelPersonal.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("personal").document(toDoModelPersonal.PersonalId).update("status",1);
                }else{
                    firestore.collection("personal").document(toDoModelPersonal.PersonalId).update("status",0);
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
