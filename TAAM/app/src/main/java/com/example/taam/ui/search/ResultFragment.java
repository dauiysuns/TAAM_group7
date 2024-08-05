package com.example.taam.ui.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taam.R;
import com.example.taam.database.DataModel;
import com.example.taam.database.DataView;
import com.example.taam.database.Item;
import com.example.taam.database.ItemAdapter;
import com.example.taam.ui.view.ViewFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements DataView {

    //added by me
    String lotNumber;
    String name;
    String category;
    String period;
    DataModel dm;
    ArrayList<Item> lotMatches = new ArrayList<>();
    ArrayList<Item> nameMatches = new ArrayList<>();
    ArrayList<Item> categoryMatches = new ArrayList<>();
    ArrayList<Item> periodMatches = new ArrayList<>();
    RecyclerView recyclerView;
    ArrayList<Item> itemList = new ArrayList<>();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        Button backButton = view.findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        TextView textViewLot = view.findViewById(R.id.textViewLot);
        textViewLot.setText(String.format("Lot #:%s", lotNumber));
        TextView textViewName = view.findViewById(R.id.textViewName);
        textViewName.setText(String.format("Name: %s", name));
        TextView textViewCategory = view.findViewById(R.id.textViewCategory);
        textViewCategory.setText(String.format("Category: %s", category));
        TextView textViewPeriod = view.findViewById(R.id.textViewPeriod);
        textViewPeriod.setText(String.format("Period: %s", period));

        recyclerView = view.findViewById(R.id.recyclerView);

        Button viewButton = view.findViewById(R.id.buttonView);
        viewButton.setOnClickListener(v -> viewItem());

        dm.displayAllItems();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void updateView(Item item) {
        if (item != null && item.period != null && item.period.equalsIgnoreCase(period)) {
            periodMatches.add(item);
        }
        if (item != null && item.category != null && item.category.equalsIgnoreCase(category)) {
            categoryMatches.add(item);
        }
        if (item != null && item.getLot() != null &&  item.getLot().equalsIgnoreCase(lotNumber)) {
            lotMatches.add(item);
        }
        if (item != null && item.name != null && item.name.equalsIgnoreCase(name)) {
            nameMatches.add(item);
        }
    }

    @Override
    public void showError(String errorMessage) {

    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onComplete() {
        lotMatches.addAll(nameMatches);
        lotMatches.addAll(nameMatches);
        lotMatches.addAll(categoryMatches);
        lotMatches.addAll(periodMatches);
        //ArrayList<Item> itemList = new ArrayList<>();
        for (int i = 0; i < lotMatches.size(); i++) {
            Item item = lotMatches.get(i);
            if (itemList.isEmpty()) {
                itemList.add(item);
            } else {
                boolean duplicate = false;
                for (int j = 0; j < itemList.size(); j++) {
                    if (itemList.get(j).getLot().equals(item.getLot())) {
                        duplicate = true;
                        break;
                    }
                }
                if (!duplicate) {
                    itemList.add(item);
                }
            }
        }

        //display results
        ItemAdapter itemAdapter = new ItemAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);

        DataModel dataModel = new DataModel(this);
        dataModel.displayAllItems();
    }

    protected void viewItem() {
        int count = 0;
        Item selected = null;
        for (Item item : itemList) {
            if (item.isSelected()) {
                count += 1;
                selected = item;
            }
            if (count > 1) {
                Toast.makeText(getContext(), "More than 1 item is selected.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (count == 1) {
            loadFragment(new ViewFragment(selected.getLot()));
        } else {
            Toast.makeText(getContext(), "Please first select an item to view.", Toast.LENGTH_SHORT).show();
        }
    }
}