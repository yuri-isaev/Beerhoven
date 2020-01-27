package ru.mobile.beerhoven.ui.orders.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;

public class OrderDetailsViewModel extends ViewModel {
   private MutableLiveData<List<Product>> mOrderDetailsList;

   public void initList(){
      if (mOrderDetailsList != null){
         return;
      }
      mOrderDetailsList = OrderDetailsRepository.getInstance().getOrderDetailsList();
   }

   public LiveData<List<Product>> getOrderDetailsList() {
      return mOrderDetailsList;
   }
}
