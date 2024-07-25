package com.example.taam;

import android.util.Log;

public class Item {
    private String lotNumber;
    public String name;
    public String category;
    public String period;
    public String description;
    //missing field for file
    private Boolean isSelected;

    public Item(
            String lotNumber,
            String name,
            String category,
            String period,
            String description) {
        this.lotNumber = lotNumber;
        this.name = name;
        this.category = category;
        this.period = period;
        this.description = description;
    }

    public Item(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public Item() {

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
}
