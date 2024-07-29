package com.example.taam.database;

import android.net.Uri;

public class Media {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;
    private String type;
    private Uri uri;

    public Media(String type, Uri uri){
        this.type = type;
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public Uri getUri() {
        return uri;
    }
}
