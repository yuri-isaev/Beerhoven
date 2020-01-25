package ru.mobile.beerhoven.ui.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.data.repository.CartRepository;
import ru.mobile.beerhoven.domain.model.Product;

public class CartViewModel extends AndroidViewModel {
   private MutableLiveData<List<Product>> mCartList;
   private final CartRepository mRepository;

   public CartViewModel(@NonNull Application application) {
      super(application);
      this.mRepository = new CartRepository();
   }

   public void initCartList() {
      if (mCartList != null) {
         return;
      }
      mCartList = mRepository.getCartList();
   }

   public LiveData<List<Product>> getCartList() {
      return mCartList;
   }

   public void deleteCartListItem(String position) {
      mRepository.deleteCartItem(position);
   }
}
