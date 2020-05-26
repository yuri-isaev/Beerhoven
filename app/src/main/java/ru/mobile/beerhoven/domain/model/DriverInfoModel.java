package ru.mobile.beerhoven.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverInfoModel {
   private String name;
   private String phoneNo;
   private String email;

   public DriverInfoModel() {}

   public DriverInfoModel(String name, String phoneNo, String email) {
      this.name = name;
      this.phoneNo = phoneNo;
      this.email = email;
   }
}
