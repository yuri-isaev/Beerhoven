package ru.mobile.beerhoven.utils;

import static ru.mobile.beerhoven.utils.Constants.*;
import static ru.mobile.beerhoven.utils.Constants.CURRENT_DATA;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint({"SimpleDateFormat", "NewApi"})
public final class CurrentDateTime {
   static Calendar calForDate = Calendar.getInstance();

   public static String getCurrentDate() {
      return new SimpleDateFormat(CURRENT_DATA).format(calForDate.getTime());
   }

   public static String getCurrentTime() {
      return new SimpleDateFormat(CURRENT_TIME).format(calForDate.getTime());
   }

   public static String parseDataTime(String date) {
      Date input = new Date(date);
      java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd/MM/yyyy");
      format.setTimeZone(TimeZone.getTimeZone("-5GMT"));
      date = format.format(input);
      return date;
   }
}
