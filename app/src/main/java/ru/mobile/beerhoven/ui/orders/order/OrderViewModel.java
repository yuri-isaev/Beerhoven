package ru.mobile.beerhoven.ui.orders.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.data.storage.CrudRepository;
import ru.mobile.beerhoven.models.Item;

public class OrderViewModel extends ViewModel {
   private MutableLiveData<List<Item>> mOrderList;
   private final CrudRepository<Item>  mRepository;

   public OrderViewModel(CrudRepository<Item> repository) {
      this.mRepository = repository;
   }

   public void initOrderList() { // check for null
      if (mOrderList != null) {
         return;
      }
      mOrderList = mRepository.readList();
   }

   public LiveData<List<Item>> getOrderList() {
      return mOrderList;
   }
}
