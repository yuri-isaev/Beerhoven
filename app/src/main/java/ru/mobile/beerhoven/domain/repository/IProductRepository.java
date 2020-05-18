package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;

public interface IProductRepository {
   MutableLiveData<Boolean> onAddProductToDatabase(Product product);
   MutableLiveData<List<Product>> getProductListFromToDatabase();
   MutableLiveData<String> addCartProductToRepository(Product product);
   MutableLiveData<String> deleteProductFromRepository(Product product);
}
