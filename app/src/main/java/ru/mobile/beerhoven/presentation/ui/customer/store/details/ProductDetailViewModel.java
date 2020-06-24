package ru.mobile.beerhoven.presentation.ui.customer.store.details;

import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IProductRepository;

public class ProductDetailViewModel extends ViewModel {
   private final IProductRepository mRepository;

   public ProductDetailViewModel(IProductRepository repo) {
      this.mRepository = repo;
   }

   public void onAddCartProductToRepository(Product product) {
      mRepository.onAddProductToCartDatabase(product);
   }
}