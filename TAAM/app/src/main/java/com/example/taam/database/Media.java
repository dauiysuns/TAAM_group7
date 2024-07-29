package com.example.taam.database;

import android.net.Uri;

public class Media {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;
    private String type;
    private String uri;

    public Media(String type, String uri){
        this.type = type;
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }
}
