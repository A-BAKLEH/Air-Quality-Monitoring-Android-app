package com.example.ceon390_projectgroup;

public class Sensors {

    private int ID;
    private String Value;
    private String Timestamp;

    public Sensors(){
    }
    public Sensors(int ID, String value, String timestamp) {
        this.ID = ID;
        Value = value;
        Timestamp = timestamp;
    }
    public Sensors(String value, String timestamp) {
        Value = value;
        Timestamp = timestamp;
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
