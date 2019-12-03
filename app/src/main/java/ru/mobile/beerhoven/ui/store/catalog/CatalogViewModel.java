package ru.mobile.beerhoven.ui.store.catalog;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.models.Item;

public class CatalogViewModel extends ViewModel {

   private MutableLiveData mCatalogList;
   private CatalogRepository mCatalogRepository;

   public CatalogViewModel(@NonNull CatalogRepository repo) {
      this.mCatalogRepository = repo;
      this.mCatalogList = new MutableLiveData();
   }

   public MutableLiveData<List<Item>> getCatalogList() {
      mCatalogList = CatalogRepository.getInstance().getList();
      return mCatalogList;
   }
}
