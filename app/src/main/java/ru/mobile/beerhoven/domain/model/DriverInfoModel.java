package ru.mobile.beerhoven.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class DriverInfoModel {
   private String email;
   private String name;
   private String phone;
}
