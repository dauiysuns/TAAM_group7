package com.example.taam.database;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DataModel {
    public static HashMap<String, String> lotToImage = new HashMap<>();
    public static DatabaseReference ref = FirebaseDatabase
            .getInstance("https://taam-cfc94-default-rtdb.firebaseio.com/")
            .getReference();
    DataView view;

    public DataModel(DataView view) {
        this.view = view;
    }

    public void getItemByLot(String lotNumber) {
        ref.child("items/" + lotNumber).orderByPriority().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Item item = snapshot.getValue(Item.class);
                if (item != null) {
                    view.updateView(item);
                } else {
                    view.showError("Item not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.showError(error.getMessage());
            }
        });
    }

    public void getItemsByCategory(String category, String query, android.content.Context context) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (category.equals("lot number")) {
                    DataSnapshot querySnapshot = snapshot.child("items").child(query);
                    if (!querySnapshot.exists()) {
                        Toast.makeText(context, "This lot number does not exist.", Toast.LENGTH_SHORT).show();
                    } else {
                        Item item = querySnapshot.getValue(Item.class);
                        if (item != null) {
                            view.updateView(item);
                            view.onComplete();
                        } else {
                            view.showError("Item not found");
                        }
                    }
                    return;
                }
                DataSnapshot querySnapshot = snapshot.child(category).child(query);
                if (!querySnapshot.exists()) {
                    Toast.makeText(context, "This " + category + " does not exist.", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DataSnapshot lot : querySnapshot.getChildren()) {
                    DataSnapshot itemSnapshot = snapshot.child("items").child(lot.getKey());
                    Item item = itemSnapshot.getValue(Item.class);
                    if (item != null) {
                        view.updateView(item);
                    }
                }
                view.onComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.showError(error.getMessage());
            }
        });
    }

    public void getAllItems() {
        ref.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Item item = dataSnapshot.getValue(Item.class);
                    if (item != null) {
                        view.updateView(item);
                    }
                }
                view.onComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.showError(error.getMessage());
            }
        });
    }

    public static void removeItem(Item item) {
        ref.child("items/" + item.getLot()).removeValue();
        ref.child("name").child(item.name).child(item.getLot()).removeValue();
        ref.child("category").child(item.category).child(item.getLot()).removeValue();
        ref.child("period").child(item.period).child(item.getLot()).removeValue();
    }

    public static void addItem(Item item, android.content.Context context, DataView.AddItemCallback callback) {
        final DatabaseReference itemRef = ref.child("items/" + item.getLot());

        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // check for duplicate item
                if (dataSnapshot.exists()) {
                    Toast.makeText(context, "This Lot Number has been used", Toast.LENGTH_SHORT).show();
                    callback.onComplete(false);
                } else {
                    // No duplicate, add item
                    itemRef.setValue(item);
                    ref.child("name").child(item.name).child(item.getLot()).setValue("null");
                    ref.child("category").child(item.category).child(item.getLot()).setValue("null");
                    ref.child("period").child(item.period).child(item.getLot()).setValue("null");
                    callback.onComplete(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("error", databaseError.getMessage());
            }
        });
    }
}
