package com.example.taam.database;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.taam.R;

import java.util.ArrayList;
import java.util.Arrays;

public class CategorySpinner {
    private static ArrayList<String> categoryList;
    private static String[] defaultCategories = {"Jade", "Paintings", "Calligraphy", "Rubbings", "Bronze", "Brass and Copper", "Gold and Silvers", "Lacquer", "Enamels"};
    private static ArrayAdapter<String> spinnerAdapter;

    private CategorySpinner(){}

    public static Spinner getSpinner(Context context, Spinner spinner){
        // if we are setting up the Spinner for the first time, fill it with default categories
        if(categoryList == null){
            insertDefaultCategories();
        }
        setUpAdapter(context, spinner);
        return spinner;
    }

    private static void insertDefaultCategories(){
        categoryList = new ArrayList<>(Arrays.asList(defaultCategories));
    }

    private static void setUpAdapter(Context context, Spinner spinner){
        spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoryList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    public static void addCategory(String category){
        categoryList.add(category);
        spinnerAdapter.notifyDataSetChanged();
    }

    // check whether the given category is a default one (cannot remove default categories)
    public static boolean isDefaultCategory(String category){
        for(String current : defaultCategories){
            if(current.equals(category)){
                return true;
            }
        }
        return false;
    }

}


