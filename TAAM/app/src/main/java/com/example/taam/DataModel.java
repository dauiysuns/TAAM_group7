package com.example.taam;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class DataModel {
    private final DatabaseReference ref = FirebaseDatabase
            .getInstance("https://taam-cfc94-default-rtdb.firebaseio.com/")
            .getReference();
    DataView view;

    public DataModel(DataView view) {
        this.view = view;
    }

    public void fetchData(DatabaseReference reference) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<TaamItem> items = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    TaamItem item = new TaamItem();
                    item.setLot(dataSnapshot.getKey());
                    Field[] fields = item.getClass().getFields();
                    for (Field field : fields) {
                        try {
                            field.set(item, dataSnapshot.child(field.getName()).getValue());
                        } catch (IllegalAccessException e) {
                            Log.v("error", Objects.requireNonNull(e.getMessage()));
                        }
                    }
                    items.add(item);
                }

                view.updateView(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.showError(error.getMessage());
            }
        });
    }

    public void displayItem(){
        fetchData(ref.child("items").getRef());
    }

    public void displayAllItems(){

    }
}
