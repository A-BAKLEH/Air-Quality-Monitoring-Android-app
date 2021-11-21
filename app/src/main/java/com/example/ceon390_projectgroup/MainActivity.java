package com.example.ceon390_projectgroup;

import static com.example.ceon390_projectgroup.SettingsActivity.SENSOR;
import static com.example.ceon390_projectgroup.SettingsActivity.sensors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

    DatabaseReference databaseReference; //For Firebase
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper db = new DatabaseHelper(this);
        sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        databaseReference = FirebaseDatabase.getInstance().getReference();

        String sensors = " ";
        System.out.print(sensors + db.getAllValues());
        System.out.println();

        Range range1 = new Range();
        Range range2 = new Range();
        Range range3 = new Range();

        range1.setColor(Color.parseColor("#d91e18"));
        range1.setFrom(0);
        range1.setTo(5000);

        range2.setColor(Color.parseColor("#f89406"));
        range2.setFrom(0);
        range2.setTo(500);

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

        sensorReturn("MQ8 Sensor", "MQ2 Sensor", "MQ4 Sensor"); //Default values
        databaseInsert();



        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.live_nav:
                        startActivity(new Intent(getApplicationContext(), LiveMonitoring.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.database_nav:
                        startActivity(new Intent(getApplicationContext(), DatabaseActivity.class));
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

    protected void onResume(){
        super.onResume();
        sensorReturn(sharedPreferencesHelper.getOuterSpinnerGas(), sharedPreferencesHelper.getMiddleSpinnerGas(), sharedPreferencesHelper.getInnerSpinnerGas());
    }

    public void sensorReturn(String sensorName1, String sensorName2 , String sensorName3) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value1 = snapshot.child(sensorName1).getValue().toString(); //Outer Ring
                String value2 = snapshot.child(sensorName2).getValue().toString(); //Middle Ring
                String value3 = snapshot.child(sensorName3).getValue().toString(); //Inner Ring

                mainGauge.setValue(Double.parseDouble(value1)); //Set value outer ring
                mainGauge.setSecondValue(Double.parseDouble(value2)); //Set value middle ring
                mainGauge.setThirdValue(Double.parseDouble(value3)); //Set value inner ring
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to get data. Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void databaseInsert() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double value1 =  Double.parseDouble(String.valueOf(snapshot.child("CO2 CCS811 Sensor").getValue()));
                double value2 = Double.parseDouble(String.valueOf(snapshot.child("MQ135 Sensor").getValue()));
                double value3 = Double.parseDouble(String.valueOf(snapshot.child("MQ2 Sensor").getValue()));
                double value4 = Double.parseDouble(String.valueOf(snapshot.child("MQ4 Sensor").getValue()));
                double value5 = Double.parseDouble(String.valueOf(snapshot.child("MQ8 Sensor").getValue()));
                double value6 = Double.parseDouble(String.valueOf(snapshot.child("MQ9 Sensor").getValue()));
                double value7 = Double.parseDouble(String.valueOf(snapshot.child("TVOC CCS811 Sensor").getValue()));
                if(value1 != 0) { databaseHelper.insertSensors(new Sensors("CO2", snapshot.child("CO2 CCS811 Sensor").getValue().toString())); }
                if(value2 != 0) { databaseHelper.insertSensors(new Sensors("Ammonia", snapshot.child("MQ135 Sensor").getValue().toString())); }
                if(value3 != 0){ databaseHelper.insertSensors(new Sensors("Smoke",snapshot.child("MQ2 Sensor").getValue().toString())); }
                if(value4 != 0){ databaseHelper.insertSensors(new Sensors("Methane",snapshot.child("MQ4 Sensor").getValue().toString())); }
                if(value5 != 0){ databaseHelper.insertSensors(new Sensors("Hydrogen",snapshot.child("MQ8 Sensor").getValue().toString())); }
                if(value6 != 0){ databaseHelper.insertSensors(new Sensors("CO",snapshot.child("MQ9 Sensor").getValue().toString())); }
                if(value7 != 0){ databaseHelper.insertSensors(new Sensors("TVOC",snapshot.child("TVOC CCS811 Sensor").getValue().toString())); }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to get data. Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}