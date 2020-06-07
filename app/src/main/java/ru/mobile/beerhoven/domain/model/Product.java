package ru.mobile.beerhoven.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
public class Product {
   private String category;
   private String country;
   private String density;
   private String description;
   private String fortress;
   private String id;
   private String image;
   private String manufacture;
   private String name;
   private double price;
   private String quantity;
   private String style;
   private double total;
}
