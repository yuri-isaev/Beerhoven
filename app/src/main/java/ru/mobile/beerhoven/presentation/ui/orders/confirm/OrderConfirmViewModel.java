package ru.mobile.beerhoven.presentation.ui.orders.confirm;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.repository.IOrderConfirmRepository;

public class OrderConfirmViewModel extends ViewModel {
   private final IOrderConfirmRepository mRepository;
   private final PreferencesStorage mStorage;

   public OrderConfirmViewModel(IOrderConfirmRepository repository, Context context) {
      this.mRepository = repository;
      this.mStorage = new PreferencesStorage((Application) context);
   }

   public void onCreateConfirmOrderToRepository() {
      mRepository.createConfirmOrder();
   }

   public void onDeleteConfirmOrderToRepository() {
      mRepository.removeConfirmOrder();
   }

   public void onDeleteCartCounterToStorage() {
      mStorage.onDeleteCounter();
   }
}
