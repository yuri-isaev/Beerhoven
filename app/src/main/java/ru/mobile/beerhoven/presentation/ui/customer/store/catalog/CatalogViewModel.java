package ru.mobile.beerhoven.presentation.ui.customer.store.catalog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IProductRepository;

public class CatalogViewModel extends ViewModel {
   private final IProductRepository iRepository;
   public CatalogViewModel(IProductRepository repo) {
      this.iRepository = repo;
   }

   public LiveData<List<Product>> getProductListToRepository() {
      return iRepository.getProductListFromDatabase();
   }

   public LiveData<List<Product>> getProductListByCategory(String category) {
      return iRepository.getProductListByCategoryFromDatabase(category);
   }

   public void onAddProductToCartRepository(Product product) {
      iRepository.onAddProductToCartDatabase(product);
   }

   public void onDeleteProductFromRepository(Product product) {
      iRepository.onDeleteProductFromDatabase(product);
   }
}
