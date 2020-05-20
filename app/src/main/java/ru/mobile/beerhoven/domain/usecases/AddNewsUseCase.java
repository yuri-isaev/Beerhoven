package ru.mobile.beerhoven.domain.usecases;

import androidx.annotation.NonNull;

import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.repository.INewsRepository;
import ru.mobile.beerhoven.utils.Constants;

public class AddNewsUseCase {
   private final INewsRepository mRepository;

   public AddNewsUseCase(INewsRepository mRepository) {
      this.mRepository = mRepository;
   }

   public void execute(News news) {
      onCheckPostUri(news);
   }

   private void onCheckPostUri(@NonNull News news) {
      if (news.getImage().equals("null")) {
         news.setImage(Constants.APP_LOGO);
         addNewsDataToRepository(news);
      } else {
         uploadDataToRepository(news);
      }
   }

   private void uploadDataToRepository(News news) {
      mRepository.addNewsPostToDatabase(news);
   }

   private void addNewsDataToRepository(News news) {
      mRepository.addNewsDataToDatabase(news);
   }
}
