package com.example.taam.ui.view;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    private static final int CHECK_INTERVAL = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_video_view);

        // set up fields
        videoView = findViewById(R.id.fullScreenVideoView);
        mediaController = new MediaController(this);
        playIcon = findViewById(R.id.playIcon);

        setUpVideo();
        setUpCloseButton();
        setUpClickableVideoView();
        checkPlayState();
    }

    private void setUpVideo(){
        // set up videoView using video url from Intent
        String videoUrl = getIntent().getStringExtra("videoUrl");
        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);

        // Set up media controller for videoView
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnCompletionListener(mp -> showPlayIcon(true));
    }

    // displays or hides play icon
    private void showPlayIcon(boolean show) {
        if(show){
            playIcon.setVisibility(VideoView.VISIBLE);
        }
        else{
            playIcon.setVisibility(VideoView.GONE);
        }
    }

    // allows changes of playing state using videoView to be reflected on the media controller
    private void setUpClickableVideoView() {
        videoView.setOnClickListener(v -> {
            if(videoView.isPlaying()){
                videoView.pause();
                showPlayIcon(true);
            }
            else{
                videoView.start();
                showPlayIcon(false);
            }
            mediaController.show();
        });
    }

    // allows changes of playing state using media controller to be reflected on the play icon
    private void checkPlayState() {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    showPlayIcon(false);
                } else {
                    showPlayIcon(true);
                }
                handler.postDelayed(this, CHECK_INTERVAL); // Re-run every CHECK_INTERVAL milliseconds
            }
        };
        handler.post(runnable);
    }

    // allows user to return to previous activity
    private void setUpCloseButton(){
        ImageView closeButton = findViewById(R.id.closeVideo);
        closeButton.setOnClickListener(v -> finish());
    }
}
