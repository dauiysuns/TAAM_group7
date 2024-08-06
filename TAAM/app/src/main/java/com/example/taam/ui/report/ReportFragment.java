package com.example.taam.ui.report;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taam.R;
import com.example.taam.ui.FragmentLoader;
import com.example.taam.ui.home.AdminHomeFragment;
import com.example.taam.ui.report.Handler.PDFHandler;
import com.example.taam.ui.report.Handler.PermissionHandler;
import com.example.taam.ui.report.Handler.generator.PDFGenerator;
import com.example.taam.ui.report.Handler.generator.ReportForAllFields;
import com.example.taam.ui.report.Handler.generator.ReportForDescriptionImage;

public class ReportFragment extends Fragment implements PDFHandler.PDFCallback, PermissionHandler.PermissionCallback, SpinnerFragment.SpinnerCallback {
    private EditText byItemText;
    private PDFHandler pdfHandler;
    private PermissionHandler permissionHandler;
    private Spinner categorySpinner, periodSpinner;
    private TextView textViewForItem;
    private String userInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        setupUI(view);
        setupPermissionLauncher();
        return view;
    }

    private void setupUI(View view) {
        Button generate = view.findViewById(R.id.buttonGenerate);
        Button cancel = view.findViewById(R.id.buttonCancel);
        Spinner spinner = view.findViewById(R.id.spinner4);
        byItemText = view.findViewById(R.id.editTextNumberLot);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        periodSpinner = view.findViewById(R.id.periodSpinner);
        textViewForItem = view.findViewById(R.id.textViewForItem);

        new SpinnerFragment(requireContext(), spinner, this).setupSpinner();

        //category spinner
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(requireContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter1);

        //period spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(requireContext(),
                R.array.periods_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(adapter2);

        setUpSpinnerListener(categorySpinner);
        setUpSpinnerListener(periodSpinner);

        generate.setOnClickListener(v -> {
            if(byItemText.getVisibility() == View.VISIBLE){
                userInput = byItemText.getText().toString();
            }
            if (userInput.equals("")) {
                Toast.makeText(getContext(), "Please enter an input", Toast.LENGTH_SHORT).show();
            }else{
                permissionHandler.handlePermissions();
            }

        });

        cancel.setOnClickListener(v -> FragmentLoader.loadFragment(getParentFragmentManager(), new AdminHomeFragment()));

        // hide the category and period spinners for now
        categorySpinner.setVisibility(View.GONE);
        periodSpinner.setVisibility(View.GONE);
    }

    private void setUpSpinnerListener(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userInput = spinner.getItemAtPosition(position).toString().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void setupPermissionLauncher() {
        ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                generatePdf();
            } else {
                Log.v("Permission", "Storage permission denied");
            }
        });

        permissionHandler = new PermissionHandler(requireActivity(), requireContext(), requestPermissionLauncher, this);
    }

    private void generatePdf() {
        pdfHandler = new PDFHandler(requireContext(), this);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("spinner", Context.MODE_PRIVATE);
        String category = sharedPreferences.getString("selected", "error");

        PDFGenerator generator;

        if(category.equals("Category with Description and Picture only") || category.equals("Period with Description and Picture only")){
            generator = new ReportForDescriptionImage();
            ((ReportForDescriptionImage) generator).setDownloadCompleteListener(pdfHandler);
        } else{
            generator = new ReportForAllFields();
            ((ReportForAllFields) generator).setDownloadCompleteListener(pdfHandler);
        }
        pdfHandler.generatePdf(category, userInput, generator);
    }

    @Override
    public void onPDFGenerated() {
        pdfHandler.viewPdf();
    }

    @Override
    public void onPermissionGranted() {
        generatePdf();
    }

    @Override
    public void onItemSelected(String selected) {
        if (selected.equals("Category") || selected.equals("Category with Description and Picture only") ){
            categorySpinner.setVisibility(View.VISIBLE);
            textViewForItem.setVisibility(View.VISIBLE);
            byItemText.setVisibility(View.GONE);
            periodSpinner.setVisibility(View.GONE);
            userInput = categorySpinner.getItemAtPosition(0).toString().toLowerCase();
        } else if (selected.equals("Period") || selected.equals("Period with Description and Picture only")) {
            periodSpinner.setVisibility(View.VISIBLE);
            textViewForItem.setVisibility(View.VISIBLE);
            byItemText.setVisibility(View.GONE);
            categorySpinner.setVisibility(View.GONE);
            userInput = periodSpinner.getItemAtPosition(0).toString().toLowerCase();
        } else if (selected.equals("All Items")) {
            byItemText.setVisibility(View.GONE);
            textViewForItem.setVisibility(View.GONE);
            categorySpinner.setVisibility(View.GONE);
            periodSpinner.setVisibility(View.GONE);
        } else{
            byItemText.setText("");
            byItemText.setVisibility(View.VISIBLE);
            textViewForItem.setVisibility(View.VISIBLE);
            categorySpinner.setVisibility(View.GONE);
            periodSpinner.setVisibility(View.GONE);
        }
    }
}



