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
}
