package ru.mobile.beerhoven.presentation.ui.customer.news.corporate;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.repository.INewsRepository;
import ru.mobile.beerhoven.domain.repository.IPreferencesStorage;

public class NewsListViewModel extends ViewModel {
   private final INewsRepository mRepository;
   private final IPreferencesStorage mStorage;

   public NewsListViewModel(Context ctx, INewsRepository repo) {
      this.mRepository = repo;
      this.mStorage = new PreferencesStorage(ctx);
   }

   public LiveData<List<News>> getNewsListFromRepository() {
      return mRepository.getNewsListFromDatabase();
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