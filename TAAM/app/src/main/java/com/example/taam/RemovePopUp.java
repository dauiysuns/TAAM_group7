package com.example.taam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class RemovePopUp {
    private ItemAdapter itemAdapter;
    private DataModel dm;
    private Context context;
    private List<Item> itemList;

    public RemovePopUp(DataModel dm, Context context,ItemAdapter itemAdapter){
        this.itemAdapter = itemAdapter;
        this.context = context;
        this.dm = dm;
        this.itemList = itemAdapter.itemList;
    }

    private void RemoveItem(ArrayList<Item> selected) {
        String strMessage = "Are you sure you want to remove the selected items?";
        AlertDialog.Builder builder = getBuilder(context, strMessage, selected);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private AlertDialog.Builder getBuilder(Context context, String strMessage, ArrayList<Item> selected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(strMessage);

        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (Item item: selected) {
                    dm.removeItem(item);
                }
                itemList.clear();
                itemAdapter.notifyDataSetChanged();
                dm.displayAllItems();
                Toast.makeText(context, "Item(s) removed successfully.", Toast.LENGTH_SHORT).show();
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

    public boolean removeItem(){
        int count = 0;
        ArrayList<Item> selected = new ArrayList<>();
        for(Item item : itemList){
            if(item.isSelected()){
                count += 1;
                selected.add(item);
            }
        }
        if (count == 0){
            Toast.makeText(context, "Please first select an item (or items) to remove.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            RemoveItem(selected);
        }
        return true;
    }
}
