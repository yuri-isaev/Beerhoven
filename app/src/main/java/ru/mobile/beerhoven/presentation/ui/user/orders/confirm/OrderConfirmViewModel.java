package ru.mobile.beerhoven.presentation.ui.user.orders.confirm;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IOrderConfirmRepository;
import ru.mobile.beerhoven.domain.repository.IPreferencesStorage;
import ru.mobile.beerhoven.domain.repository.IPushMessagingService;

public class OrderConfirmViewModel extends ViewModel {
   private final IOrderConfirmRepository iRepository;
   private final IPreferencesStorage iStorage;
   private final IPushMessagingService iService;

   public OrderConfirmViewModel(Context ctx, IOrderConfirmRepository repo, IPushMessagingService service) {
      this.iRepository = repo;
      this.iService = service;
      this.iStorage = new PreferencesStorage(ctx);
   }

   public void onCreateConfirmOrderToRepository(Order order) {
      iRepository.onCreateOrderConfirmToDatabase(order);
   }

   public void onDeleteOrderCartToRepository() {
      iRepository.onDeleteOrderCartFromDatabase();
   }

   public void onDeleteCartCounterToStorage() {
      iStorage.onDeleteCartCountValue();
   }

   public void onSendPushNotificationToService(FragmentActivity activity) {
      iService.onSendPushNotification(activity);
   }
}
