package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;

public interface IOrderRepository extends IUserStateRepository {
   MutableLiveData<List<Product>> getOrderMutableList();
   String readOrderId();
   void readOrderConfirmList();
   void deleteOrderById(String keyId);
}
