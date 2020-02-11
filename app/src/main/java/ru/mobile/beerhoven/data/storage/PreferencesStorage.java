package ru.mobile.beerhoven.data.storage;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import ru.mobile.beerhoven.utils.Constants;

/**
 * Class for accessing SharedPreferences,
 * used to control the value of the counter.
 */
public class PreferencesStorage {
   private final SharedPreferences mSharedPref;
   private int mCounterValue;

   public PreferencesStorage(Application context) {
      this.mSharedPref = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
   }

   public void onSetIntCount() {
      this.mCounterValue = mSharedPref.getInt(Constants.COUNTER_VALUE, 0);
   }

   public void onCounterSave() {
      SharedPreferences.Editor editor = mSharedPref.edit();
      editor.putInt(Constants.COUNTER_VALUE, mCounterValue).apply();
   }

   public void onDeleteCounter() {
      mSharedPref.edit().remove(Constants.COUNTER_VALUE).apply();
   }
}
