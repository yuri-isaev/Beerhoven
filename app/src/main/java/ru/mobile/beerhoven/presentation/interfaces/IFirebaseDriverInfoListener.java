package ru.mobile.beerhoven.presentation.interfaces;

import ru.mobile.beerhoven.domain.model.DriverGeoModel;

public interface IFirebaseDriverInfoListener {
    void onDriverInfoLoadSuccess(DriverGeoModel driverGeoModel);
    void onOrderInfoLoadSuccess(DriverGeoModel driverGeoModel);
}
