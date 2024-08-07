package com.example.taam.ui.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.taam.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class MediaAdapter {
    private ArrayList<HashMap<String, String>> mediaUrls;
    private Context context;
    private LinearLayout mediaContainer;
    private LayoutInflater inflater;
    private final String defaultImage = "gs://taam-cfc94.appspot.com/uploads/not available.jpg";

    public MediaAdapter(ArrayList<HashMap<String, String>> mediaUrls, Context context, LinearLayout mediaContainer) {
        this.mediaUrls = mediaUrls;
        this.context = context;
        this.mediaContainer = mediaContainer;
    }

    public void addMediaItems(int number) {
        inflater = LayoutInflater.from(context);

        // add default image if there are no media content
        if(mediaUrls.isEmpty()){
            addImage(defaultImage);
            return;
        }

        // otherwise, add desired number of media items dynamically based on their type
        for(int i = 0; i < number; i++){
            HashMap<String, String> mediaItem = mediaUrls.get(i);
            if (mediaItem.containsKey("image")) {
                String imageUrl = mediaItem.get("image");
                addImage(imageUrl);
            } else if (mediaItem.containsKey("video")) {
                String videoUrl = mediaItem.get("video");
                addVideo(videoUrl);
            }
        }
    }

    private void addImage(String imageUrl){
        View view = inflater.inflate(R.layout.image_holder, mediaContainer, false);
        ImageView imageView = view.findViewById(R.id.imageView);

        // load image and set up listener to respond to user action
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(imageUrl);
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context)
                    .load(uri)
                    .override(700, 700)
                    .into(imageView);

            imageView.setOnClickListener(v -> {
                Intent intent = new Intent(context, FullScreenImageActivity.class);
                intent.putExtra("imageUrl", uri.toString());
                context.startActivity(intent);
            });
        });
        mediaContainer.addView(view);
    }

    private void addVideo(String videoUrl) {
        View view = inflater.inflate(R.layout.video_holder, mediaContainer, false);
        VideoView videoView = view.findViewById(R.id.videoView);

        // Load and prepare the video
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(videoUrl);
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            videoView.setVideoURI(uri);

            // Automatically play video when it becomes visible
            videoView.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                videoView.start();
            });

            videoView.setOnClickListener(v -> {
                Intent intent = new Intent(context, FullScreenVideoActivity.class);
                intent.putExtra("videoUrl", uri.toString());
                context.startActivity(intent);
            });
        });
        mediaContainer.addView(view);
    }

}
