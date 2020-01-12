package ru.mobile.beerhoven.ui.cart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.data.repository.CartRepository;
import ru.mobile.beerhoven.models.Item;

public class CartViewModel extends AndroidViewModel {
   private MutableLiveData<List<Item>> mCartList;
   private final CartRepository mRepository;

   public CartViewModel(@NonNull Application application) {
      super(application);
      this.mRepository = new CartRepository();
      this.mCartList = new MutableLiveData<List<Item>>();
   }

   public void initList() {
      if (mCartList != null) {
         return;
      }
      mCartList = mRepository.getCartList();
   }

   public LiveData<List<Item>> getCartList() {
      return mCartList;
   }
}
