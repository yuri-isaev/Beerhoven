package ru.mobile.beerhoven.domain.model;

/**
 * Order Id    == user phone number
 * Order phone == user phone number to confirm
 */
public class Order {
   private String address;
   private String color;
   private String contactName;
   private String date;
   private String id;
   private String phone;
   private String time;
   private double total;

   public String getId() {
      return id;
   }

   public Order setId(String id) {
      this.id = id;
      return this;
   }

   public String getAddress() {
      return address;
   }

   public Order setAddress(String address) {
      this.address = address;
      return this;
   }

   public String getColor() {
      return color;
   }

   public Order setColor(String color) {
      this.color = color;
      return this;
   }

   public String getDate() {
      return date;
   }

   public Order setDate(String date) {
      this.date = date;
      return this;
   }

   public String getContactName() {
      return contactName;
   }

   public Order setContactName(String contactName) {
      this.contactName = contactName;
      return this;
   }

   public String getPhone() {
      return phone;
   }

   public Order setPhone(String phone) {
      this.phone = phone;
      return this;
   }

   public String getTime() {
      return time;
   }

   public Order setTime(String time) {
      this.time = time;
      return this;
   }

   public double getTotal() {
      return total;
   }

   public Order setTotal(double total) {
      this.total = total;
      return this;
   }
}
