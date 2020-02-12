package ru.mobile.beerhoven.ui.store.details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import ru.mobile.beerhoven.data.remote.CatalogDetailsRepository;

public class DetailsViewModel extends AndroidViewModel {
   private MutableLiveData<String> mResponse;
   private final CatalogDetailsRepository mRepository;

   public DetailsViewModel(@NonNull Application application) {
      super(application);
      this.mRepository = new CatalogDetailsRepository();
      this.mResponse = new MutableLiveData<>();
   }

   public MutableLiveData<String> addProductToCart() {
      mResponse = mRepository.createProductToCart();
      return mResponse;
   }
}
