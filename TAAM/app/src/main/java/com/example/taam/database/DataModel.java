package com.example.taam.database;

import android.util.Log;

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

    private Item setFields(DataSnapshot snapshot) {
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
        return item;
    }

    public void getItemByLot(String lotNumber){
        fetchData(ref.child("items/" + lotNumber).orderByPriority().getRef(), new DataListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                view.updateView(setFields(snapshot));
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                view.showError(error.getMessage());
            }
        });
    }

    public void getItemsByCategory(String category, String query) {
        fetchData(ref, new DataListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (category.equals("lot number")) {
                    view.updateView(setFields(snapshot.child("items").child(query)));
                    view.onComplete();
                    return;
                }
                DataSnapshot querySnapshot = snapshot.child(category).child(query);
                for (DataSnapshot lot: querySnapshot.getChildren()) {
                    DataSnapshot snapshot1 = snapshot.child("items").child(lot.getKey().toString());
                        view.updateView(setFields(snapshot.child("items").child(lot.getKey().toString())));
                }
                view.onComplete();
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                view.showError(error.getMessage());
            }
        });
    }

    public void getAllItems(){
        fetchData(ref.child("items").getRef(), new DataListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    view.updateView(setFields(dataSnapshot));
                }
                view.onComplete();
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

    public static void addItem(Item item) {
        Field []fields = item.getClass().getFields();
        int i = 0;
        for (Field field: fields) {
            try {
                ref.child("items/" + item.getLot() + "/" + field.getName()).setValue(field.get(item));
                ref.child(field.getName()).child((String) Objects.requireNonNull(field.get(item))).child(item.getLot()).setValue("null");
            } catch (IllegalAccessException e) {
                Log.v("error", e.getMessage());
            }
        }
    }
}