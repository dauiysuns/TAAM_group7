package com.example.taam;

public class TaamItem {
    private String lotNumber;
    public String name;
    public String category;
    public String period;
    public String description;
    //missing field for file

    public TaamItem(
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

    public TaamItem(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public TaamItem() {

    }

    public void setLot(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getLot() {
        return this.lotNumber;
    }
}
