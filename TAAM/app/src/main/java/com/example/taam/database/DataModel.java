package com.example.taam.database;

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
    public static DatabaseReference ref = FirebaseDatabase
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

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
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

    public static void removeItem(Item item) {
        ref.child("items/" + item.getLot()).removeValue();
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
                    Field[] fields = item.getClass().getFields();
                    for (Field field : fields) {
                        try {
                            itemRef.child(field.getName()).setValue(field.get(item));
                            ref.child(field.getName()).child((String) Objects.requireNonNull(field.get(item))).child(item.getLot()).setValue("null");
                        } catch (IllegalAccessException e) {
                            Log.v("error", e.getMessage());
                        }
                    }
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