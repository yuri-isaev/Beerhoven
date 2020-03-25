package ru.mobile.beerhoven.data.remote;

import android.location.Location;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.domain.model.DriverGeoModel;
import ru.mobile.beerhoven.domain.model.DriverInfoModel;
import ru.mobile.beerhoven.domain.model.GeoQueryModel;
import ru.mobile.beerhoven.domain.repository.IMapRepository;
import ru.mobile.beerhoven.presentation.interfaces.IFirebaseDriverInfoListener;
import ru.mobile.beerhoven.presentation.interfaces.IFirebaseFailedListener;
import ru.mobile.beerhoven.presentation.ui.map.Geo;

public class MapRepository implements IMapRepository, IFirebaseDriverInfoListener, IFirebaseFailedListener {
   private double mDistance = 1.0;
   private static final double LIMIT_RANGE = 10.0;
   private final GoogleMap mGoogleMap;
   private static final String TAG = "MapRepository";

   private final IFirebaseDriverInfoListener iFirebaseDriverInfoListener;
   private final IFirebaseFailedListener iFirebaseFailedListener;

   public MapRepository(GoogleMap map) {
      this.mGoogleMap = map;
      this.iFirebaseDriverInfoListener = this;
      this.iFirebaseFailedListener = this;
   }

   @Override
   public void getDriverLocation(@NonNull Location location, String cityName) {
      DatabaseReference driverLocationRef = FirebaseDatabase.getInstance().getReference(Geo.DRIVER_LOCATION_REFERENCES).child(cityName);
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
               getDriverLocation(location, cityName); // Continue searching at a new distance
            } else {
               mDistance = 1.0; // Reset
               addDriverMarker();
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

   @Override
   public void addDriverMarker() {
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

   @Override
   public void findDriverByKey(@NonNull DriverGeoModel driverGeoModel) {
      FirebaseDatabase.getInstance().getReference(Geo.DRIVER_INFO_REFERENCES)
          .child(driverGeoModel.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
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
   public void onDriverInfoLoadSuccess(@NonNull DriverGeoModel driverGeoModel) {
      Geo.markerList.put(driverGeoModel.getKey(), mGoogleMap.addMarker(new MarkerOptions()
          .position(new LatLng(driverGeoModel.getGeoLocation().latitude, driverGeoModel.getGeoLocation().longitude))
          .title(Geo.buildTitle(driverGeoModel.getDriverInfoModel().getName()))
          .snippet(driverGeoModel.getDriverInfoModel().getPhoneNo())
          .icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar))
          .flat(true))
      );
   }

   @Override
   public void onOrderInfoLoadSuccess(DriverGeoModel driverGeoModel) {}

   @Override
   public void onFirebaseLoadFailed(String message) {
      Log.w(TAG, "firebase load failed");
   }
}
