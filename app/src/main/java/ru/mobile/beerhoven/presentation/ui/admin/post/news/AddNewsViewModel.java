package ru.mobile.beerhoven.presentation.ui.admin.post.news;

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

   public AddNewsViewModel() {
      this.mResponse = new MutableLiveData<>();
      this.mAddNewsUseCase = new AddNewsUseCase(new NewsRepository());
   }

   public LiveData<Boolean> onAddNewsToRepository(News news) {
      mResponse.setValue(true);
      try {
         mAddNewsUseCase.execute(news);
      } catch (Exception e) {
         mResponse.setValue(false);
         Log.e(TAG, e.getMessage());
      }
      return mResponse;
   }
}
