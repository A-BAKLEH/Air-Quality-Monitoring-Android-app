package com.example.ceon390_projectgroup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ekn.gruzer.gaugelibrary.MultiGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView navBar;
    MultiGauge mainGauge;

    DatabaseReference databaseReference; //For Firebase
    SharedPreferencesHelper sharedPreferencesHelper;
    FirebaseAuth mAuth;

    TextView tvOuterGauge, tvMiddleGauge, tvInnerGauge, locationName, roomName;
    LinearLayout linearOuter, linearMiddle, linearInner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        sensorReturn(sharedPreferencesHelper.getOuterSpinnerGas(), sharedPreferencesHelper.getMiddleSpinnerGas(), sharedPreferencesHelper.getInnerSpinnerGas()); //Default values
        databaseInsert();
        initNavBar();
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }


    protected void onResume(){
        super.onResume();
        sensorReturn(sharedPreferencesHelper.getOuterSpinnerGas(), sharedPreferencesHelper.getMiddleSpinnerGas(), sharedPreferencesHelper.getInnerSpinnerGas());
    }

    public void sensorReturn(String sensorName1, String sensorName2 , String sensorName3) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value1 = Objects.requireNonNull(snapshot.child(sensorName1).getValue()).toString(); //Outer Ring
                String value2 = Objects.requireNonNull(snapshot.child(sensorName2).getValue()).toString(); //Middle Ring
                String value3 = Objects.requireNonNull(snapshot.child(sensorName3).getValue()).toString(); //Inner Ring

                mainGauge.setValue(Double.parseDouble(value1)); //Set value outer ring
                mainGauge.setSecondValue(Double.parseDouble(value2)); //Set value middle ring
                mainGauge.setThirdValue(Double.parseDouble(value3)); //Set value inner ring

                tvOuterGauge.setText(sensorName1 + ": " + value1 + " ppm");
                tvMiddleGauge.setText(sensorName2 + ": " + value2 + " ppm");
                tvInnerGauge.setText(sensorName3 + ": " + value3 + " ppm");

                linearOuter.setVisibility(View.VISIBLE);
                linearMiddle.setVisibility(View.VISIBLE);
                linearInner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to get data. Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setupUI(){
        mAuth = FirebaseAuth.getInstance();

        sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        databaseReference = FirebaseDatabase.getInstance().getReference();

        locationName = findViewById(R.id.locationName);
        roomName = findViewById(R.id.roomName);

        mainGauge = findViewById(R.id.multiGauge);
        tvOuterGauge = findViewById(R.id.tv_outer_gauge);
        tvMiddleGauge = findViewById(R.id.tv_middle_gauge);
        tvInnerGauge = findViewById(R.id.tv_inner_gauge);

        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.home_nav);

        linearOuter = findViewById(R.id.linear_outer);
        linearMiddle = findViewById(R.id.linear_middle);
        linearInner = findViewById(R.id.linear_inner);

        locationName.setText(sharedPreferencesHelper.getLocation());
        roomName.setText(sharedPreferencesHelper.getRoom());
        mainGauge();
    }

    public void mainGauge(){
        setOuterRing();
        setMiddleRing();
        setInnerRing();
    }

    public void setOuterRing(){
        Range outer = new Range();
        outer.setColor(Color.parseColor("#98a5e3"));
        switch (sharedPreferencesHelper.getOuterSpinnerGas()) {
            case "Alcohol":
            case "Liquefied Petroleum Gas":
            case "Methane":
                mainGauge.setMinValue(0);
                mainGauge.setMaxValue(10000);
                mainGauge.addRange(outer);
                break;
            case "Ammonia":
                mainGauge.setMinValue(0);
                mainGauge.setMaxValue(300);
                mainGauge.addRange(outer);
                break;
            case "Carbon Dioxide":
                mainGauge.setMinValue(400);
                mainGauge.setMaxValue(29206);
                mainGauge.addRange(outer);
                break;
            case "Carbon Monoxide":
                mainGauge.setMinValue(0);
                mainGauge.setMaxValue(1000);
                mainGauge.addRange(outer);
                break;
            case "Total Volatile Organic Compound":
                mainGauge.setMinValue(0);
                mainGauge.setMaxValue(32768);
                mainGauge.addRange(outer);
                break;
        }
    }

    public void setMiddleRing(){
        Range middle = new Range();
        middle.setColor(Color.parseColor("#8a7fb5"));
        switch (sharedPreferencesHelper.getMiddleSpinnerGas()) {
            case "Alcohol":
            case "Liquefied Petroleum Gas":
            case "Methane":
                mainGauge.setSecondMinValue(0);
                mainGauge.setSecondMaxValue(10000);
                mainGauge.addSecondRange(middle);
                break;
            case "Ammonia":
                mainGauge.setSecondMinValue(0);
                mainGauge.setSecondMaxValue(300);
                mainGauge.addSecondRange(middle);
                break;
            case "Carbon Dioxide":
                mainGauge.setSecondMinValue(400);
                mainGauge.setSecondMaxValue(29206);
                mainGauge.addSecondRange(middle);
                break;
            case "Carbon Monoxide":
                mainGauge.setSecondMinValue(0);
                mainGauge.setSecondMaxValue(1000);
                mainGauge.addSecondRange(middle);
                break;
            case "Total Volatile Organic Compound":
                mainGauge.setSecondMinValue(0);
                mainGauge.setSecondMaxValue(32768);
                mainGauge.addSecondRange(middle);
                break;
        }
    }

    public void setInnerRing(){
        Range inner = new Range();
        inner.setColor(Color.parseColor("#d18ca0"));
        switch (sharedPreferencesHelper.getMiddleSpinnerGas()) {
            case "Alcohol":
            case "Liquefied Petroleum Gas":
            case "Methane":
                mainGauge.setThirdMinValue(0);
                mainGauge.setThirdMaxValue(10000);
                mainGauge.addThirdRange(inner);
                break;
            case "Ammonia":
                mainGauge.setThirdMinValue(0);
                mainGauge.setThirdMaxValue(300);
                mainGauge.addRange(inner);
                break;
            case "Carbon Dioxide":
                mainGauge.setThirdMinValue(400);
                mainGauge.setThirdMaxValue(29206);
                mainGauge.addRange(inner);
                break;
            case "Carbon Monoxide":
                mainGauge.setThirdMinValue(0);
                mainGauge.setThirdMaxValue(1000);
                mainGauge.addRange(inner);
                break;
            case "Total Volatile Organic Compound":
                mainGauge.setThirdMinValue(0);
                mainGauge.setThirdMaxValue(32768);
                mainGauge.addRange(inner);
                break;
        }
    }

    public void initNavBar(){
        navBar.setOnItemSelectedListener(item -> {
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
        });
    }
    public void databaseInsert() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double value1 =  Double.parseDouble(String.valueOf(snapshot.child("Carbon Dioxide").getValue()));
                double value2 = Double.parseDouble(String.valueOf(snapshot.child("Ammonia").getValue()));
                double value3 = Double.parseDouble(String.valueOf(snapshot.child("Liquefied Petroleum Gas").getValue()));
                double value4 = Double.parseDouble(String.valueOf(snapshot.child("Methane").getValue()));
                double value5 = Double.parseDouble(String.valueOf(snapshot.child("Alcohol").getValue()));
                double value6 = Double.parseDouble(String.valueOf(snapshot.child("Carbon Monoxide").getValue()));
                double value7 = Double.parseDouble(String.valueOf(snapshot.child("Total Volatile Organic Compound").getValue()));
                if(value1 != 0) { databaseHelper.insertSensors(new Sensors("Carbon Dioxide", Objects.requireNonNull(snapshot.child("Carbon Dioxide").getValue()).toString())); }
                if(value2 != 0) { databaseHelper.insertSensors(new Sensors("Ammonia", Objects.requireNonNull(snapshot.child("Ammonia").getValue()).toString())); }
                if(value3 != 0){ databaseHelper.insertSensors(new Sensors("LPG", Objects.requireNonNull(snapshot.child("Liquefied Petroleum Gas").getValue()).toString())); }
                if(value4 != 0){ databaseHelper.insertSensors(new Sensors("Methane", Objects.requireNonNull(snapshot.child("Methane").getValue()).toString())); }
                if(value5 != 0){ databaseHelper.insertSensors(new Sensors("Alcohol", Objects.requireNonNull(snapshot.child("Alcohol").getValue()).toString())); }
                if(value6 != 0){ databaseHelper.insertSensors(new Sensors("Carbon Monoxide", Objects.requireNonNull(snapshot.child("Carbon Monoxide").getValue()).toString())); }
                if(value7 != 0){ databaseHelper.insertSensors(new Sensors("TVOC", Objects.requireNonNull(snapshot.child("Total Volatile Organic Compound").getValue()).toString())); }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to get data. Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}