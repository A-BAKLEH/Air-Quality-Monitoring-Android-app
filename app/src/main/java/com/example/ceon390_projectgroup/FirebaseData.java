package com.example.ceon390_projectgroup;

public class FirebaseData {

    public String Alcohol;
    public String Ammonia;
    public String Carbon_Dioxide;
    public String Carbon_Monoxide;
    public String Liquefied_Petroleum_Gas;
    public String Methane;
    public String Total_Volatile_Organic_Compound;

    public FirebaseData(String al, String am, String cd, String cm, String lpg, String m, String t){
        this.Alcohol = al + " ppm";
        this.Ammonia = am + " ppm";
        this.Carbon_Dioxide = cd + " ppm";
        this.Carbon_Monoxide = cm + " ppm";
        this.Liquefied_Petroleum_Gas = lpg + " ppm";
        this.Methane = m + " ppm";
        this.Total_Volatile_Organic_Compound = t + " ppm";
    }
}