package ru.mobile.beerhoven.presentation.authentication;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.domain.model.User;
import ru.mobile.beerhoven.domain.repository.IAuthRepository;
import ru.mobile.beerhoven.domain.repository.IAuthVerificationService;
import ru.mobile.beerhoven.domain.repository.IPreferencesStorage;

public class AuthViewModel extends ViewModel {
   private IAuthRepository mRepository;
   private IAuthVerificationService mService;
   private IPreferencesStorage mStorage;

   public AuthViewModel(Context context) {
      this.mStorage = new PreferencesStorage(context);
   }

   public AuthViewModel(IAuthRepository repository) {
      this.mRepository = repository;
   }

   public AuthViewModel(Context context, IAuthRepository repository, IAuthVerificationService service) {
      this.mRepository = repository;
      this.mStorage = new PreferencesStorage(context);
      this.mService = service;
   }

   public void onCreateUserToRepository(User customer) {
      mRepository.onCreateUser(customer);
   }

   public LiveData<User> getCurrentUserToRepository() {
      return mRepository.getCurrentUserObject();
   }

   public String getUserNameToStorage() {
      return mStorage.getUserNameToStorage();
   }

   public void onSaveNameToStorage(String name) {
      mStorage.onSaveUserName(name);
   }

   public LiveData<Boolean> onSendVerificationCodeToUser(String phoneNumber) {
      return mService.sendVerificationCodeToUser(phoneNumber);
   }
}
