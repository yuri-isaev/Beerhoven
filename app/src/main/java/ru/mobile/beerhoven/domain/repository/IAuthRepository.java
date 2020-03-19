package ru.mobile.beerhoven.domain.repository;

import ru.mobile.beerhoven.domain.model.User;

public interface IAuthRepository {
   Object getCurrentUser();
   void onCreateUser(User user);
}
