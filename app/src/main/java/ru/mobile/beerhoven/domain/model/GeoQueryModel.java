package ru.mobile.beerhoven.domain.model;

import java.util.ArrayList;

public class GeoQueryModel {
    private String g;
    private ArrayList<Double> l;
    
    public GeoQueryModel() {}
    
    public String getG() {
        return g;
    }
    
    public void setG(String g) {
        this.g = g;
    }
    
    public ArrayList<Double> getL() {
        return l;
    }
    
    public void setL(ArrayList<Double> l) {
        this.l = l;
    }
}
