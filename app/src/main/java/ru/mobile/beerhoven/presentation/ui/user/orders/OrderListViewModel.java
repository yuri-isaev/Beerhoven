package ru.mobile.beerhoven.presentation.ui.user.orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IOrderRepository;

public class OrderListViewModel extends ViewModel {
   private final IOrderRepository mRepository;
   private MutableLiveData<List<Order>> mOrderList;

   public OrderListViewModel(IOrderRepository repository) {
      this.mRepository = repository;
   }

   public void initOrderList() {
      if (mOrderList != null) {
         return;
      }
      mOrderList = mRepository.getOrderMutableList();
   }

   public LiveData<List<Order>> getOrderList() {
      return mOrderList;
   }

   public void onDeleteOrderByIdToRepository(String key) {
      mRepository.onDeleteOrderById(key);
   }
}
