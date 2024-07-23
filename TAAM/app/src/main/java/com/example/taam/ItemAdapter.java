package com.example.taam;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private final int maxDescriptionLength = 350;

    public ItemAdapter(List<Item> itemList) {
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
        Item item = itemList.get(position);
        holder.textViewLot.setText(item.getLot());

        if (holder.isExpanded) {
            holder.groupExpand.setVisibility(View.VISIBLE);
            holder.rollUp.setVisibility(View.VISIBLE);
            holder.dropDown.setVisibility(View.GONE);
        } else {
            holder.groupExpand.setVisibility(View.GONE);
            holder.rollUp.setVisibility(View.GONE);
            holder.dropDown.setVisibility(View.VISIBLE);
        }

        holder.textViewName.setText(item.name);
        holder.textViewCategory.setText(item.category);
        holder.textViewPeriod.setText(item.period);

        String itemDescription = item.description;
        if(itemDescription.length() > maxDescriptionLength){
            String shortened = itemDescription.substring(0, maxDescriptionLength - 3) + "...";
            holder.textViewDescription.setText(shortened);
        }
        else{
            holder.textViewDescription.setText(itemDescription);
        }

        // getting image/video code missing here

        holder.checkBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                item.setSelected(holder.checkBox.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLot, textViewName, textViewCategory, textViewPeriod, textViewDescription;
        ImageView dropDown, rollUp;
        //ImageView imageViewPicOrVid;
        CheckBox checkBox;
        boolean isExpanded = false;
        Group groupExpand;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            groupExpand = itemView.findViewById(R.id.groupExpand);
            textViewLot = itemView.findViewById(R.id.textViewLot);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewPeriod = itemView.findViewById(R.id.textViewPeriod);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
           // imageViewPicOrVid = itemView.findViewById(R.id.imageViewPicOrVid);
            checkBox = itemView.findViewById(R.id.checkBox);
            dropDown = itemView.findViewById(R.id.dropDown);
            rollUp = itemView.findViewById(R.id.rollUp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isExpanded = !isExpanded;
                    Log.v("test activity", "clicked" + textViewLot.getText()+isExpanded);
                    ItemAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }
}
