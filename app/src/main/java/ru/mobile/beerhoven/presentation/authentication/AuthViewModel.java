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
   private IAuthRepository mRepository;
   private IAuthService mService;
   private IPreferencesStorage mStorage;

   public AuthViewModel(Context context) {
      this.mStorage = new PreferencesStorage(context);
   }

   public AuthViewModel(IAuthRepository repository) {
      this.mRepository = repository;
   }

   public AuthViewModel(Context context, IAuthRepository repository, IAuthService service) {
      this.mRepository = repository;
      this.mStorage = new PreferencesStorage(context);
      this.mService = service;
   }

   public void onCreateUserToRepository(User customer) {
      mRepository.onCreateUser(customer);
   }

   public LiveData<User> getCurrentUser() {
      return mRepository.getCurrentUserObject();
   }

   public String getUserNameToStorage() {
      return mStorage.getUserNameToStorage();
   }

   public void onSaveNameToStorage(String name) {
      mStorage.onSaveUserName(name);
   }

   public LiveData<Boolean> onAuthenticationConfirm(String phoneNumber) {
      return mService.sendVerificationCodeToUser(phoneNumber);
   }
}
