package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Order;

public interface IOrderRepository {
   MutableLiveData<List<Order>> getOrderListFromDatabase();
   void onDeleteOrderByIdToDatabase(String keyId);
}
