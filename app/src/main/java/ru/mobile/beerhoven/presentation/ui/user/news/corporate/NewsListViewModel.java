package ru.mobile.beerhoven.presentation.ui.user.news.corporate;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.repository.INewsRepository;
import ru.mobile.beerhoven.domain.repository.IPreferencesStorage;

public class NewsListViewModel extends ViewModel {
   private final INewsRepository iRepository;
   private final IPreferencesStorage iStorage;

   public NewsListViewModel(Context ctx, INewsRepository repo) {
      this.iRepository = repo;
      this.iStorage = new PreferencesStorage(ctx);
   }

   public LiveData<List<News>> getNewsListFromRepository() {
      return iRepository.getNewsListFromDatabase();
   }

   public int getNewsCountFromStorage() {
      return iStorage.onGetNewsCountValue();
   }

   public void onSaveNewsCounterToStorage(int counterValue) {
      iStorage.onSaveNewsCountValue(counterValue);
   }

   public void onDeleteNewsCounterToStorage() {
      iStorage.onDeleteNewsCountValue();
   }
}