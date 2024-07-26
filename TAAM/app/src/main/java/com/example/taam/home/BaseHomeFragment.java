package com.example.taam.home;

import static com.example.taam.FragmentLoader.loadFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taam.DataModel;
import com.example.taam.DataView;
import com.example.taam.Item;
import com.example.taam.ItemAdapter;
import com.example.taam.ViewFragment;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseHomeFragment extends Fragment implements DataView {

    protected Button buttonView, buttonSearch, buttonBack, buttonAdd, buttonRemove, buttonReport;
    protected RecyclerView recyclerView;
    public ItemAdapter itemAdapter;
    public List<Item> itemList;
    public DataModel dm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*
        BaseHomeFragment's onCreateView is called when we go to Admin or User fragments?
        But initializeViews and setupButtonListeners are called from the child fragments?
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);

        // setting up recycler view
        dm = new DataModel(this);
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);

        // initialize buttons and RecyclerView
        initializeViews(view);

        // set up button listeners
        setupButtonListeners();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(itemAdapter);

        dm.displayAllItems();

        // For demo purposes, adding items

        return view;
    }

    protected abstract int getLayoutId();
    protected abstract void initializeViews(View view);
    protected abstract void setupButtonListeners();

    @Override
    public void updateView(Item item) {
        itemList.add(item);
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String errorMessage) {
        Log.v("Main Screen", errorMessage);
    }

    public void reset() {
        itemList.clear();
        itemAdapter.notifyDataSetChanged();
        dm.displayAllItems();
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
            loadFragment(getParentFragmentManager(), new ViewFragment(selected.getLot()));
        } else {
            Toast.makeText(getContext(), "Please first select an item to view.", Toast.LENGTH_SHORT).show();
        }
    }
}