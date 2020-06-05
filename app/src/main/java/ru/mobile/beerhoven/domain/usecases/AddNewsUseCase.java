package ru.mobile.beerhoven.domain.usecases;

import androidx.annotation.NonNull;

import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.repository.INewsRepository;

public class AddNewsUseCase {
   private final INewsRepository mRepository;

   public AddNewsUseCase(INewsRepository mRepository) {
      this.mRepository = mRepository;
   }

   public void execute(News news) {
      onCheckNewsPostImage(news);
   }

   private void onCheckNewsPostImage(@NonNull News news) {
      if (news.getImage().equals("null")) {
         onAddNewsDataToRepository(news);
      } else {
         onUploadDataToRepository(news);
      }
   }

   private void onAddNewsDataToRepository(News news) {
      mRepository.onAddNewsWithoutImageToDatabase(news);
   }

   private void onUploadDataToRepository(News news) {
      mRepository.onAddNewsToDatabase(news);
   }
}
