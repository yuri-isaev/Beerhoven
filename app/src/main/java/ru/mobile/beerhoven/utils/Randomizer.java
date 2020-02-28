package ru.mobile.beerhoven.utils;

import android.graphics.Color;

import java.util.Random;

public class Randomizer {
   static Random rnd = new Random();
   
   public int getRandomColor() {
      return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
   }
   
   public static float getRandomColorMarker() {
      return rnd.nextInt(360);
   }

   public static int getRandomInt() {
      return rnd.nextInt(9999 - 1000) + 1000;
   }
}
