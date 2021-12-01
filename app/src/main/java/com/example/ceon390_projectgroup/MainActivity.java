package com.example.ceon390_projectgroup;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ekn.gruzer.gaugelibrary.MultiGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView navBar;
    MultiGauge mainGauge;
    NotificationManagerCompat notificationManager;

    DatabaseReference databaseReference; //For Firebase
    SharedPreferencesHelper sharedPreferencesHelper;
    FirebaseAuth mAuth;

    TextView tvOuterGauge, tvMiddleGauge, tvInnerGauge, locationName, roomName;
    LinearLayout linearOuter, linearMiddle, linearInner;
    String alcohol, ammonia, carbon_dioxide, carbon_monoxide, liquefied_petroleum_gas, methane, total_volatile_organic_compound;
    int al, am, cd, cm, lpg, me, tvoc; //Used for the read function to store gas value for notification


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        sensorReturn(sharedPreferencesHelper.getOuterSpinnerGas(), sharedPreferencesHelper.getMiddleSpinnerGas(), sharedPreferencesHelper.getInnerSpinnerGas()); //Default values
        databaseInsert();
        initNavBar();
        if(!sharedPreferencesHelper.getLocation().equals("")) {
            notification();
        }
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } else {
            FirebaseUserMetadata metadata = mAuth.getCurrentUser().getMetadata();
            if(sharedPreferencesHelper.getLocation().equals("")){
                if(Objects.requireNonNull(metadata).getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                }
            }
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
                String s1 = sensorName1 + ": " + value1 + " ppm";
                String s2 = sensorName2 + ": " + value2 + " ppm";
                String s3 = sensorName3 + ": " + value3 + " ppm";
                tvOuterGauge.setText(s1);
                tvMiddleGauge.setText(s2);
                tvInnerGauge.setText(s3);

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

    public void notification(){
        Intent intent = new Intent(getApplicationContext(), LiveMonitoring.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder warning = new NotificationCompat.Builder(getApplicationContext(), App.CHANNEL_ID)
                .setContentTitle("WARNING")
                .setSmallIcon(R.drawable.logo_amq)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationCompat.Builder danger = new NotificationCompat.Builder(getApplicationContext(), App.CHANNEL_ID)
                .setContentTitle("DANGER")
                .setSmallIcon(R.drawable.logo_amq)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        notificationManager = NotificationManagerCompat.from(getApplicationContext());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carbon_dioxide = Objects.requireNonNull(dataSnapshot.child("Carbon Dioxide").getValue()).toString();
                cd = Integer.parseInt(carbon_dioxide);
                if(cd >= 1000 && cd <= 3000){
                    notificationManager.notify(1, warning.setContentText("Carbon Dioxide levels are rising and can affect human health.").build());
                }else if(cd > 3000){
                    notificationManager.notify(2, danger.setContentText("Carbon Dioxide levels have reached a toxic limit.").build());
                }
                ammonia = Objects.requireNonNull(dataSnapshot.child("Ammonia").getValue()).toString();
                am = Integer.parseInt(ammonia);
                if(am >= 50 && am <= 200){
                    notificationManager.notify(3, warning.setContentText("Ammonia levels are rising and can affect human health.").build());
                }else if(am > 200){
                    notificationManager.notify(4, danger.setContentText("Ammonia levels have reached a toxic limit.").build());
                }
                liquefied_petroleum_gas = Objects.requireNonNull(dataSnapshot.child("Liquefied Petroleum Gas").getValue()).toString();
                lpg = Integer.parseInt(liquefied_petroleum_gas);
                if(lpg >= 1000 && lpg <= 3000){
                    notificationManager.notify(5, warning.setContentText("LPG levels are rising and can affect human health.").build());
                }else if(lpg > 3000){
                    notificationManager.notify(6, danger.setContentText("LPG levels have reached a toxic limit.").build());
                }
                methane = Objects.requireNonNull(dataSnapshot.child("Methane").getValue()).toString();
                me = Integer.parseInt(methane);
                if(lpg >= 1000 && lpg <= 3000){
                    notificationManager.notify(7, warning.setContentText("Methane levels are rising and can affect human health.").build());
                }else if(lpg > 3000){
                    notificationManager.notify(8, danger.setContentText("Methane levels have reached a toxic limit.").build());
                }
                alcohol = Objects.requireNonNull(dataSnapshot.child("Alcohol").getValue()).toString();
                al = Integer.parseInt(alcohol);
                if(al >= 1000 && al <= 3000){
                    notificationManager.notify(9, warning.setContentText("Alcohol levels are rising and can affect human health.").build());
                }else if(al > 3000){
                    notificationManager.notify(10, danger.setContentText("Alcohol levels have reached a toxic limit.").build());
                }
                carbon_monoxide = Objects.requireNonNull(dataSnapshot.child("Carbon Monoxide").getValue()).toString();
                cm = Integer.parseInt(carbon_monoxide);
                if(cm >= 200 && cm <= 500){
                    notificationManager.notify(11, warning.setContentText("Carbon Monoxide levels are rising and can affect human health.").build());
                }else if(cm > 500){
                    notificationManager.notify(12, danger.setContentText("Carbon Monoxide levels have reached a toxic limit.").build());
                }
                total_volatile_organic_compound = Objects.requireNonNull(dataSnapshot.child("Total Volatile Organic Compound").getValue()).toString();
                tvoc = Integer.parseInt(total_volatile_organic_compound);
                if(tvoc >= 500 && tvoc <= 1000){
                    notificationManager.notify(13, warning.setContentText("TVOC levels are rising and can affect human health.").build());
                }else if(tvoc > 1000){
                    notificationManager.notify(14, danger.setContentText("TVOC levels have reached a toxic limit.").build());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
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
                mainGauge.setMaxValue(1000);
                mainGauge.addRange(outer);
                break;
            case "Carbon Monoxide":
                mainGauge.setMinValue(0);
                mainGauge.setMaxValue(100);
                mainGauge.addRange(outer);
                break;
            case "Ammonia":
                mainGauge.setMinValue(0);
                mainGauge.setMaxValue(300);
                mainGauge.addRange(outer);
                break;
            case "Carbon Dioxide":
                mainGauge.setMinValue(0);
                mainGauge.setMaxValue(2920);
                mainGauge.addRange(outer);
                break;
            case "Total Volatile Organic Compound":
                mainGauge.setMinValue(0);
                mainGauge.setMaxValue(3276);
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
                mainGauge.setSecondMaxValue(1000);
                mainGauge.addSecondRange(middle);
                break;
            case "Carbon Monoxide":
                mainGauge.setSecondMinValue(0);
                mainGauge.setSecondMaxValue(100);
                mainGauge.addSecondRange(middle);
                break;
            case "Ammonia":
                mainGauge.setSecondMinValue(0);
                mainGauge.setSecondMaxValue(300);
                mainGauge.addSecondRange(middle);
                break;
            case "Carbon Dioxide":
                mainGauge.setSecondMinValue(0);
                mainGauge.setSecondMaxValue(2920);
                mainGauge.addSecondRange(middle);
                break;
            case "Total Volatile Organic Compound":
                mainGauge.setSecondMinValue(0);
                mainGauge.setSecondMaxValue(3276);
                mainGauge.addSecondRange(middle);
                break;
        }
    }

    public void setInnerRing(){
        Range inner = new Range();
        inner.setColor(Color.parseColor("#d18ca0"));
        switch (sharedPreferencesHelper.getInnerSpinnerGas()) {
            case "Alcohol":
            case "Liquefied Petroleum Gas":
            case "Methane":
                mainGauge.setThirdMinValue(0);
                mainGauge.setThirdMaxValue(1000);
                mainGauge.addThirdRange(inner);
                break;
            case "Carbon Monoxide":
                mainGauge.setThirdMinValue(0);
                mainGauge.setThirdMaxValue(100);
                mainGauge.addThirdRange(inner);
                break;
            case "Ammonia":
                mainGauge.setThirdMinValue(0);
                mainGauge.setThirdMaxValue(300);
                mainGauge.addThirdRange(inner);
                break;
            case "Carbon Dioxide":
                mainGauge.setThirdMinValue(0);
                mainGauge.setThirdMaxValue(2920);
                mainGauge.addThirdRange(inner);
                break;
            case "Total Volatile Organic Compound":
                mainGauge.setThirdMinValue(0);
                mainGauge.setThirdMaxValue(3276);
                mainGauge.addThirdRange(inner);
                break;
        }
    }

    public void initNavBar(){
        navBar.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.live_nav){
                startActivity(new Intent(getApplicationContext(), LiveMonitoring.class));
                overridePendingTransition(0, 0);
                return true;
            }else if(item.getItemId() == R.id.database_nav){
                startActivity(new Intent(getApplicationContext(), DatabaseActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }else if(item.getItemId() == R.id.settings_nav){
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
                double value1 = Double.parseDouble(String.valueOf(snapshot.child("Carbon Dioxide").getValue()));
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