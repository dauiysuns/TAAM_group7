package com.example.taam.ui.report;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.taam.R;
import com.example.taam.database.DataModel;
import com.example.taam.database.DataView;
import com.example.taam.database.Item;
import com.example.taam.ui.FragmentLoader;
import com.example.taam.ui.home.AdminHomeFragment;
import com.example.taam.ui.report.generator.Reports;
import com.example.taam.ui.report.generator.PDFGenerator;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import android.Manifest;
import android.content.Intent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Objects;

public class ReportFragment extends Fragment implements DataView{
    private String pdfPath;
    private HashMap<String, PDFGenerator> generatorHashMap = new HashMap<String, PDFGenerator>() {{
        put("Category", new Reports());
        put("Period", new Reports());
        put("Lot number", new Reports());
        put("Name", new Reports());
        put("Category with Description and Picture only", new Reports());
        put("Period with Description and Picture only", new Reports());
        put("All reports", new Reports());
    }};;
    private Document document;
    PDFGenerator generator;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        Button generate = view.findViewById(R.id.buttonGenerate);

        Spinner spinner = view.findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.report_options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Initialize the permission launcher
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission granted
                        generatePdf();
                    } else {
                        // Permission denied
                        Log.v("Permission", "Storage permission denied");
                    }
                }
        );

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPreference = requireContext().getSharedPreferences("spinner", Context.MODE_PRIVATE);
                sharedPreference.edit().putString("selected", parent.getItemAtPosition(position).toString()).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        generate.setOnClickListener(v -> {
            generatorHashMap.clear();
            generatorHashMap = new HashMap<String, PDFGenerator>() {{
                put("Category", new Reports());
                put("Period", new Reports());
                put("Lot number", new Reports());
                put("Name", new Reports());
                put("Category with Description and Picture only", new Reports());
                put("Period with Description and Picture only", new Reports());
                put("All reports", new Reports());
                }};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // Permission granted
                    generatePdf();
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                }
            } else {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                else {
                    // Permission already granted
                    generatePdf();
                }
            }
        });

        return view;
    }

    public void generatePdf() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("spinner", Context.MODE_PRIVATE);
        String category = sharedPreferences.getString("selected", "error");
        generator = generatorHashMap.get(category);

        EditText byItemText = requireView().findViewById(R.id.editTextNumberLot);

        pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/TAAMReport.pdf";
        File file = new File(pdfPath);

        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(writer);
            this.document = new Document(pdfDoc);
            DataModel dm = new DataModel(this);

            generator.generate(document);
            if (category.equals("Category with Description and Picture only"))
                dm.getItemsByCategory("category", byItemText.getText().toString());
            if (category.equals("Period with Description and Picture only"))
                dm.getItemsByCategory("period", byItemText.getText().toString());
            else
                dm.getItemsByCategory((category.toLowerCase()), byItemText.getText().toString());

            Log.v("Generate Report", "Successful");
        } catch (FileNotFoundException e) {
            Log.v("error", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void viewPdf() {
        File file = new File(pdfPath);
        Uri pdfUri = FileProvider.getUriForFile(requireContext(),
                requireContext().getPackageName() + ".provider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent chooser = Intent.createChooser(intent, "Open PDF");
        startActivity(chooser);
    }

    @Override
    public void updateView(Item item) {
        generator.populate(item);
    }

    @Override
    public void onComplete() {
        generator.applyChanges();
        document.close();
        viewPdf();
    }

    @Override
    public void showError(String errorMessage) {

    }
}
