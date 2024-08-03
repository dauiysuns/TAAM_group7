package com.example.taam.ui.view;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taam.R;

public class FullScreenVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_video_view);

        videoView = findViewById(R.id.fullScreenVideoView);
        mediaController = new MediaController(this);

        setUpVideo();
        setUpCloseButton();
    }

    private void setUpVideo(){
        // Get the video URL from the Intent
        String videoUrl = getIntent().getStringExtra("videoUrl");

        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);

        // Set up media controller
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(mp -> videoView.pause());
    }

    private void setUpCloseButton(){
        ImageView closeButton = findViewById(R.id.closeVideo);
        closeButton.setOnClickListener(v -> finish());
    }
}
