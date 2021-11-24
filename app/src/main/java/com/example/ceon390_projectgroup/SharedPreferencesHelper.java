package com.example.ceon390_projectgroup;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferencesHelper(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(this.context.getString(R.string.SharedPreferences), Context.MODE_PRIVATE);
    }

    public String getLocation(){
        return this.sharedPreferences.getString(this.context.getString(R.string.LocationEditText), "");
    }

    public void saveLocation(String location){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(this.context.getString(R.string.LocationEditText), location);
        editor.commit();
    }

    public String getRoom(){
        return this.sharedPreferences.getString(this.context.getString(R.string.RoomEditText), "");
    }

    public void saveRoom(String room){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(this.context.getString(R.string.RoomEditText), room);
        editor.commit();
    }

    public int getInnerSpinnerSelection(){
        return this.sharedPreferences.getInt(this.context.getString(R.string.InnerSpinner),0);
    }

    public String getInnerSpinnerGas(){
        return this.sharedPreferences.getString(this.context.getString(R.string.InnerSpinnerGas),"Alcohol");
    }

    public void saveInnerSpinnerSelection(int sv){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(this.context.getString(R.string.InnerSpinner), sv);
        editor.commit();
    }

    public void saveInnerSpinnerGas(String gas){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(this.context.getString(R.string.InnerSpinnerGas), gas);
        editor.commit();
    }

    public int getMiddleSpinnerSelection(){
        return this.sharedPreferences.getInt(this.context.getString(R.string.MiddleSpinner), 0);
    }

    public String getMiddleSpinnerGas(){
        return this.sharedPreferences.getString(this.context.getString(R.string.MiddleSpinnerGas),"Alcohol");
    }

    public void saveMiddleSpinnerSelection(int sv){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(this.context.getString(R.string.MiddleSpinner), sv);
        editor.commit();
    }

    public void saveMiddleSpinnerGas(String gas){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(this.context.getString(R.string.MiddleSpinnerGas), gas);
        editor.commit();
    }

    public int getOuterSpinnerSelection(){
        return this.sharedPreferences.getInt(this.context.getString(R.string.OuterSpinner), 0);
    }

    public String getOuterSpinnerGas(){
        return this.sharedPreferences.getString(this.context.getString(R.string.OuterSpinnerGas),"Alcohol");
    }

    public void saveOuterSpinnerSelection(int sv){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt(this.context.getString(R.string.OuterSpinner), sv);
        editor.commit();
    }

    public void saveOuterSpinnerGas(String gas){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(this.context.getString(R.string.OuterSpinnerGas), gas);
        editor.commit();
    }
}
