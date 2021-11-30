package com.example.ceon390_projectgroup;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_monitoring);


        databaseReference = FirebaseDatabase.getInstance().getReference();  //get instance for firebase
        sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        if(sharedPreferencesHelper.getLocation().equals("")){
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }
        initViews();
        setListeners();
        initializeGaugesValues();

        navBar.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home_nav){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

        gaugeLPG();
        gaugeAmmonia();
        gaugeCarbonDioxide();
        gaugeCarbonMonoxide();
        gaugeAlcohol();
        gaugeMethane();
        gaugeTVOC();

    }

    public void gaugeLPG(){
        Range lpgGreen = new Range();
        lpgGreen.setColor(Color.parseColor("#badc58"));
        lpgGreen.setFrom(0);
        lpgGreen.setTo(1000);

        Range lpgYellow = new Range();
        lpgYellow.setColor(Color.parseColor("#f6e58d"));
        lpgYellow.setFrom(1000);
        lpgYellow.setTo(2100);

        Range lpgRed = new Range();
        lpgRed.setColor(Color.parseColor("#ff7979"));
        lpgRed.setFrom(2100);
        lpgRed.setTo(10000);

        halfGaugeMQ2.addRange(lpgGreen);
        halfGaugeMQ2.addRange(lpgYellow);
        halfGaugeMQ2.addRange(lpgRed);
        halfGaugeMQ2.setMinValue(0);
        halfGaugeMQ2.setMaxValue(10000);
    }

    public void gaugeAmmonia(){
        Range ammoniaGreen = new Range();
        ammoniaGreen.setColor(Color.parseColor("#badc58"));
        ammoniaGreen.setFrom(0);
        ammoniaGreen.setTo(30);

        Range ammoniaYellow = new Range();
        ammoniaYellow.setColor(Color.parseColor("#f6e58d"));
        ammoniaYellow.setFrom(30);
        ammoniaYellow.setTo(140);

        Range ammoniaRed = new Range();
        ammoniaRed.setColor(Color.parseColor("#ff7979"));
        ammoniaRed.setFrom(140);
        ammoniaRed.setTo(300);

        halfGaugeMQ135.addRange(ammoniaGreen);
        halfGaugeMQ135.addRange(ammoniaYellow);
        halfGaugeMQ135.addRange(ammoniaRed);
        halfGaugeMQ135.setMinValue(0);
        halfGaugeMQ135.setMaxValue(300);
    }

    public void gaugeCarbonDioxide(){
        Range co2Green = new Range();
        co2Green.setColor(Color.parseColor("#badc58"));
        co2Green.setFrom(400);
        co2Green.setTo(1000);

        Range co2Yellow = new Range();
        co2Yellow.setColor(Color.parseColor("#f6e58d"));
        co2Yellow.setFrom(1000);
        co2Yellow.setTo(29000);

        Range co2Red = new Range();
        co2Red.setColor(Color.parseColor("#ff7979"));
        co2Red.setFrom(29000);
        co2Red.setTo(29206);

        halfGaugeCO2.addRange(co2Green);
        halfGaugeCO2.addRange(co2Yellow);
        halfGaugeCO2.addRange(co2Red);
        halfGaugeCO2.setMinValue(400);
        halfGaugeCO2.setMaxValue(29206);
    }

    public void gaugeCarbonMonoxide(){
        Range coGreen = new Range();
        coGreen.setColor(Color.parseColor("#badc58"));
        coGreen.setFrom(0);
        coGreen.setTo(50);

        Range coYellow = new Range();
        coYellow.setColor(Color.parseColor("#f6e58d"));
        coYellow.setFrom(50);
        coYellow.setTo(400);

        Range coRed = new Range();
        coRed.setColor(Color.parseColor("#ff7979"));
        coRed.setFrom(400);
        coRed.setTo(1000);

        halfGaugeMQ9.addRange(coGreen);
        halfGaugeMQ9.addRange(coYellow);
        halfGaugeMQ9.addRange(coRed);
        halfGaugeMQ9.setMinValue(0);
        halfGaugeMQ9.setMaxValue(1000);
    }

    public void gaugeAlcohol(){
        Range alcoholGreen = new Range();
        alcoholGreen.setColor(Color.parseColor("#badc58"));
        alcoholGreen.setFrom(0);
        alcoholGreen.setTo(1000);

        Range alcoholYellow = new Range();
        alcoholYellow.setColor(Color.parseColor("#f6e58d"));
        alcoholYellow.setFrom(1000);
        alcoholYellow.setTo(3300);

        Range alcoholRed = new Range();
        alcoholRed.setColor(Color.parseColor("#ff7979"));
        alcoholRed.setFrom(3300);
        alcoholRed.setTo(10000);

        halfGaugeMQ8.addRange(alcoholGreen);
        halfGaugeMQ8.addRange(alcoholYellow);
        halfGaugeMQ8.addRange(alcoholRed);
        halfGaugeMQ8.setMinValue(0);
        halfGaugeMQ8.setMaxValue(10000);
    }

    public void gaugeMethane(){
        Range methaneGreen = new Range();
        methaneGreen.setColor(Color.parseColor("#badc58"));
        methaneGreen.setFrom(0);
        methaneGreen.setTo(1000);

        Range methaneYellow = new Range();
        methaneYellow.setColor(Color.parseColor("#f6e58d"));
        methaneYellow.setFrom(1000);
        methaneYellow.setTo(5000);

        Range methaneRed = new Range();
        methaneRed.setColor(Color.parseColor("#ff7979"));
        methaneRed.setFrom(5000);
        methaneRed.setTo(10000);

        halfGaugeMQ4.addRange(methaneGreen);
        halfGaugeMQ4.addRange(methaneYellow);
        halfGaugeMQ4.addRange(methaneRed);
        halfGaugeMQ4.setMinValue(0);
        halfGaugeMQ4.setMaxValue(10000);
    }

    public void gaugeTVOC(){
        Range tvocGreen = new Range();
        tvocGreen.setColor(Color.parseColor("#badc58"));
        tvocGreen.setFrom(0);
        tvocGreen.setTo(1);

        Range tvocYellow = new Range();
        tvocYellow.setColor(Color.parseColor("#f6e58d"));
        tvocYellow.setFrom(1);
        tvocYellow.setTo(10);

        Range tvocRed = new Range();
        tvocRed.setColor(Color.parseColor("#ff7979"));
        tvocRed.setFrom(10);
        tvocRed.setTo(32768);

        halfGaugeTVOC.addRange(tvocGreen);
        halfGaugeTVOC.addRange(tvocYellow);
        halfGaugeTVOC.addRange(tvocRed);
        halfGaugeTVOC.setMinValue(0);
        halfGaugeTVOC.setMaxValue(32768);
    }

    // This method is to read the Data from Firebase
    public void Read() {  //referenced from https://firebase.google.com/docs/database/android/start
        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = Objects.requireNonNull(dataSnapshot.child("Carbon Dioxide").getValue()).toString();
                halfGaugeCO2.setValue(Integer.parseInt(value));
                String value1 = Objects.requireNonNull(dataSnapshot.child("Ammonia").getValue()).toString();
                halfGaugeMQ135.setValue(Integer.parseInt(value1));
                String value2 = Objects.requireNonNull(dataSnapshot.child("Liquefied Petroleum Gas").getValue()).toString();
                halfGaugeMQ2.setValue(Integer.parseInt(value2));
                String value3 = Objects.requireNonNull(dataSnapshot.child("Methane").getValue()).toString();
                halfGaugeMQ4.setValue(Integer.parseInt(value3));
                String value4 = Objects.requireNonNull(dataSnapshot.child("Alcohol").getValue()).toString();
                halfGaugeMQ8.setValue(Integer.parseInt(value4));
                String value5 = Objects.requireNonNull(dataSnapshot.child("Carbon Monoxide").getValue()).toString();
                halfGaugeMQ9.setValue(Integer.parseInt(value5));
                String value6 = Objects.requireNonNull(dataSnapshot.child("Total Volatile Organic Compound").getValue()).toString();
                halfGaugeTVOC.setValue(Integer.parseInt(value6));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(LiveMonitoring.this, "Failed to get data. Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Overridden method to handle the click on views
    /**
     * @param view
     * /*app:title="Live Monitoring"
     */
    @Override
    public void onClick(View view) {
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        Intent intent = new Intent(LiveMonitoring.this, DatabaseActivity.class);

        switch (view.getId()) {

            case R.id.halfGaugeCO2:
                sharedPreferencesHelper.saveFilter("Carbon Dioxide");
                startActivity(intent);
                break;
            case R.id.halfGaugeMQ135:
                sharedPreferencesHelper.saveFilter("Ammonia");
                startActivity(intent);
                break;
            case R.id.halfGaugeMQ2:
                sharedPreferencesHelper.saveFilter("LPG");
                startActivity(intent);
                break;
            case R.id.halfGaugeMQ4:
                sharedPreferencesHelper.saveFilter("Methane");
                startActivity(intent);
                break;
            case R.id.halfGaugeMQ8:
                sharedPreferencesHelper.saveFilter("Alcohol");
                startActivity(intent);
                break;
            case R.id.halfGaugeMQ9:
                sharedPreferencesHelper.saveFilter("Carbon Monoxide");
                startActivity(intent);
                break;
            case R.id.halfGaugeTVOC:
                sharedPreferencesHelper.saveFilter("TVOC");
                startActivity(intent);
                break;
            case R.id.ivCO2:
                String sensorNameCO2 = tvSensorNameCO2.getText().toString();
                String marketNameCO2 = "SparkFun CCS811 board";
                String gasDetectionCO2 = tvSensorNameCO2.getText().toString();
                String dangersCO2 = "CO\u2082 produces many health effects, such as headaches, dizziness, restlessness," +
                        " difficulty breathing, sweating, increased heart rate, and convulsions.";
                String moreInfoCO2 = "https://www.cdc.gov/niosh/npg/npgd0103.html";
                showDialog(sensorNameCO2, marketNameCO2, gasDetectionCO2, dangersCO2, moreInfoCO2);
                break;
            case R.id.ivMQ135:
                String sensorNameMQ135 = tvSensorNameMQ135.getText().toString();
                String marketNameMQ135 = "MQ135 Sensor";
                String gasDetectionMQ135 = tvSensorNameMQ135.getText().toString();
                String dangersMQ135 = "High concentrations of Ammonia in air causes immediate burning " +
                        "of the eyes, nose, throat and respiratory tract and can result in blindness, lung damage or death.";
                String moreInfoMQ135 = "https://www.ccohs.ca/oshanswers/chemicals/chem_profiles/ammonia.html";
                showDialog(sensorNameMQ135, marketNameMQ135, gasDetectionMQ135, dangersMQ135, moreInfoMQ135);
                break;
            case R.id.ivMQ2:
                String sensorNameMQ2 = tvSensorNameMQ2.getText().toString();
                String marketNameMQ2 = "MQ2 Sensor";
                String gasDetectionMQ2 = tvSensorNameMQ2.getText().toString();
                String dangersMQ2 = "Inhaling LPG vapor at high concentration even for a short time " +
                        "can cause asphyxiation, seizures, heart problems and death.";
                String moreInfoMQ2 = "https://www.hsa.ie/eng/Topics/Liquid_Petroleum_Gas_LPG_/";
                showDialog(sensorNameMQ2, marketNameMQ2, gasDetectionMQ2, dangersMQ2, moreInfoMQ2);
                break;
            case R.id.ivMQ4:
                String sensorNameMQ4 = tvSensorNameMQ4.getText().toString();
                String marketNameMQ4 = "MQ4 Sensor";
                String gasDetectionMQ4 = tvSensorNameMQ4.getText().toString();
                String dangersMQ4 = "Methane case reduce the amount of oxygen breathed from the air " +
                        "and can cause vision problems and unconsciousness.";
                String moreInfoMQ4 = "https://sciencing.com/what-are-the-dangers-of-methane-gas-13404265.html";
                showDialog(sensorNameMQ4, marketNameMQ4, gasDetectionMQ4, dangersMQ4, moreInfoMQ4);
                break;
            case R.id.ivMQ8:
                String sensorNameMQ8 = tvSensorNameMQ8.getText().toString();
                String marketNameMQ8 = "MQ8 Sensor";
                String gasDetectionMQ8 = tvSensorNameMQ8.getText().toString();
                String dangersMQ8 = "Exposure to Ethyl Alcohol can cause headache, drowsiness, nausea, vomiting, and unconsciousness";
                String moreInfoMQ8 = "https://www.poison.org/articles/inhaling-alcohol-is-dangerous";
                showDialog(sensorNameMQ8, marketNameMQ8, gasDetectionMQ8, dangersMQ8, moreInfoMQ8);
                break;
            case R.id.ivMQ9:
                String sensorNameMQ9 = tvSensorNameMQ9.getText().toString();
                String marketNameMQ9 = "MQ9 Sensor";
                String gasDetectionMQ9 = tvSensorNameMQ9.getText().toString();
                String dangersMQ9 = "CO can lead to death, in case the human body is exposed to over " +
                        "100 ppm or greater, since inhaling CO replaces the oxygen in human blood.";
                String moreInfoMQ9 = "https://www.nhs.uk/conditions/carbon-monoxide-poisoning/";
                showDialog(sensorNameMQ9, marketNameMQ9, gasDetectionMQ9, dangersMQ9, moreInfoMQ9);
                break;
            case R.id.ivTVOC:
                String sensorNameTVOC = tvSensorNameTVOC.getText().toString();
                String marketNameTVOC = "SparkFun CCS811 board";
                String gasDetectionTVOC = tvSensorNameTVOC.getText().toString();
                String dangersTVOC = "VOCs can irritate the eyes, nose and throat, and can cause difficulty " +
                        "breathing and nausea as well as damaging the central nervous system.";
                String moreInfoTVOC = "https://www.health.state.mn.us/communities/environment/air/toxins/voc.htm";
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