package com.example.taam;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        FirebaseDatabase db = firebaseSetup();

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            loadFragment(new MainScreenFragment());
        }
    }

    protected FirebaseDatabase firebaseSetup() {
        db = FirebaseDatabase.getInstance("https://taam-application-default-rtdb.firebaseio.com/");


        DatabaseReference ref = db.getReference("items/lotNumber (integer)");
        //uncomment to see database schema
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

    /**
     * Retrieves the value for the corresponding key for item
     * (i.e category, period)
     * @param item : TaamItem to search for in database
     * @param key : key to find value for
     * @param db : Firebase database
     * @return List : value
     */
    protected List<String> getValue(TaamItem item, String key, FirebaseDatabase db) {
        final List<String> value = Collections.emptyList();
        if (!key.equals("category") || !key.equals("period")) {
            throw new RuntimeException("Invalid ket");
        }
        db.getReference("item/"+ item.lotNumber + "/" + key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                value.add(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw new RuntimeException(error.getMessage());
            }
        });

        return value;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack(); // reverses last fragment change
        } else {
            super.onBackPressed(); // takes user back to home screen
        }
    }
}