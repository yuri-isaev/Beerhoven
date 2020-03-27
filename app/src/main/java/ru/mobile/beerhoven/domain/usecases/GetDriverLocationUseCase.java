package ru.mobile.beerhoven.domain.usecases;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.annotation.SuppressLint;
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

import ru.mobile.beerhoven.data.remote.MapRepository;
import ru.mobile.beerhoven.domain.repository.IMapRepository;

public class GetDriverLocationUseCase {
   private final Activity mActivity;
   private final Context mContext;
   private String mCityName;
   private static final String TAG = "GetDriverLocationUseCase";

   private final IMapRepository mRepository;

   // Location
   private boolean mArea = true;
   private static final double LIMIT_RANGE = 10.0;
   private Geocoder mGeocoder;
   private final GoogleMap mGoogleMap;
   private Location mCurrentLocation;
   private Location mPreviousLocation;
   private LocationRequest mLocationRequest;
   private LocationCallback mLocationCallback;

   public GetDriverLocationUseCase(Activity activity, Context context, GoogleMap map) {
      this.mActivity = activity;
      this.mContext = context;
      this.mGoogleMap = map;
      this.mRepository = new MapRepository(map);
   }

   public void invoke() {
      initCurrentLocation();
   }

   @SuppressLint("LongLogTag")
   private void initCurrentLocation() {
      if (checkSelfPermission(mContext, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
          checkSelfPermission(mContext, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
         Log.e(TAG, "permission r1");
         return;
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
            LatLng newPosition = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
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
            if (mPreviousLocation.distanceTo(mCurrentLocation) / 1000 <= LIMIT_RANGE)
               loadDriverOnMap();
            else {
               Log.e(TAG, "display limit exceeded");
            }
         }
      };

      if (checkSelfPermission(mContext, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
          checkSelfPermission(mContext, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
         Log.e(TAG, "permission r2");
         return;
      }

      loadDriverOnMap();
   }

   @SuppressLint({"MissingPermission", "LongLogTag"})
   private void loadDriverOnMap() {
      FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
      mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
      mGeocoder = new Geocoder(mActivity, Locale.getDefault());

      mFusedLocationProviderClient.getLastLocation()
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
