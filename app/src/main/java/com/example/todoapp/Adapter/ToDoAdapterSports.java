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

import com.example.todoapp.AddNewSports;
import com.example.todoapp.AddNewStudy;
import com.example.todoapp.AddNewTask;
import com.example.todoapp.AddProjectActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.ToDoModelSports;
import com.example.todoapp.Model.ToDoModelStudy;
import com.example.todoapp.R;
import com.example.todoapp.Sports;
import com.example.todoapp.Study;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapterSports extends RecyclerView.Adapter<ToDoAdapterSports.MyViewHolder> {

    private List<ToDoModelSports> todoList;
    private Sports activity;
    private FirebaseFirestore firestore;

    public ToDoAdapterSports(Sports sports, List<ToDoModelSports> todoList){
        this.todoList=todoList;
        activity=sports;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.each_study,parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new MyViewHolder(view);

    }

    public void deleteTask(int position){
        ToDoModelSports toDoModelSports=todoList.get(position);
        firestore.collection("sports").document(toDoModelSports.SportsId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return  activity;
    }
    public void editTask(int position){
        ToDoModelSports toDoModelSports=todoList.get(position);

        Bundle bundle=new Bundle();
        bundle.putString("sports",toDoModelSports.getSports());
        bundle.putString("due",toDoModelSports.getDue());
        bundle.putString("id", toDoModelSports.SportsId);

        AddNewSports addNewSports=new AddNewSports();
        addNewSports.setArguments(bundle);
        addNewSports.show(activity.getSupportFragmentManager(), addNewSports.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModelSports toDoModelSports=todoList.get(position);
        holder.mCheckBox.setText(toDoModelSports.getSports());
        holder.mDueDateTv.setText("Do Before " + toDoModelSports.getDue());

        holder.mCheckBox.setChecked(toBoolean(toDoModelSports.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("sports").document(toDoModelSports.SportsId).update("status",1);
                }else{
                    firestore.collection("sports").document(toDoModelSports.SportsId).update("status",0);
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
