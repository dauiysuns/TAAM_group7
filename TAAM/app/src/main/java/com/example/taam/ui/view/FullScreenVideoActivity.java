package com.example.taam.ui.view;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taam.R;

public class FullScreenVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private ImageView playIcon;
    private static final int CHECK_INTERVAL = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_video_view);

        // Set up fields
        videoView = findViewById(R.id.fullScreenVideoView);
        playIcon = findViewById(R.id.playIcon);

        setUpVideo();
        setUpCloseButton();
        setUpClickableVideoView();
        checkPlayState();

        // Automatically play video when it becomes visible
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoView.start();
            showPlayIcon(false); // Ensure play icon is hidden when video starts
        });
    }

    private void setUpVideo() {
        // Set up videoView using video url from Intent
        String videoUrl = getIntent().getStringExtra("videoUrl");
        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);

        // Hide media controller
        videoView.setMediaController(null);
    }

    // Displays/hides play icon with animation
    private void showPlayIcon(boolean show) {
        playIcon.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // Allows changes of playing state using videoView to be reflected on the play icon
    private void setUpClickableVideoView() {
        videoView.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                showPlayIcon(true);
            } else {
                videoView.start();
                showPlayIcon(false);
            }
        });
    }

    // Checks play state and updates the play icon
    private void checkPlayState() {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                showPlayIcon(!videoView.isPlaying());
                handler.postDelayed(this, CHECK_INTERVAL); // Re-run every CHECK_INTERVAL milliseconds
            }
        };
        handler.post(runnable);
    }

    // Allows user to return to the previous activity
    private void setUpCloseButton() {
        ImageView closeButton = findViewById(R.id.closeVideo);
        closeButton.setOnClickListener(v -> finish());
    }
}
