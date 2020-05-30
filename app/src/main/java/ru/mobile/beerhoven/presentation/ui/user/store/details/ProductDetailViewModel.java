package ru.mobile.beerhoven.presentation.ui.user.store.details;

import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IProductRepository;

public class ProductDetailViewModel extends ViewModel {
   private final IProductRepository iRepository;

   public ProductDetailViewModel(IProductRepository repo) {
      this.iRepository = repo;
   }

   public void onAddCartProductToRepository(Product product) {
      iRepository.onAddProductToCartDatabase(product);
   }
}