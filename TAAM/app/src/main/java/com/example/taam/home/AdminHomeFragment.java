package com.example.taam.home;

import static com.example.taam.FragmentLoader.loadFragment;

import android.view.View;

import com.example.taam.R;
import com.example.taam.RemovePopUp;

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
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    protected void setupButtonListeners() {
        buttonView.setOnClickListener(v -> viewItem());
        buttonBack.setOnClickListener(v -> loadFragment(getParentFragmentManager(), new UserHomeFragment()));
        buttonRemove.setOnClickListener(v -> {
            RemovePopUp popUp = new RemovePopUp(dm, getContext(), itemAdapter);
            popUp.removeItem();
        });
        // Add listeners for other buttons if needed
    }
}
