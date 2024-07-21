package com.example.taam;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

class FirebaseModel {
    public FirebaseDatabase db;

    public FirebaseModel() {
        db = FirebaseDatabase
                .getInstance("https://taam-cfc94-default-rtdb.firebaseio.com/");
    }

    public void setValue(int lotNumber, String field, Object value) {
        db.getReference("items/" + lotNumber).child(field).setValue(value);
    }

    private void setField(@NonNull TaamItem item, String field) {
        getValue("items/" + item.lotNumber + "/" + field).thenAccept(data -> {
            if (data != null) {
                try {
                    Field field1 = item.getClass().getDeclaredField(field);
                    field1.set(field, data);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    Log.v("error", "no such field: " + field);
                }
            } else {
                Log.v("main activity", "no data in database");
            }
        });
    }

    public TaamItem itemSetup(int lotNumber) {
        TaamItem item = new TaamItem(lotNumber);
        Field[] fields = item.getClass().getDeclaredFields();
        for (Field field: fields) {
            setField(item, field.getName());
        }
        return item;
    }

    public void databaseSetup(TaamItem item) {
        int lotNumber = item.lotNumber;
        Field[] fields = item.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                setValue(lotNumber, field.getName(), field.get(item));
            } catch (IllegalAccessException e) {
                Log.v("error", "no value");
            }

        }
    }

    public void displayValue(String path){
        getValue(path).thenAccept(data -> {
            if (data != null) {
                // TODO:Process the data
                Log.v("main activity", data);
            } else {
                Log.v("main activity", "no data");
            }
        });
    }

    @NonNull
    private CompletableFuture<String> getValue(String path) {
        CompletableFuture<String> future = new CompletableFuture<>();

        db.getReference(path)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String data = snapshot.getValue(String.class);
                        Boolean completed = future.complete(data);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw new RuntimeException(error.getMessage());
                    }
                });
        return future;
    }

    private FirebaseDatabase firebaseSchema() {

        DatabaseReference ref = db.getReference("items/lotNumber (integer)");
        db.getReference().removeValue();
        ref.child("name").setValue("string");
        ref.child("category").setValue("string");
        ref.child("period").setValue("string");
        ref.child("description").setValue("string");
        ref.child("display").setValue("image file");

        ref = db.getReference("category");
        ref.child("category (string)/lotNumber").setValue("null");

        ref = db.getReference("period");
        ref.child("period (string)/lotNumber").setValue("null");

        return db;
    }
}
