package com.example.taam.database;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.taam.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EditablePeriodSpinner {
    public static ArrayList<String> periodList;

    private EditablePeriodSpinner(){
    }

    public static void setUpSpinnerAdapter(Context context, Spinner spinner){
        insertDefaultPeriods(context);
        Collections.addAll(periodList, "Another Period");
        ArrayAdapter<String> spinnerAdapterCat = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, periodList);
        spinnerAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapterCat);
    }

    private static void insertDefaultPeriods(Context context){
        String[] categoriesArray = context.getResources().getStringArray(R.array.periods_array);
        periodList = new ArrayList<>(Arrays.asList(categoriesArray));
    }
}
