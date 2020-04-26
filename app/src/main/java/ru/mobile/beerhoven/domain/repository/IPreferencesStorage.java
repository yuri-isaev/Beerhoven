package ru.mobile.beerhoven.domain.repository;

public interface IPreferencesStorage {
   String getUserNameToStorage();
   int onGetNewsCountValue();
   void onSaveNewsCountValue(int value);
   void onDeleteNewsCountValue();
   void onSaveCartCountValue(int value);
   void onSaveUserName(String name);
   int onGetCartCountValue();
   void onDeleteCartCountValue();
}
