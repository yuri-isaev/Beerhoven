package ru.mobile.beerhoven.presentation.authentication;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.User;
import ru.mobile.beerhoven.domain.repository.IAuthRepository;

public class AuthViewModel extends ViewModel {
   private final IAuthRepository mRepository;
   private PreferencesStorage mStorage;

   public AuthViewModel(IAuthRepository repository) {
      this.mRepository = repository;
   }

   public AuthViewModel(IAuthRepository repository, Context context) {
      this.mRepository = repository;
      this.mStorage = new PreferencesStorage((Application) context);
   }

   public void onCreateUserToRepository(User user) {
      mRepository.onCreateUser(user);
   }

   public Object getCurrentUserToRepository() {
      return mRepository.getCurrentUser();
   }

   public void onSaveNameToStorage(String name) {
      mStorage.onSaveUserName(name);
   }
}
