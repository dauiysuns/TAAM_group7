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

public class MainScreenFragment extends Fragment {
    private RecyclerViewFragment recyclerViewFragment = RecyclerViewFragment.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_screen_fragment, container, false);

        Button buttonAdmin = view.findViewById(R.id.buttonAdmin);
        Button buttonView = view.findViewById(R.id.buttonView);
        Button buttonSearch = view.findViewById(R.id.buttonSearch);
        //buttonAdmin.setOnClickListener(v -> loadFragment(new AdminFragment());
        buttonAdmin.setOnClickListener(v -> FragmentLoader.loadFragment(getParentFragmentManager(), new CollectionManagementFragment()));

        buttonView.setOnClickListener(v -> checkOneBoxSelected());

        //buttonSearch.setOnClickListener(v -> loadFragment(new SearchFragment());

        FragmentLoader.loadRecyclerViewFragment(getParentFragmentManager(), recyclerViewFragment);
        return view;
    }

    private void checkOneBoxSelected(){
        int count = 0;
        Item selected = null;
        for(Item item : recyclerViewFragment.itemList){
            if(item.isSelected()){
                count += 1;
                selected = item;
            }
            if(count > 1){
                Toast.makeText(getContext(), "More than 1 check box is selected.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(count == 1){
            FragmentLoader.loadFragment(getParentFragmentManager(), new ViewFragment(selected.getLot()));
        }
        else{
            Toast.makeText(getContext(), "No check boxes are selected", Toast.LENGTH_SHORT).show();
        }
    }
}
