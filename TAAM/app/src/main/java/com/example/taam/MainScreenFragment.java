package com.example.taam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;


public class MainScreenFragment extends Fragment {
    private RecyclerViewFragment recyclerView = new RecyclerViewFragment();
    private List<Item> itemList = recyclerView.itemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_screen_fragment, container, false);

        Button buttonAdmin = view.findViewById(R.id.buttonAdmin);
        Button buttonView = view.findViewById(R.id.buttonView);
        Button buttonSearch = view.findViewById(R.id.buttonSearch);
        buttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loadFragment(new AdminFragment());
            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOneBoxSelected();
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loadFragment(new SearchFragment());
            }
        });

        loadRecyclerViewFragment(recyclerView);
        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadRecyclerViewFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.recycler_view_container, fragment);
        transaction.commit();
    }

    private void checkOneBoxSelected(){
        int count = 0;
        Item selected = null;
        for(Item item : itemList){
            if(item.isSelected()){
                count++;
                selected = item;
            }
            if(count > 1){
                Toast.makeText(getContext(), "More than 1 check box is selected.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(count == 1){
            loadFragment(new ViewFragment(selected.getLot()));
        }
        else{
            Toast.makeText(getContext(), "No check boxes are selected", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
