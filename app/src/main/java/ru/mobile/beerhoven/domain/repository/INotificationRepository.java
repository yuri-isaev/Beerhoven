package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Order;

public interface INotificationRepository {
   MutableLiveData<List<Order>> getNotificationListFromDatabase();
}
