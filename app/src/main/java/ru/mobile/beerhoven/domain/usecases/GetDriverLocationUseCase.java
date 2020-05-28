package ru.mobile.beerhoven.domain.usecases;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ru.mobile.beerhoven.domain.repository.IMapRepository;
import ru.mobile.beerhoven.utils.Permission;

public class GetDriverLocationUseCase {
   private final Activity mActivity;
   private final Context mContext;
   private final IMapRepository mRepository;
   private String mCityName;
   private static final String TAG = "DriverLocationUseCase";

   // Location
   private boolean mArea;
   private Geocoder mGeocoder;
   private Location mCurrentLocation;
   private Location mPreviousLocation;
   private LocationRequest mLocationRequest;
   private LocationCallback mLocationCallback;
   private final GoogleMap mGoogleMap;
   private final Permission mPermission;

   public GetDriverLocationUseCase(Activity activity, Context ctx, GoogleMap map, IMapRepository repo) {
      this.mActivity = activity;
      this.mArea = true;
      this.mContext = ctx;
      this.mGoogleMap = map;
      this.mPermission = new Permission(mActivity);
      this.mRepository = repo;
   }

   public void execute() {
      initCurrentLocation();
   }

   private void initCurrentLocation() {
      if (mPermission.checkLocationPermission()) {
         mPermission.requestLocationPermission();
      }

      // Location display options
      mLocationRequest = new LocationRequest();     // Positioning accuracy priority
      mLocationRequest.setSmallestDisplacement(10); // Set smallest offset
      mLocationRequest.setInterval(500);            // Update interval
      mLocationRequest.setFastestInterval(300);     // Set the fastest location update interval
      mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // High display accuracy

      // Returns the value of the current location
      mLocationCallback = new LocationCallback() {
         @Override
         public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            // Approaching an object during location initialization
            LatLng newPosition = new LatLng(locationResult.getLastLocation().getLatitude(),
                locationResult.getLastLocation().getLongitude());
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 18f));

            // If the user has changed the terrain, calculate and download the driver again
            if (mArea) {
               mPreviousLocation = mCurrentLocation = locationResult.getLastLocation();
               mArea = false;
            } else {
               mPreviousLocation = mCurrentLocation;
               mCurrentLocation = locationResult.getLastLocation();
            }

            // Range limits
            if (mPreviousLocation.distanceTo(mCurrentLocation) / 1000 <= 10.0)
               onLoadDriverOnMap();
            else {
               Log.e(TAG, "display limit exceeded");
            }
         }
      };

      onLoadDriverOnMap();
   }

   private void onLoadDriverOnMap() {
      if (mPermission.checkLocationPermission()) {
         mPermission.requestLocationPermission();
      }
      FusedLocationProviderClient providerClient = LocationServices.getFusedLocationProviderClient(mContext);
      providerClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
      mGeocoder = new Geocoder(mActivity, Locale.getDefault());

      providerClient.getLastLocation()
          .addOnFailureListener(e -> Log.e(TAG, "error load driver"))
          .addOnSuccessListener(location -> {
             try {
                List<Address> list = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (list.size() > 0) {
                   mCityName = list.get(0).getLocality();
                   mRepository.onGetDriverLocation(location, mCityName);
                }
             } catch (IOException e) {
                e.printStackTrace();
             }
          });
   }
}
