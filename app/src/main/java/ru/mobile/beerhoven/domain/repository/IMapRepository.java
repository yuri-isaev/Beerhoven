package ru.mobile.beerhoven.domain.repository;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;

import ru.mobile.beerhoven.domain.model.DriverGeoModel;

public interface IMapRepository {
   void onAddDriverMarker();
   void onGetDriverLocation(Location location, String cityName);
   void onGetOrderLocation(FusedLocationProviderClient providerClient, Context context, Geocoder geocoder);
   void onFindDriverByKey(DriverGeoModel driverGeoModel);
}
