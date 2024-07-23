package com.example.taam;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RemoveFragment extends Fragment {
    private String selectedLotNumber;
    private DataModel dm;

    public RemoveFragment(String selectedLotNumber){
        this.selectedLotNumber = selectedLotNumber;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remove, container, false);

        String strMessage = "Are you sure you want to remove the selected item(Lot# " + selectedLotNumber + ")?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        TextView textView = view.findViewById(R.id.context);
        textView.setText(strMessage);

        view.findViewById(R.id.yes_button).setOnClickListener(v -> dm.removeItem(selectedLotNumber, getContext()));
        view.findViewById(R.id.cancel_button).setOnClickListener(v -> dialog.dismiss());

        dialog.show();

        return view;
    }

}
