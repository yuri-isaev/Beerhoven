package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.News;

public interface INewsRepository {
   void addNewsDataToDatabase(News model);
   void addNewsPostToDatabase(News model);
   MutableLiveData<List<News>> getNewsList();
}
