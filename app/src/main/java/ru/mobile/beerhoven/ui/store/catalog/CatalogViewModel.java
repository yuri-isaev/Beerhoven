package ru.mobile.beerhoven.ui.store.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.data.repository.CatalogRepository;

public class CatalogViewModel extends ViewModel {
   private MutableLiveData mCatalogList;
   private MutableLiveData<String> mResponse;

   public CatalogViewModel() {
      this.mCatalogList = new MutableLiveData();
   }

   public MutableLiveData getCatalogList() {
      mCatalogList = CatalogRepository.getInstance().readList();
      return mCatalogList;
   }

   public LiveData<String> getReportAddCart() {
      mResponse = CatalogRepository.getInstance().createItem();
      return mResponse;
   }

   public LiveData<String> getResponseDeleteItem() {
      mResponse = CatalogRepository.getInstance().deleteItem();
      return mResponse;
   }
}
