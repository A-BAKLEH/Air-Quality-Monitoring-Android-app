package com.example.ceon390_projectgroup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {

    Button resetButton;

    EditText emailEditText;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();

        resetButton = findViewById(R.id.resetButton);
        emailEditText = findViewById(R.id.resetPassEditText);

        resetButton.setOnClickListener(view -> resetPassword());


    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("An email is required");
            emailEditText.requestFocus();
        } else {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPasswordActivity.this, "Reset link sent to email", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Reset Error: "+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}