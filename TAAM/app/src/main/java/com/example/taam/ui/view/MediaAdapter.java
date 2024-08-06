package com.example.taam.ui.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.taam.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<HashMap<String, String>> mediaUrls;
    private Context context;
    private final int TYPE_IMAGE = 0;
    private final int TYPE_VIDEO = 1;

    public MediaAdapter(ArrayList<HashMap<String, String>> mediaUrls, Context context){
        this.mediaUrls = mediaUrls;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_IMAGE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_holder, parent, false);
            return new ImageViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_holder, parent, false);
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
            String imageUrl = mediaItem.get("image");

            // the URL needs to be a Firebase Storage URL (need to start with gs://)
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(imageUrl);
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(context)
                        .load(uri)
                        .override(700,700) // or set fixed dimensions like .override(600, 600)
                        .into(imageViewHolder.imageView);
            }).addOnFailureListener(exception -> {
                Log.v("Load Image", "Error while loading image");
            });

        } else {
            VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
            String videoUrl = mediaItem.get("video");

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(videoUrl);
            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                videoViewHolder.videoView.setVideoURI(uri);
                MediaController mediaController = new MediaController(context);
                mediaController.setAnchorView(videoViewHolder.videoView);
                videoViewHolder.videoView.setMediaController(mediaController);
                videoViewHolder.videoView.setOnPreparedListener(mp -> videoViewHolder.videoView.start());
            }).addOnFailureListener(exception -> {
                Log.e("Video", "Failed to get video URL.", exception);
            });
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
