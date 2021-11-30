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
        this.Alcohol = al;
        this.Ammonia = am;
        this.Carbon_Dioxide = cd;
        this.Carbon_Monoxide = cm;
        this.Liquefied_Petroleum_Gas = lpg;
        this.Methane = m;
        this.Total_Volatile_Organic_Compound = t;
    }
}