package ru.mobile.beerhoven.data.local;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ru.mobile.beerhoven.domain.model.DriverGeoModel;

public final class MapStorage {
   // Geo
   public static HashMap<String, Marker> markerList = new HashMap<>();
   public static Set<DriverGeoModel> driverSet = new HashSet<DriverGeoModel>();
}
