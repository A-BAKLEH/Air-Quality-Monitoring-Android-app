package com.example.ceon390_projectgroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    Button monitoringButton;
    Button databaseButton;
    BottomNavigationView navBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monitoringButton= findViewById(R.id.monitorringBttn);
        databaseButton = findViewById(R.id.databaseBtn);
        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.home_nav);

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.live_nav :
                        startActivity(new Intent(getApplicationContext(), LiveMonitoring.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings_nav:
                        startActivity(new Intent(getApplicationContext(), DatabaseActivity.class));
                        overridePendingTransition(0,0);

                        return true;
                }

                return false;
            }
        });

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