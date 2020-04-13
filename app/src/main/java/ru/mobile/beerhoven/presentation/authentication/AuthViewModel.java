package ru.mobile.beerhoven.presentation.authentication;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.User;
import ru.mobile.beerhoven.domain.repository.IAuthRepository;

public class AuthViewModel extends ViewModel {
   private IAuthRepository mRepository;
   private PreferencesStorage mStorage;

   public AuthViewModel(Context context) {
      this.mStorage = new PreferencesStorage((Application) context);
   }

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

   public String getUserNameToStorage() {
      return mStorage.getUserNameToStorage();
   }

   public void onSaveNameToStorage(String name) {
      mStorage.onSaveUserName(name);
   }
}
