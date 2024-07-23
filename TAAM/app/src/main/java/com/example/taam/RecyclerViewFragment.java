package com.example.taam;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Singleton design
public final class RecyclerViewFragment extends Fragment implements DataView{

    private static RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    List<Item> itemList;
    private DataModel dm;

    private RecyclerViewFragment(){
        itemList = new ArrayList<>();
    }

    public static RecyclerViewFragment getInstance(){
        return recyclerViewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        dm = new DataModel(this);
        dm.displayAllItems();

        return view;
    }

    @Override
    public void updateView(Item item) {
        itemList.add(item);
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String errorMessage) {
        Log.v("recyclerView", errorMessage);
    }
}
