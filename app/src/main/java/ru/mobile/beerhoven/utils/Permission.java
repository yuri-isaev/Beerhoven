package ru.mobile.beerhoven.utils;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest.permission;
import android.app.Activity;
import android.content.pm.PackageManager;

public class Permission {
   private final Activity activity;
   private final String[] cameraPermissions;
   private final String[] storagePermissions;
   private final String[] locationPermissions;


   public Permission(Activity activity) {
      this.activity = activity;
      this.cameraPermissions = new String[]{permission.CAMERA, permission.WRITE_EXTERNAL_STORAGE};
      this.storagePermissions = new String[]{permission.WRITE_EXTERNAL_STORAGE};
      this.locationPermissions = new String[]{permission.ACCESS_FINE_LOCATION};
   }

   public boolean checkLocationPermission() {
      return checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
          checkSelfPermission(activity, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED;
   }

   public void requestLocationPermission() {
      requestPermissions(activity, locationPermissions, Constants.STORAGE_REQUEST_CODE);
   }

   public boolean checkStoragePermission() {
      return checkSelfPermission(activity,
          permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
   }

   public void requestStoragePermission() {
      requestPermissions(activity, storagePermissions, Constants.STORAGE_REQUEST_CODE);
   }

   public boolean checkCameraPermission() {
      return checkSelfPermission(activity,
          permission.CAMERA) == (PackageManager.PERMISSION_GRANTED) &&
          checkSelfPermission(activity,
              permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
   }

   public void requestCameraPermission() {
      requestPermissions(activity, cameraPermissions, Constants.CAMERA_REQUEST_CODE);
   }
}
