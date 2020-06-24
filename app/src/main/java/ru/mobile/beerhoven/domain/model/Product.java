package ru.mobile.beerhoven.domain.model;

import lombok.Data;

@Data
public class Product {
   private String id;
   private String capacity;
   private String category;
   private String country;
   private String density;
   private String description;
   private String fortress;
   private String image;
   private String name;
   private double price;
   private String quantity;
   private String style;
   private double total;
}
