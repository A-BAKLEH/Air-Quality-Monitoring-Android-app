package com.example.ceon390_projectgroup;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class signUpActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button createAccountButton;
    EditText emailEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        createAccountButton = findViewById(R.id.createAccountButton);
        emailEditText = findViewById(R.id.emailSignUpEditText);
        passwordEditText = findViewById(R.id.passwordSignUpEditText);

        createAccountButton.setOnClickListener(view -> createUser());
    }

    private void createUser(){
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Email cannot be empty");
            emailEditText.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Password cannot be empty");
            passwordEditText.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(signUpActivity.this,"User registered successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
                } else {
                    Toast.makeText(signUpActivity.this,"Registration Error: " + Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}