package com.example.ceon390_projectgroup;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    public static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a table
        String CREATE_TABLE = "CREATE TABLE " + Config.TABLE_NAME + " ("
                + Config.COLUMN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Config.COLUMN_GAS + " TEXT NOT NULL ," + Config.COLUMN_VALUE +
                " TEXT NOT NULL," + Config.COLUMN_TIMESTAMP + " TEXT NOT NULL)";
        Log.d(TAG, CREATE_TABLE);

        db.execSQL(CREATE_TABLE);

        Log.d(TAG, "db created!");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //Write into table
    public long insertSensors(Sensors sensors) {
        long id = -1;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_GAS, sensors.getGas());
        contentValues.put(Config.COLUMN_VALUE, sensors.getValue());
        contentValues.put(Config.COLUMN_TIMESTAMP, sensors.getTimestamp());

        try {
            id = db.insertOrThrow(Config.TABLE_NAME, null, contentValues);
        } catch (SQLException e) {
            Log.d(TAG, "EXCEPTION: " +e);
            Toast.makeText(context, "Operation failed: "+e, Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return id;
    }

    public List<Sensors> getFilterValues(String gas){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(Config.TABLE_NAME,null,Config.COLUMN_GAS +"= ?",new String[]{gas},
                    null, null, null);

            if(cursor != null) {
                if (cursor.moveToFirst()) {
                    List<Sensors> sensorsList = new ArrayList<>();

                    do {
                        @SuppressLint("Range") int ID =
                                cursor.getInt(cursor.getColumnIndex(Config.COLUMN_KEY_ID));
                        @SuppressLint("Range") String name =
                                cursor.getString(cursor.getColumnIndex(Config.COLUMN_GAS));
                        @SuppressLint("Range") String value =
                                cursor.getString(cursor.getColumnIndex(Config.COLUMN_VALUE));
                        @SuppressLint("Range") String timestamp =
                                cursor.getString(cursor.getColumnIndex(Config.COLUMN_TIMESTAMP));

                        sensorsList.add(new Sensors(ID,name, value, timestamp));
                    } while (cursor.moveToNext());
                    return sensorsList;
                }
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "EXCEPTION: " +e);
            Toast.makeText(context, "Operation Failed!: " +e, Toast.LENGTH_LONG).show();
        } finally {
            if(cursor == null) {
                cursor.close();
            }
            db.close();
        }
        return Collections.emptyList();

    }

    public List<Sensors> getAllValues() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(Config.TABLE_NAME,null,null,null,null, null, null);
            if(cursor != null) {
                if (cursor.moveToFirst()) {
                    List<Sensors> sensorsList = new ArrayList<>();

                    do {
                        @SuppressLint("Range") int ID =
                                cursor.getInt(cursor.getColumnIndex(Config.COLUMN_KEY_ID));
                        @SuppressLint("Range") String name =
                                cursor.getString(cursor.getColumnIndex(Config.COLUMN_GAS));
                        @SuppressLint("Range") String value =
                                cursor.getString(cursor.getColumnIndex(Config.COLUMN_VALUE));
                        @SuppressLint("Range") String timestamp =
                                cursor.getString(cursor.getColumnIndex(Config.COLUMN_TIMESTAMP));

                                sensorsList.add(new Sensors(ID,name, value, timestamp));
                    } while (cursor.moveToNext());
                    return sensorsList;
                }
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "EXCEPTION: " +e);
            Toast.makeText(context, "Operation Failed!: " +e, Toast.LENGTH_LONG).show();
        } finally {
            if(cursor == null) {
                cursor.close();
            }
            db.close();
        }
        return Collections.emptyList();
    }

    public long deleteSensors(String delID) {

        Sensors sensors = new Sensors(Integer.parseInt(delID), "deleted");

        long id = -1;

        SQLiteDatabase db = this.getWritableDatabase();

        try{
            id = db.delete(Config.TABLE_NAME,Config.COLUMN_KEY_ID+" = ?",new String[]{delID});
        }
        catch (SQLiteException e){
            Log.d(TAG, "EXCEPTION: "+ e);
            Toast.makeText(context, "Operation Failed!: "+ e, Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
        }
        return id;
    }
}
