package ru.mobile.beerhoven.domain.repository;

import android.location.Location;

import ru.mobile.beerhoven.domain.model.DriverGeoModel;

public interface IMapRepository {
   void getDriverLocation(Location location, String cityName);
   void addDriverMarker();
   void findDriverByKey(DriverGeoModel driverGeoModel);
}
