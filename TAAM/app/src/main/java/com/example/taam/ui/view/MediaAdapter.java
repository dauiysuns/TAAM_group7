package com.example.taam.ui.view;

import android.content.Context;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.HashMap;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<HashMap<String, String>> mediaUrls;
    Context context;
    private final int TYPE_IMAGE = 0;
    private final int TYPE_VIDEO = 1;

    public MediaAdapter(ArrayList<HashMap<String, String>> mediaUrls, Context context){
        this.mediaUrls = mediaUrls;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0){
            View view = LayoutInflater.from(context).inflate(R.layout.image_holder, parent, false);
            return new ImageViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.video_holder, parent, false);
            return new VideoViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mediaUrls.get(position).containsKey("image")){
            return TYPE_IMAGE;
        }
        return TYPE_VIDEO;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HashMap<String, String> mediaItem = mediaUrls.get(position);
        if (holder.getItemViewType() == TYPE_IMAGE) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            Glide.with(context).load(mediaItem.get("image")).into(imageViewHolder.imageView);
        } else {
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            videoViewHolder.videoView.setVideoURI(Uri.parse(mediaItem.get("video")));

            MediaController mediaController = new MediaController(context);
            mediaController.setAnchorView(videoViewHolder.videoView);
            videoViewHolder.videoView.setMediaController(mediaController);

            videoViewHolder.videoView.setOnPreparedListener(mp -> videoViewHolder.videoView.start());
        }
    }

    @Override
    public int getItemCount() {
        return mediaUrls.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
        }
    }
}
