package com.example.ceon390_projectgroup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ekn.gruzer.gaugelibrary.MultiGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView navBar;
    MultiGauge mainGauge;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper db = new DatabaseHelper(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Range range1 = new Range();
        Range range2 = new Range();
        Range range3 = new Range();

        range1.setColor(Color.parseColor("#d91e18"));
        range1.setFrom(0);
        range1.setTo(5000);

        range2.setColor(Color.parseColor("#f89406"));
        range2.setFrom(0);
        range2.setTo(50);

        range3.setColor(Color.parseColor("#f5e51b"));
        range3.setFrom(0);
        range3.setTo(500);


        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.home_nav);
        mainGauge = findViewById(R.id.multiGauge);

        mainGauge.setMinValue(0);
        mainGauge.setMaxValue(5000);
        mainGauge.setSecondMinValue(0);
        mainGauge.setSecondMaxValue(50);
        mainGauge.setThirdMinValue(0);
        mainGauge.setThirdMaxValue(500);

        mainGauge.addRange(range1);
        mainGauge.addSecondRange(range2);
        mainGauge.addThirdRange(range3);

        sensorReturn("CO2 CCS811 Sensor", "MQ9 Sensor", "TVOC CCS811 Sensor");

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.live_nav:
                        startActivity(new Intent(getApplicationContext(), LiveMonitoring.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings_nav:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0, 0);

                        return true;
                }

                return false;
            }
        });
    }
    public void sensorReturn(String sensorName1, String sensorName2 , String sensorName3) {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value1 = snapshot.child(sensorName1).getValue().toString();
                String value2 = snapshot.child(sensorName2).getValue().toString();
                String value3 = snapshot.child(sensorName3).getValue().toString();

                mainGauge.setValue(Double.parseDouble(value1));
                mainGauge.setSecondValue(Double.parseDouble(value2));
                mainGauge.setThirdValue(Double.parseDouble(value3));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to get data. Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}