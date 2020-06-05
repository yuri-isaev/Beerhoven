package ru.mobile.beerhoven.presentation.ui.admin.post.news;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.NewsRepository;
import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.usecases.AddNewsUseCase;
import ru.mobile.beerhoven.utils.Toasty;

public class AddNewsViewModel extends ViewModel {
   private final AddNewsUseCase mAddNewsUseCase;
   private static final String TAG = "AddNewsViewModel";

   public AddNewsViewModel() {
      this.mAddNewsUseCase = new AddNewsUseCase(new NewsRepository());
   }

   public void onAddNewsToRepository(FragmentActivity activity, News news) {
      try {
         mAddNewsUseCase.execute(news);
         Toasty.success(activity, R.string.news_add_success);
      } catch (Exception e) {
         Log.e(TAG, e.getMessage());
         Toasty.error(activity, R.string.news_add_failed);
      }
   }
}
