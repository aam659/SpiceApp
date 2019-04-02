package com.example.spiceapp.FirebaseObjects;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MoodTest {
    private Mood x;
    private String name;
    private String mealTime;
    private double distance;
    private Price price;
    private ArrayList<String> keywords;
    @Before
    public void before(){
        name = "john jacob jingleheimer schmidt";
        mealTime = "Dinner";
        distance = 10;
        price = new Price();
        price.setHighPrice(5);
        price.setLowPrice(3);
        keywords = new ArrayList<>();
        keywords.add("Taco");
        keywords.add("Bar");
        x = new Mood(name,mealTime,distance,price,keywords);
    }

    @Test
    public void getName() {
        assert(x.getName().equals(name));
    }

    @Test
    public void getMealTime() {
        assert(x.getMealTime().equals(mealTime));
    }

    @Test
    public void getDistance() {
        assert(x.getDistance() == distance);
    }

    @Test
    public void getPrice() {
        assertEquals(x.getPrice(),price);
    }

    @Test
    public void getCategories() {
        assertEquals(x.getCategories(),keywords);
    }
}