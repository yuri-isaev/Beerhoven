package ru.mobile.beerhoven.presentation.ui.map;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ru.mobile.beerhoven.domain.model.DriverGeoModel;

public class Geo {
    public static final String DRIVER_LOCATION_REFERENCES = "DriverLocation";
    public static final String DRIVER_INFO_REFERENCES = "DriverInfo";
    
    public static HashMap<String, Marker> markerList = new HashMap<>();
    public static Set<DriverGeoModel> driverSet = new HashSet<DriverGeoModel>();
    
    public static String buildTitle(String title) {
        return new StringBuilder(title).toString();
    }
}
