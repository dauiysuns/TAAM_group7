package com.example.taam;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class FirebaseModel {
    private FirebaseDatabase db;

    public FirebaseModel() {
        db = FirebaseDatabase
                .getInstance("https://taam-cfc94-default-rtdb.firebaseio.com/");
    }

    protected void getValue(TaamItem item, String key) {

        db.getReference("items/" + item.lotNumber + "/" + key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String data = snapshot.getValue(String.class);
                        //process data
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        throw new RuntimeException(error.getMessage());
                    }
                });

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
