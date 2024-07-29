package com.example.taam.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taam.R;
import com.example.taam.database.Media;

import java.util.ArrayList;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Media> mediaItems;
    Context context;

    public MediaAdapter(ArrayList<Media> mediaItems, Context context){
        this.mediaItems = mediaItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == Media.TYPE_IMAGE){
            View view = LayoutInflater.from(context).inflate(R.layout.image_holder, parent, false);
            return new ImageViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.video_holder, parent, false);
            return new VideoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Media mediaItem = mediaItems.get(position);
        if (holder.getItemViewType() == Media.TYPE_IMAGE) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            Glide.with(context).load(mediaItem.getUri()).into(imageViewHolder.imageView);
        } else {
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            videoViewHolder.videoView.setVideoURI(mediaItem.getUri());

            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(videoViewHolder.videoView);
            videoViewHolder.videoView.setMediaController(mediaController);

            videoViewHolder.videoView.setOnPreparedListener(mp -> videoViewHolder.videoView.start());
        }
    }

    @Override
    public int getItemCount() {
        return mediaItems.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
        }
    }
}
