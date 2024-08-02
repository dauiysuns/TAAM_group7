package com.example.taam.ui.home;

import static com.example.taam.ui.FragmentLoader.loadFragment;

import android.view.View;
import android.widget.Button;

import com.example.taam.R;
import com.example.taam.ui.login.LoginFragmentView;
import com.example.taam.ui.search.SearchFragment;

public class UserHomeFragment extends BaseHomeFragment {
    private Button buttonAdmin;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_home;
    }

    @Override
    protected void initializeViews(View view) {
        buttonView = view.findViewById(R.id.view_button);
        buttonSearch = view.findViewById(R.id.search_button);
        buttonAdmin = view.findViewById(R.id.admin_button);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    @Override
    protected void setupButtonListeners() {
        buttonView.setOnClickListener(v -> viewItem());

        buttonSearch.setOnClickListener(v -> {
            // Implement search functionality
            loadFragment(getParentFragmentManager(), new SearchFragment());
        });

        buttonAdmin.setOnClickListener(v -> loadFragment(getParentFragmentManager(), new LoginFragmentView()));
    }

    @Override
    public void onComplete() {

    }
}
