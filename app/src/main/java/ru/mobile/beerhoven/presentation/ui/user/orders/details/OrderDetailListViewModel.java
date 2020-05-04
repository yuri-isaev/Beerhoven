package ru.mobile.beerhoven.presentation.ui.user.orders.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IOrderDetailRepository;

public class OrderDetailListViewModel extends ViewModel {
   private final IOrderDetailRepository mRepository;
   private MutableLiveData<List<Product>> mOrderDetailList;

   public OrderDetailListViewModel(IOrderDetailRepository repository) {
      this.mRepository = repository;
   }

   public void initOrderDetailList(String orderKey) {
      if (mOrderDetailList != null) {
         return;
      }
      mOrderDetailList = mRepository.getOrderDetailList(orderKey);
   }

   public LiveData<List<Product>> getOrderDetailList() {
      return mOrderDetailList;
   }
}
