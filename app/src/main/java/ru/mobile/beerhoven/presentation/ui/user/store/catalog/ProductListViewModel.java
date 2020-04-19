package ru.mobile.beerhoven.presentation.ui.user.store.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IProductRepository;

public class ProductListViewModel extends ViewModel {
   private final IProductRepository mRepository;

   public ProductListViewModel(IProductRepository repository) {
      this.mRepository = repository;
   }

   public LiveData<List<Product>> getCatalogList() {
      return mRepository.getProductList();
   }

   public LiveData<String> addProductCartToRepository(Product product) {
      return mRepository.addCartProductToRepository(product);
   }

   public LiveData<String> deleteProductFromRepository(Product product) {
      return mRepository.deleteProductFromRepository(product);
   }
}
