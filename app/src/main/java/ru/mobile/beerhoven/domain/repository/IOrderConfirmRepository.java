package ru.mobile.beerhoven.domain.repository;

import ru.mobile.beerhoven.domain.model.Order;

public interface IOrderConfirmRepository {
   void onCreateOrderConfirm(Order order);
   void onDeleteOrderCart();
}
