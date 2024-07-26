package com.example.taam.ui.remove;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.taam.database.Item;
import com.example.taam.database.DataModel;
import com.example.taam.ui.home.BaseHomeFragment;

import java.util.ArrayList;
import java.util.List;

public class RemoveDialogFragment extends DialogFragment{
    private List<Item> selected;
    private BaseHomeFragment homeFragment;


    public RemoveDialogFragment(ArrayList<Item> selected, BaseHomeFragment homeFragment){
        this.selected = selected;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Remove confirmation")
                .setMessage("Are you sure you want to remove the selected items?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Item item: selected) {
                            DataModel.removeItem(item);
                        }
                        homeFragment.reset();
                        Toast.makeText(getContext(), "Item(s) removed successfully.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
