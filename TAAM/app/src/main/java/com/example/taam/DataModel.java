package com.example.taam;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.Objects;

public class DataModel {
    public DatabaseReference ref = FirebaseDatabase
            .getInstance("https://taam-cfc94-default-rtdb.firebaseio.com/")
            .getReference();
    DataView view;

    public DataModel() {}
    public DataModel(DataView view) {
        this.view = view;
    }

    public interface DataListener {
        void onDataChange(DataSnapshot snapshot);
        void onError(DatabaseError error);
    }

    public void fetchData(DatabaseReference reference, DataListener listener) {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listener != null) {
                    listener.onDataChange(snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listener != null) {
                    listener.onError(error);
                }
            }
        });
    }

    public void displayItem(String lotNumber){
        fetchData(ref.child("items/" + lotNumber).getRef(), new DataListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Item item = new Item();
                item.setLot(snapshot.getKey());
                Field[] fields = item.getClass().getFields();
                for (Field field : fields) {
                    try {
                        field.set(item, snapshot.child(field.getName()).getValue());
                    } catch (IllegalAccessException e) {
                        Log.v("error", Objects.requireNonNull(e.getMessage()));
                    }
                }
                view.updateView(item);
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                view.showError(error.getMessage());
            }
        });
    }

    public void displayAllItems(){
        fetchData(ref.child("items").getRef(), new DataListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //itemList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Item item = new Item();
                    item.setLot(dataSnapshot.getKey());
                    Field[] fields = item.getClass().getFields();
                    for (Field field : fields) {
                        try {
                            field.set(item, dataSnapshot.child(field.getName()).getValue());
                        } catch (IllegalAccessException e) {
                            Log.v("error", Objects.requireNonNull(e.getMessage()));
                        }
                    }
                    view.updateView(item);
                }
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                view.showError(error.getMessage());
            }
        });
    }

//    public void removeItem(String lotNumber, Context context) {
//        fetchData(ref.child("items/" + lotNumber).getRef(), new DataListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Item item = snapshot.getValue(Item.class);
//                    if (item != null && item.getLot().equals(lotNumber)) {
//                        snapshot.getRef().removeValue().addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                Toast.makeText(context, "Item removed successfully.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(context, "Failed to remove item.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onError(@NonNull DatabaseError error) {
//                Log.v("removeItem", error.getMessage());
//            }
//        });
//    }

    public void removeItem(Item item) {
        ref.child("items/" + item.getLot()).removeValue();
        //displayAllItems();
        //ref.child("items/123/category").setValue("0");
    }

    public void addItem(Item item) {
        Field []fields = item.getClass().getFields();
        for (Field field: fields) {
            try {
                ref.child("items/" + item.getLot() + "/" + field.getName()).setValue(field.get(item));
            } catch (IllegalAccessException e) {
                Log.v("error", e.getMessage());
            }
        }
    }

}