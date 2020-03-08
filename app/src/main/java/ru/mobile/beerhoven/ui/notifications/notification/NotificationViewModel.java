package ru.mobile.beerhoven.ui.notifications.notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.INotificationRepository;

public class NotificationViewModel extends ViewModel {
   private MutableLiveData<List<Order>> mNotificationList;
   private final INotificationRepository mRepository;

   public NotificationViewModel(INotificationRepository repository) {
      this.mRepository = repository;
   }

   public void initNotificationList() {
      if (mNotificationList != null) {
         return;
      }
      mNotificationList = mRepository.getNotificationList();
   }

   public LiveData<List<Order>> getNotificationList() {
      return mNotificationList;
   }
}
