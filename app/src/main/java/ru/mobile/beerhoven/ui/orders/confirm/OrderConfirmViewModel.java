package ru.mobile.beerhoven.ui.orders.confirm;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.repository.IOrderConfirmRepository;

public class OrderConfirmViewModel extends ViewModel {
   private final IOrderConfirmRepository mRepository;
   private PreferencesStorage mStorage;

   public OrderConfirmViewModel(IOrderConfirmRepository repository, Context context) {
      this.mRepository = repository;
      this.mStorage = new PreferencesStorage((Application) context);
   }

   public void onCreateConfirmOrderOnDatabase() {
      mRepository.createConfirmOrder();
   }

   public void onDeleteConfirmOrderOnDatabase() {
      mRepository.removeConfirmOrder();
   }

   public void onDeleteCartCounterFromStorage() {
      mStorage.onDeleteCounter();
   }
}
