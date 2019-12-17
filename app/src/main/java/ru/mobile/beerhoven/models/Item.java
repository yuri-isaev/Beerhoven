package ru.mobile.beerhoven.models;

import androidx.annotation.Nullable;

public class Item {
   private String id;// == pushID(PID)
   private String name;
   private String country;
   private String manufacture;
   private String density;
   private String description;
   private String quantity;
   private String style;
   private String fortress;
   private String address;
   private String phone;
   private String time;
   private String date;
   private String url;
   private double price;
   private double total;
   private double common;
   private float color;

   public Item() {}

   public Item(String id, String title, String country, String manufacture, String density, String description, String quantity,
               String style, String fortress, String address, String phone, String time, String date, String url,
               double price, double total, double common, float color) {
      this.id = id;
      this.name = title;
      this.country = country;
      this.manufacture = manufacture;
      this.density = density;
      this.description = description;
      this.quantity = quantity;
      this.style = style;
      this.fortress = fortress;
      this.address = address;
      this.phone = phone;
      this.time = time;
      this.date = date;
      this.url = url;
      this.price = price;
      this.total = total;
      this.common = common;
      this.color = color;
   }

   @Override
   public boolean equals(@Nullable Object obj) {
      assert obj != null;
      return id.equals(((Item) obj).id);
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getTitle() {
      return name;
   }

   public void setTitle(String title) {
      this.name = title;
   }

   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public String getManufacture() {
      return manufacture;
   }

   public void setManufacture(String manufacture) {
      this.manufacture = manufacture;
   }

   public String getDensity() {
      return density;
   }

   public void setDensity(String density) {
      this.density = density;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
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

   public String getFortress() {
      return fortress;
   }

   public void setFortress(String fortress) {
      this.fortress = fortress;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public String getTime() {
      return time;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
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

   public double getCommon() {
      return common;
   }

   public void setCommon(double common) {
      this.common = common;
   }

   public float getColor() {
      return color;
   }

   public void setColor(float color) {
      this.color = color;
   }
}
