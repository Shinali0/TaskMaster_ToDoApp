package com.example.todoapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.ToDoScreenAvtivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoScreenAdapter extends RecyclerView.Adapter<ToDoScreenAdapter.MyViewHolder>{

    private List<ToDoModel> todoList;
    private ToDoScreenAvtivity activity7;
    private FirebaseFirestore firestore;

    public ToDoScreenAdapter(ToDoScreenAvtivity toDoScreenAvtivity, List<ToDoModel> todoList){
        this.todoList=todoList;
        activity7=toDoScreenAvtivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity7).inflate(R.layout.each_task_todo,parent,false);
        firestore=FirebaseFirestore.getInstance();
        return new ToDoScreenAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel toDoModel=todoList.get(position);
        holder.taskdisplay.setText(toDoModel.getTask());
        holder.mDueDateTv.setText("Due On " + toDoModel.getDue());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mDueDateTv;
        TextView taskdisplay;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mDueDateTv=itemView.findViewById(R.id.due_date_tv);
            taskdisplay=itemView.findViewById(R.id.taskdisplay);
        }
    }
}
