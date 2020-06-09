package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;

public interface ICartRepository {
   MutableLiveData<List<Product>> getCartListFromDatabase();
   void onDeleteCartItemFromDatabase(String item);
   void onDeleteUserCartListFromDatabase();
}
