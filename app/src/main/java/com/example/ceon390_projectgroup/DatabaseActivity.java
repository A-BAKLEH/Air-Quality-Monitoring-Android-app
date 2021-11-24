package com.example.ceon390_projectgroup;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DatabaseActivity extends AppCompatActivity {

    BottomNavigationView navBar;
    DatabaseHelper databaseHelper;
    DatabaseReference databaseReference;
    ListView databaseList;
    public static ArrayList<String> SensorList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseHelper = new DatabaseHelper(this);


        navBar  = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.database_nav);

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_nav:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings_nav:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.live_nav:
                        startActivity(new Intent(getApplicationContext(), LiveMonitoring.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        setDatabaseList();

    }

    protected void setDatabaseList(){
        List<Sensors> sensorsDatabase = databaseHelper.getAllValues();
        databaseList = findViewById(R.id.databaseList);


        ArrayList<String> viewDatabaseList = new ArrayList<>();

        for(int i=0; i < sensorsDatabase.size(); i++){
            String display = sensorsDatabase.get(i).getGas()+" PPM: "
                + sensorsDatabase.get(i).getValue()+ " | "
                + sensorsDatabase.get(i).getTimestamp();
            viewDatabaseList.add(display);
        }
    ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,viewDatabaseList);
    databaseList.setAdapter(arrayAdapter);
    }

}