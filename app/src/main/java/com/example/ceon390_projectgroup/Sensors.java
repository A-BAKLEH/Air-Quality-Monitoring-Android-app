package com.example.ceon390_projectgroup;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Sensors {

    private int ID;
    private String Gas;
    private String Value;
    private String Timestamp;

    public Sensors(int i, String deleted){
    }

    public Sensors(int ID, String gas, String value, String timestamp) {
        this.ID = ID;
        Gas = gas;
        Value = value;
        Timestamp = timestamp;
    }
    public Sensors(String gas, String value, String timestamp) {
        Gas = gas;
        Value = value;
        Timestamp = timestamp;
    }

    public Sensors(String gas, String value) {
        String pattern = "yyyy-MM-dd @ HH:mm:ss";
        SimpleDateFormat dateFormat =   new SimpleDateFormat(pattern);

        Gas = gas;
        Value = value;
        this.Timestamp = dateFormat.format(new Date());
    }

    public Sensors(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getValue() {
        return Value;
    }

    public String getGas() {
        return Gas;
    }

    public void setGas(String gas) {
        Gas = gas;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }
}
