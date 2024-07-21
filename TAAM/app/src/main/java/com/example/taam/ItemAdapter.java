package com.example.taam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<TaamItem> itemList;

    public ItemAdapter(List<TaamItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_adapater, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        TaamItem item = itemList.get(position);
        holder.textViewLot.setText(String.valueOf(item.getLotNumber()));
        holder.textViewName.setText(item.getName());
        holder.textViewCategory.setText(item.getCategory());
        holder.textViewPeriod.setText(item.getPeriod());
        holder.textViewDescription.setText(item.getDescription());
        // getting image/video code missing here
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLot, textViewName, textViewCategory, textViewPeriod, textViewDescription;
        //ImageView imageViewPicOrVid;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLot = itemView.findViewById(R.id.textViewLot);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewPeriod = itemView.findViewById(R.id.textViewPeriod);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
           // imageViewPicOrVid = itemView.findViewById(R.id.imageViewPicOrVid);
        }
    }
}
