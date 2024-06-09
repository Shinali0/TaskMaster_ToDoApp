package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

//hjfw
    public static final String TAG = "TAG";
    EditText editTextemail, editTextusername, editTextpassword;
    boolean passwordVisible;
    private Button signup;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(getApplicationContext(), ToDoScreenAvtivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextemail=findViewById(R.id.email);
        editTextusername=findViewById(R.id.username);
        editTextpassword=findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressBar);

        TextView btn=findViewById(R.id.loginText);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });

        signup=findViewById(R.id.btn_signup);
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                progressBar.setVisibility(View.VISIBLE);
                String email,password, username;
                email=String.valueOf(editTextemail.getText());
                username=String.valueOf(editTextusername.getText());
                password=String.valueOf(editTextpassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this,"Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }



                if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this,"Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()<8){
                    Toast.makeText(MainActivity.this,"Password must be at least8 characters...",Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Signup Successful...", Toast.LENGTH_SHORT).show();
                                    userID=mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference=fStore.collection("users").document(userID);
                                    Map<String,Object> user=new HashMap<>();
                                    user.put("userName",username);
                                    user.put("email",email);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "onSuccess: user Profile is created for "+userID);
                                        }
                                    });
                                    Intent intent=new Intent(getApplicationContext(), ToDoScreenAvtivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, "Signup failed...",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        //textView
        TextView mTextView=findViewById(R.id.loginText);
        //The text to set in TextView
        String mText="Already have an account? LOGIN";
        //Creating spannable string from normal string,we will use it to apply StyleSpan to substrings
        SpannableString mSpannableString=new SpannableString(mText);
        //styles to apply on substrings
        StyleSpan mBold=new StyleSpan(Typeface.BOLD);

        //applying styles to substrings
        mSpannableString.setSpan(mBold,25,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //setting text to text view
        mTextView.setText(mSpannableString);


        editTextpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {

                        if(event.getRawX()>=editTextpassword.getRight()-editTextpassword.getCompoundDrawables()[Right].getBounds().width()){
                            int selection=editTextpassword.getSelectionEnd();
                            if(passwordVisible){
                                editTextpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24,  0);
                                editTextpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                passwordVisible=false;
                            }else{
                                editTextpassword.setCompoundDrawablesRelativeWithIntrinsicBounds( 0, 0, R.drawable.baseline_visibility_24, 0);
                                editTextpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                passwordVisible=true;
                            }
                            editTextpassword.setSelection(selection);
                            return true;
                        }


                }
                return false;
            }
        });

    }
}