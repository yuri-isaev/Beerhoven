package ru.mobile.beerhoven.domain.enums;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public enum Category {
   ALCOHOL("Алкоголь"),
   PRODUCTS("Продукты");

   private final String value;

   Category(String value) {
      this.value = value;
   }

   public String getValue() {
      return value;
   }

   @NonNull
   @Contract(" -> new")
   public static String[] getValues() {
      String[] categories = new String[Category.values().length];
      for (int i = 0; i < Category.values().length; i++) {
         categories[i] = Category.values()[i].value;
      }
      return categories;
   }
}
