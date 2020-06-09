package ru.mobile.beerhoven.presentation.authentication;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.User;
import ru.mobile.beerhoven.domain.repository.IAuthRepository;
import ru.mobile.beerhoven.domain.repository.IAuthService;
import ru.mobile.beerhoven.domain.repository.IPreferencesStorage;

public class AuthViewModel extends ViewModel {
   private IAuthRepository iRepository;
   private IAuthService iService;
   private IPreferencesStorage iStorage;

   public AuthViewModel(Context context) {
      this.iStorage = new PreferencesStorage(context);
   }

   public AuthViewModel(IAuthRepository repository) {
      this.iRepository = repository;
   }

   public AuthViewModel(Context context, IAuthRepository repository, IAuthService service) {
      this.iRepository = repository;
      this.iStorage = new PreferencesStorage(context);
      this.iService = service;
   }

   public void onCreateUserToRepository(User customer) {
      iRepository.onCreateUserToDatabase(customer);
   }

   public LiveData<User> getCurrentUser() {
      return iRepository.getCurrentUserFromDatabase();
   }

   public String getUserNameToStorage() {
      return iStorage.getUserName();
   }

   public void onSaveNameToStorage(String name) {
      iStorage.onSaveUserName(name);
   }

   public LiveData<Boolean> onAuthenticationConfirm(String phoneNumber) {
      return iService.sendVerificationCodeToUser(phoneNumber);
   }
}
