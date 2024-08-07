package com.example.taam.database;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.taam.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PeriodSpinner {
    private static ArrayList<String> periodList;
    private static String[] defaultPeriods = {"Xia", "Shang", "Zhou", "Chuanqiu", "Zhanggou", "Qin", "Hang", "Shangou", "Ji", "South and North", "Shui", "Tang", "Liao", "Song", "Jin", "Yuan", "Ming", "Qing", "Modern"};
    private static ArrayAdapter<String> spinnerAdapter;
    private static ArrayList<String> addedPeriods;

    private PeriodSpinner(){}

    public static void getSpinner(Context context, Spinner spinner){
        if(periodList == null){
            insertPeriods();
        }
        setUpAdapter(context, spinner);
    }

    // add None option
    public static void getSearchSpinner(Context context, Spinner spinner){
        ArrayList<String> newList = new ArrayList<>();
        newList.add("None");
        insertPeriods();
        newList.addAll(periodList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, newList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private static void insertPeriods(){
        // insert default and user added periods
        periodList = new ArrayList<>(Arrays.asList(defaultPeriods));
        addedPeriods = new ArrayList<>();
        DataModel.loadNewCategoriesOrPeriods("newPeriods", periodList, addedPeriods);
    }

    public static void removeUserAddedPeriod(String period){
        periodList.remove(period);
        addedPeriods.remove(period);
        spinnerAdapter.notifyDataSetChanged();
    }

    private static void setUpAdapter(Context context, Spinner spinner){
        spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, periodList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    public static void addPeriod(Context context, String period, Spinner spinner){
        if(periodList.contains(period)){
            Toast.makeText(context, "This period already exists", Toast.LENGTH_SHORT).show();
            return;
        }
        periodList.add(period);
        DataModel.storeNewCategoryOrPeriod("newPeriods", period);

        // set the spinner to the new added period
        spinnerAdapter.notifyDataSetChanged();
        int position = spinnerAdapter.getPosition(period);
        spinner.setSelection(position);
    }

    // check whether the given period is a default one (cannot remove default period)
    public static boolean isUserAddedPeriod(String period){
        return addedPeriods.contains(period);
    }

}
