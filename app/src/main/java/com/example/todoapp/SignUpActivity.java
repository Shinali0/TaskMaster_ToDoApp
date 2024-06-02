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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    EditText editTextemail, editTextpassword;
    boolean passwordVisible;

    private Button login;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

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
        setContentView(R.layout.activity_sign_up);

        editTextemail=findViewById(R.id.email);
        editTextpassword=findViewById(R.id.editTextTextPassword3);

        mAuth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);

        TextView btn=findViewById(R.id.SignUpText);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
            }
        });

        TextView forgotpassword=findViewById(R.id.forgotPassword);
        forgotpassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(SignUpActivity.this,ForgotPassword.class));
            }
        });


        login=findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                progressBar.setVisibility(View.VISIBLE);
                String email, username, password;
                email=String.valueOf(editTextemail.getText());
                password=String.valueOf(editTextpassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUpActivity.this,"Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpActivity.this,"Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this,"Login Successful...",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(getApplicationContext(), ToDoScreenAvtivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignUpActivity.this,"Login Failed...",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //textView
        TextView mTextView=findViewById(R.id.SignUpText);
        //The text to set in TextView
        String mText="Don't have an account? SIGN UP";
        //Creating spannable string from normal string,we will use it to apply StyleSpan to substrings
        SpannableString mSpannableString=new SpannableString(mText);
        //styles to apply on substrings
        StyleSpan mBold=new StyleSpan(Typeface.BOLD);

        //applying styles to substrings
        mSpannableString.setSpan(mBold,23,29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //setting text to text view
        mTextView.setText(mSpannableString);




        editTextpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
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
