package com.example.taam.ui.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.taam.R;
import com.example.taam.ui.FragmentLoader;

public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ImageButton closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        EditText lotText = view.findViewById(R.id.editTextLot);
        EditText nameText = view.findViewById(R.id.editTextName);
        EditText categoryText = view.findViewById(R.id.editTextCategory);
        EditText periodText = view.findViewById(R.id.editTextPeriod);

        Button resultButton = view.findViewById(R.id.buttonResult);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lotNumber = lotText.getText().toString();
                String name = nameText.getText().toString();
                String category = categoryText.getText().toString();
                String period = periodText.getText().toString();
                FragmentLoader.loadFragment(getParentFragmentManager(), new ResultFragment(lotNumber, name, category, period));
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}