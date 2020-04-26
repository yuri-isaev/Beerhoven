package ru.mobile.beerhoven.data.local;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import ru.mobile.beerhoven.domain.repository.IPreferencesStorage;
import ru.mobile.beerhoven.utils.Constants;

/**
 * Class accessing SharedPreferences, used to save the value of the counter.
 */
public class PreferencesStorage implements IPreferencesStorage {
   private final SharedPreferences mSettings;
   private final SharedPreferences.Editor mEditor;

   public PreferencesStorage(@NonNull Application context) {
      this.mSettings = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
      this.mEditor = mSettings.edit();
   }

   public String getUserNameToStorage() {
      return mSettings.getString(Constants.CURRENT_USER_NAME, "");
   }

   public int onGetNewsCountValue() {
      return mSettings.getInt(Constants.COUNTER_NEWS_VALUE, 0);
   }

   public void onSaveNewsCountValue(int value) {
      mEditor.putInt(Constants.COUNTER_NEWS_VALUE, value).apply();
   }

   public void onDeleteNewsCountValue() {
      mEditor.remove(Constants.COUNTER_NEWS_VALUE).apply();
   }

   public void onSaveCartCountValue(int value) {
      mEditor.putInt(Constants.COUNTER_CART_VALUE, value).apply();
   }

   public void onSaveUserName(String name) {
      mEditor.putString(Constants.CURRENT_USER_NAME, name).apply();
   }

   public int onGetCartCountValue() {
      return mSettings.getInt(Constants.COUNTER_CART_VALUE, 0);
   }

   public void onDeleteCartCountValue() {
      mEditor.remove(Constants.COUNTER_CART_VALUE).apply();
   }
}
