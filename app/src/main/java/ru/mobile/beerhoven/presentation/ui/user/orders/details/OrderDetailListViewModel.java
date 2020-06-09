package ru.mobile.beerhoven.presentation.ui.user.orders.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IOrderDetailRepository;

public class OrderDetailListViewModel extends ViewModel {
   private final IOrderDetailRepository iRepository;

   public OrderDetailListViewModel(IOrderDetailRepository repo) {
      this.iRepository = repo;
   }

   public LiveData<List<Product>> getOrderDetailListFromRepository(String orderKey) {
      return iRepository.getOrderDetailListFromDatabase(orderKey);
   }
}
