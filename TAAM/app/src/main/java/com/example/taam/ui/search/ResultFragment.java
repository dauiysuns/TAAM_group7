package com.example.taam.ui.search;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taam.R;
import com.example.taam.database.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements ValueEventListener {

    //added by me
    String lotNumber;
    String name;
    String category;
    String period;
    FirebaseDatabase db;
    DatabaseReference ref;

    public ResultFragment() {
        // Required empty public constructor
    }

    //added by me
    public ResultFragment(String lotNumber, String name, String category, String period) {
        this.lotNumber = lotNumber;
        this.name = name;
        this.category = category;
        this.period = period;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        db = FirebaseDatabase.getInstance();
        ArrayList<Item> lotMatches = new ArrayList<>();
        ArrayList<Item> nameMatches = new ArrayList<>();
        ArrayList<Item> categoryMatches = new ArrayList<>();
        ArrayList<Item> periodMatches = new ArrayList<>();

        searchByLot(lotMatches);
        searchByName(nameMatches);
        searchByCategory(categoryMatches);
        searchByPeriod(periodMatches);

        lotMatches.addAll(nameMatches);
        lotMatches.addAll(nameMatches);
        lotMatches.addAll(categoryMatches);
        lotMatches.addAll(periodMatches);

        //display results
        TableLayout table = view.findViewById(R.id.table);
        for (int i = 0; i < lotMatches.size(); i++) {
            TableRow row = new TableRow(getContext());
            row.setLayoutParams(new TableRow.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT));

            TextView l = new TextView(getContext());
            l.setText(lotMatches.get(i).getLot());

            TextView n = new TextView(getContext());
            n.setText(lotMatches.get(i).name);

            TextView c = new TextView(getContext());
            c.setText(lotMatches.get(i).category);

            TextView p = new TextView(getContext());
            p.setText(lotMatches.get(i).period);

            TextView d = new TextView(getContext());
            d.setText(lotMatches.get(i).description);

            table.addView(row);
        }

        // Inflate the layout for this fragment
        return view;
    }

    private void searchByLot(ArrayList<Item> lotMatches) {
        ref = db.getReference("items/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null && item.getLot() != null &&  item.getLot().equalsIgnoreCase(lotNumber)) {
                        lotMatches.add(item);
                        found = true;
                    }
                }
                if (!found) {
                    Toast.makeText(getContext(), "Lot number not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchByName(ArrayList<Item> nameMatches) {
        ref = db.getReference("name/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null && item.name != null && item.name.equalsIgnoreCase(name)) {
                        nameMatches.add(item);
                        found = true;
                    }
                }
                if (!found) {
                    Toast.makeText(getContext(), "Name not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchByCategory(ArrayList<Item> categoryMatches) {
        ref = db.getReference("category/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null && item.category != null && item.category.equalsIgnoreCase(category)) {
                        categoryMatches.add(item);
                        found = true;
                    }
                }
                if (!found) {
                    Toast.makeText(getContext(), "Category not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchByPeriod(ArrayList<Item> periodMatches) {
        ref = db.getReference("period/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    if (item != null && item.period != null && item.period.equalsIgnoreCase(period)) {
                        periodMatches.add(item);
                        found = true;
                    }
                }
                if (!found) {
                    Toast.makeText(getContext(), "Period not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}