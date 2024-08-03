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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private DataModel dm;
    private ArrayList<HashMap<String, String>> mediaUrls;

    public ViewFragment(String selectedLotNumber){
        this.selectedLotNumber = selectedLotNumber;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        textViewLot = view.findViewById(R.id.textViewLot);
        textViewName = view.findViewById(R.id.textViewName);
        textViewCategory = view.findViewById(R.id.textViewCategory);
        textViewPeriod = view.findViewById(R.id.textViewPeriod);
        textViewDescription = view.findViewById(R.id.textViewDescription);
        mediaContainer = view.findViewById(R.id.mediaContainer);

        closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        dm = new DataModel(this);
        mediaUrls = new ArrayList<>();
        dm.getItemByLot(selectedLotNumber);

        return view;
    }

    @Override
    public void updateView(Item item) {
        textViewLot.setText(item.getLot());
        textViewName.setText(item.name);
        textViewCategory.setText(item.category);
        textViewPeriod.setText(item.period);
        textViewDescription.setText(item.description);

        // if an item has no picture/video then mediaUrls is null
        if(item.mediaUrls != null){
            mediaUrls = item.mediaUrls;
        }
        mediaAdapter = new MediaAdapter(mediaUrls, getContext(), mediaContainer);
        mediaAdapter.addMediaItems(mediaUrls.size()); // display all media items
    }

    @Override
    public void showError(String errorMessage) {
        Log.v("View", errorMessage);
    }

    @Override
    public void onComplete(){
    }
}
