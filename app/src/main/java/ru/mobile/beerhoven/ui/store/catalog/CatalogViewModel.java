package ru.mobile.beerhoven.ui.store.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.domain.repository.ICatalogRepository;

public class CatalogViewModel extends ViewModel {
   private MutableLiveData mCatalogList;
   private final ICatalogRepository mRepository;

   public CatalogViewModel(ICatalogRepository repository) {
      this.mCatalogList = new MutableLiveData();
      this.mRepository = repository;
   }

   public MutableLiveData getCatalogList() {
      mCatalogList = mRepository.readProductList();
      return mCatalogList;
   }

   public LiveData<String> addProductToCart() {
      return mRepository.addProductToCart();
   }

   public LiveData<String> removeProductFromCart() {
      return mRepository.deleteProductFromCart();
   }
}
