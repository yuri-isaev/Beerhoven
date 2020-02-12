package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;

public interface ICartRepository {
   MutableLiveData<List<Product>> getCartMutableList();
   void onDeleteCartItem(String position);
   void onDeleteUserCartList();
}
