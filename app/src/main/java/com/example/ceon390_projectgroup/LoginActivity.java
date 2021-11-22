package com.example.ceon390_projectgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button signUpButton;
    Button loginButton;
    TextView forgetPassText;
    EditText emailEditText;
    EditText passwordEditText;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        signUpButton = findViewById(R.id.signUpButton);
        forgetPassText = findViewById(R.id.forgetPassTextView);
        loginButton = findViewById(R.id.loginButton);
        emailEditText = findViewById(R.id.emailLoginEditText);
        passwordEditText = findViewById(R.id.passwordLoginEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),signUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.slide_in_right,R.animator.slide_out_left);
            }
        });

        forgetPassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
            }
        });
    }

    private void loginUser(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Email cannot be empty");
            emailEditText.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Password cannot be empty");
            passwordEditText.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"User Login successfully",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this,"Login Error: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}