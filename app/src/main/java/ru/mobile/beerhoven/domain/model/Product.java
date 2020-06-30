package ru.mobile.beerhoven.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Data
@RequiredArgsConstructor
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
   private String quantity;
   private String style;
   private double price;
   private double total;
}
