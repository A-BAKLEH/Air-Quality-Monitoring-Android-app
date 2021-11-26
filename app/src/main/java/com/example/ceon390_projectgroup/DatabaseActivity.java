package com.example.ceon390_projectgroup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseActivity extends AppCompatActivity {

    BottomNavigationView navBar;
    DatabaseHelper databaseHelper;
    DatabaseReference databaseReference;
    ListView databaseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseHelper = new DatabaseHelper(this);



        navBar  = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.database_nav);

        navBar.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.live_nav){
                startActivity(new Intent(getApplicationContext(), LiveMonitoring.class));
                overridePendingTransition(0, 0);
                return true;
            }else if(item.getItemId() == R.id.home_nav){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }else if(item.getItemId() == R.id.settings_nav){
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
        setDatabaseList();

    }

    protected void setDatabaseList(){
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this);
        List<Sensors> sensorsDatabase;

        if(sharedPreferencesHelper.getFilter() == ""){
            sensorsDatabase = databaseHelper.getAllValues();
        }
        else {
            sensorsDatabase = databaseHelper.getFilterValues(sharedPreferencesHelper.getFilter());
        }
        databaseList = findViewById(R.id.databaseList);

        ArrayList<String> viewDatabaseList = new ArrayList<>();

        for(int i=0; i < sensorsDatabase.size(); i++){
            String display = sensorsDatabase.get(i).getGas()+" PPM: "
                + sensorsDatabase.get(i).getValue()+ " | "
                + sensorsDatabase.get(i).getTimestamp();
            viewDatabaseList.add(display);
        }
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,viewDatabaseList);
    databaseList.setAdapter(arrayAdapter);
    }

}