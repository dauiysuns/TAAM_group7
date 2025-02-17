package com.example.taam.ui.search;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.taam.R;
import com.example.taam.database.DataModel;
import com.example.taam.database.Item;
import com.example.taam.ui.home.BaseHomeFragment;

import java.util.ArrayList;

public class ResultFragment extends BaseHomeFragment {

    //added by me
    String lotNumber;
    String name;
    String category;
    String period;
    DataModel dm;
    ArrayList<Item> lotMatches = new ArrayList<>();

    //added by me
    public ResultFragment(String lotNumber, String name, String category, String period) {
        this.lotNumber = lotNumber;
        this.name = name;
        this.category = category;
        this.period = period;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm = new DataModel(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_result;
    }

    @Override
    protected void initializeViews(View view) {
        TextView textViewLot = view.findViewById(R.id.textViewLot);
        textViewLot.setText(String.format("Lot #:%s", lotNumber));
        TextView textViewName = view.findViewById(R.id.textViewName);
        textViewName.setText(String.format("Name: %s", name));
        TextView textViewCategory = view.findViewById(R.id.textViewCategory);
        textViewCategory.setText(String.format("Category: %s", category));
        TextView textViewPeriod = view.findViewById(R.id.textViewPeriod);
        textViewPeriod.setText(String.format("Period: %s", period));

        recyclerView = view.findViewById(R.id.recyclerView);
        buttonBack = view.findViewById(R.id.buttonBack);
        buttonView = view.findViewById(R.id.buttonView);
    }

    @Override
    protected void setupButtonListeners() {
        buttonBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        buttonView.setOnClickListener(v -> viewItem());
    }

    @Override
    public void updateView(Item item) {
        if (item == null) return;

        // Initialize match flag to true
        boolean matchesAllCriteria = lotNumber == null || lotNumber.equals(item.getLot()) || lotNumber.isEmpty();

        // Check each criterion and set flag accordingly
        if (name != null && !item.getName().contains(name)) {
            matchesAllCriteria = false;
        }
        if (category != null && !category.equalsIgnoreCase(item.getCategory()) && !category.isEmpty()) {
            matchesAllCriteria = false;
        }
        if (period != null && !period.equalsIgnoreCase(item.getPeriod()) && !period.isEmpty()) {
            matchesAllCriteria = false;
        }

        // Add item to the list if it matches all criteria
        if (matchesAllCriteria) {
            if (!lotMatches.contains(item)) {
                lotMatches.add(item);
            }
        }
    }

    @Override
    public void showError(String errorMessage) {
        // Handle error
    }

    @Override
    public void onComplete() {
        itemList.clear();
        for (Item item : lotMatches) {
            if (!itemList.contains(item)) {
                itemList.add(item);
            }

        }
        lotMatches.clear();
        itemAdapter.notifyDataSetChanged();
    }
}
