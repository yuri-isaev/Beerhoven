package ru.mobile.beerhoven.presentation.ui.user.cart;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.ICartRepository;
import ru.mobile.beerhoven.domain.repository.IPreferencesStorage;

public class CartListViewModel extends ViewModel {
   private final ICartRepository mRepository;
   private IPreferencesStorage mStorage;
   private LiveData<List<Product>> mCartList;

   public CartListViewModel(ICartRepository repository) {
      this.mRepository = repository;
   }

   public CartListViewModel(Application context, ICartRepository repository) {
      this.mRepository = repository;
      this.mCartList = new MutableLiveData<>();
      this.mStorage = new PreferencesStorage(context);
   }

   public void initCartList() {
      if (mCartList != null) {
         return;
      }
      mCartList = mRepository.getCartMutableList();
   }

   public LiveData<List<Product>> getCartList() {
      return mCartList;
   }

   public void onDeleteCartItemFromRepository(String item) {
      mRepository.onDeleteCartItem(item);
   }

   public void onDeleteCartListToRepository() {
      mRepository.onDeleteUserCartList();
   }

   public int getCartCountFromStorage() {
      return mStorage.onGetCartCountValue();
   }

   public void onSaveCartCounterToStorage(int counterValue) {
      mStorage.onSaveCartCountValue(counterValue);
   }

   public void onDeleteCartCounterToStorage() {
      mStorage.onDeleteCartCountValue();
   }
}
