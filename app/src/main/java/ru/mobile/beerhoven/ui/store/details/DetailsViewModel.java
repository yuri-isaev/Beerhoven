package ru.mobile.beerhoven.ui.store.details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class DetailsViewModel extends AndroidViewModel {
   private MutableLiveData<String> mResponse;

   public DetailsViewModel(@NonNull Application application) {
      super(application);
      mResponse = new MutableLiveData<>();
   }
}
