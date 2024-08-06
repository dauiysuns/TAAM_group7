package com.example.taam.database;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.taam.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EditableCategorySpinner {
    public static ArrayList<String> categoryList;

    private EditableCategorySpinner(){
    }

    public static void setUpSpinnerAdapter(Context context, Spinner spinner){
        insertDefaultCategories(context);
        Collections.addAll(categoryList, "Another Category");
        ArrayAdapter<String> spinnerAdapterCat = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoryList);
        spinnerAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapterCat);
    }

    private static void insertDefaultCategories(Context context){
        String[] categoriesArray = context.getResources().getStringArray(R.array.categories_array);
        categoryList = new ArrayList<>(Arrays.asList(categoriesArray));
    }
}
