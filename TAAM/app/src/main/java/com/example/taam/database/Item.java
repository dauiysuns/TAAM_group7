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
    private ArrayList<String> picUrls;
    private ArrayList<String> vidUrls;
    private boolean isSelected;

    public Item(){
        this.picUrls = new ArrayList<>();
        this.vidUrls = new ArrayList<>();
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
        this.picUrls = new ArrayList<>();
        this.vidUrls = new ArrayList<>();
        this.isSelected = false;
    }

    public Item(String lotNumber, String name, String category, String period, String description, ArrayList<String> picUrls, ArrayList<String> vidUrls) {
        this.lotNumber = lotNumber;
        this.name = name;
        this.category = category;
        this.period = period;
        this.description = description;
        this.picUrls = picUrls;
        this.vidUrls = vidUrls;
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

//    public void addPicUrl(String url) {
//        this.picUrls.add(url);
//    }
//
//    public List<String> getPicUrls() {
//        return this.picUrls;
//    }
//
//    public void addVidUrl(String url) {
//        this.vidUrls.add(url);
//    }
//
//    public List<String> getVidUrls() {
//        return this.vidUrls;
//    }
}
