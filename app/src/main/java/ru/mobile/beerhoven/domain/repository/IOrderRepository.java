package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Order;

public interface IOrderRepository extends IUserRepository {
   MutableLiveData<List<Order>> getOrderMutableList();
   MutableLiveData<String> getOrderMutableData();
   void readOrderConfirmList();
   void deleteOrderById(String keyId);
}
