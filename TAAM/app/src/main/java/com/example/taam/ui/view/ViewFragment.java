package com.example.taam.ui.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taam.database.Item;
import com.example.taam.R;
import com.example.taam.database.DataModel;
import com.example.taam.database.DataView;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewFragment extends Fragment implements DataView {
    private String selectedLotNumber;
    private TextView textViewLot, textViewName, textViewCategory, textViewPeriod, textViewDescription;
    private MediaAdapter mediaAdapter;
    private LinearLayout mediaContainer;
    private ImageButton closeButton;
    private ArrayList<HashMap<String, String>> mediaUrls;

    public ViewFragment(String selectedLotNumber){
        this.selectedLotNumber = selectedLotNumber;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        setUpUI(view);

        // connect to database and obtain information for selected item to display
        DataModel dm = new DataModel(this);
        dm.getItemByLot(selectedLotNumber);

        return view;
    }

    private void setUpUI(View view){
        textViewLot = view.findViewById(R.id.textViewLot);
        textViewName = view.findViewById(R.id.textViewName);
        textViewCategory = view.findViewById(R.id.textViewCategory);
        textViewPeriod = view.findViewById(R.id.textViewPeriod);
        textViewDescription = view.findViewById(R.id.textViewDescription);
        mediaContainer = view.findViewById(R.id.mediaContainer);
        closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    // updates UI elements based on information obtained from database
    @Override
    public void updateView(Item item) {
        textViewLot.setText(item.getLot());
        textViewName.setText(item.name);
        textViewCategory.setText(item.category);
        textViewPeriod.setText(item.period);
        textViewDescription.setText(item.description);
        mediaUrls = item.mediaUrls;
        mediaAdapter = new MediaAdapter(mediaUrls, getContext(), mediaContainer);
        mediaAdapter.addMediaItems(mediaUrls.size()); // display all media items
    }

    @Override
    public void showError(String errorMessage) {
        Log.v("View", errorMessage);
    }

    @Override
    public void onComplete(){ Log.v("View", "Item information obtained successfully."); }
}
