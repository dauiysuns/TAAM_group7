package com.example.taam.ui.home;

import static com.example.taam.ui.FragmentLoader.loadFragment;

import android.view.View;
import android.widget.Toast;

import com.example.taam.database.Item;
import com.example.taam.R;
import com.example.taam.ui.add.AddFunction;
import com.example.taam.ui.FragmentLoader;
import com.example.taam.ui.remove.RemoveDialogFragment;
import com.example.taam.ui.report.ReportFragment;
import com.example.taam.ui.search.SearchFragment;

import java.util.ArrayList;

public class AdminHomeFragment extends BaseHomeFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_admin_home;
    }

    @Override
    protected void initializeViews(View view) {
        buttonView = view.findViewById(R.id.view_button);
        buttonSearch = view.findViewById(R.id.search_button);
        buttonBack = view.findViewById(R.id.back_button);
        buttonAdd = view.findViewById(R.id.add_button);
        buttonRemove = view.findViewById(R.id.remove_button);
        buttonReport = view.findViewById(R.id.report_button);
        recyclerView = view.findViewById(R.id.mediaRecyclerView);
    }

    @Override
    protected void setupButtonListeners() {
        buttonView.setOnClickListener(v -> viewItem());
        buttonBack.setOnClickListener(v -> loadFragment(getParentFragmentManager(), new UserHomeFragment()));
        buttonRemove.setOnClickListener(v -> {
            removeItem();
        });
        buttonSearch.setOnClickListener(v -> FragmentLoader.loadFragment(getParentFragmentManager(), new SearchFragment()));
        buttonAdd.setOnClickListener(v -> loadFragment(getParentFragmentManager(), new AddFunction()));
        buttonReport.setOnClickListener(v -> loadFragment(getParentFragmentManager(), new ReportFragment()));
        // Add listeners for other buttons if needed
    }

    private void removeItem(){
        int count = 0;
        ArrayList<Item> selected = new ArrayList<>();
        for(Item item : itemList){
            if(item.isSelected()){
                count += 1;
                selected.add(item);
            }
        }
        if (count == 0){
            Toast.makeText(getContext(), "Please first select an item (or items) to remove.", Toast.LENGTH_SHORT).show();
        } else {
            RemoveDialogFragment dialogFragment = new RemoveDialogFragment(selected, this);
            dialogFragment.show(getParentFragmentManager(), "remove dialog");
        }
    }
}
