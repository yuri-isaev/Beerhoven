package ru.mobile.beerhoven.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Product {
   private String country;
   private String density;
   private String description;
   private String fortress;
   private String id;
   private String manufacture;
   private String name;
   private double price;
   private String quantity;
   private String style;
   private double total;
   private String uri;
}
