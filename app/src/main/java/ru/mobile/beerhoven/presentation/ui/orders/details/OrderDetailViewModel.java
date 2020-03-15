package ru.mobile.beerhoven.presentation.ui.orders.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IOrderDetailRepository;

public class OrderDetailViewModel extends ViewModel {
   private final IOrderDetailRepository mRepository;
   private MutableLiveData<List<Product>> mOrderDetailsList;

   public OrderDetailViewModel(IOrderDetailRepository repository) {
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
