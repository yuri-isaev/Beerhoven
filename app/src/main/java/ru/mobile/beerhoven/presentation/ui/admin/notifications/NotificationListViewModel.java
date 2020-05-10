package ru.mobile.beerhoven.presentation.ui.admin.notifications;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.INotificationRepository;
import ru.mobile.beerhoven.domain.repository.IPreferencesStorage;

public class NotificationListViewModel extends ViewModel {
   //Context mContext;
   private final INotificationRepository mRepository;
   private IPreferencesStorage mStorage;
   private MutableLiveData<List<Order>> mNotificationList;

   public NotificationListViewModel(INotificationRepository repository) {
      this.mRepository = repository;
   }

   public NotificationListViewModel(Context context, INotificationRepository repository) {
      this.mRepository = repository;
      this.mStorage = new PreferencesStorage(context);
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

   public void onSaveNotificationCounterToStorage(int counterValue) {
      mStorage.onSaveNotificationCountValue(counterValue);
   }

   public int getNotificationCountFromStorage() {
      return mStorage.onGetNotificationCountValue();
   }
}
