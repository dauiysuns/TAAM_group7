package com.example.taam.database;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

public class EditablePeriodSpinner {
    public static ArrayList<String> periodList = new ArrayList<>();

    private EditablePeriodSpinner(){
        Collections.addAll(periodList, "xia", "another period");
    }

    public static void setUpSpinnerAdapter(Context context, Spinner spinner){
        ArrayAdapter<String> spinnerAdapterCat = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, periodList);
        spinnerAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapterCat);
    }
}
