package ru.mobile.beerhoven.utils;

import static ru.mobile.beerhoven.utils.Constants.CURRENT_DATA;
import static ru.mobile.beerhoven.utils.Constants.CURRENT_TIME;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

@SuppressLint({"SimpleDateFormat", "NewApi"})
public final class CurrentDateTime {
   static Calendar calForDate = Calendar.getInstance();

   public static String getCurrentDate() {
      return new SimpleDateFormat(CURRENT_DATA).format(calForDate.getTime());
   }

   public static String getCurrentTime() {
      return new SimpleDateFormat(CURRENT_TIME).format(calForDate.getTime());
   }

   @NonNull
   public static String parseDateTime(String date) {
      Date input = new Date(date);
      SimpleDateFormat format = new SimpleDateFormat("dd LLL yyyy");
      format.setTimeZone(TimeZone.getTimeZone("UTC"));
      date = format.format(input);
      return date;
   }
}
