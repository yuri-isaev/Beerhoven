package ru.mobile.beerhoven.data.listeners;

import ru.mobile.beerhoven.domain.model.DriverGeoModel;

public interface FirebaseDriverInfoListener {
   void onDriverInfoLoadSuccess(DriverGeoModel driverGeoModel);
   void onOrderInfoLoadSuccess(DriverGeoModel driverGeoModel);
}
