package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.News;

public interface INewsRepository {
   MutableLiveData<List<News>> getNewsListFromDatabase();
   void onAddNewsWithoutImageToDatabase(News news);
   void onAddNewsToDatabase(News news);
}
