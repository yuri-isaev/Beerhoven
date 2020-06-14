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
   private final ICartRepository iRepository;
   private IPreferencesStorage iStorage;

   public CartListViewModel(ICartRepository repo) {
      this.iRepository = repo;
   }

   public CartListViewModel(Context ctx, ICartRepository repo) {
      this.iRepository = repo;
      this.iStorage = new PreferencesStorage(ctx);
   }

   public LiveData<List<Product>> getCartListFromRepository() {
      return iRepository.getCartListFromDatabase();
   }

   public void onDeleteCartItemFromRepository(String item) {
      iRepository.onDeleteCartItemFromDatabase(item);
   }

   public void onDeleteCartListToRepository() {
      iRepository.onDeleteUserCartListFromDatabase();
   }

   public int getCartCountFromStorage() {
      return iStorage.onGetCartCountValue();
   }

   public void onSaveCartCounterToStorage(int counterValue) {
      iStorage.onSaveCartCountValue(counterValue);
   }

   public void onDeleteCartCounterToStorage() {
      iStorage.onDeleteCartCountValue();
   }
}
