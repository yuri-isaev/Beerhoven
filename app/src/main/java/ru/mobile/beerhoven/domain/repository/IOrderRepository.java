package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Order;

public interface IOrderRepository extends IUserStateRepository {
   MutableLiveData<List<Order>> getOrderMutableList();
   String readOrderId();
   void readOrderConfirmList();
   void deleteOrderById(String keyId);
}
