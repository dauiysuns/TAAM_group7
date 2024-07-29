package com.example.taam.database;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class Item {
    private String lotNumber;
    public String name;
    public String category;
    public String period;
    public String description;
    private ArrayList<Media> mediaUrls;
    private boolean isSelected;

    public Item(){
        this.mediaUrls = new ArrayList<>();
    }

    public Item(String lotNumber) {
        this.lotNumber = lotNumber;
        this.mediaUrls = new ArrayList<>();
    }

    public Item(String lotNumber, String name, String category, String period, String description, ArrayList<Media> mediaUrls) {
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

    public void addMediaUrl(Media mediaItem) {
        this.mediaUrls.add(mediaItem);
    }

    public ArrayList<Media> getMediaUrls() {
        return this.mediaUrls;
    }

}
