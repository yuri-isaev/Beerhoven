package ru.mobile.beerhoven.presentation.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.domain.model.DriverGeoModel;
import ru.mobile.beerhoven.domain.model.DriverInfoModel;
import ru.mobile.beerhoven.domain.model.GeoQueryModel;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.presentation.interfaces.IFirebaseDriverInfoListener;
import ru.mobile.beerhoven.presentation.interfaces.IFirebaseFailedListener;
import ru.mobile.beerhoven.utils.Constants;

public class ClientMapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener,
    GoogleMap.InfoWindowAdapter, OnMapReadyCallback, IFirebaseDriverInfoListener, IFirebaseFailedListener {

   private Activity mActivity;
   private boolean terrain = true;
   private float mOrderColorMarker;
   private GoogleMap mMap;
   private Geocoder mGeocoder;
   private final List<Order> mOrderList = new ArrayList<>();
   private String mCityName;
   private String mOrderName;
   private SupportMapFragment mSupportMapFragment;

   // Location
   private double mDistance = 1.0;
   private static final double LIMIT_RANGE = 10.0;
   private FusedLocationProviderClient mFusedLocationProviderClient;
   private Location mCurrentLocation;
   private Location mPreviousLocation;
   private LocationRequest mLocationRequest;
   private LocationCallback mLocationCallback;

   private IFirebaseDriverInfoListener iFirebaseDriverInfoListener;
   private IFirebaseFailedListener iFirebaseFailedListener;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_client_map, container, false);
      initializeCurrentLocation();
      loadOrderOnMap();
      return view;
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
      if (mSupportMapFragment != null) {
         mSupportMapFragment.getMapAsync(this);
      }
   }

   @Override
   public void onStart() {
      super.onStart();
      // An activity is created and applied as a context
      mActivity = getActivity();
      if (isAdded() && mActivity != null) {
         Toasty.success(requireActivity(), "activity created", Toast.LENGTH_LONG, true).show();
      }
   }

   private void initializeCurrentLocation() {
      iFirebaseDriverInfoListener = this;
      iFirebaseFailedListener = this;

      if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
          ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         Toasty.error(requireActivity(), "permission r1", Toast.LENGTH_LONG, true).show();
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
            super.onLocationResult(locationResult); // Called when there is information about the location of the device

            // Approaching an object during location initialization
            LatLng newPosition = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 18f));

            // If the user has changed the terrain, calculate and download the driver again
            if (terrain) {
               mPreviousLocation = mCurrentLocation = locationResult.getLastLocation();
               terrain = false;
            } else {
               mPreviousLocation = mCurrentLocation;
               mCurrentLocation = locationResult.getLastLocation();
            }

            // Range limits
            if (mPreviousLocation.distanceTo(mCurrentLocation) / 1000 <= LIMIT_RANGE)
               loadDriverOnMap();
            else {
               Toasty.error(requireActivity(), "display limit exceeded", Toast.LENGTH_LONG, true).show();
            }
         }
      };

      if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
          ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         Toasty.error(requireActivity(), "permission r2", Toast.LENGTH_LONG, true).show();
         return;
      }

      // Main input class for getting location information
      mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
      mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

      loadDriverOnMap();
   }

   @SuppressLint("MissingPermission")
   private void loadDriverOnMap() {
      mFusedLocationProviderClient.getLastLocation()
          .addOnFailureListener(e -> {
             Toasty.error(requireActivity(), "error load driver", Toast.LENGTH_LONG, true).show();
          })
          .addOnSuccessListener(location -> {
             // Load all drivers in city on map
             mGeocoder = new Geocoder(mActivity, Locale.getDefault());
             try {
                List<Address> list = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (list.size() > 0) {
                   mCityName = list.get(0).getLocality();

                   // Firebase query for city name to work with distance
                   DatabaseReference driver_location_ref = FirebaseDatabase.getInstance().getReference(Geo.DRIVER_LOCATION_REFERENCES).child(mCityName);
                   GeoFire geoFire = new GeoFire(driver_location_ref);
                   GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), mDistance);

                   geoQuery.removeAllListeners(); // Remove all event listeners for GeoQuery

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
                         Toasty.error(requireActivity(), error.getMessage(), Toast.LENGTH_LONG, true).show();
                      }
                   });

                   // Firebase listener adding new driver
                   driver_location_ref.addChildEventListener(new ChildEventListener() {
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

                         if (newDistance <= LIMIT_RANGE)
                            findDriverByKey(driverGeoModel); // If the driver is in the coverage area, add him to the map
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

   private void addDriverMarker() {
      if (Geo.driverSet.size() > 0) {
         Observable.fromIterable(Geo.driverSet)    // Observable from it with individual set items
             .subscribeOn(Schedulers.newThread())  // the thread on which the operation is being performed
             .observeOn(AndroidSchedulers.mainThread()) // the thread where the result of the operation is returned
             .subscribe(new Consumer<DriverGeoModel>() {
                           @Override
                           public void accept(DriverGeoModel driverGeoModel) throws Throwable {
                              ClientMapFragment.this.findDriverByKey(driverGeoModel);
                           }
                        },
                 new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                       Toasty.error(ClientMapFragment.this.requireActivity(), "thread error", Toast.LENGTH_SHORT, true).show();
                    }
                 },
                 () -> {
             });
      } else {
         Toasty.error(requireActivity(), "driver not found", Toast.LENGTH_SHORT, true).show();
      }
   }

   @Override
   public void onMapReady(GoogleMap googleMap) {
      mMap = googleMap;
      googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

      // Request permission to add current location - pinpoint location using GPS
      Dexter.withContext(getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
         @Override
         public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               return;
            }

            // My location enable
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setOnMyLocationButtonClickListener(() -> {
               if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                  return false;
               }

               // Last known location query
               mFusedLocationProviderClient.getLastLocation()
                   .addOnFailureListener(e -> Toasty.error(requireActivity(), Objects.requireNonNull(e.getMessage()), Toast.LENGTH_LONG, true).show())
                   .addOnSuccessListener(location -> {
                      LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 18f));
                   });
               return true;
            });

            View locationButton = ((View) mSupportMapFragment.requireView()
                .findViewById(Integer.parseInt("1"))
                .getParent()).findViewById(Integer.parseInt("2"));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            params.setMargins(0, 0, 40, 400); // Move the view to see the zoom control
         }

         @Override
         public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
            Toasty.warning(requireActivity(), permissionDeniedResponse.getPermissionName() + " need enable", Toast.LENGTH_SHORT, true).show();
         }

         @Override
         public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {}
      })
          .check();

      mMap.getUiSettings().setZoomControlsEnabled(true); // Setting zoom controls
      mMap.setOnInfoWindowClickListener(this); // Set a listener for marker click.
      mMap.setInfoWindowAdapter(this);
   }

   @Override
   public void onDriverInfoLoadSuccess(DriverGeoModel driverGeoModel) {
      // if (!Geo.markerList.containsKey(driverGeoModel.getKey())) // If there is already a marker with this key, then do not load it again
      Geo.markerList.put(driverGeoModel.getKey(),
          mMap.addMarker(new MarkerOptions()
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
                     Geo.markerList.get(driverGeoModel.getKey()).remove();
                  Geo.markerList.remove(driverGeoModel.getKey());
                  driverLocation.removeEventListener(this);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               Toasty.warning(requireActivity(), "driver info load success", Toast.LENGTH_LONG, true).show();
            }
         });
      }
   }

   @Override
   public void onOrderInfoLoadSuccess(DriverGeoModel driverGeoModel) {}

   @Override
   public void onFirebaseLoadFailed(String message) {
      Toasty.warning(requireActivity(), "firebase load failed", Toast.LENGTH_LONG, true).show();
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

   private void loadOrderOnMap() {
      DatabaseReference order_location_ref = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_CONFIRMS);

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
            Toast.makeText(getActivity(),  error.getMessage(), Toast.LENGTH_SHORT).show();
         }
      };

      order_location_ref.addValueEventListener(vListener);

      if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
          ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         Toasty.error(requireActivity(), "permission r3", Toast.LENGTH_LONG, true).show();
         return;
      }

      mFusedLocationProviderClient.getLastLocation()
          .addOnFailureListener(error ->
              Toasty.error(requireActivity(), "error load order", Toast.LENGTH_LONG, true).show())
          .addOnSuccessListener(location -> {
             mGeocoder = new Geocoder(requireContext(), Locale.getDefault());

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
                          //.title(orderPhone)
                          .snippet(address.getAddressLine(0))
                          .icon(BitmapDescriptorFactory.defaultMarker(mOrderColorMarker));

                      // markerOptions.position(new LatLng(lat, lon));
                      Marker marker = mMap.addMarker(markerOptions);
                   }

                } catch (IOException e) {
                   e.printStackTrace();
                }
             }
          });
   }

   @Override
   public void onInfoWindowClick(Marker marker) {}

   @Override
   public View getInfoWindow(Marker marker) {
      return null;
   }

   @Override
   public View getInfoContents(Marker marker) {
      View layout = getLayoutInflater().inflate(R.layout.window_info, null);
      TextView t1_locality = (TextView) layout.findViewById(R.id.locality);
      TextView t2_snippet = (TextView) layout.findViewById(R.id.snippet);

      t1_locality.setText(marker.getTitle());
      t2_snippet.setText(marker.getSnippet());

      return layout;
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
      mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
   }
}
