package ru.mobile.beerhoven.presentation.ui.user.news.corporate;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.repository.INewsRepository;

public class NewsListViewModel extends ViewModel {
   private final INewsRepository mRepository;
   private LiveData<List<News>> mNewsList;
   private PreferencesStorage mStorage;

   public NewsListViewModel(INewsRepository repository) {
      this.mRepository = repository;
   }

   public NewsListViewModel(INewsRepository repository, Application applicationContext) {
      this.mRepository = repository;
      this.mStorage = new PreferencesStorage(applicationContext);
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

   public int getNewsCountFromStorage() {
      return mStorage.onGetNewsCountValue();
   }

   public void onSaveNewsCounterToStorage(int counterValue) {
      mStorage.onSaveNewsCountValue(counterValue);
   }
}