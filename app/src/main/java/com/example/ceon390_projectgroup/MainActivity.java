package com.example.ceon390_projectgroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ekn.gruzer.gaugelibrary.MultiGauge;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView navBar;
    MultiGauge mainGauge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.home_nav);
        mainGauge = findViewById(R.id.multiGauge);

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.live_nav :
                        startActivity(new Intent(getApplicationContext(), LiveMonitoring.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings_nav:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);

                        return true;
                }

                return false;
            }
        });
    }
}