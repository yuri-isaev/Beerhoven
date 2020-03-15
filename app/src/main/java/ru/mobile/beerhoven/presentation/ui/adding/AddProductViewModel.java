package ru.mobile.beerhoven.presentation.ui.adding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.domain.repository.IAddProductRepository;

public class AddProductViewModel extends ViewModel {
   private MutableLiveData<Boolean> mMutableList;
   private final IAddProductRepository mRepository;

   public AddProductViewModel(IAddProductRepository repository) {
      this.mMutableList = new MutableLiveData<>();
      this.mRepository = repository;
   }

   public LiveData<Boolean> onGetPostResponseToRepository() {
      return mMutableList = mRepository.onGetCreatePostResponse();
   }

   public LiveData<Boolean> onGetCameraResponseToRepository() {
      return mMutableList = mRepository.onGetCameraResponse();
   }

   public LiveData<Boolean> onGetImageResponseToRepository() {
      return mMutableList = mRepository.onGetImageResponse();
   }
}
