package ru.mobile.beerhoven.ui.cart;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.ICartRepository;

public class CartViewModel extends ViewModel {
   private LiveData<List<Product>> mCartList;
   private final ICartRepository mRepository;
   private PreferencesStorage mStorage;

   public CartViewModel(ICartRepository repository) {
      this.mRepository = repository;
   }

   public CartViewModel(ICartRepository repository, Application applicationContext) {
      this.mCartList = new MutableLiveData<List<Product>>();
      this.mRepository = repository;
      this.mStorage = new PreferencesStorage(applicationContext);
   }

   // Initialize adapter list when create fragment
   public void initCartList() {
      if (mCartList != null) {
         return;
      }
      mCartList = mRepository.getCartMutableList();
   }

   public LiveData<List<Product>> getCartList() {
      return mCartList;
   }

   public void onDeleteCartListItem(String position) {
      mRepository.onDeleteCartItem(position);
   }

   public void onDeleteCartList() {
      mRepository.onDeleteUserCartList();
   }

   public void onDeleteCartCounter() {
      mStorage.onDeleteCounter();
   }

   public void onSetCartCount() {
      mStorage.onSetIntCount();
   }

   public void onCounterCartSave() {
      mStorage.onCounterSave();
   }
}
