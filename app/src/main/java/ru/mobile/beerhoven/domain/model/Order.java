package ru.mobile.beerhoven.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Order Id == user phone number
 * Order phone == user phone number to confirm
 */
@Getter
@NoArgsConstructor
@Setter
public class Order {
   private String address;
   private String color;
   private String contactName;
   private String date;
   private String id;
   private String phone;
   private String time;
   private double total;
}
