package com.example.taam.database;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.taam.R;

import java.util.ArrayList;
import java.util.Arrays;

public class CategorySpinner {
    private static CategorySpinner categorySpinner;
    public static ArrayList<String> categoryList;
    private static String[] defaultCategories;

    private CategorySpinner(){

    }

    // this spinner is used for AddFunction
    public static void setUpSpinner(Context context, Spinner spinner){
        insertDefaultCategories(context);
        ArrayAdapter<String> spinnerAdapterCat = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoryList);
        spinnerAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapterCat);
    }

    private static void insertDefaultCategories(Context context){
        defaultCategories = context.getResources().getStringArray(R.array.categories_array);
        categoryList = new ArrayList<>(Arrays.asList(defaultCategories));
    }

    // this spinner is used for report, search
    public static void setUpNonEditableSpinner(){

    }


}


