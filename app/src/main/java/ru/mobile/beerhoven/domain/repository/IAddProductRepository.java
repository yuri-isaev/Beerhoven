package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

public interface IAddProductRepository {
   MutableLiveData<Boolean> getListImageCamera();
   MutableLiveData<Boolean> getListImageGallery();
   MutableLiveData<Boolean> getListDataBase();
}
