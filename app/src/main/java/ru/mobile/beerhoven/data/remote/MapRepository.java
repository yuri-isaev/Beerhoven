package ru.mobile.beerhoven.data.remote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.domain.model.DriverGeoModel;
import ru.mobile.beerhoven.domain.model.DriverInfoModel;
import ru.mobile.beerhoven.domain.model.GeoQueryModel;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IMapRepository;
import ru.mobile.beerhoven.presentation.interfaces.IFirebaseDriverInfoListener;
import ru.mobile.beerhoven.presentation.interfaces.IFirebaseFailedListener;
import ru.mobile.beerhoven.utils.Constants;

public class MapRepository implements IMapRepository, IFirebaseDriverInfoListener, IFirebaseFailedListener {
   private double mDistance = 1.0;
   private static final double LIMIT_RANGE = 10.0;
   private final DatabaseReference mFirebaseRef;
   private final GoogleMap mGoogleMap;

   private final IFirebaseDriverInfoListener iFirebaseDriverInfoListener;
   private final IFirebaseFailedListener iFirebaseFailedListener;

   private static final String TAG = "MapRepository";

   public MapRepository(GoogleMap map) {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mGoogleMap = map;
      this.iFirebaseDriverInfoListener = this;
      this.iFirebaseFailedListener = this;
   }

   @Override
   public void onGetDriverLocation(@NonNull Location location, String cityName) {
      DatabaseReference driverLocationRef = mFirebaseRef.child(Constants.DRIVER_LOCATION_REFERENCES).child(cityName);
      GeoFire geoFire = new GeoFire(driverLocationRef);
      GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), mDistance);

      geoQuery.removeAllListeners();
      geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
         @Override
         public void onKeyEntered(String key, GeoLocation location) {
            MapStorage.driverSet.add(new DriverGeoModel(key, location));
         }

         @Override
         public void onKeyExited(String key) {}

         @Override
         public void onKeyMoved(String key, GeoLocation location) {}

         @Override
         public void onGeoQueryReady() {
            if (mDistance <= LIMIT_RANGE) {
               mDistance++;
               onGetDriverLocation(location, cityName); // Continue searching at a new distance
            } else {
               mDistance = 1.0; // Reset
               onAddDriverMarker();
            }
         }

         @Override
         public void onGeoQueryError(DatabaseError error) {
            Log.e(TAG, error.getMessage());
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
               onFindDriverByKey(driverGeoModel);
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

   @Override
   public void onAddDriverMarker() {
      if (MapStorage.driverSet.size() > 0) {
         Observable.fromIterable(MapStorage.driverSet)  // Observable from it with individual set items
             .subscribeOn(Schedulers.newThread())       // the thread on which the operation is being performed
             .observeOn(AndroidSchedulers.mainThread()) // the thread where the result of the operation is returned
             .subscribe(this::onFindDriverByKey,
                 throwable -> Log.e(TAG, "thread error", throwable));
      } else {
         Log.e(TAG, "driver not found");
      }
   }

   @Override
   public void onFindDriverByKey(@NonNull DriverGeoModel driverGeoModel) {
      mFirebaseRef.child(Constants.DRIVER_INFO_REFERENCES).child(driverGeoModel.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
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

   @Override
   public void onDriverInfoLoadSuccess(@NonNull DriverGeoModel driver) {
      MapStorage.markerList.put(driver.getKey(), mGoogleMap.addMarker(new MarkerOptions()
          .position(new LatLng(driver.getGeoLocation().latitude, driver.getGeoLocation().longitude))
          .title(driver.getDriverInfoModel().getName())
          .snippet(driver.getDriverInfoModel().getPhoneNo())
          .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar))
          .flat(true))
      );
   }

   @Override
   public void onOrderInfoLoadSuccess(DriverGeoModel driverGeoModel) {}

   @SuppressLint("MissingPermission")
   @Override
   public void onGetOrderLocation(@NonNull FusedLocationProviderClient providerClient, Context ctx, Geocoder geocoder) {
      List<Order> orderList = new ArrayList<>();
      final float[] orderColorMarker = new float[1];
      final String[] orderName = new String[1];

      mFirebaseRef.child(Constants.NODE_CONFIRMS).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
               for (DataSnapshot snapshot : noteDataSnapshot.getChildren()) {
                  Order order = snapshot.getValue(Order.class);
                  orderList.add(order);
                  assert order != null;
                  orderName[0] = order.getName();
                  orderColorMarker[0] = Float.parseFloat(order.getColor());
               }
            }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, error.getMessage());
         }
      });

      providerClient.getLastLocation()
          .addOnFailureListener(error -> Log.e(TAG, "error load order", error))
          .addOnSuccessListener(location -> {
             for (int i = 0; i < orderList.size(); i++) {
                List<Address> addressList = null;
                try {
                   addressList = geocoder.getFromLocationName(orderList.get(i).getAddress(), 2);

                   if (addressList.size() > 0) {
                      // cityName = list.get(0).getLocality();
                      Address address = addressList.get(0);
                      LatLng orderPosition = new LatLng(address.getLatitude(), address.getLongitude());

                  MarkerOptions markerOptions = new MarkerOptions()
                      .position(orderPosition)
                      .title(orderName[0])
                      .snippet(address.getAddressLine(0))
                      .icon(BitmapDescriptorFactory.defaultMarker(orderColorMarker[0]));

                  // markerOptions.position(new LatLng(lat, lon));
                  mGoogleMap.addMarker(markerOptions);
               }

            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      });
   }

   @Override
   public void onFirebaseLoadFailed(String message) {
      Log.w(TAG, "firebase load failed");
   }
}
