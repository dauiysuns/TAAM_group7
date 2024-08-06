package com.example.taam.ui.view;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.taam.R;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_view);

        setUpImage();
        setUpCloseButton();
    }

    // loads image using image URL from the Intent
    private void setUpImage(){
        ImageView imageView = findViewById(R.id.fullScreenImageView);
        String imageUrl = getIntent().getStringExtra("imageUrl");
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);
    }

    // allows user to return to previous activity
    private void setUpCloseButton(){
        ImageView closeButton = findViewById(R.id.closeImage);
        closeButton.setOnClickListener(v -> finish());
    }
}
