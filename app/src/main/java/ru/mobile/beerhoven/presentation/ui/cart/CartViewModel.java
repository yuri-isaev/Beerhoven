package ru.mobile.beerhoven.presentation.ui.cart;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.ICartRepository;

public class CartViewModel extends ViewModel {
   private final ICartRepository mRepository;
   private LiveData<List<Product>> mCartList;
   private PreferencesStorage mStorage;

   public CartViewModel(ICartRepository repository) {
      this.mRepository = repository;
   }

   public CartViewModel(ICartRepository repository, Application applicationContext) {
      this.mRepository = repository;
      this.mCartList = new MutableLiveData<>();
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

   public void onDeleteCartListItemToRepository(String position) {
      mRepository.onDeleteCartItem(position);
   }

   public void onDeleteCartListToRepository() {
      mRepository.onDeleteUserCartList();
   }

   public void onDeleteCartCounterToStorage() {
      mStorage.onDeleteCounter();
   }

   public void onSetCartCountToStorage() {
      mStorage.onSetIntCount();
   }

   public void onSaveCartCounterToStorage() {
      mStorage.onSaveCounter();
   }
}
