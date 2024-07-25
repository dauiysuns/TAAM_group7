package com.example.taam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class RemoveFragment {
    private ItemAdapter itemAdapter;
    private DataModel dm;
    private Context context;
    private List<Item> itemList;

    public RemoveFragment(DataModel dm, Context context,ItemAdapter itemAdapter){
        this.itemAdapter = itemAdapter;
        this.context = context;
        this.dm = dm;
        this.itemList = itemAdapter.itemList;
    }

    public void RemoveItem() {
        //View view = inflater.inflate(R.layout.fragment_remove, container, false);

        String strMessage = "Are you sure you want to remove the selected items";
        AlertDialog.Builder builder = getBuilder(context, strMessage);

        AlertDialog dialog = builder.create();

//        TextView textView = view.findViewById(R.id.context);
//        textView.setText(strMessage);

//        view.findViewById(R.id.yes_button).setOnClickListener(v -> removeItem());
//        view.findViewById(R.id.cancel_button).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private AlertDialog.Builder getBuilder(Context context, String strMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setView(view);
//        builder.setCancelable(false);
        builder.setMessage(strMessage);

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItem();
                itemList.clear();
                itemAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }

    private void removeItem(){
        int count = 0;
        ArrayList<Item> selected = new ArrayList<>();
        for(Item item : itemList){
            if(item.isSelected()){
                count += 1;
                selected.add(item);
            }
        }
        if (count == 0){
            Toast.makeText(context, "Please first select an item to remove.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            for (Item item: selected) {
                dm.removeItem(item);
            }
        }
    }

}
