package ru.mobile.beerhoven.domain.model;

import lombok.Data;

/**
 * Order Id == user phone number
 * Order phone == user phone number to confirm
 */
@Data
public class Order {
   private String id;
   private String address;
   private String color;
   private String contactName;
   private String date;
   private String phone;
   private String time;
   private double total;
}
