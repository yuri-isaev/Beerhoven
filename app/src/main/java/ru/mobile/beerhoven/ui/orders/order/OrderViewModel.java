package ru.mobile.beerhoven.ui.orders.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IOrderRepository;

public class OrderViewModel extends ViewModel {
   private MutableLiveData<List<Product>> mOrderList;
   private final IOrderRepository mRepository;

   public OrderViewModel(IOrderRepository repository) {
      this.mRepository = repository;
   }

   public void initOrderList() {
      if (mOrderList != null) {
         return;
      }
      mOrderList = mRepository.getOrderMutableList();
   }

   public LiveData<List<Product>> getOrderList() {
      return mOrderList;
   }

   public void getId(String key) {
      mRepository.deleteOrderById(key);
   }

   public String gePushId() {
      return mRepository.readOrderId();
   }

   public String getCurrentUserPhone() {
      return mRepository.getCurrentUserPhoneNumber();
   }
}
