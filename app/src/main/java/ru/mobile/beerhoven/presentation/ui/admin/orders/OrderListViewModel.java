package ru.mobile.beerhoven.presentation.ui.admin.orders;

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

   public void onDeleteOrderByIdToRepository(String orderId) {
      mRepository.onDeleteOrderByIdToDatabase(orderId);
   }
}
