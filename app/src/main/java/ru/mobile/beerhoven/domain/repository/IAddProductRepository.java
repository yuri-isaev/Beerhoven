package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

public interface IAddProductRepository {
   MutableLiveData<Boolean> onGetCameraResponse();
   MutableLiveData<Boolean> onGetImageResponse();
   MutableLiveData<Boolean> onGetCreatePostResponse();
}
