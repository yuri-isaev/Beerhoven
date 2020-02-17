package ru.mobile.beerhoven.ui.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mobile.beerhoven.data.remote.AddProductRepository;
import ru.mobile.beerhoven.domain.repository.IAddProductRepository;

public class AddProductViewModel extends ViewModel {
   private MutableLiveData<Boolean> mMutableList;
   private final AddProductRepository mRepository;

   public AddProductViewModel(IAddProductRepository repository) {
      this.mMutableList = new MutableLiveData<>();
      this.mRepository = (AddProductRepository) repository;
   }

   public LiveData<Boolean> getReportAddDataBase() {
      return mMutableList = mRepository.getListDataBase();
   }

   public LiveData<Boolean> getReportImageCamera() {
      return mMutableList = mRepository.getListImageCamera();
   }

   public LiveData<Boolean> getReportImageGallery() {
      return mMutableList = mRepository.getListImageGallery();
   }
}
