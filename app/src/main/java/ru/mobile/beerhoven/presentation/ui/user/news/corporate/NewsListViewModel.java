package ru.mobile.beerhoven.presentation.ui.user.news.corporate;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.repository.INewsRepository;

public class NewsListViewModel extends ViewModel {
   private final INewsRepository iRepository;
   private final PreferencesStorage mStorage;

   public NewsListViewModel(Context context, INewsRepository repository) {
      this.iRepository = repository;
      this.mStorage = new PreferencesStorage(context);
   }

   public LiveData<List<News>> getNewsListFromRepository() {
      return iRepository.getNewsListFromDatabase();
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