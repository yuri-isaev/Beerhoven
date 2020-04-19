package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;

public interface IProductRepository {
   MutableLiveData<List<Product>> getProductList();
   MutableLiveData<String> addCartProductToRepository(Product product);
   MutableLiveData<String> deleteProductFromRepository(Product product);
}
