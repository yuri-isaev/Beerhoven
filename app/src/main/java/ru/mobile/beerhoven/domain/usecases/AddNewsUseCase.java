package ru.mobile.beerhoven.domain.usecases;

import android.app.Activity;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import es.dmoral.toasty.Toasty;
import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.repository.INewsRepository;
import ru.mobile.beerhoven.utils.Constants;

public class AddNewsUseCase {
   private final Activity mActivity;
   private final INewsRepository mRepository;

   public AddNewsUseCase(Activity activity, INewsRepository mRepository) {
      this.mActivity = activity;
      this.mRepository = mRepository;
   }

   public void execute(News model) {
      checkPostState(model);
   }

   private void checkPostState(@NonNull News model) {
      if (TextUtils.isEmpty(model.getTitle())) {
         Toasty.error(mActivity, "Введите заголовок поста", Toasty.LENGTH_SHORT).show();
         return;
      }

      if (TextUtils.isEmpty(model.getDescription())) {
         Toasty.error(mActivity, "Введите описание поста", Toasty.LENGTH_SHORT).show();
         return;
      }

      if (TextUtils.isEmpty(model.getUri())) {
         Toasty.error(mActivity, "Добавте изображение поста", Toasty.LENGTH_SHORT).show();
      }

      if (model.getUri() == null) {
         model.setUri(Constants.APP_LOGO);
         addNewsDataToRepository(model);
      } else {
         uploadDataToRepository(model);
      }
   }

   private void uploadDataToRepository(News model) {
      mRepository.addNewsPostToDatabase(model);
   }

   private void addNewsDataToRepository(News model) {
      mRepository.addNewsDataToDatabase(model);
   }
}
