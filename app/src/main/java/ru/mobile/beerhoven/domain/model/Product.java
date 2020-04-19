package ru.mobile.beerhoven.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Product {
   private String id;
   private String country;
   private String description;
   private String density;
   private String fortress;
   private String manufacture;
   private String name;
   private double price;
   private double total;
   private String quantity;
   private String style;
   private String url;
}
