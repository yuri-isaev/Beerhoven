package ru.mobile.beerhoven.presentation.ui.user.orders.confirm;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IOrderConfirmRepository;
import ru.mobile.beerhoven.domain.repository.IPreferencesStorage;
import ru.mobile.beerhoven.domain.repository.IPushMessagingService;

public class OrderConfirmViewModel extends ViewModel {
   private final IOrderConfirmRepository mRepository;
   private final IPreferencesStorage mStorage;
   private final IPushMessagingService mService;

   public OrderConfirmViewModel(IOrderConfirmRepository repository, IPushMessagingService service,
       IPreferencesStorage storage)
   {
      this.mRepository = repository;
      this.mService = service;
      this.mStorage = storage;
   }

   public void onCreateConfirmOrderToRepository(Order order) {
      mRepository.onCreateConfirmOrder(order);
   }

   public void onDeleteConfirmOrderToRepository() {
      mRepository.onRemoveConfirmOrder();
   }

   public void onDeleteCartCounterToStorage() {
      mStorage.onDeleteCartCountValue();
   }

   public void onSendPushNotificationToService(FragmentActivity activity) {
      mService.onSendPushNotification(activity);
   }
}
