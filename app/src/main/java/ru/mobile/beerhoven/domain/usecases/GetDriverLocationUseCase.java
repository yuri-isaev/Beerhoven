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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
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
import com.google.firebase.database.ChildEventListener;
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

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.domain.model.DriverGeoModel;
import ru.mobile.beerhoven.domain.model.DriverInfoModel;
import ru.mobile.beerhoven.domain.model.GeoQueryModel;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.presentation.interfaces.IFirebaseDriverInfoListener;
import ru.mobile.beerhoven.presentation.interfaces.IFirebaseFailedListener;
import ru.mobile.beerhoven.presentation.ui.map.Geo;
import ru.mobile.beerhoven.utils.Constants;

public class GetDriverLocationUseCase implements IFirebaseDriverInfoListener, IFirebaseFailedListener {

   private final Activity mActivity;
   private final Context mContext;

   private double mDistance = 1.0;
   private static final double LIMIT_RANGE = 10.0;
   private FusedLocationProviderClient mFusedLocationProviderClient;
   private Geocoder mGeocoder;
   private String mCityName;
   private final GoogleMap mGoogleMap;

   private float mOrderColorMarker;
   private final List<Order> mOrderList = new ArrayList<>();
   private String mOrderName;

   // Location
   private boolean mArea = true;
   private Location mCurrentLocation;
   private Location mPreviousLocation;
   private LocationRequest mLocationRequest;
   private LocationCallback mLocationCallback;

   private IFirebaseDriverInfoListener iFirebaseDriverInfoListener;
   private IFirebaseFailedListener iFirebaseFailedListener;

   private static final String TAG = "GetDriverLocationUseCase";

   public GetDriverLocationUseCase(Activity activity, Context context, GoogleMap map) {
      this.mActivity = activity;
      this.mContext = context;
      this.mGoogleMap = map;
   }

   public void execute() {
      initCurrentLocation();
      loadOrderOnMap();
   }

   @SuppressLint("LongLogTag")
   private void initCurrentLocation() {
      iFirebaseDriverInfoListener = this;
      iFirebaseFailedListener = this;

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

                   // Firebase query for city name to work with distance
                   DatabaseReference driverLocationRef = FirebaseDatabase.getInstance().getReference(Geo.DRIVER_LOCATION_REFERENCES).child(mCityName);
                   GeoFire geoFire = new GeoFire(driverLocationRef);
                   GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), mDistance);

                   geoQuery.removeAllListeners();
                   geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                      @Override
                      public void onKeyEntered(String key, GeoLocation location) {
                         Geo.driverSet.add(new DriverGeoModel(key, location));
                      }

                      @Override
                      public void onKeyExited(String key) {}

                      @Override
                      public void onKeyMoved(String key, GeoLocation location) {}

                      @Override
                      public void onGeoQueryReady() {
                         if (mDistance <= LIMIT_RANGE) {
                            mDistance++;
                            loadDriverOnMap(); // Continue searching at a new distance
                         } else {
                            mDistance = 1.0; // Reset
                            addDriverMarker();
                         }
                      }

                      @Override
                      public void onGeoQueryError(DatabaseError error) {
                         Toasty.error(mActivity, error.getMessage(), Toast.LENGTH_LONG, true).show();
                      }
                   });

                   // Firebase listener adding new driver
                   driverLocationRef.addChildEventListener(new ChildEventListener() {
                      @Override
                      public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                         GeoQueryModel geoQueryModel = snapshot.getValue(GeoQueryModel.class);
                         assert geoQueryModel != null;
                         GeoLocation geoLocation = new GeoLocation(geoQueryModel.getL().get(0), geoQueryModel.getL().get(1));
                         DriverGeoModel driverGeoModel = new DriverGeoModel(snapshot.getKey(), geoLocation);
                         Location newDriverLocation = new Location(" ");
                         newDriverLocation.setLatitude(geoLocation.latitude);
                         newDriverLocation.setLongitude(geoLocation.longitude);
                         float newDistance = location.distanceTo(newDriverLocation) / 1000;

                         if (newDistance <= LIMIT_RANGE) // If the driver is in the coverage area, add him to the map
                            findDriverByKey(driverGeoModel);
                      }

                      @Override
                      public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                      @Override
                      public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                      @Override
                      public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {}
                   });
                }
             } catch (IOException e) {
                e.printStackTrace();
             }
          });
   }

   @SuppressLint("LongLogTag")
   private void addDriverMarker() {
      if (Geo.driverSet.size() > 0) {
         Observable.fromIterable(Geo.driverSet)         // Observable from it with individual set items
             .subscribeOn(Schedulers.newThread())       // the thread on which the operation is being performed
             .observeOn(AndroidSchedulers.mainThread()) // the thread where the result of the operation is returned
             .subscribe(this::findDriverByKey,
                 throwable -> Log.e(TAG, "thread error", throwable));
      } else {
         Log.e(TAG, "driver not found");
      }
   }

   // public void onMapReady(GoogleMap googleMap) {
   //    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
   //    mGoogleMap.setOnInfoWindowClickListener(this);
   //    mGoogleMap.setInfoWindowAdapter(this);
   // }

    @Override
   public void onDriverInfoLoadSuccess(DriverGeoModel driverGeoModel) {
      // if (!Geo.markerList.containsKey(driverGeoModel.getKey()))
      Geo.markerList.put(driverGeoModel.getKey(),
          mGoogleMap.addMarker(new MarkerOptions()
              .position(new LatLng(driverGeoModel.getGeoLocation().latitude, driverGeoModel.getGeoLocation().longitude))
              .title(Geo.buildTitle(driverGeoModel.getDriverInfoModel().getName()))
              .snippet(driverGeoModel.getDriverInfoModel().getPhoneNo())
              .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar))
              .flat(true))
      );

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
   @Override
   public void onFirebaseLoadFailed(String message) {
      Log.w(TAG, "firebase load failed");
   }

   private void findDriverByKey(DriverGeoModel driverGeoModel) {
      FirebaseDatabase.getInstance()
          .getReference(Geo.DRIVER_INFO_REFERENCES)
          .child(driverGeoModel.getKey())
          .addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                   driverGeoModel.setDriverInfoModel(snapshot.getValue(DriverInfoModel.class));
                   iFirebaseDriverInfoListener.onDriverInfoLoadSuccess(driverGeoModel);
                } else {
                   iFirebaseFailedListener.onFirebaseLoadFailed("key not found" + driverGeoModel.getKey());
                }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                iFirebaseFailedListener.onFirebaseLoadFailed("key not found" + driverGeoModel.getKey());
             }
          });
   }

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
