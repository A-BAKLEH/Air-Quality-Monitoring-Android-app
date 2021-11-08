package com.example.ceon390_projectgroup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.ekn.gruzer.gaugelibrary.FullGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

public class LiveMonitoring extends AppCompatActivity {

    FullGauge fullGaugeCO2;
    FullGauge fullGaugeMQ135;
    FullGauge fullGaugeMQ2;



    BottomNavigationView navBar;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_monitoring);

        Range range3 = new Range();
        range3.setColor(Color.parseColor("#00FF00"));
        range3.setFrom(0);
        range3.setTo(500);

        databaseReference = FirebaseDatabase.getInstance().getReference();  //get instance for firebase
        fullGaugeCO2 = findViewById(R.id.fullGaugeCO2);
        fullGaugeMQ135 = findViewById(R.id.fullGaugeMQ135);
        fullGaugeMQ2 = findViewById(R.id.fullGaugeMQ2);

        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.live_nav);

        fullGaugeCO2.setMinValue(0);
        fullGaugeCO2.setMaxValue(5000);
        fullGaugeCO2.addRange(range3);

        fullGaugeMQ135.setMinValue(0);
        fullGaugeMQ135.setMaxValue(5000);
        fullGaugeMQ135.addRange(range3);

        fullGaugeMQ2.setMinValue(0);
        fullGaugeMQ2.setMaxValue(5000);
        fullGaugeMQ2.addRange(range3);


        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_nav:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

        Read();


    }
    public void Read() {  //referenced from https://firebase.google.com/docs/database/android/start
        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.child("MQ135 Sensor").getValue().toString();
                fullGaugeMQ135.setValue(Double.parseDouble(value));
                String value1 = dataSnapshot.child("CO2 CCS811 Sensor").getValue().toString();
                fullGaugeCO2.setValue(Double.parseDouble(value1));
                String value2 = dataSnapshot.child("MQ2 Sensor").getValue().toString();
                fullGaugeMQ2.setValue(Double.parseDouble(value2));
                //String value3 = dataSnapshot.child("MQ4 Sensor").getValue().toString();
                //MQ4values.setText(value3);
                //String value4 = dataSnapshot.child("MQ8 Sensor").getValue().toString();
                //MQ8values.setText(value4);
                //String value5 = dataSnapshot.child("MQ9 Sensor").getValue().toString();
                //MQ9values.setText(value5);
                //String value6 = dataSnapshot.child("TVOC CCS811 Sensor").getValue().toString();
                //TVOCvalues.setText(value6);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(LiveMonitoring.this, "Failed to get data. Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}