package ru.mobile.beerhoven.ui.orders.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IOrderDetailsRepository;

public class OrderDetailsViewModel extends ViewModel {
   private MutableLiveData<List<Product>> mOrderDetailsList;
   private final IOrderDetailsRepository mRepository;

   public OrderDetailsViewModel(IOrderDetailsRepository repository) {
      this.mRepository = repository;
   }

   public void initOrderDetailsList() {
      if (mOrderDetailsList != null) {
         return;
      }
      mOrderDetailsList = mRepository.getOrderDetailsList();
   }

   public LiveData<List<Product>> getOrderDetailsList() {
      return mOrderDetailsList;
   }
}
