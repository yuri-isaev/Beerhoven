package ru.mobile.beerhoven.ui.store.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.models.Item;

public class CatalogViewModel extends ViewModel {

   private MutableLiveData<List<Item>> mCatalogList;

   public void initList() {
      if (mCatalogList != null) {
         return;
      }
      mCatalogList = CatalogRepository.getInstance().getList();
   }

   public LiveData<List<Item>> getCatalogList() {
      return mCatalogList;
   }
}
