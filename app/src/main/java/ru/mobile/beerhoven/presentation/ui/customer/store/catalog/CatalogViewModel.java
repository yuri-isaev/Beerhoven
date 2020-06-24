package ru.mobile.beerhoven.presentation.ui.customer.store.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IProductRepository;

public class CatalogViewModel extends ViewModel {
   private final IProductRepository mRepository;

   public CatalogViewModel(IProductRepository repo) {
      this.mRepository = repo;
   }

   public LiveData<List<Product>> getProductListToRepository() {
      return mRepository.getProductListFromDatabase();
   }

   public LiveData<List<Product>> getProductListByCategory(String category) {
      return mRepository.getProductListByCategoryFromDatabase(category);
   }

   public void onAddProductToCartRepository(Product product) {
      mRepository.onAddProductToCartDatabase(product);
   }

   public void onDeleteProductFromRepository(Product product) {
      mRepository.onDeleteProductFromDatabase(product);
   }
}
