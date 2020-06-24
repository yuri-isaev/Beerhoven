package ru.mobile.beerhoven.presentation.ui.customer.cart;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.ICartRepository;
import ru.mobile.beerhoven.domain.repository.IPreferencesStorage;

public class CartListViewModel extends ViewModel {
   private final ICartRepository mRepository;
   private IPreferencesStorage mStorage;

   public CartListViewModel(ICartRepository repo) {
      this.mRepository = repo;
   }

   public CartListViewModel(Context ctx, ICartRepository repo) {
      this.mRepository = repo;
      this.mStorage = new PreferencesStorage(ctx);
   }

   public LiveData<List<Product>> getCartListFromRepository() {
      return mRepository.getCartListFromDatabase();
   }

   public void onDeleteCartItemFromRepository(String item) {
      mRepository.onDeleteCartItemFromDatabase(item);
   }

   public void onDeleteCartListToRepository() {
      mRepository.onDeleteUserCartListFromDatabase();
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
