package ru.mobile.beerhoven.presentation.ui.admin.notifications;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.INotificationRepository;
import ru.mobile.beerhoven.domain.repository.IPreferencesStorage;

public class NotificationListViewModel extends ViewModel {
   private final INotificationRepository mRepository;
   private IPreferencesStorage mStorage;

   public NotificationListViewModel(INotificationRepository repo) {
      this.mRepository = repo;
   }

   public NotificationListViewModel(Context ctx, INotificationRepository repo) {
      this.mRepository = repo;
      this.mStorage = new PreferencesStorage(ctx);
   }

   public LiveData<List<Order>> getNotificationListFromRepository() {
      return mRepository.getNotificationListFromDatabase();
   }

   public void onSaveNotificationCounterToStorage(int counterValue) {
      mStorage.onSaveNotificationCountValue(counterValue);
   }

   public int getNotificationCountFromStorage() {
      return mStorage.onGetNotificationCountValue();
   }
}
