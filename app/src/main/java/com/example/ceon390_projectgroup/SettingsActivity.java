package com.example.ceon390_projectgroup;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    public static final String SENSOR = "Sensor";

    BottomNavigationView navBar;
    EditText location;
    EditText room;
    TextView innerRing;
    TextView middleRing;
    TextView outerRing;
    Spinner innerSpinner;
    Spinner middleSpinner;
    Spinner outerSpinner;
    Button saveButton;
    ArrayAdapter arrayAdapter1;
    ArrayAdapter arrayAdapter2;
    ArrayAdapter arrayAdapter3;
    ImageView logout;

    SharedPreferencesHelper sharedPreferencesHelper;
    FirebaseAuth mAuth;


    public static String [] sensors = {"MQ135 Sensor", "MQ2 Sensor", "MQ4 Sensor", "MQ8 Sensor", "MQ9 Sensor", "CO2 CCS811 Sensor", "TVOC CCS811 Sensor"};
    //Change sensors to gases
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.settings_nav);
        location = findViewById(R.id.LocationEditText);
        room = findViewById(R.id.RoomEditText);
        innerRing = findViewById(R.id.InnerRingTextView);
        middleRing = findViewById(R.id.MiddleRingTextView);
        outerRing = findViewById(R.id.OuterRingTextView);
        saveButton = findViewById(R.id.SaveButton);
        logout = findViewById(R.id.logoutImageView);

        //inner spinner code
        innerSpinner = findViewById(R.id.InnerSpinner);
        arrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sensors);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        innerSpinner.setAdapter(arrayAdapter1);

        //middle spinner code
        middleSpinner = findViewById(R.id.MiddleSpinner);
        arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sensors);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        middleSpinner.setAdapter(arrayAdapter2);

        //outer spinner code
        outerSpinner = findViewById(R.id.OuterSpinner);
        arrayAdapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sensors);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        outerSpinner.setAdapter(arrayAdapter3);
        //Limit length of input to 15 characters
        int maxLength = 15;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        location.setFilters(FilterArray);
        room.setFilters(FilterArray);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationName = location.getText().toString();
                String roomName = room.getText().toString();
                if(locationName.equals("") || roomName.equals("")){
                    Toast.makeText(SettingsActivity.this, "Cannot be empty, try again", Toast.LENGTH_SHORT).show();
                }
                sharedPreferencesHelper.saveLocation(locationName);
                sharedPreferencesHelper.saveRoom(roomName);
                location.setText(sharedPreferencesHelper.getLocation());
                room.setText(sharedPreferencesHelper.getRoom());
            }
        });

        innerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               int innerSpinnerValue = innerSpinner.getSelectedItemPosition();
               sharedPreferencesHelper.saveInnerSpinnerSelection(innerSpinnerValue);
               String innerSpinnerGas = sensors[i];
               sharedPreferencesHelper.saveInnerSpinnerGas(innerSpinnerGas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        middleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int middleSpinnerValue = middleSpinner.getSelectedItemPosition();
                sharedPreferencesHelper.saveMiddleSpinnerSelection(middleSpinnerValue);
                String middleSpinnerGas = sensors[i];
                sharedPreferencesHelper.saveMiddleSpinnerGas(middleSpinnerGas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        outerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int outerSpinnerValue = outerSpinner.getSelectedItemPosition();
                sharedPreferencesHelper.saveOuterSpinnerSelection(outerSpinnerValue);
                String outerSpinnerGas = sensors[i];
                sharedPreferencesHelper.saveOuterSpinnerGas(outerSpinnerGas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home_nav:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.database_nav:
                        startActivity(new Intent(getApplicationContext(), DatabaseActivity.class));
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        location.setText(sharedPreferencesHelper.getLocation());
        room.setText(sharedPreferencesHelper.getRoom());
        innerSpinner.setSelection(sharedPreferencesHelper.getInnerSpinnerSelection());
        middleSpinner.setSelection(sharedPreferencesHelper.getMiddleSpinnerSelection());
        outerSpinner.setSelection(sharedPreferencesHelper.getOuterSpinnerSelection());
    }
}