package ru.mobile.beerhoven.presentation.ui.user.store.details;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IProductRepository;

public class ProductDetailViewModel extends ViewModel {
   private final IProductRepository mRepository;

   public ProductDetailViewModel(IProductRepository repository) {
      this.mRepository = repository;
   }

   public MutableLiveData<String> addCartProductToRepository(Product product) {
      return mRepository.addCartProductToRepository(product);
   }
}