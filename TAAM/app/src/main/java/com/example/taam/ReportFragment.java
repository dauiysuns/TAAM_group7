package com.example.taam;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ReportFragment extends Fragment {
    private Spinner spinner;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        Button generate = view.findViewById(R.id.buttonGenerate);

        //Spinner spinner = view.findViewById(R.id.spinner4);

//        spinner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new SpinnerFragment());
//            }
//        });

        spinner = view.findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePdf();
            }
        });
        return view;
    }

    public void generatePdf(){
        PdfDocument doc = new PdfDocument();
    }

//    private void loadFragment(Fragment fragment) {
//        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

}
