package ru.mobile.beerhoven.domain.repository;

public interface IPreferencesStorage {
   String getUserName();
   void onSaveUserName(String name);
   int onGetNewsCountValue();
   void onSaveNewsCountValue(int value);
   void onDeleteNewsCountValue();
   void onSaveCartCountValue(int value);
   int onGetCartCountValue();
   void onDeleteCartCountValue();
   int onGetNotificationCountValue();
   void onSaveNotificationCountValue(int value);
}
