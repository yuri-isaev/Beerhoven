package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

public interface IAuthService {
   MutableLiveData<Boolean> sendVerificationCodeToUser(String phoneNumber);
}
