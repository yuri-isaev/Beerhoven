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
   private final IOrderConfirmRepository mRepository;
   private final IPreferencesStorage mStorage;
   private final IPushMessagingService mService;

   public OrderConfirmViewModel(Context context, IOrderConfirmRepository repository,
       IPushMessagingService service) {
      this.mRepository = repository;
      this.mService = service;
      this.mStorage = new PreferencesStorage(context);
   }

   public void onCreateConfirmOrderToRepository(Order order) {
      mRepository.onCreateOrderConfirm(order);
   }

   public void onDeleteOrderCartToRepository() {
      mRepository.onDeleteOrderCart();
   }

   public void onDeleteCartCounterToStorage() {
      mStorage.onDeleteCartCountValue();
   }

   public void onSendPushNotificationToService(FragmentActivity activity) {
      mService.onSendPushNotification(activity);
   }
}
