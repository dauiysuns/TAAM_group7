package com.example.taam.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Item {
    private String lotNumber;
    public String name;
    public String category;
    public String period;
    public String description;
    public ArrayList<HashMap<String, String>> mediaUrls;
    private boolean isSelected;
    private boolean isExpanded;

    public Item(){
        this.mediaUrls = new ArrayList<>();
    }

    public Item(String lotNumber) {
        this.lotNumber = lotNumber;
        this.mediaUrls = new ArrayList<>();
    }

    public Item(String lotNumber, String name, String category, String period, String description, ArrayList<HashMap<String, String>> mediaUrls) {
        this.lotNumber = lotNumber;
        this.name = name;
        this.category = category;
        this.period = period;
        this.description = description;
        this.mediaUrls = mediaUrls;
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected){
        this.isSelected = selected;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
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
