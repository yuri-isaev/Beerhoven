package ru.mobile.beerhoven.presentation.ui.admin.post;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.data.remote.NewsRepository;
import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.usecases.AddNewsUseCase;

public class AddNewsViewModel extends ViewModel {
   private final AddNewsUseCase mAddNewsUseCase;
   private final MutableLiveData<Boolean> mResponse;

   private static final String TAG = "AddNewsViewModel";

   public AddNewsViewModel(Activity activity) {
      this.mResponse = new MutableLiveData<>();
      this.mAddNewsUseCase = new AddNewsUseCase(activity, new NewsRepository());
   }

   public LiveData<Boolean> onAddPostResponse(News model) {
      mResponse.setValue(Boolean.TRUE);
      try {
         mAddNewsUseCase.execute(model);
      } catch (Exception e) {
         mResponse.setValue(Boolean.FALSE);
         Log.e(TAG, e.getMessage());
      }
      return mResponse;
   }
}
