package com.example.spiceapp.FirebaseObjects;

import java.util.ArrayList;

public class Mood {

    private String name, mealTime;
    private double distance;
    private Price price;
    private ArrayList<String> categories;

    public String getName() {
        return name;
    }

    public String getMealTime() {
        return mealTime;
    }

    public double getDistance() {
        return distance;
    }

    public com.example.spiceapp.FirebaseObjects.Price getPrice() {
        return price;
    }

    public ArrayList<String> getCategories(){
        return categories;
    }

    public Mood(){

    }




}
