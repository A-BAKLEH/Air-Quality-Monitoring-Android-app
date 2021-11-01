package com.example.ceon390_projectgroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button monitoringButton;
    Button databaseButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monitoringButton= findViewById(R.id.monitorringBttn);
        databaseButton = findViewById(R.id.databaseBtn);

        monitoringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLiveMonitoring();
            }
        });
        databaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDatabase();
            }
        });
    }

    public void goToLiveMonitoring(){
        Intent intent = new Intent(this, LiveMonitoring.class);
        startActivity(intent);
    }

    public void goToDatabase() {
        Intent intent = new Intent(this, DatabaseActivity.class);
        startActivity(intent);
    }
}