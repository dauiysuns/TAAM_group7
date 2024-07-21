package com.example.taam;

public class TaamItem {
    int lotNumber;
    String name;
    String category;
    String period;
    String description;
    //missing field for file

    public TaamItem(
            int lotNumber,
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

    public TaamItem(int lotNumber) {
        this.lotNumber = lotNumber;
    }

    private TaamItem() {

    }
}
