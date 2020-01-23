package ru.mobile.beerhoven.data.storage;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.models.Item;

public interface IOrderRepository extends IUserStateRepository {
   String readOrderId();
   MutableLiveData<List<Item>> getOrderMutableList();
   void readOrderConfirmList();
   void deleteOrderById(String keyId);
}
