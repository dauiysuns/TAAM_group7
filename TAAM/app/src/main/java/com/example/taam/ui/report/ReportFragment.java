package com.example.taam.ui.report;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.taam.ui.report.Handler.generator.ReportForAllFields;

public class ReportFragment extends Fragment implements PDFHandler.PDFCallback, PermissionHandler.PermissionCallback, SpinnerFragment.SpinnerCallback {
    private EditText byItemText;
    private PDFHandler pdfHandler;
    private PermissionHandler permissionHandler;

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

        new SpinnerFragment(requireContext(), spinner, this).setupSpinner();

        generate.setOnClickListener(v -> {
            if (byItemText.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Please enter an input", Toast.LENGTH_SHORT).show();
            } else {
                permissionHandler.handlePermissions();
            }
        });

        cancel.setOnClickListener(v -> FragmentLoader.loadFragment(getParentFragmentManager(), new AdminHomeFragment()));
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

        ReportForAllFields generator = new ReportForAllFields();
        generator.setDownloadCompleteListener(pdfHandler);

        pdfHandler.generatePdf(category, byItemText.getText().toString(), generator);
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
        //TODO: Handle item selection
    }
}



