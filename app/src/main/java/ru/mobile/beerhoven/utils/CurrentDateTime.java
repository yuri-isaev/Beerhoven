package ru.mobile.beerhoven.utils;

import static ru.mobile.beerhoven.utils.Constants.*;
import static ru.mobile.beerhoven.utils.Constants.CURRENT_DATA;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.N)
public final class CurrentDateTime {

   static Calendar calForDate = Calendar.getInstance();

   @SuppressLint("SimpleDateFormat")
   public static String getCurrentDate() {
      return new SimpleDateFormat(CURRENT_DATA).format(calForDate.getTime());
   }

   @SuppressLint("SimpleDateFormat")
   public static String getCurrentTime() {
      return new SimpleDateFormat(CURRENT_TIME).format(calForDate.getTime());
   }
}
