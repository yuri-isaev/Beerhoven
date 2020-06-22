package ru.mobile.beerhoven.presentation.ui.admin.orders.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IOrderDetailRepository;

public class OrderDetailListViewModel extends ViewModel {
   private final IOrderDetailRepository mRepository;

   public OrderDetailListViewModel(IOrderDetailRepository repo) {
      this.mRepository = repo;
   }

   public LiveData<List<Product>> getOrderDetailListFromRepository(String orderKey) {
      return mRepository.getOrderDetailListFromDatabase(orderKey);
   }
}
