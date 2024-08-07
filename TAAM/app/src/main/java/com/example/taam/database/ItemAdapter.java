package com.example.taam.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taam.R;
import com.example.taam.ui.view.MediaAdapter;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private final int MAX_DESCRIPTION_LENGTH = 350; // restricts the description length for preview
    private Context context;

    public ItemAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
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

        // display information for item
        holder.textViewLot.setText(item.getLot());
        holder.textViewName.setText(item.name);
        holder.textViewCategory.setText(item.category);
        holder.textViewPeriod.setText(item.period);
        String itemDescription = item.description;

        // restrict length of item description if needed
        if(itemDescription.length() > MAX_DESCRIPTION_LENGTH){
            String shortened = itemDescription.substring(0, MAX_DESCRIPTION_LENGTH - 3) + "...";
            holder.textViewDescription.setText(shortened);
        }
        else{
            holder.textViewDescription.setText(itemDescription);
        }

        // dynamically expand and collapse item view based on user input (clicks)
        if (item.isExpanded()) {
            holder.groupExpand.setVisibility(View.VISIBLE);
            holder.rollUp.setVisibility(View.VISIBLE);
            holder.dropDown.setVisibility(View.GONE);
        } else {
            holder.groupExpand.setVisibility(View.GONE);
            holder.rollUp.setVisibility(View.GONE);
            holder.dropDown.setVisibility(View.VISIBLE);
        }

        // Clear container before adding new media content to prevent problematic behavior (eg, duplicate images)
        holder.mediaContainer.removeAllViews();

        // set up linearLayout that displays images/videos (restrict to only one media item)
        MediaAdapter mediaAdapter = new MediaAdapter(item.mediaUrls, context, holder.mediaContainer);
        mediaAdapter.addMediaItems(1);

        // set up checkBox
        holder.checkBox.setChecked(item.isSelected());
        holder.checkBox.setOnClickListener(v -> item.setSelected(holder.checkBox.isChecked()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewLot, textViewName, textViewCategory, textViewPeriod, textViewDescription;
        ImageView dropDown, rollUp;
        LinearLayout mediaContainer;
        CheckBox checkBox;
        Group groupExpand;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            groupExpand = itemView.findViewById(R.id.groupExpand);
            textViewLot = itemView.findViewById(R.id.textViewLot);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewPeriod = itemView.findViewById(R.id.textViewPeriod);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            mediaContainer = itemView.findViewById(R.id.mediaContainer);
            checkBox = itemView.findViewById(R.id.checkBox);
            dropDown = itemView.findViewById(R.id.dropDown);
            rollUp = itemView.findViewById(R.id.rollUp);

            // set up listener to respond to user actions
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item item = itemList.get(getAdapterPosition());
                    item.setExpanded(!item.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
