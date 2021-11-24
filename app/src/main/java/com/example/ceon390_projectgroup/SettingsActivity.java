package com.example.ceon390_projectgroup;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

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
    Button editButton;

    ImageView logoutImage;

    ArrayAdapter<String> arrayAdapter1;
    ArrayAdapter<String> arrayAdapter2;
    ArrayAdapter<String> arrayAdapter3;

    SharedPreferencesHelper sharedPreferencesHelper;

    FirebaseDatabase firebaseDatabase; //Firebase object to initialize
    FirebaseAuth mAuth;
    DatabaseReference databaseReference; //For gas values from Arduino
    DatabaseReference AQMReference; //For the Air Quality Monitoring Structure
    FirebaseData gasData; //Object to send to firebase
    String alcohol, ammonia, carbon_dioxide, carbon_monoxide, liquefied_petroleum_gas, methane, total_volatile_organic_compound; //Strings to save gas values

    public static String [] sensors = {"Alcohol", "Ammonia", "Carbon Dioxide", "Carbon Monoxide", "Liquefied Petroleum Gas", "Methane", "Total Volatile Organic Compounds"};
    //Change sensors to gases
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        AQMReference = FirebaseDatabase.getInstance().getReference("Air Quality Monitoring");
        Read(); //Find a way to update the firebase structure!!
        navBar = findViewById(R.id.navBar);
        navBar.setSelectedItemId(R.id.settings_nav);
        location = findViewById(R.id.LocationEditText);
        room = findViewById(R.id.RoomEditText);
        innerRing = findViewById(R.id.InnerRingTextView);
        middleRing = findViewById(R.id.MiddleRingTextView);
        outerRing = findViewById(R.id.OuterRingTextView);
        saveButton = findViewById(R.id.SaveButton);
        editButton = findViewById(R.id.editButton);
        logoutImage = findViewById(R.id.logoutImageView);

        //inner spinner code
        innerSpinner = findViewById(R.id.InnerSpinner);
        arrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sensors);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        innerSpinner.setAdapter(arrayAdapter1);

        //middle spinner code
        middleSpinner = findViewById(R.id.MiddleSpinner);
        arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sensors);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        middleSpinner.setAdapter(arrayAdapter2);

        //outer spinner code
        outerSpinner = findViewById(R.id.OuterSpinner);
        arrayAdapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sensors);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        outerSpinner.setAdapter(arrayAdapter3);
        //Limit length of input to 15 characters
        int maxLength = 15;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        location.setFilters(FilterArray);
        room.setFilters(FilterArray);
        editButton.setClickable(false);

        saveButton.setOnClickListener(view -> {
            String locationName = location.getText().toString();
            String roomName = room.getText().toString();
            if(locationName.equals("") || roomName.equals("")){
                Toast.makeText(SettingsActivity.this, "Cannot be empty, try again", Toast.LENGTH_SHORT).show();
            }
            sharedPreferencesHelper.saveLocation(locationName);
            sharedPreferencesHelper.saveRoom(roomName);
            location.setText(sharedPreferencesHelper.getLocation());
            room.setText(sharedPreferencesHelper.getRoom());
            //Create firebase data object (gases) to send to Firebase AQM Structure
            gasData = new FirebaseData(alcohol, ammonia, carbon_dioxide, carbon_monoxide, liquefied_petroleum_gas, methane, total_volatile_organic_compound);
            AQMReference.child("Location: " + sharedPreferencesHelper.getLocation()).child("Room: " + sharedPreferencesHelper.getRoom()).setValue(gasData);
            saveButton.setClickable(false);
            saveButton.setAlpha(.5f);
            editButton.setClickable(true);
            location.setEnabled(false);
            room.setEnabled(false);
        });

        editButton.setOnClickListener(view -> {
            saveButton.setClickable(true);
            saveButton.setAlpha(1f);
            location.setEnabled(true);
            room.setEnabled(true);
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


        navBar.setOnItemSelectedListener(item -> {
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
        });

        logoutImage.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            Toast.makeText(SettingsActivity.this,"User logged out",Toast.LENGTH_LONG).show();

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
    // This method is to read the Data from Firebase
    public void Read() {  //referenced from https://firebase.google.com/docs/database/android/start
        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                carbon_dioxide = Objects.requireNonNull(dataSnapshot.child("Carbon Dioxide").getValue()).toString();
                ammonia = Objects.requireNonNull(dataSnapshot.child("Ammonia").getValue()).toString();
                liquefied_petroleum_gas = Objects.requireNonNull(dataSnapshot.child("Liquefied Petroleum Gas").getValue()).toString();
                methane = Objects.requireNonNull(dataSnapshot.child("Methane").getValue()).toString();
                alcohol = Objects.requireNonNull(dataSnapshot.child("Alcohol").getValue()).toString();
                carbon_monoxide = Objects.requireNonNull(dataSnapshot.child("Carbon Monoxide").getValue()).toString();
                total_volatile_organic_compound = Objects.requireNonNull(dataSnapshot.child("Total Volatile Organic Compound").getValue()).toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(SettingsActivity.this, "Failed to get data. Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}