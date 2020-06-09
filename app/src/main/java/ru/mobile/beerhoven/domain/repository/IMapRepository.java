package ru.mobile.beerhoven.domain.repository;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.DriverGeoModel;
import ru.mobile.beerhoven.domain.model.Order;

public interface IMapRepository {
   MutableLiveData<List<Order>> getOrderLocationListFromDatabase();
   void onAddDriverMarker();
   void onFindDriverByKey(DriverGeoModel driverGeoModel);
   void onGetDriverLocationFromDatabase(Location location, String cityName);
}
