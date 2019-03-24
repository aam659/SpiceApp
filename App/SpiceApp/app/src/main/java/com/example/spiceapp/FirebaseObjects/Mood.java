package com.example.spiceapp.FirebaseObjects;

import java.util.ArrayList;

public class Mood {

    private String Name, MealTime;
    private double Distance;
    private Price Price;
    private ArrayList<String> Categories;

    public String getName() {
        return Name;
    }

    public String getMealTime() {
        return MealTime;
    }

    public double getDistance() {
        return Distance;
    }

    public com.example.spiceapp.FirebaseObjects.Price getPrice() {
        return Price;
    }

    public ArrayList<String> getCategories(){
        return Categories;
    }

    public Mood(){

    }




}
