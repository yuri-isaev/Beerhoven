package ru.mobile.beerhoven.domain.repository;

public interface IOrderConfirmRepository {
   void onCreateConfirmOrder();
   void onRemoveConfirmOrder();
}
