package com.example.taam.ui.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.example.taam.database.Media;

import java.util.ArrayList;

public class ViewFragment extends Fragment implements DataView {
    private String selectedLotNumber;
    private TextView textViewLot, textViewName, textViewCategory, textViewPeriod, textViewDescription;
    private MediaAdapter mediaAdapter;
    private RecyclerView mediaRecyclerView;
    private ImageButton closeButton;
    private DataModel dm;
    private ArrayList<Media> mediaUrls;

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
        mediaRecyclerView = view.findViewById(R.id.mediaRecyclerView);

        closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        dm = new DataModel(this);
        dm.getItemByLot(selectedLotNumber);

        // set up Media recyclerView
        mediaUrls = new ArrayList<>();
        dm.getItemByLot(selectedLotNumber);
        mediaAdapter = new MediaAdapter(mediaUrls, getContext());
        mediaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mediaRecyclerView.setAdapter(mediaAdapter);

        return view;
    }

    @Override
    public void updateView(Item item) {
        textViewLot.setText(item.getLot());
        textViewName.setText(item.name);
        textViewCategory.setText(item.category);
        textViewPeriod.setText(item.period);
        textViewDescription.setText(item.description);
        mediaUrls = item.getMediaUrls();
    }

    @Override
    public void showError(String errorMessage) {
        Log.v("View", errorMessage);
    }

    @Override
    public void onComplete(){

    }
}
