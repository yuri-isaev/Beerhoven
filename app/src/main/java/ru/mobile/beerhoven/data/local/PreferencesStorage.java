package ru.mobile.beerhoven.data.local;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import ru.mobile.beerhoven.utils.Constants;

/**
 * Class accessing SharedPreferences, used to save the value of the counter.
 */
public class PreferencesStorage {
   private final SharedPreferences mSharedPref;
   private final SharedPreferences.Editor mEditor;
   private int mCounterValue;

   public PreferencesStorage(Application context) {
      this.mSharedPref = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
      this.mEditor = mSharedPref.edit();
   }

   public void onSetIntCount() {
      this.mCounterValue = mSharedPref.getInt(Constants.COUNTER_VALUE, 0);
   }

   public void onSaveCounter() {
      mEditor.putInt(Constants.COUNTER_VALUE, mCounterValue).apply();
   }

   public void onDeleteCounter() {
      mEditor.remove(Constants.COUNTER_VALUE).apply();
   }

   public void onSaveUserName(String name) {
      mEditor.putString(Constants.CURRENT_USER_NAME, name).apply();
   }
}
