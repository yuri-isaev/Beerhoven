package ru.mobile.beerhoven.ui.store.catalog;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ru.mobile.beerhoven.interfaces.CrudRepository;
import ru.mobile.beerhoven.models.Item;

public class CatalogRepository implements CrudRepository<Item> {

   private static CatalogRepository sInstance;
   private static List<Item> mDataList = new ArrayList<>();
   private MutableLiveData<List<Item>> mMutableList = new MutableLiveData<>();

   private CatalogRepository() {}

   // Singleton pattern.
   public static CatalogRepository getInstance() {
      if (sInstance == null) {
         sInstance = new CatalogRepository();
      }
      return sInstance;
   }

   @Override
   public MutableLiveData<List<Item>> getList() {
      if (mDataList.size() == 0) {
         loadList();
      }
      mMutableList.setValue(mDataList);
      return mMutableList;
   }

   private void loadList() {
      for (int i = 1; i <= 10; i++)
         mDataList.add(new Item());
   }
}

