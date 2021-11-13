package com.example.ceon390_projectgroup;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private Object Integer;


    public interface SensorsTable {
        String DATABASE_NAME = "database_log";
        //Table Names
        String TABLE_NAME_1 = "CO2";
        String TABLE_NAME_2 = "Ammonia";
        String TABLE_NAME_3 = "Smoke";
        String TABLE_NAME_4 = "Methane";
        String TABLE_NAME_5 = "Hydrogen";
        String TABLE_NAME_6 = "Carbon Monoxide (CO)";
        String TABLE_NAME_7 = "TVOC";
        //Column Names
        String COLUMN_KEY_ID = "ID";
        String COLUMN_VALUE = "Value";
        String COLUMN_TIMESTAMP = "Timestamp";
    }
    
    public DatabaseHelper(@Nullable Context context) {
        super(context, SensorsTable.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a table
        String CREATE_TABLE1;  //CO2 Sensor
        CREATE_TABLE1 = "CREATE TABLE " + SensorsTable.TABLE_NAME_1 + " ("
                + SensorsTable.COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SensorsTable.COLUMN_VALUE + " TEXT," + SensorsTable.COLUMN_TIMESTAMP + " TEXT" + ")";
        db.execSQL(CREATE_TABLE1);

        String CREATE_TABLE2;  //Ammonia Sensor
        CREATE_TABLE2 = "CREATE TABLE " + SensorsTable.TABLE_NAME_2 + " ("
                + SensorsTable.COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SensorsTable.COLUMN_VALUE + " TEXT," + SensorsTable.COLUMN_TIMESTAMP + " TEXT" + ")";
        db.execSQL(CREATE_TABLE2);

        String CREATE_TABLE3;  //Smoke Sensor
        CREATE_TABLE3 = "CREATE TABLE " + SensorsTable.TABLE_NAME_3 + " ("
                + SensorsTable.COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SensorsTable.COLUMN_VALUE + " TEXT," + SensorsTable.COLUMN_TIMESTAMP + " TEXT" + ")";
        db.execSQL(CREATE_TABLE3);

        String CREATE_TABLE4;  //Methane Sensor
        CREATE_TABLE4 = "CREATE TABLE " + SensorsTable.TABLE_NAME_4 + " ("
                + SensorsTable.COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SensorsTable.COLUMN_VALUE + " TEXT," + SensorsTable.COLUMN_TIMESTAMP + " TEXT" + ")";
        db.execSQL(CREATE_TABLE4);

        String CREATE_TABLE5;  //Hydrogen Sensor
        CREATE_TABLE5 = "CREATE TABLE " + SensorsTable.TABLE_NAME_5 + " ("
                + SensorsTable.COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SensorsTable.COLUMN_VALUE + " TEXT," + SensorsTable.COLUMN_TIMESTAMP+" TEXT" + ")";
        db.execSQL(CREATE_TABLE5);

        String CREATE_TABLE6;  //Carbon Monoxide Sensor
        CREATE_TABLE6 = "CREATE TABLE " + SensorsTable.TABLE_NAME_6 + " ("
                + SensorsTable.COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SensorsTable.COLUMN_VALUE + " TEXT," + SensorsTable.COLUMN_TIMESTAMP+" TEXT " + ")";
        db.execSQL(CREATE_TABLE6);

        String CREATE_TABLE7;  //TVOC Sensor
        CREATE_TABLE7 = "CREATE TABLE " + SensorsTable.TABLE_NAME_7 + " ("
                + SensorsTable.COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SensorsTable.COLUMN_VALUE + " TEXT," + SensorsTable.COLUMN_TIMESTAMP+" TEXT " + ")";
        db.execSQL(CREATE_TABLE7);
        Log.d(TAG, "db created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    //Drop older table
        db.execSQL("DROP TABLE IF EXISTS " + SensorsTable.TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + SensorsTable.TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXISTS " + SensorsTable.TABLE_NAME_3);
        db.execSQL("DROP TABLE IF EXISTS " + SensorsTable.TABLE_NAME_4);
        db.execSQL("DROP TABLE IF EXISTS " + SensorsTable.TABLE_NAME_5);
        db.execSQL("DROP TABLE IF EXISTS " + SensorsTable.TABLE_NAME_6);
        db.execSQL("DROP TABLE IF EXISTS " + SensorsTable.TABLE_NAME_7);
        onCreate(db);
    }

    //Write into table
    public long insertSensors(Sensors sensors) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SensorsTable.COLUMN_VALUE, sensors.getValue());
        contentValues.put(SensorsTable.COLUMN_TIMESTAMP, sensors.getTimestamp());

        try {
            id = db.insertOrThrow(SensorsTable.TABLE_NAME_1, null, contentValues);  //CO2 Sensor
            db.insertOrThrow(SensorsTable.TABLE_NAME_2, null, contentValues);  //Ammonia Sensor
            db.insertOrThrow(SensorsTable.TABLE_NAME_3, null, contentValues);  //Smoke Sensor
            db.insertOrThrow(SensorsTable.TABLE_NAME_4, null, contentValues);  //Methane Sensor
            db.insertOrThrow(SensorsTable.TABLE_NAME_5, null, contentValues);  //Hydrogen Sensor
            db.insertOrThrow(SensorsTable.TABLE_NAME_6, null, contentValues);  //Carbon Monoxide (CO) Sensor
            db.insertOrThrow(SensorsTable.TABLE_NAME_7, null, contentValues);  //TVOC Sensor
        } catch (SQLException e) {
            Toast.makeText(context, "Operation failed: "+e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return id;
    }

    //Get all values for CO2 Sensor
    public List<Sensors> getAllValues1() {
        List<Sensors> sensorList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SensorsTable.TABLE_NAME_1;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Sensors value1 = new Sensors();
                value1.setID(cursor.getInt(0));
                value1.setValue(cursor.getString(1));
                value1.setTimestamp(cursor.getString(2));

                String values1 = cursor.getString(1) + "\n" + cursor.getString(2);
                sensorList.add(value1);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return sensorList;
    }

    //Get all values for Ammonia Sensor
    public List<Sensors> getAllValues2() {
        List<Sensors> sensorList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SensorsTable.TABLE_NAME_2;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Sensors value2 = new Sensors();
                value2.setID(cursor.getInt(0));
                value2.setValue(cursor.getString(1));
                value2.setTimestamp(cursor.getString(2));

                String values2 = cursor.getString(1) + "\n" + cursor.getString(2);
                sensorList.add(value2);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return sensorList;
    }

    //Get all values for Smoke Sensor
    public List<Sensors> getAllValues3() {
        List<Sensors> sensorList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SensorsTable.TABLE_NAME_3;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Sensors value3 = new Sensors();
                value3.setID(cursor.getInt(0));
                value3.setValue(cursor.getString(1));
                value3.setTimestamp(cursor.getString(2));

                String values3 = cursor.getString(1) + "\n" + cursor.getString(2);
                sensorList.add(value3);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return sensorList;
    }

    //Get all values for Methane Sensor
    public List<Sensors> getAllValues4() {
        List<Sensors> sensorList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SensorsTable.TABLE_NAME_4;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Sensors value4 = new Sensors();
                value4.setID(cursor.getInt(0));
                value4.setValue(cursor.getString(1));
                value4.setTimestamp(cursor.getString(2));

                String values4 = cursor.getString(1) + "\n" + cursor.getString(2);
                sensorList.add(value4);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return sensorList;
    }

    //Get all values for Hydrogen Sensor
    public List<Sensors> getAllValues5() {
        List<Sensors> sensorList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SensorsTable.TABLE_NAME_5;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Sensors value5 = new Sensors();
                value5.setID(cursor.getInt(0));
                value5.setValue(cursor.getString(1));
                value5.setTimestamp(cursor.getString(2));

                String values5 = cursor.getString(1) + "\n" + cursor.getString(2);
                sensorList.add(value5);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return sensorList;
    }

    //Get all values for Carbon Monoxide Sensor
    public List<Sensors> getAllValues6() {
        List<Sensors> sensorList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SensorsTable.TABLE_NAME_6;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Sensors value6 = new Sensors();
                value6.setID(cursor.getInt(0));
                value6.setValue(cursor.getString(1));
                value6.setTimestamp(cursor.getString(2));

                String values6 = cursor.getString(1) + "\n" + cursor.getString(2);
                sensorList.add(value6);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return sensorList;
    }

    //Get all values for TVOC Sensor
    public List<Sensors> getAllValues7() {
        List<Sensors> sensorList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SensorsTable.TABLE_NAME_7;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Sensors value7 = new Sensors();
                value7.setID(cursor.getInt(0));
                value7.setValue(cursor.getString(1));
                value7.setTimestamp(cursor.getString(2));

                String values7 = cursor.getString(1) + "\n" + cursor.getString(2);
                sensorList.add(value7);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return sensorList;
    }

}
