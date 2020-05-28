package ru.mobile.beerhoven.data.remote;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.listeners.FirebaseDriverInfoListener;
import ru.mobile.beerhoven.data.listeners.FirebaseFailedListener;
import ru.mobile.beerhoven.domain.model.DriverGeoModel;
import ru.mobile.beerhoven.domain.model.DriverInfoModel;
import ru.mobile.beerhoven.domain.model.GeoQueryModel;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IMapRepository;
import ru.mobile.beerhoven.utils.Constants;

public class MapRepository implements IMapRepository, FirebaseFailedListener, FirebaseDriverInfoListener {
   private double mDistance = 1.0;
   private final DatabaseReference mFirebaseRef;
   private final FirebaseDriverInfoListener iFirebaseDriverInfoListener;
   private final FirebaseFailedListener iFirebaseFailedListener;
   private final GoogleMap mGoogleMap;
   private final List<Order> mOrderList;
   private final MutableLiveData<List<Order>> mMutableList;
   private final Set<DriverGeoModel> driverSet;
   private static final double LIMIT_RANGE = 10.0;
   private static final String TAG = "MapRepository";

   public MapRepository(GoogleMap map) {
      this.iFirebaseDriverInfoListener = this;
      this.iFirebaseFailedListener = this;
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mGoogleMap = map;
      this.mOrderList = new ArrayList<>();
      this.mMutableList = new MutableLiveData<>();
      this.driverSet = new HashSet<>();
   }

   @Override
   public MutableLiveData<List<Order>> getOrderLocationListFromDatabase() {
      if (mOrderList.size() == 0) {
         onGetOrderLocationList();
      }
      mMutableList.setValue(mOrderList);
      return mMutableList;
   }

   public void onGetOrderLocationList() {
      mFirebaseRef.child(Constants.NODE_CONFIRMS).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
               for (DataSnapshot snapshot : noteDataSnapshot.getChildren()) {
                  Order order = snapshot.getValue(Order.class);
                  assert order != null;
                  if (!mOrderList.contains(order)) {
                     mOrderList.add(order);
                  }
                  mMutableList.postValue(mOrderList);
               }
            }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, error.getMessage());
         }
      });
   }

   @Override
   public void onGetDriverLocation(@NonNull Location location, String cityName) {
      DatabaseReference driverLocationRef = mFirebaseRef.child(Constants.DRIVER_LOCATION_REF).child(cityName);
      GeoFire geoFire = new GeoFire(driverLocationRef);
      GeoQuery geoQuery = geoFire.queryAtLocation(
          new GeoLocation(location.getLatitude(), location.getLongitude()), mDistance);

      geoQuery.removeAllListeners();

      geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
         @Override
         public void onKeyEntered(String key, GeoLocation location) {
            driverSet.add(new DriverGeoModel(key, location));
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

      driverLocationRef.addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            GeoQueryModel geoQueryModel = snapshot.getValue(GeoQueryModel.class);
            assert geoQueryModel != null;
            GeoLocation geoLocation = new GeoLocation(geoQueryModel.getL().get(0), geoQueryModel.getL().get(1));
            DriverGeoModel driverGeoModel = new DriverGeoModel(snapshot.getKey(), geoLocation);
            Location newDriverLocation = new Location("");
            newDriverLocation.setLatitude(geoLocation.latitude);
            newDriverLocation.setLongitude(geoLocation.longitude);
            float newDistance = location.distanceTo(newDriverLocation) / 1000;

            // If the driver is in the coverage area, add him to the map
            if (newDistance <= LIMIT_RANGE) {
               onFindDriverByKey(driverGeoModel);
            }
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
      if (driverSet.size() > 0) {
         Observable.fromIterable(driverSet)             // Observable from it with individual set items
             .subscribeOn(Schedulers.newThread())       // the thread on which the operation is being performed
             .observeOn(AndroidSchedulers.mainThread()) // the thread where the result of the operation is returned
             .subscribe(this::onFindDriverByKey,
                 throwable -> Log.e(TAG, "thread error", throwable));
      } else {
         Log.e(TAG, "driver not found");
      }
   }

   @Override
   public void onFindDriverByKey(@NonNull DriverGeoModel driver) {
      mFirebaseRef.child(Constants.DRIVER_INFO_REF).child(driver.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.hasChildren()) {
               driver.setDriverInfoModel(snapshot.getValue(DriverInfoModel.class));
               iFirebaseDriverInfoListener.onDriverInfoLoadSuccess(driver);
            } else {
               iFirebaseFailedListener.onFirebaseLoadFailed("key not found" + driver.getKey());
            }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {
            iFirebaseFailedListener.onFirebaseLoadFailed("key not found" + driver.getKey());
         }
      });
   }

   @Override
   public void onDriverInfoLoadSuccess(@NonNull DriverGeoModel driver) {
      MarkerOptions orderOptions = new MarkerOptions()
          .position(new LatLng(driver.getGeoLocation().latitude, driver.getGeoLocation().longitude))
          .title(driver.getDriverInfoModel().getName())
          .snippet(driver.getDriverInfoModel().getPhone())
          .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar))
          .flat(true);
      mGoogleMap.addMarker(orderOptions);
   }

   @Override
   public void onOrderInfoLoadSuccess(DriverGeoModel driverGeoModel) {}

   @Override
   public void onFirebaseLoadFailed(String message) {
      Log.w(TAG, "firebase load failed");
   }
}
