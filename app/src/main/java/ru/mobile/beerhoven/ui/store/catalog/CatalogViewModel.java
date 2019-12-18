package ru.mobile.beerhoven.ui.store.catalog;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.interfaces.CrudRepository;
import ru.mobile.beerhoven.models.Item;

public class CatalogViewModel extends ViewModel {
   private MutableLiveData mCatalogList;
   private final CrudRepository<Item> mRepo;
   private MutableLiveData<String> mResponse;

   public CatalogViewModel(@NonNull CrudRepository<Item> repo) {
      this.mRepo = repo;
      this.mCatalogList = new MutableLiveData();
   }

   public MutableLiveData getCatalogList() {
      mCatalogList = CatalogRepository.getInstance().getList();
      return mCatalogList;
   }

   public LiveData<String> getReportAddCart() {
      mResponse = CatalogRepository.getInstance().addCartItem();
      return mResponse;
   }

   public LiveData<String> getResponseDeleteItem() {
      mResponse = CatalogRepository.getInstance().deleteCatalogItem();
      return mResponse;
   }
}
