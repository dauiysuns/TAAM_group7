package com.example.taam.database;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.taam.R;

import java.util.ArrayList;
import java.util.Arrays;

public class CategorySpinner {
    private static ArrayList<String> categoryList;
    private static String[] defaultCategories = {"Jade", "Paintings", "Calligraphy", "Rubbings", "Bronze", "Brass and Copper", "Gold and Silvers", "Lacquer", "Enamels"};
    private static ArrayAdapter<String> spinnerAdapter;
    private static ArrayList<String> addedCategories;

    private CategorySpinner(){}

    public static Spinner getSpinner(Context context, Spinner spinner){
        // if we are setting up the Spinner for the first time, fill it with default categories + any user added categories
        if(categoryList == null){
            insertCategories();
        }
        setUpAdapter(context, spinner);
        return spinner;
    }

    private static void insertCategories(){
        // insert default categories
        categoryList = new ArrayList<>(Arrays.asList(defaultCategories));
        // insert user added categories, if any
        addedCategories = new ArrayList<>();
        DataModel.loadNewCategories(categoryList, addedCategories);
    }

    private static void setUpAdapter(Context context, Spinner spinner){
        spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoryList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    public static void addCategory(Context context, String category, Spinner spinner){
        if(categoryList.contains(category)){
            Toast.makeText(context, "This category already exists", Toast.LENGTH_SHORT).show();
            return;
        }
        categoryList.add(category);
        addedCategories.add(category);
        DataModel.storeNewCategory(category);

        // set the spinner to the new added category
        spinnerAdapter.notifyDataSetChanged();
        int position = spinnerAdapter.getPosition(category);
        spinner.setSelection(position);
    }

    // check whether the given category is a new one (cannot remove default categories)
    public static boolean isNewCategory(String category){
        return addedCategories.contains(category); // dont need this method
    }

}


