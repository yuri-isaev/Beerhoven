package ru.mobile.beerhoven.presentation.ui.user.news.corporate;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.repository.INewsRepository;

public class NewsListViewModel extends ViewModel {
   private final INewsRepository mRepository;
   private LiveData<List<News>> mNewsList;
   private final PreferencesStorage mStorage;

   public NewsListViewModel(Context context, INewsRepository repository) {
      this.mRepository = repository;
      this.mStorage = new PreferencesStorage(context);
   }

   public void initNewsList() {
      if (mNewsList != null) {
         return;
      }
      this.mNewsList = mRepository.getNewsListFromDatabase();
   }

   public LiveData<List<News>> getNewsListFromRepository() {
      return mNewsList;
   }

   public int getNewsCountFromStorage() {
      return mStorage.onGetNewsCountValue();
   }

   public void onSaveNewsCounterToStorage(int counterValue) {
      mStorage.onSaveNewsCountValue(counterValue);
   }

   public void onDeleteNewsCounterToStorage() {
      mStorage.onDeleteNewsCountValue();
   }
}