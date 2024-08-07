package com.example.taam.ui.report;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.taam.R;

public class SpinnerFragment {
    public interface SpinnerCallback{
        public void onItemSelected(String selected);
    }

    Context context;
    Spinner spinner;
    SpinnerCallback spinnerCallback;

    public SpinnerFragment(Context context, Spinner spinner, SpinnerCallback spinnerCallback) {
        this.context = context;
        this.spinner = spinner;
        this.spinnerCallback = spinnerCallback;
    }

    public void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.report_options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sharedPreference = context.getSharedPreferences("spinner", Context.MODE_PRIVATE);
                String selected = parent.getItemAtPosition(position).toString();
                sharedPreference.edit().putString("selected", selected).apply();
                spinnerCallback.onItemSelected(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }
}
