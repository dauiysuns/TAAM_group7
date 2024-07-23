package com.example.taam;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ViewFragment extends Fragment implements DataView{
    private String selectedLotNumber;
    private TextView textViewLot, textViewName, textViewCategory, textViewPeriod, textViewDescription;
    //private ImageView imageViewPicOrVid;
    private DataModel dm;

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
        // imageViewPicOrVid = itemView.findViewById(R.id.imageViewPicOrVid);

        dm = new DataModel(this);
        dm.displayItem(selectedLotNumber);

        return view;
    }

    @Override
    public void updateView(Item item) {
        textViewLot.setText(item.getLot());
        textViewName.setText(item.name);
        textViewCategory.setText(item.category);
        textViewPeriod.setText(item.period);
        textViewDescription.setText(item.description);
    }

    @Override
    public void showError(String errorMessage) {
        Log.v("View", errorMessage);
    }

}
