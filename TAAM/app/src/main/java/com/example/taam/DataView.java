package com.example.taam;

import java.util.ArrayList;

/**
 * Implement when you need to access the database to do some processing
 * Create a new instance of DataModel with your class as an argument then call displayData from
 * DataModel
 * displayData will call updateView with all the values in the database as an argument
 * Implement updateView using the data from items
 */
public interface DataView {
    void updateView(ArrayList<TaamItem> items);
    void showError(String errorMessage);
}
