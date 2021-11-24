package com.example.ceon390_projectgroup;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LiveMonitoring extends AppCompatActivity implements View.OnClickListener {

    /*FullGauge fullGaugeCO2;
    FullGauge fullGaugeMQ135;
    FullGauge fullGaugeMQ2;*/

    // HalfGauges for gauge charts
    HalfGauge halfGaugeCO2;
    HalfGauge halfGaugeMQ135;
    HalfGauge halfGaugeMQ2;
    HalfGauge halfGaugeMQ4;
    HalfGauge halfGaugeMQ8;
    HalfGauge halfGaugeMQ9;
    HalfGauge halfGaugeTVOC;

    // TextViews for sensors names
    TextView tvSensorNameCO2;
    TextView tvSensorNameMQ135;
    TextView tvSensorNameMQ2;
    TextView tvSensorNameMQ4;
    TextView tvSensorNameMQ8;
    TextView tvSensorNameMQ9;
    TextView tvSensorNameTVOC;

    // ImageViews for info icons
    ImageView ivCO2;
    ImageView ivMQ135;
    ImageView ivMQ2;
    ImageView ivMQ4;
    ImageView ivMQ8;
    ImageView ivMQ9;
    ImageView ivTVOC;

    // Bottom Navigation Bar
    BottomNavigationView navBar;

    // Firebase Database Reference
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_monitoring);


        databaseReference = FirebaseDatabase.getInstance().getReference();  //get instance for firebase

        initViews();
        setListeners();
        initializeGaugesValues();

        /*Range range3 = new Range();
        range3.setColor(Color.parseColor("#00FF00"));
        range3.setFrom(0);
        range3.setTo(500);
        fullGaugeCO2 = findViewById(R.id.fullGaugeCO2);
        fullGaugeMQ135 = findViewById(R.id.fullGaugeMQ135);
        fullGaugeMQ2 = findViewById(R.id.fullGaugeMQ2);

        fullGaugeCO2.setMinValue(0);
        fullGaugeCO2.setMaxValue(5000);
        fullGaugeCO2.addRange(range3);

        fullGaugeMQ135.setMinValue(0);
        fullGaugeMQ135.setMaxValue(5000);
        fullGaugeMQ135.addRange(range3);

        fullGaugeMQ2.setMinValue(0);
        fullGaugeMQ2.setMaxValue(5000);
        fullGaugeMQ2.addRange(range3);*/

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_nav:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

        Read();
    }

    // This method is used to set initialize views
    private void initViews() {
        halfGaugeCO2 = findViewById(R.id.halfGaugeCO2);
        halfGaugeMQ135 = findViewById(R.id.halfGaugeMQ135);
        halfGaugeMQ2 = findViewById(R.id.halfGaugeMQ2);
        halfGaugeMQ4 = findViewById(R.id.halfGaugeMQ4);
        halfGaugeMQ8 = findViewById(R.id.halfGaugeMQ8);
        halfGaugeMQ9 = findViewById(R.id.halfGaugeMQ9);
        halfGaugeTVOC = findViewById(R.id.halfGaugeTVOC);

        tvSensorNameCO2 = findViewById(R.id.tvSensorNameCO2);
        tvSensorNameMQ135 = findViewById(R.id.tvSensorNameMQ135);
        tvSensorNameMQ2 = findViewById(R.id.tvSensorNameMQ2);
        tvSensorNameMQ4 = findViewById(R.id.tvSensorNameMQ4);
        tvSensorNameMQ8 = findViewById(R.id.tvSensorNameMQ8);
        tvSensorNameMQ9 = findViewById(R.id.tvSensorNameMQ9);
        tvSensorNameTVOC = findViewById(R.id.tvSensorNameTVOC);

        ivCO2 = findViewById(R.id.ivCO2);
        ivMQ135 = findViewById(R.id.ivMQ135);
        ivMQ2 = findViewById(R.id.ivMQ2);
        ivMQ4 = findViewById(R.id.ivMQ4);
        ivMQ8 = findViewById(R.id.ivMQ8);
        ivMQ9 = findViewById(R.id.ivMQ9);
        ivTVOC = findViewById(R.id.ivTVOC);

        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.live_nav);
    }

    // This method is used to set click listeners
    private void setListeners() {
        halfGaugeCO2.setOnClickListener(this);
        halfGaugeMQ135.setOnClickListener(this);
        halfGaugeMQ2.setOnClickListener(this);
        halfGaugeMQ4.setOnClickListener(this);
        halfGaugeMQ8.setOnClickListener(this);
        halfGaugeMQ9.setOnClickListener(this);
        halfGaugeTVOC.setOnClickListener(this);

        ivCO2.setOnClickListener(this);
        ivMQ135.setOnClickListener(this);
        ivMQ2.setOnClickListener(this);
        ivMQ4.setOnClickListener(this);
        ivMQ8.setOnClickListener(this);
        ivMQ9.setOnClickListener(this);
        ivTVOC.setOnClickListener(this);
    }

    // This method is used to initialize the half gauges values
    private void initializeGaugesValues() {
        Range range1 = new Range();
        range1.setColor(Color.parseColor("#badc58"));
        range1.setFrom(0);
        range1.setTo(4500);

        Range range2 = new Range();
        range2.setColor(Color.parseColor("#f6e58d"));
        range2.setFrom(4501);
        range2.setTo(7500);

        Range range3 = new Range();
        range3.setColor(Color.parseColor("#ff7979"));
        range3.setFrom(7501);
        range3.setTo(9999);

        halfGaugeCO2.addRange(range1);
        halfGaugeCO2.addRange(range2);
        halfGaugeCO2.addRange(range3);
        halfGaugeCO2.setMinValue(0);
        halfGaugeCO2.setMaxValue(9999);

        halfGaugeMQ135.addRange(range1);
        halfGaugeMQ135.addRange(range2);
        halfGaugeMQ135.addRange(range3);
        halfGaugeMQ135.setMinValue(0);
        halfGaugeMQ135.setMaxValue(9999);

        halfGaugeMQ2.addRange(range1);
        halfGaugeMQ2.addRange(range2);
        halfGaugeMQ2.addRange(range3);
        halfGaugeMQ2.setMinValue(0);
        halfGaugeMQ2.setMaxValue(9999);

        halfGaugeMQ4.addRange(range1);
        halfGaugeMQ4.addRange(range2);
        halfGaugeMQ4.addRange(range3);
        halfGaugeMQ4.setMinValue(0);
        halfGaugeMQ4.setMaxValue(9999);

        halfGaugeMQ8.addRange(range1);
        halfGaugeMQ8.addRange(range2);
        halfGaugeMQ8.addRange(range3);
        halfGaugeMQ8.setMinValue(0);
        halfGaugeMQ8.setMaxValue(9999);

        halfGaugeMQ9.addRange(range1);
        halfGaugeMQ9.addRange(range2);
        halfGaugeMQ9.addRange(range3);
        halfGaugeMQ9.setMinValue(0);
        halfGaugeMQ9.setMaxValue(9999);

        halfGaugeTVOC.addRange(range1);
        halfGaugeTVOC.addRange(range2);
        halfGaugeTVOC.addRange(range3);
        halfGaugeTVOC.setMinValue(0);
        halfGaugeTVOC.setMaxValue(9999);
    }

    // This method is to read the Data from Firebase
    public void Read() {  //referenced from https://firebase.google.com/docs/database/android/start
        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.child("Carbon Monoxide").getValue().toString();
                halfGaugeCO2.setValue(Double.parseDouble(value));
                String value1 = dataSnapshot.child("Ammonia").getValue().toString();
                halfGaugeMQ135.setValue(Double.parseDouble(value1));
                String value2 = dataSnapshot.child("Liquefied Petroleum Gas").getValue().toString();
                halfGaugeMQ2.setValue(Double.parseDouble(value2));
                String value3 = dataSnapshot.child("Methane").getValue().toString();
                halfGaugeMQ4.setValue(Double.parseDouble(value3));
                String value4 = dataSnapshot.child("Alcohol").getValue().toString();
                halfGaugeMQ8.setValue(Double.parseDouble(value4));
                String value5 = dataSnapshot.child("Carbon Monoxide").getValue().toString();
                halfGaugeMQ9.setValue(Double.parseDouble(value5));
                String value6 = dataSnapshot.child("Total Volatile Organic Compound").getValue().toString();
                halfGaugeTVOC.setValue(Double.parseDouble(value6));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(LiveMonitoring.this, "Failed to get data. Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Overrided method to handle the click on views
    /**
     * @param view
     * /*app:title="Live Monitoring"
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.halfGaugeCO2:
            case R.id.halfGaugeMQ135:
            case R.id.halfGaugeMQ2:
            case R.id.halfGaugeMQ4:
            case R.id.halfGaugeMQ8:
            case R.id.halfGaugeMQ9:
            case R.id.halfGaugeTVOC:
                Intent intent = new Intent(LiveMonitoring.this, DatabaseActivity.class);
                startActivity(intent);
                break;
            case R.id.ivCO2:
                String sensorNameCO2 = tvSensorNameCO2.getText().toString();
                String marketNameCO2 = "SparkFun CCS811 board";
                String gasDetectionCO2 = tvSensorNameCO2.getText().toString();
                String dangersCO2 = "CO\u2082 produces many health effects, such as headaches, dizziness, restlessness," +
                        " difficulty breathing, sweating, increased heart rate, and convulsions";
                String moreInfoCO2 = "https://www.cdc.gov/niosh/npg/npgd0103.html";
                showDialog(sensorNameCO2, marketNameCO2, gasDetectionCO2, dangersCO2, moreInfoCO2);
                break;
            case R.id.ivMQ135:
                String sensorNameMQ135 = tvSensorNameMQ135.getText().toString();
                String marketNameMQ135 = "MQ135 Sensor";
                String gasDetectionMQ135 = tvSensorNameMQ135.getText().toString();
                String dangersMQ135 = "High concentrations of Ammonia in air causes immediate burning " +
                        "of the eyes, nose, throat and respiratory tract and can result in blindness, lung damage or death";
                String moreInfoMQ135 = "https://www.ccohs.ca/oshanswers/chemicals/chem_profiles/ammonia.html";
                showDialog(sensorNameMQ135, marketNameMQ135, gasDetectionMQ135, dangersMQ135, moreInfoMQ135);
                break;
            case R.id.ivMQ2:
                String sensorNameMQ2 = tvSensorNameMQ2.getText().toString();
                String marketNameMQ2 = "MQ2 Sensor";
                String gasDetectionMQ2 = tvSensorNameMQ2.getText().toString();
                String dangersMQ2 = "Inhaling harmful smoke can inflame lungs and airway, causing them to swell and block oxygen";
                String moreInfoMQ2 = "https://www.epa.gov/pm-pollution/how-smoke-fires-can-affect-your-health";
                showDialog(sensorNameMQ2, marketNameMQ2, gasDetectionMQ2, dangersMQ2, moreInfoMQ2);
                break;
            case R.id.ivMQ4:
                String sensorNameMQ4 = tvSensorNameMQ4.getText().toString();
                String marketNameMQ4 = "MQ4 Sensor";
                String gasDetectionMQ4 = tvSensorNameMQ4.getText().toString();
                String dangersMQ4 = "Methane case reduce the amount of oxygen breathed from the air " +
                        "and can cause vision problems and unconsciousness";
                String moreInfoMQ4 = "https://sciencing.com/what-are-the-dangers-of-methane-gas-13404265.html";
                showDialog(sensorNameMQ4, marketNameMQ4, gasDetectionMQ4, dangersMQ4, moreInfoMQ4);
                break;
            case R.id.ivMQ8:
                String sensorNameMQ8 = tvSensorNameMQ8.getText().toString();
                String marketNameMQ8 = "MQ8 Sensor";
                String gasDetectionMQ8 = tvSensorNameMQ8.getText().toString();
                String dangersMQ8 = "Hydrogen could cause death in case of high concentrations in air," +
                        " since hydrogen is highly compressed and liquid hydrogen is extremely cold";
                String moreInfoMQ8 = "https://www.nap.edu/read/12032/chapter/9#155";
                showDialog(sensorNameMQ8, marketNameMQ8, gasDetectionMQ8, dangersMQ8, moreInfoMQ8);
                break;
            case R.id.ivMQ9:
                String sensorNameMQ9 = tvSensorNameMQ9.getText().toString();
                String marketNameMQ9 = "MQ9 Sensor";
                String gasDetectionMQ9 = tvSensorNameMQ9.getText().toString();
                String dangersMQ9 = "CO can lead to death, in case the human body is exposed to over " +
                        "100 ppm or greater, since inhaling CO replaces the oxygen in human blood";
                String moreInfoMQ9 = "https://www.nhs.uk/conditions/carbon-monoxide-poisoning/";
                showDialog(sensorNameMQ9, marketNameMQ9, gasDetectionMQ9, dangersMQ9, moreInfoMQ9);
                break;
            case R.id.ivTVOC:
                String sensorNameTVOC = tvSensorNameTVOC.getText().toString();
                String marketNameTVOC = "SparkFun CCS811 board";
                String gasDetectionTVOC = tvSensorNameTVOC.getText().toString();
                String dangersTVOC = "VOCs can irritate the eyes, nose and throat, and can cause difficulty " +
                        "breathing and nausea as well as damaging the central nervous system";
                String moreInfoTVOC = "shorturl.at/mqwxK";
                showDialog(sensorNameTVOC, marketNameTVOC, gasDetectionTVOC, dangersTVOC, moreInfoTVOC);
                break;
        }
    }

    // This method is to show pop up for sensor info
    /**
     * @param sensorName
     * @param marketName
     * @param gasDetection
     * @param dangers
     * @param moreInfo
     */
    private void showDialog(String sensorName, String marketName, String gasDetection, String dangers, String moreInfo) {
        SensorInfoDialog dialog = new SensorInfoDialog(LiveMonitoring.this,
                sensorName + " Info", marketName, gasDetection + " Gases", dangers, moreInfo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
}