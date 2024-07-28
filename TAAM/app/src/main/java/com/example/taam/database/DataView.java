package com.example.taam.database;

/**
 * Implement when you need to access the database to do some processing
 *
 * Create a new instance of DataModel with your class as an argument then call displayData
 * or displayAllData from DataModel
 *
 * displayData will call updateView with all the values in the database as an argument
 * Implement updateView using the data from items
 *
 * displayAllData will call updateView for all items
 */
public interface DataView {
    void updateView(Item item);
    void showError(String errorMessage);
    //void onComplete();

    public interface checkComplete{
        void onComplete();
    }

    // used for checking whether item added successfully or not
    public interface AddItemCallback {
        void onComplete(boolean success);
    }
}
