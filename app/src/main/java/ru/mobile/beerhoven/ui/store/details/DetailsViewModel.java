package ru.mobile.beerhoven.ui.store.details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import ru.mobile.beerhoven.data.repository.DetailsRepository;

public class DetailsViewModel extends AndroidViewModel {
   private MutableLiveData<String> mResponse;
   private final DetailsRepository mRepository;

   public DetailsViewModel(@NonNull Application application) {
      super(application);
      this.mRepository = new DetailsRepository(getApplication());
      this.mResponse = new MutableLiveData<>();
   }

   public MutableLiveData<String> addItemOrder() {
      mResponse = mRepository.addItemInOrder();
      return mResponse;
   }
}
