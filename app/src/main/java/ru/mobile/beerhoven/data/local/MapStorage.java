package ru.mobile.beerhoven.data.local;

import android.net.Uri;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ru.mobile.beerhoven.domain.model.DriverGeoModel;

public final class MapStorage {
   // Catalog
   public static HashMap<String, String> productMap = new HashMap<>();
   public static HashMap<String, Double> priceMap = new HashMap<>();
   public static HashMap<String, Uri> uriMap = new HashMap<>();

   // Geo
   public static HashMap<String, Marker> markerList = new HashMap<>();
   public static Set<DriverGeoModel> driverSet = new HashSet<DriverGeoModel>();
}
