package ru.mobile.beerhoven.presentation.ui.admin.post.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IProductRepository;

public class AddProductViewModel extends ViewModel {
   private final IProductRepository iRepository;

   public AddProductViewModel(IProductRepository repo) {
      this.iRepository = repo;
   }

   public LiveData<Boolean> onAddProductToRepository(Product product) {
      return iRepository.onAddProductToDatabase(product);
   }
}
