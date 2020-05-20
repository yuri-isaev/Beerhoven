package ru.mobile.beerhoven.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {
   private final Activity activity;
   private final String[] cameraPermissions;
   private final String[] storagePermissions;


   public Permission(Activity activity) {
      this.activity = activity;
      this.cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
      this.storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
   }

   public boolean checkStoragePermission() {
      return ContextCompat.checkSelfPermission(activity,
          Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
   }

   public void requestStoragePermission() {
      ActivityCompat.requestPermissions(activity, storagePermissions, Constants.STORAGE_REQUEST_CODE);
   }

   public boolean checkCameraPermission() {
      return ContextCompat.checkSelfPermission(activity,
          Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED) &&
          ContextCompat.checkSelfPermission(activity,
              Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
   }

   public void requestCameraPermission() {
      ActivityCompat.requestPermissions(activity, cameraPermissions, Constants.CAMERA_REQUEST_CODE);
   }
}
