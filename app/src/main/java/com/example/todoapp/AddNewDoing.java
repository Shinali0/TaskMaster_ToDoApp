package com.example.todoapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewDoing extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewDoing";

    private TextView setDueDate;
    private EditText mTaskEdit;
    private Button mSaveBtn;
    private FirebaseFirestore firestore;
    private Context context;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String dueDate = "";
    private String id="";
    private String dueDateUpdate="";

    public static AddNewDoing newInstance(){
        return new AddNewDoing();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_task_lables , container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDueDate=view.findViewById(R.id.set_due_tv);
        mTaskEdit=view.findViewById(R.id.task_edittext);
        mSaveBtn=view.findViewById(R.id.save_btn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firestore=FirebaseFirestore.getInstance();

        boolean isUpdate=false;

        final  Bundle bundle=getArguments();
        if (bundle!=null){
            isUpdate=true;
            String doing = bundle.getString("doing");
            id=bundle.getString("id");
            dueDateUpdate=bundle.getString("due");

            mTaskEdit.setText(doing);
            setDueDate.setText(dueDate);

            if (doing.length()>0){
                mSaveBtn.setEnabled(false);
                mSaveBtn.setBackgroundColor(Color.GRAY);
            }
        }

        mTaskEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    mSaveBtn.setEnabled(false);
                    mSaveBtn.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }else{
                    mSaveBtn.setEnabled(true);
                    mSaveBtn.setBackgroundColor(getResources().getColor(R.color.green_blue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();

                int MONTH= calendar.get(Calendar.MONTH);
                int YEAR= calendar.get(Calendar.YEAR);
                int DAY= calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month=month+1;
                        setDueDate.setText(dayOfMonth + "/" + month + "/" + year);
                        dueDate=dayOfMonth + "/" + month + "/" + year;

                    }
                }, YEAR , MONTH , DAY);

                datePickerDialog.show();
            }
        });

        boolean finalIsUpdate = isUpdate;
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String doing = mTaskEdit.getText().toString();

                if (finalIsUpdate) {
                    firestore.collection("doing").document(id).update("doing",doing,"due",dueDate);
                    Toast.makeText(context,"Task Updated",Toast.LENGTH_SHORT).show();
                }else {

                    if (doing.isEmpty()) {
                        Toast.makeText(context, "Empty Task not Allowed !!", Toast.LENGTH_SHORT).show();
                    } else {

                        if (user != null) {
                            String userId = user.getUid();

                            Map<String, Object> taskMap = new HashMap<>();

                            taskMap.put("doing", doing);
                            taskMap.put("due", dueDate);
                            taskMap.put("status", 0);
                            taskMap.put("time", FieldValue.serverTimestamp());
                            taskMap.put("userId", userId);


                            firestore.collection("doing").add(taskMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Doing Activity Saved", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                }
                dismiss();
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity=getActivity();
        if (activity instanceof OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}
