package ru.mobile.beerhoven.presentation.ui.user.map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.MapRepository;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.utils.Permission;

public class CustomerMapFragment extends Fragment implements OnMapReadyCallback, OnInfoWindowClickListener, InfoWindowAdapter {
   private FusedLocationProviderClient mProviderClient;
   private GoogleMap mGoogleMap;
   private List<Order> mOrderList;
   private Permission mPermission;
   private SupportMapFragment mSupportMapFragment;
   private static final String TAG = "ClientMapFragment";

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_customer_map, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mPermission = new Permission(requireActivity());
      this.mProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
      this.mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
      if (mSupportMapFragment != null) {
         mSupportMapFragment.getMapAsync(this);
      }
   }

   @Override
   public void onMapReady(@NonNull GoogleMap googleMap) {
      this.mGoogleMap = googleMap;
      googleMap.setMapType(MAP_TYPE_NORMAL);
      CustomerMapViewModel mViewModel = new CustomerMapViewModel(getActivity(),
          requireActivity().getApplicationContext(), mGoogleMap, new MapRepository(mGoogleMap));
      mOrderList = requireNonNull(mViewModel.getOrderListFromRepository().getValue());

      // Request permission to add current location - pinpoint location using GPS
      Dexter.withContext(getActivity())
          .withPermission(ACCESS_FINE_LOCATION)
          .withListener(new PermissionListener() {
             @Override
             public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (mPermission.checkLocationPermission()) {
                   mPermission.requestLocationPermission();
                }

                // My location enable
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

                View locationButton = ((View) mSupportMapFragment
                    .requireView()
                    .findViewById(Integer.parseInt("1"))
                    .getParent())
                    .findViewById(Integer.parseInt("2"));

                LayoutParams params = (LayoutParams) locationButton.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                params.setMargins(0, 0, 40, 400);
             }

             @Override
             public void onPermissionDenied(PermissionDeniedResponse response) {
                Log.w(TAG, response.getPermissionName() + " need enable");
             }
             @Override
             public void onPermissionRationaleShouldBeShown(PermissionRequest request, PermissionToken token) {}
          })
          .check();

      mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
      mViewModel.onGetDriverLocationToUseCase();
      onGetOrderLocationMarker();
   }

   private void onGetOrderLocationMarker() {
      Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
      if (mPermission.checkLocationPermission()) {
         mPermission.requestLocationPermission();
      }

      mProviderClient.getLastLocation()
          .addOnFailureListener(error -> Log.e(TAG, "error load order", error))
          .addOnSuccessListener(location -> {
             for (int i = 0; i < mOrderList.size(); i++) {
                List<Address> addressList;
                try {
                   addressList = geocoder.getFromLocationName(mOrderList.get(i).getAddress(), 2);

                   if (addressList.size() > 0) {
                      Address address = addressList.get(0);
                      LatLng orderPosition = new LatLng(address.getLatitude(), address.getLongitude());

                      // Customer order location
                      MarkerOptions orderOptions = new MarkerOptions()
                          .icon(BitmapDescriptorFactory.defaultMarker(Float.parseFloat(mOrderList.get(0).getColor())))
                          .position(orderPosition)
                          .snippet(address.getAddressLine(0))
                          .title(mOrderList.get(i).getContactName());
                      mGoogleMap.addMarker(orderOptions);

                      // Static store location
                      LatLng beerhoven = new LatLng(54.688795, 20.531652);
                      MarkerOptions storeOption = new MarkerOptions()
                          .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_map_beerhoven))
                          .position(beerhoven)
                          .snippet("Калининград, ул. Дзержинского, 98")
                          .title("Beerhoven");
                      mGoogleMap.addMarker(storeOption);
                      mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(beerhoven, 16));
                   }
                } catch (IOException e) {
                   e.printStackTrace();
                }
             }
          });
   }

   @Override
   public View getInfoWindow(Marker marker) {
      return null;
   }

   @Override
   public View getInfoContents(@NonNull Marker marker) {
      @SuppressLint("InflateParams")
      View layout = requireActivity().getLayoutInflater().inflate(R.layout.window_info, null);
      TextView locality = layout.findViewById(R.id.locality);
      TextView snippet = layout.findViewById(R.id.snippet);
      locality.setText(marker.getTitle());
      snippet.setText(marker.getSnippet());
      return null;
   }

   @NonNull
   private BitmapDescriptor bitmapDescriptorFromVector(Context ctx, @DrawableRes int vectorDrawableResourceId) {
      Drawable drawable = ContextCompat.getDrawable(ctx, vectorDrawableResourceId);
      assert drawable != null;
      drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
      Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
          drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(bitmap);
      drawable.draw(canvas);
      return BitmapDescriptorFactory.fromBitmap(bitmap);
   }

   @Override
   public void onInfoWindowClick(Marker marker) {
      NavDirections action = CustomerMapFragmentDirections.actionNavMapToNavOrders()
          .setOrderId(new Order().getId());
      Navigation.findNavController(requireView()).navigate(action);
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
      //FusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
   }
}
