package ru.mobile.beerhoven.ui.store.catalog;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mobile.beerhoven.interfaces.CrudRepository;
import ru.mobile.beerhoven.models.Item;

public class CatalogViewModel extends ViewModel {

   private MutableLiveData mCatalogList;
   private final CrudRepository<Item> mRepo;

   public CatalogViewModel(@NonNull CrudRepository<Item> repo) {
      this.mRepo = repo;
      this.mCatalogList = new MutableLiveData();
   }

   public MutableLiveData<List<Item>> getCatalogList() {
      mCatalogList = CatalogRepository.getInstance().getList();
      return mCatalogList;
   }
}
