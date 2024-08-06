package com.example.taam.database;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.taam.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EditableCategorySpinner {
    public static ArrayList<String> categoryList;
    private static String[] defaultCategories;

    private EditableCategorySpinner(){
    }

    // this spinner is used for AddFunction
    public static void setUpEditableSpinner(Context context, Spinner spinner){
        insertDefaultCategories(context);
        Collections.addAll(categoryList, "Another Category");
        ArrayAdapter<String> spinnerAdapterCat = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoryList);
        spinnerAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapterCat);
        setUpSpinnerListener(spinner);
    }

    private static void insertDefaultCategories(Context context){
        defaultCategories = context.getResources().getStringArray(R.array.categories_array);
        categoryList = new ArrayList<>(Arrays.asList(defaultCategories));
    }

    public static void setUpSpinnerListener(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item
                String selectedItem = parent.getItemAtPosition(position).toString();

                // Check if "Another Category" is selected
                if (selectedItem.equals("Another Category")) {
                    // Handle the selection of "Another Category"
                    Toast.makeText(context, "Another Category selected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // this spinner is used for report, search
    public static void setUpNonEditableSpinner(){

    }

    // usedfor removing categories, default categories should not be removed
    public static boolean inDefaultCategories(){

    }
}


