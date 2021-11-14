package com.example.ceon390_projectgroup;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;


    public interface SensorsTable {
        String DATABASE_NAME = "database_log";
        String TABLE_NAME = "Sensors";
        //Column Names
        String COLUMN_KEY_ID = "ID";
        String COLUMN_GAS = "Gas Name";
        String COLUMN_VALUE = "Value";
        String COLUMN_TIMESTAMP = "Timestamp";
    }
    
    public DatabaseHelper(Context context) {
        super(context, SensorsTable.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a table
        String CREATE_TABLE;
        CREATE_TABLE = "CREATE TABLE " + SensorsTable.TABLE_NAME + " ("
                + SensorsTable.COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SensorsTable.COLUMN_GAS + " ," + SensorsTable.COLUMN_VALUE +
                " TEXT," + SensorsTable.COLUMN_TIMESTAMP + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);

        Log.d(TAG, "db created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //Drop older table
        db.execSQL("DROP TABLE IF EXISTS " + SensorsTable.TABLE_NAME);
        onCreate(db);
    }

    //Write into table
    public long insertSensors(Sensors sensors) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SensorsTable.COLUMN_GAS, sensors.getGas());
        contentValues.put(SensorsTable.COLUMN_VALUE, sensors.getValue());
        contentValues.put(SensorsTable.COLUMN_TIMESTAMP, sensors.getTimestamp());

        try {
            id = db.insertOrThrow(SensorsTable.TABLE_NAME, null, contentValues);
        } catch (SQLException e) {
            Toast.makeText(context, "Operation failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return id;
    }

    public List<Sensors> getAllValues() {
        List<Sensors> sensorList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SensorsTable.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Sensors value = new Sensors();
                value.setID(cursor.getInt(0));
                value.setGas(cursor.getString(1));
                value.setValue(cursor.getDouble(2));
                value.setTimestamp(cursor.getString(3));

                String values = cursor.getString(1) +"\n" +
                        cursor.getDouble(2) + "\n" + cursor.getString(3);
                MainActivity.SensorList.add(values);
                sensorList.add(value);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return sensorList;
    }
}
