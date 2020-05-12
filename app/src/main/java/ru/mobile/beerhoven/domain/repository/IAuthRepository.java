package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import ru.mobile.beerhoven.domain.model.User;

public interface IAuthRepository {
   MutableLiveData<User> getCurrentUserObject();
   void onCreateUser(User customer);
}
