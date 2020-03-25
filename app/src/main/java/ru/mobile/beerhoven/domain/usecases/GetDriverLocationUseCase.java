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
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ru.mobile.beerhoven.data.remote.MapRepository;
import ru.mobile.beerhoven.domain.model.DriverGeoModel;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IMapRepository;
import ru.mobile.beerhoven.presentation.interfaces.IFirebaseDriverInfoListener;
import ru.mobile.beerhoven.presentation.ui.map.Geo;
import ru.mobile.beerhoven.utils.Constants;

public class GetDriverLocationUseCase implements IFirebaseDriverInfoListener {
   private final Activity mActivity;
   private final Context mContext;
   private float mOrderColorMarker;
   private final List<Order> mOrderList = new ArrayList<>();
   private String mCityName;
   private String mOrderName;
   private static final String TAG = "GetDriverLocationUseCase";

   private final IMapRepository mRepository;

   // Location
   private boolean mArea = true;
   private static final double LIMIT_RANGE = 10.0;
   private FusedLocationProviderClient mFusedLocationProviderClient;
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
      loadOrderOnMap();
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
      mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
      mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
      mGeocoder = new Geocoder(mActivity, Locale.getDefault());

      mFusedLocationProviderClient.getLastLocation()
          .addOnFailureListener(e -> Log.e(TAG, "error load driver"))
          .addOnSuccessListener(location -> {
             // Load all drivers in city on map
             try {
                List<Address> list = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (list.size() > 0) {
                   mCityName = list.get(0).getLocality();

                   mRepository.getDriverLocation(location, mCityName);
                }
             } catch (IOException e) {
                e.printStackTrace();
             }
          });
   }

   // public void onMapReady(GoogleMap googleMap) {
   //    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
   //    mGoogleMap.setOnInfoWindowClickListener(this);
   //    mGoogleMap.setInfoWindowAdapter(this);
   // }

    @Override
   public void onDriverInfoLoadSuccess(DriverGeoModel driverGeoModel) {
      if (!TextUtils.isEmpty(mCityName)) {
         DatabaseReference driverLocation = FirebaseDatabase.getInstance()
             .getReference(Geo.DRIVER_LOCATION_REFERENCES)
             .child(mCityName)
             .child(driverGeoModel.getKey());

         driverLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (!snapshot.hasChildren()) {
                  if (Geo.markerList.get(driverGeoModel.getKey()) != null)
                     Objects.requireNonNull(Geo.markerList.get(driverGeoModel.getKey())).remove();
                  Geo.markerList.remove(driverGeoModel.getKey());
                  driverLocation.removeEventListener(this);
               }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               Log.w(TAG, error.getMessage());
            }
         });
      }
   }

   @Override
   public void onOrderInfoLoadSuccess(DriverGeoModel driverGeoModel) {}

   @SuppressLint("LongLogTag")
   private void loadOrderOnMap() {
      DatabaseReference orderLocationRef = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_CONFIRMS);

      ValueEventListener vListener = new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
               for (DataSnapshot snapshot : noteDataSnapshot.getChildren()) {
                  Order order = snapshot.getValue(Order.class);
                  mOrderList.add(order);
                  assert order != null;
                  mOrderName = order.getName();
                  mOrderColorMarker = order.getColor();
               }
            }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, error.getMessage());
         }
      };

      orderLocationRef.addValueEventListener(vListener);

      if (checkSelfPermission(mContext, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
          checkSelfPermission(mContext, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
         Log.e(TAG, "permission r3");
         return;
      }

      mFusedLocationProviderClient.getLastLocation()
          .addOnFailureListener(error -> Log.e(TAG, "error load order", error))
          .addOnSuccessListener(location -> {
             mGeocoder = new Geocoder(mContext, Locale.getDefault());

             // Load all drivers in city on map
             for (int i = 0; i < mOrderList.size(); i++) {
                List<Address> addressList = null;

                try {
                   addressList = mGeocoder.getFromLocationName(mOrderList.get(i).getAddress(), 2);

                   if (addressList.size() > 0) {
                      // cityName = list.get(0).getLocality();
                      Address address = addressList.get(0);
                      LatLng orderPosition = new LatLng(address.getLatitude(), address.getLongitude());

                      MarkerOptions markerOptions = new MarkerOptions()
                          .position(orderPosition)
                          .title(mOrderName)
                          .snippet(address.getAddressLine(0))
                          .icon(BitmapDescriptorFactory.defaultMarker(mOrderColorMarker));

                      // markerOptions.position(new LatLng(lat, lon));
                      mGoogleMap.addMarker(markerOptions);
                   }

                } catch (IOException e) {
                   e.printStackTrace();
                }
             }
          });
   }
}
