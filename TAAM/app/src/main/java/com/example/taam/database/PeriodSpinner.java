package com.example.taam.database;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.taam.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PeriodSpinner {
    private static ArrayList<String> periodList;
    private static String[] defaultPeriods = {"Xia", "Shang", "Zhou", "Chuanqiu", "Zhanggou", "Qin", "Hang", "Shangou", "Ji", "South and North", "Shui", "Tang", "Liao", "Song", "Jin", "Yuan", "Ming", "Qing", "Modern"};
    private static ArrayAdapter<String> spinnerAdapter;

    private PeriodSpinner(){}

    public static Spinner getSpinner(Context context, Spinner spinner){
        // if we are setting up the Spinner for the first time, fill it with default periods
        if(periodList == null){
            insertDefaultPeriods();
        }
        setUpAdapter(context, spinner);
        return spinner;
    }

    private static void insertDefaultPeriods(){
        periodList = new ArrayList<>(Arrays.asList(defaultPeriods));
    }

    private static void setUpAdapter(Context context, Spinner spinner){
        spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, periodList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    public static void addPeriod(String period){
        periodList.add(period);
        spinnerAdapter.notifyDataSetChanged();
    }

    // check whether the given period is a default one (cannot remove default period)
    public static boolean isDefaultPeriod(String period){
        for(String current : defaultPeriods){
            if(current.equals(period)){
                return true;
            }
        }
        return false;
    }

}
