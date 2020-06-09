package ru.mobile.beerhoven.domain.usecases;

import androidx.annotation.NonNull;

import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.repository.INewsRepository;

public class AddNewsUseCase {
   private final INewsRepository iRepository;

   public AddNewsUseCase(INewsRepository repo) {
      this.iRepository = repo;
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
      iRepository.onAddNewsWithoutImageToDatabase(news);
   }

   private void onUploadDataToRepository(News news) {
      iRepository.onAddNewsToDatabase(news);
   }
}
