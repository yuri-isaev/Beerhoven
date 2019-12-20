package ru.mobile.beerhoven.ui.store.catalog;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.HashMapRepository;
import ru.mobile.beerhoven.interfaces.CrudRepository;
import ru.mobile.beerhoven.models.Item;

public class CatalogRepository implements CrudRepository<Item> {

   private static CatalogRepository sInstance;
   private static final List<Item> mDataList = new ArrayList<>();
   private final MutableLiveData<List<Item>> mMutableList = new MutableLiveData<>();
   private String data;
   private final MutableLiveData<String> mValue = new MutableLiveData<>();
   private final String UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   private final DatabaseReference mCartListRefer = FirebaseDatabase.getInstance().getReference();

   private CatalogRepository() {}

   // Singleton pattern.
   public static CatalogRepository getInstance() {
      if (sInstance == null) {
         sInstance = new CatalogRepository();
      }
      return sInstance;
   }

   // READ
   @Override
   public MutableLiveData<List<Item>> getList() {
      if (mDataList.size() == 0) {
         loadList();
      }
      mMutableList.setValue(mDataList);
      return mMutableList;
   }

   private void loadList() {
      for (int i = 1; i <= 10; i++)
         mDataList.add(new Item());
   }

   // UPDATE
   public MutableLiveData<String> addCartItem() {
      if (data == null) {
         createItem();
      }
      mValue.setValue(data);
      return mValue;
   }

   private void createItem() {
      HashMap<String, String> id = HashMapRepository.idMap;
      HashMap<String, String> catalog = HashMapRepository.catalogMap;
      HashMap<String, Double> price = HashMapRepository.priceMap;

      Item post = new Item();
      post.setTitle(catalog.get("name"));
      post.setCountry(catalog.get("country"));
      post.setManufacture(catalog.get("manufacture"));
      post.setStyle(catalog.get("style"));
      post.setFortress(catalog.get("fortress"));
      post.setDensity(catalog.get("density"));
      post.setDescription(catalog.get("description"));
      post.setDate(catalog.get("data"));
      post.setTime(catalog.get("time"));
      post.setUrl(catalog.get("url"));
      post.setQuantity(catalog.get("quantity"));
      post.setPrice(price.get("catalog_price"));
      post.setTotal(price.get("catalog_total"));

      assert UID != null;
      mCartListRefer.child(Constants.NODE_CART).child(UID)
          .child(Objects.requireNonNull(id.get("catalog_id"))).setValue(post);
   }

   // DELETE
   public MutableLiveData<String> deleteCatalogItem() {
      if (data == null) deleteItem();
      mValue.setValue(data);
      return mValue;
   }
   private void deleteItem() {
      HashMap<String, String> pid = HashMapRepository.pushMap;

      mCartListRefer
          .child(Constants.NODE_ITEMS)
          .child(Objects.requireNonNull(pid.get("item_id")))
          .removeValue();
      FirebaseStorage.getInstance()
          .getReferenceFromUrl(Objects.requireNonNull(pid.get("image")))
          .delete();
   }
}

