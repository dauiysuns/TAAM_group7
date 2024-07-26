package com.example.taam.ui.search;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taam.R;
import com.example.taam.database.DataModel;
import com.example.taam.database.DataView;
import com.example.taam.database.Item;
import com.example.taam.ui.FragmentLoader;
import com.example.taam.ui.home.AdminHomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements DataView {

    //added by me
    String lotNumber;
    String name;
    String category;
    String period;
    DataModel dm;
    ArrayList<Item> lotMatches = new ArrayList<>();
    ArrayList<Item> nameMatches = new ArrayList<>();
    ArrayList<Item> categoryMatches = new ArrayList<>();
    ArrayList<Item> periodMatches = new ArrayList<>();

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
        dm = new DataModel(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        Button backButton = view.findViewById(R.id.button3);

        ArrayList<Item> lotMatches = new ArrayList<>();
        ArrayList<Item> nameMatches = new ArrayList<>();
        ArrayList<Item> categoryMatches = new ArrayList<>();
        ArrayList<Item> periodMatches = new ArrayList<>();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentLoader.loadFragment(getParentFragmentManager(), new AdminHomeFragment());
            }
        });
        dm.displayAllItems();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void updateView(Item item) {
        if (item != null && item.period != null && item.period.equalsIgnoreCase(period)) {
            periodMatches.add(item);
        }
        if (item != null && item.category != null && item.category.equalsIgnoreCase(category)) {
            categoryMatches.add(item);
        }
        if (item != null && item.getLot() != null &&  item.getLot().equalsIgnoreCase(lotNumber)) {
            lotMatches.add(item);
        }
        if (item != null && item.name != null && item.name.equalsIgnoreCase(name)) {
            nameMatches.add(item);
        }
    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void onComplete() {
        //maybe check if item already in array before adding?
        lotMatches.addAll(nameMatches);
        lotMatches.addAll(nameMatches);
        lotMatches.addAll(categoryMatches);
        lotMatches.addAll(periodMatches);

        //display results
        TableLayout table = getView().findViewById(R.id.table);
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
    }
}