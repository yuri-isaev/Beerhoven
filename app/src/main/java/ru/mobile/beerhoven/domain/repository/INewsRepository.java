package ru.mobile.beerhoven.domain.repository;

import ru.mobile.beerhoven.domain.model.News;

public interface INewsRepository {
   void addNewsDataToDatabase(News model);
   void addNewsPostToDatabase(News model);
}
