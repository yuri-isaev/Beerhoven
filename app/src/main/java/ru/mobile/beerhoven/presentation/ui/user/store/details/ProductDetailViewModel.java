package ru.mobile.beerhoven.presentation.ui.user.store.details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import ru.mobile.beerhoven.data.remote.ProductRepository;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IProductRepository;

public class ProductDetailViewModel extends AndroidViewModel {
   private MutableLiveData<String> mResponse;
   private final IProductRepository mRepository;

   public ProductDetailViewModel(@NonNull Application application) {
      super(application);
      this.mRepository = new ProductRepository();
      this.mResponse = new MutableLiveData<>();
   }

   public MutableLiveData<String> addCartProductToRepository(Product product) {
      return mResponse = mRepository.addCartProductToRepository(product);
   }
}