package ru.mobile.beerhoven.domain.model;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
   ALL("Все товары"),
   ALCOHOL("Алкоголь"),
   PRODUCTS("Продукты");

   private final String value;

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
