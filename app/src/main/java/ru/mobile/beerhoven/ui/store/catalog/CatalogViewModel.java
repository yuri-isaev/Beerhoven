package ru.mobile.beerhoven.ui.store.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.data.storage.CrudRepository;
import ru.mobile.beerhoven.models.Item;

public class CatalogViewModel extends ViewModel {
   private MutableLiveData mCatalogList;
   private MutableLiveData<String> mResponse;
   private final CrudRepository<Item> mRepository;

   public CatalogViewModel(CrudRepository<Item> repository) {
      this.mCatalogList = new MutableLiveData();
      this.mRepository = repository;
   }

   public MutableLiveData getCatalogList() {
      mCatalogList = mRepository.readList();
      return mCatalogList;
   }

   public LiveData<String> getReportAddCart() {
      mResponse = mRepository.createItem();
      return mResponse;
   }

   public LiveData<String> getResponseDeleteItem() {
      mResponse = mRepository.deleteItem();
      return mResponse;
   }
}
