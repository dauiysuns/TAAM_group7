package com.example.taam.database;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

public class EditableCategorySpinner {
    public static ArrayList<String> categoryList = new ArrayList<>();

    private EditableCategorySpinner(){
        Collections.addAll(categoryList, "jade", "another category");
    }

    public static void setUpSpinnerAdapter(Context context, Spinner spinner){
        ArrayAdapter<String> spinnerAdapterCat = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoryList);
        spinnerAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapterCat);
    }
}
