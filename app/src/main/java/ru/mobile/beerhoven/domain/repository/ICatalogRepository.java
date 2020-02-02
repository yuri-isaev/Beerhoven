package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;

public interface ICatalogRepository {
   MutableLiveData<List<Product>> readProductList();
   MutableLiveData<String> addProductToCart();
   MutableLiveData<String> deleteProductFromCart();
}
