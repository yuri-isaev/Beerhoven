package ru.mobile.beerhoven.models;

import androidx.annotation.Nullable;

public class Item {
   private String id;
   private String title;
   private String style;
   private String fortress;
   private double price;

   public Item() {}

   public Item(String id, String title, double price, String style, String fortress) {
      this.id = id;
      this.title = title;
      this.price = price;
      this.style = style;
      this.fortress = fortress;
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
      return title;
   }

   public void setTitle(String name) {
      this.title = name;
   }

   public double getPrice() {
      return price;
   }

   public void setPrice(double price) {
      this.price = price;
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
}
