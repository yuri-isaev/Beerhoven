package ru.mobile.beerhoven.presentation.ui.orders.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IOrderRepository;

public class OrderViewModel extends ViewModel {
   private final IOrderRepository mRepository;
   private MutableLiveData<List<Order>> mOrderList;
   private String mOrderData;

   public OrderViewModel(IOrderRepository repository) {
      this.mRepository = repository;
   }

   public void initOrderList() {
      if (mOrderList != null) {
         return;
      }
      mOrderList = mRepository.getOrderMutableList();
   }

   public String getCurrentOrderIdToRepository() {
      if (mOrderData != null) {
         return "error";
      }
       return mOrderData = mRepository.getOrderMutableData().getValue();
   }

   public LiveData<List<Order>> getOrderList() {
      return mOrderList;
   }

   public void onDeleteOrderByIdToRepository(String key) {
      mRepository.onDeleteOrderById(key);
   }

   public String getCurrentUserPhoneToRepository() {
      return mRepository.getCurrentUserPhoneNumber();
   }
}
