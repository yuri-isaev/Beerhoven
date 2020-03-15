package ru.mobile.beerhoven.presentation.ui.store.details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import ru.mobile.beerhoven.data.remote.CatalogDetailRepository;

public class ProductDetailViewModel extends AndroidViewModel {
   private MutableLiveData<String> mResponse;
   private final CatalogDetailRepository mRepository;

   public ProductDetailViewModel(@NonNull Application application) {
      super(application);
      this.mRepository = new CatalogDetailRepository();
      this.mResponse = new MutableLiveData<>();
   }

   public MutableLiveData<String> addCartProductToRepository() {
      return mResponse = mRepository.createProductToCart();
   }
}