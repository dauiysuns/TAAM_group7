package com.example.taam.ui.report.Handler;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

public class PermissionHandler {
    public interface PermissionCallback {
        void onPermissionGranted();
    }

    private Activity activity;
    private Context context;
    private PermissionCallback permissionCallback;
    ActivityResultLauncher<String> requestPermissionLauncher;

    public PermissionHandler(Activity activity,
                             Context context,
                             ActivityResultLauncher<String> requestPermissionLauncher,
                             PermissionCallback permissionCallback) {
        this.activity = activity;
        this.context = context;
        this.requestPermissionLauncher = requestPermissionLauncher;
        this.permissionCallback = permissionCallback;
    }

    public void handlePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                permissionCallback.onPermissionGranted();
            } else {
                activity.startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                permissionCallback.onPermissionGranted();
            }
        }
    }
}
