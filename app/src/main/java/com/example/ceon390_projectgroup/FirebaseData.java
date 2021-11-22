package com.example.ceon390_projectgroup;

public class FirebaseData {

    public String Ammonia;
    public String Carbon_Dioxide;
    public String Carbon_Monoxide;
    public String Hydrogen;
    public String LPG;
    public String Methane;
    public String TVOC;

    public FirebaseData(String a, String cd, String cm, String h, String l, String m, String t){
        this.Ammonia = a + " ppm";
        this.Carbon_Dioxide = cd + " ppm";
        this.Carbon_Monoxide = cm + " ppm";
        this.Hydrogen = h + " ppm";
        this.LPG = l + " ppm";
        this.Methane = m + " ppm";
        this.TVOC = t + " ppm";
    }
}