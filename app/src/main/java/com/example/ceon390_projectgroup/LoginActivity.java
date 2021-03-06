package com.example.ceon390_projectgroup;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

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

        loginButton.setOnClickListener(view -> loginUser());

        signUpButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),signUpActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.slide_in_right,R.animator.slide_out_left);
        });

        forgetPassText.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
        });
    }

    private void loginUser(){
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Email cannot be empty");
            emailEditText.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Password cannot be empty");
            passwordEditText.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"User Logged In",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this,"Login Error: " + Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                }

            });
        }
    }
}