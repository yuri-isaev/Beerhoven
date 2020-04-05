package ru.mobile.beerhoven.presentation.ui.user.news.corporate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.repository.INewsRepository;

public class NewsListViewModel extends ViewModel {
   private final INewsRepository mRepository;
   private LiveData<List<News>> mNewsList;

   public NewsListViewModel(INewsRepository repository) {
      this.mRepository = repository;
   }

   public void initNewsList() {
      if (mNewsList != null) {
         return;
      }
      this.mNewsList = mRepository.getNewsList();
   }

   public LiveData<List<News>> getNewsList() {
      return mNewsList;
   }
}