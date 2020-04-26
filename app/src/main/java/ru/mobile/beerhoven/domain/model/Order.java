package ru.mobile.beerhoven.domain.model;

import lombok.Data;

@Data
public class Order {
   private String id;
   private String address;
   private String color;
   private String date;
   private String name;
   private String phone;
   private String time;
   private double total;
}
