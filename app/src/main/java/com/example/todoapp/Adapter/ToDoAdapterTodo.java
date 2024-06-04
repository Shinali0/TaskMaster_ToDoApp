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
import com.example.todoapp.AddNewTodo;
import com.example.todoapp.Model.ToDoModelStudy;
import com.example.todoapp.Model.ToDoModelTodo;
import com.example.todoapp.R;
import com.example.todoapp.Todo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapterTodo extends RecyclerView.Adapter<ToDoAdapterTodo.MyViewHolder> {

    private List<ToDoModelTodo> todoList;
    private Todo activity;
    private FirebaseFirestore firestore;

    public ToDoAdapterTodo(Todo todo, List<ToDoModelTodo> todoList){
        this.todoList=todoList;
        activity=todo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.each_study,parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new MyViewHolder(view);

    }

    public void deleteTask(int position){
        ToDoModelTodo toDoModelTodo=todoList.get(position);
        firestore.collection("todo").document(toDoModelTodo.TodoId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return  activity;
    }
    public void editTask(int position){
        ToDoModelTodo toDoModelTodo=todoList.get(position);

        Bundle bundle=new Bundle();
        bundle.putString("todo",toDoModelTodo.getTodo());
        bundle.putString("due",toDoModelTodo.getDue());
        bundle.putString("id", toDoModelTodo.TodoId);

        AddNewTodo addNewTodo=new AddNewTodo();
        addNewTodo.setArguments(bundle);
        addNewTodo.show(activity.getSupportFragmentManager(), addNewTodo.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModelTodo toDoModelTodo=todoList.get(position);
        holder.mCheckBox.setText(toDoModelTodo.getTodo());
        holder.mDueDateTv.setText("Do Before " + toDoModelTodo.getDue());

        holder.mCheckBox.setChecked(toBoolean(toDoModelTodo.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("todo").document(toDoModelTodo.TodoId).update("status",1);
                }else{
                    firestore.collection("todo").document(toDoModelTodo.TodoId).update("status",0);
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
