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

   // Permissions constants
   public static final int CAMERA_REQUEST_CODE = 100;
   public static final int STORAGE_REQUEST_CODE = 200;

   private final Activity mActivity;
   private final String[] mCameraPermissions;
   private final String[] mLocationPermissions;
   private final String[] mStoragePermissions;


   public Permission(Activity activity) {
      this.mActivity = activity;
      this.mCameraPermissions = new String[]{permission.CAMERA, permission.WRITE_EXTERNAL_STORAGE};
      this.mLocationPermissions = new String[]{permission.ACCESS_FINE_LOCATION};
      this.mStoragePermissions = new String[]{permission.WRITE_EXTERNAL_STORAGE};
   }

   public boolean checkLocationPermission() {
      return
          checkSelfPermission(mActivity, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
          checkSelfPermission(mActivity, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED;
   }

   public void requestLocationPermission() {
      requestPermissions(mActivity, mLocationPermissions, STORAGE_REQUEST_CODE);
   }

   public boolean checkStoragePermission() {
      return checkSelfPermission(mActivity,
          permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
   }

   public void requestStoragePermission() {
      requestPermissions(mActivity, mStoragePermissions, STORAGE_REQUEST_CODE);
   }

   public boolean checkCameraPermission() {
      return
          checkSelfPermission(mActivity, permission.CAMERA) == (PackageManager.PERMISSION_GRANTED) &&
          checkSelfPermission(mActivity, permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
   }

   public void requestCameraPermission() {
      requestPermissions(mActivity, mCameraPermissions, CAMERA_REQUEST_CODE);
   }
}
