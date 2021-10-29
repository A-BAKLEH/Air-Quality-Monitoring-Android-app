package com.example.ceon390_projectgroup;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




public class MainActivity extends AppCompatActivity {

    TextView MQ135values;  //variable for text view
    TextView MQ2values;
    TextView CO2values;
    TextView MQ4values;
    TextView MQ8values;
    TextView TVOCvalues;
    TextView MQ9values;

    DatabaseReference databaseReference;//variable for database reference for database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();  //get instance for firebase
        MQ135values = findViewById(R.id.MQ135Sensor);  //initialize object class variable
        MQ2values = findViewById(R.id.MQ2Sensor);
        MQ4values = findViewById(R.id.MQ4Sensor);
        CO2values = findViewById(R.id.CO2Sensor);
        MQ8values = findViewById(R.id.MQ8Sensor);
        MQ9values = findViewById(R.id.MQ9Sensor);
        TVOCvalues = findViewById(R.id.TVOCsensor);

        Read();  //calling method
    }


    public void Read() {  //referenced from https://firebase.google.com/docs/database/android/start
    // Read from the database
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            String value = dataSnapshot.child("MQ135 Sensor").getValue().toString();
            MQ135values.setText(value);
            String value1 = dataSnapshot.child("CO2 CCS811 Sensor").getValue().toString();
            CO2values.setText(value1);
            String value2 = dataSnapshot.child("MQ2 Sensor").getValue().toString();
            MQ2values.setText(value2);
            String value3 = dataSnapshot.child("MQ4 Sensor").getValue().toString();
            MQ4values.setText(value3);
            String value4 = dataSnapshot.child("MQ8 Sensor").getValue().toString();
            MQ8values.setText(value4);
            String value5 = dataSnapshot.child("MQ9 Sensor").getValue().toString();
            MQ9values.setText(value5);
            String value6 = dataSnapshot.child("TVOC CCS811 Sensor").getValue().toString();
            TVOCvalues.setText(value6);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Failed to read value
           Toast.makeText(MainActivity.this, "Failed to get data. Error", Toast.LENGTH_SHORT).show();
        }
    });
}

}