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

// NOT COMPLETE!!!
public class CollectionManagementFragment extends Fragment {
    private RecyclerViewFragment recyclerViewFragment = RecyclerViewFragment.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_management, container, false);

        Button buttonAdd = view.findViewById(R.id.buttonAdd);
        Button buttonRemove = view.findViewById(R.id.buttonRemove);
        Button buttonBack = view.findViewById(R.id.buttonBack);

        //buttonAdd.setOnClickListener(v -> loadFragment(new AddItemFragment());
        buttonRemove.setOnClickListener(v -> checkOneItemSelected());
        buttonBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        FragmentLoader.loadRecyclerViewFragment(getParentFragmentManager(), recyclerViewFragment);

        return view;
    }

    private void checkOneItemSelected(){
        int count = 0;
        Item selected = null;
        for(Item item : recyclerViewFragment.itemList){
            if(item.isSelected()){
                count += 1;
                selected = item;
            }
            if(count > 1){
                Toast.makeText(getContext(), "More than 1 item is selected.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(count == 1){
            FragmentLoader.loadFragment(getParentFragmentManager(), new RemoveFragment(selected.getLot()));
        }
        else{
            Toast.makeText(getContext(), "Please first select an item to remove.", Toast.LENGTH_SHORT).show();
        }
    }
}
