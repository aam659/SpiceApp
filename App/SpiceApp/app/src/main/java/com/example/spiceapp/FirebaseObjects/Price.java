package com.example.spiceapp.FirebaseObjects;

//Class to hold price object retrieved from firebase, each var has their own getter and setter
public class Price {

    private int HighPrice;
    private int LowPrice;

    public int getHighPrice() {
        return HighPrice;
    }

    public void setHighPrice(int highPrice) {
        HighPrice = highPrice;
    }

    public int getLowPrice() {
        return LowPrice;
    }

    public void setLowPrice(int lowPrice) {
        LowPrice = lowPrice;
    }





    public Price(){

    }

}
