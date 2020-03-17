package ru.mobile.beerhoven.domain.model;

import androidx.annotation.Nullable;

public class Product {
   private String id;
   private String country;
   private String description;
   private String density;
   private String fortress;
   private String manufacture;
   private String name;
   private String phone;
   private double price;
   private double total;
   private String quantity;
   private String style;
   private String url;

   public Product() {}

   public Product(String id, String country, String description, String density, String fortress, String manufacture,
                  String name, String phone, double price, double total, String quantity, String style, String url) {
      this.id = id;
      this.country = country;
      this.description = description;
      this.density = density;
      this.fortress = fortress;
      this.manufacture = manufacture;
      this.name = name;
      this.phone = phone;
      this.price = price;
      this.total = total;
      this.quantity = quantity;
      this.style = style;
      this.url = url;
   }

   @Override
   public boolean equals(@Nullable Object obj) {
      assert obj != null;
      return id.equals(((Product) obj).id);
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getDensity() {
      return density;
   }

   public void setDensity(String density) {
      this.density = density;
   }

   public String getFortress() {
      return fortress;
   }

   public void setFortress(String fortress) {
      this.fortress = fortress;
   }

   public String getManufacture() {
      return manufacture;
   }

   public void setManufacture(String manufacture) {
      this.manufacture = manufacture;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public double getPrice() {
      return price;
   }

   public void setPrice(double price) {
      this.price = price;
   }

   public double getTotal() {
      return total;
   }

   public void setTotal(double total) {
      this.total = total;
   }

   public String getQuantity() {
      return quantity;
   }

   public void setQuantity(String quantity) {
      this.quantity = quantity;
   }

   public String getStyle() {
      return style;
   }

   public void setStyle(String style) {
      this.style = style;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }
}
