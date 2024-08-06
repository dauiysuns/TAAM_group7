package com.example.taam.database;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.taam.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PeriodSpinner {
    public static ArrayList<String> periodList;

    private PeriodSpinner(){
    }

    // this spinner is used for AddFunction
    public static void setUpEditableSpinner(Context context, Spinner spinner){
        insertDefaultPeriods(context);
        ArrayAdapter<String> spinnerAdapterCat = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, periodList);
        spinnerAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapterCat);
    }

    private static void insertDefaultPeriods(Context context){
        String[] categoriesArray = context.getResources().getStringArray(R.array.periods_array);
        periodList = new ArrayList<>(Arrays.asList(categoriesArray));
    }


}
