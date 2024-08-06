package com.example.taam.database;

import android.util.Log;

public class Item {
    private String lotNumber;
    public String name;
    public String category;
    public String period;
    public String description;
    //missing field for file
    private boolean isSelected;

    public Item(){

    }
    public Item(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public Item(String lotNumber, String name, String category, String period, String description) {
        this.lotNumber = lotNumber;
        this.name = name;
        this.category = category;
        this.period = period;
        this.description = description;
       // image
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected){
        this.isSelected = selected;
    }


    public void setLot(String lotNumber) {
        if (this.lotNumber == null) {
            this.lotNumber = lotNumber;
        } else {
            Log.v("error", "cannot change an initialized lot number");
        }
    }

    public String getLot() {
        return this.lotNumber;
    }

    public String getName() {
        return this.name;
    }

    public String getCategory() {
        return this.category;
    }

    public String getPeriod() {
        return this.period;
    }
}
