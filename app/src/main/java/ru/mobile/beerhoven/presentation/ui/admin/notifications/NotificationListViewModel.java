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
   private final INotificationRepository iRepository;
   private IPreferencesStorage iStorage;

   public NotificationListViewModel(INotificationRepository repo) {
      this.iRepository = repo;
   }

   public NotificationListViewModel(Context ctx, INotificationRepository repo) {
      this.iRepository = repo;
      this.iStorage = new PreferencesStorage(ctx);
   }

   public LiveData<List<Order>> getNotificationListFromRepository() {
      return iRepository.getNotificationListFromDatabase();
   }

   public void onSaveNotificationCounterToStorage(int counterValue) {
      iStorage.onSaveNotificationCountValue(counterValue);
   }

   public int getNotificationCountFromStorage() {
      return iStorage.onGetNotificationCountValue();
   }
}
