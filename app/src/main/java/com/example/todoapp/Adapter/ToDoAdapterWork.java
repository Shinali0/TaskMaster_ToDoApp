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
import com.example.todoapp.AddNewWork;
import com.example.todoapp.AddProjectActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.ToDoModelStudy;
import com.example.todoapp.Model.ToDoModelWork;
import com.example.todoapp.R;
import com.example.todoapp.Study;
import com.example.todoapp.Work;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapterWork extends RecyclerView.Adapter<ToDoAdapterWork.MyViewHolder> {

    private List<ToDoModelWork> todoList;
    private Work activity;
    private FirebaseFirestore firestore;

    public ToDoAdapterWork(Work work, List<ToDoModelWork> todoList){
        this.todoList=todoList;
        activity=work;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.each_study,parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new MyViewHolder(view);

    }

    public void deleteTask(int position){
        ToDoModelWork toDoModelWork=todoList.get(position);
        firestore.collection("work").document(toDoModelWork.WorkId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return  activity;
    }
    public void editTask(int position){
        ToDoModelWork toDoModelWork=todoList.get(position);

        Bundle bundle=new Bundle();
        bundle.putString("work",toDoModelWork.getWork());
        bundle.putString("due",toDoModelWork.getDue());
        bundle.putString("id", toDoModelWork.WorkId);

        AddNewWork addNewWork=new AddNewWork();
        addNewWork.setArguments(bundle);
        addNewWork.show(activity.getSupportFragmentManager(), addNewWork.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModelWork toDoModelWork=todoList.get(position);
        holder.mCheckBox.setText(toDoModelWork.getWork());
        holder.mDueDateTv.setText("Do Before " + toDoModelWork.getDue());

        holder.mCheckBox.setChecked(toBoolean(toDoModelWork.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("work").document(toDoModelWork.WorkId).update("status",1);
                }else{
                    firestore.collection("work").document(toDoModelWork.WorkId).update("status",0);
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
