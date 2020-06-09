package ru.mobile.beerhoven.domain.model;

import com.firebase.geofire.GeoLocation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class DriverGeoModel {
   private String key;
   private GeoLocation geoLocation;
   private DriverInfoModel driverInfoModel;

   public DriverGeoModel(String key, GeoLocation geoLocation) {
      this.key = key;
      this.geoLocation = geoLocation;
   }
}
