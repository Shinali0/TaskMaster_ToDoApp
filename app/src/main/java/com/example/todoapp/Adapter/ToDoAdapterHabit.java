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

import com.example.todoapp.AddNewHabit;
import com.example.todoapp.AddNewStudy;
import com.example.todoapp.AddNewTask;
import com.example.todoapp.AddProjectActivity;
import com.example.todoapp.Habit;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Model.ToDoModelHabit;
import com.example.todoapp.Model.ToDoModelStudy;
import com.example.todoapp.R;
import com.example.todoapp.Study;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapterHabit extends RecyclerView.Adapter<ToDoAdapterHabit.MyViewHolder> {

    private List<ToDoModelHabit> todoList;
    private Habit activity;
    private FirebaseFirestore firestore;

    public ToDoAdapterHabit(Habit habit, List<ToDoModelHabit> todoList){
        this.todoList=todoList;
        activity=habit;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.each_study,parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new MyViewHolder(view);

    }

    public void deleteTask(int position){
        ToDoModelHabit toDoModelHabit=todoList.get(position);
        firestore.collection("habit").document(toDoModelHabit.HabitId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return  activity;
    }
    public void editTask(int position){
        ToDoModelHabit toDoModelHabit=todoList.get(position);

        Bundle bundle=new Bundle();
        bundle.putString("habit",toDoModelHabit.getHabit());
        bundle.putString("due",toDoModelHabit.getDue());
        bundle.putString("id", toDoModelHabit.HabitId);

        AddNewHabit addNewHabit=new AddNewHabit();
        addNewHabit.setArguments(bundle);
        addNewHabit.show(activity.getSupportFragmentManager(), addNewHabit.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModelHabit toDoModelHabit=todoList.get(position);
        holder.mCheckBox.setText(toDoModelHabit.getHabit());
        holder.mDueDateTv.setText("Do Before " + toDoModelHabit.getDue());

        holder.mCheckBox.setChecked(toBoolean(toDoModelHabit.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("habit").document(toDoModelHabit.HabitId).update("status",1);
                }else{
                    firestore.collection("habit").document(toDoModelHabit.HabitId).update("status",0);
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
