package ru.mobile.beerhoven.presentation.ui.customer.orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IOrderRepository;

public class OrderListViewModel extends ViewModel {
   private final IOrderRepository mRepository;

   public OrderListViewModel(IOrderRepository repo) {
      this.mRepository = repo;
   }

   public LiveData<List<Order>> getOrderListFromRepository() {
      return mRepository.getOrderListFromDatabase();
   }
}
