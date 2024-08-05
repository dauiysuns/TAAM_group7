package com.example.taam.ui.view;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taam.R;

public class FullScreenVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    private ImageView playIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_video_view);

        videoView = findViewById(R.id.fullScreenVideoView);
        mediaController = new MediaController(this);
        playIcon = findViewById(R.id.playIcon);

        setUpVideo();
        setUpCloseButton();
        setUpPlayFeatures();
    }

    private void setUpVideo(){
        // Get the video URL from the Intent
        String videoUrl = getIntent().getStringExtra("videoUrl");

        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);

        // Set up media controller
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnCompletionListener(mp -> showStoppedIcon(true));
    }

    private void setUpCloseButton(){
        ImageView closeButton = findViewById(R.id.closeVideo);
        closeButton.setOnClickListener(v -> finish());
    }

    private void showStoppedIcon(boolean show) {
        if(show){
            playIcon.setVisibility(VideoView.VISIBLE);
        }
        else{
            playIcon.setVisibility(VideoView.GONE);
        }
    }

    private void setUpPlayFeatures() {
        playIcon.setOnClickListener(v -> {
            videoView.start();
            showStoppedIcon(false);
        });

        videoView.setOnClickListener(v -> {
            if(playIcon.getVisibility() == View.GONE){
                videoView.pause();
                showStoppedIcon(true);
            }
        });
    }

}
