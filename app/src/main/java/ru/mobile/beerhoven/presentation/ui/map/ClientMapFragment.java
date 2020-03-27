package ru.mobile.beerhoven.presentation.ui.map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import static com.google.android.gms.maps.GoogleMap.*;
import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Locale;

import ru.mobile.beerhoven.R;

public class ClientMapFragment extends Fragment implements OnMapReadyCallback, OnInfoWindowClickListener, InfoWindowAdapter {
   private Activity mActivity;
   private FusedLocationProviderClient mFusedLocationProviderClient;
   private LocationCallback mLocationCallback;
   private GoogleMap mGoogleMap;
   private SupportMapFragment mSupportMapFragment;
   private static final String TAG = "ClientMapFragment";

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_client_map, container, false);
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
         Log.i(TAG, "activity created");
      }
   }

   @Override
   public void onMapReady(GoogleMap googleMap) {
      this.mGoogleMap = googleMap;
      googleMap.setMapType(MAP_TYPE_NORMAL);
      mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

      // Request permission to add current location - pinpoint location using GPS
      Dexter.withContext(getContext()).withPermission(ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
         @Override
         public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
            if (checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
                checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
               return;
            }

            // My location enable
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mGoogleMap.setOnMyLocationButtonClickListener(() -> {
               if (checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
                  return false;
               }

               // Last known location query
               mFusedLocationProviderClient.getLastLocation()
                   .addOnFailureListener(error -> Log.e(TAG, error.getMessage()))
                   .addOnSuccessListener(location -> {
                  LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                  mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 18f));
               });
               return true;
            });

            View mLocationButton = ((View) mSupportMapFragment.requireView()
                .findViewById(Integer.parseInt("1")).getParent())
                .findViewById(Integer.parseInt("2"));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLocationButton.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            params.setMargins(0, 0, 40, 400);
         }

         @Override
         public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
            Log.w(TAG, permissionDeniedResponse.getPermissionName() + " need enable");
         }

         @Override
         public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {}
      })
          .check();

      mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

      Geocoder mGeocoder = new Geocoder(mActivity, Locale.getDefault());
      ClientMapViewModel viewModel = new ClientMapViewModel(getActivity(), requireNonNull(getActivity()).getApplicationContext(), mGoogleMap);

      viewModel.onGetDriverLocationToUseCase();
      viewModel.onGetOrderLocationToUseCase(mFusedLocationProviderClient, mGeocoder);
   }

   @Override
   public View getInfoWindow(Marker marker) {
      return null;
   }

   @Override
   public View getInfoContents(Marker marker) {

      @SuppressLint("InflateParams")
      View layout = mActivity.getLayoutInflater().inflate(R.layout.window_info, null);
      TextView locality = (TextView) layout.findViewById(R.id.locality);
      TextView snippet = (TextView) layout.findViewById(R.id.snippet);
      locality.setText(marker.getTitle());
      snippet.setText(marker.getSnippet());

      return null;
   }

   @Override
   public void onInfoWindowClick(Marker marker) {}

   @Override
   public void onDestroy() {
      super.onDestroy();
      mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
   }
}
