package ru.mobile.beerhoven.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ru.mobile.beerhoven.data.storage.CrudRepository;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.HashMapRepository;
import ru.mobile.beerhoven.models.Item;

public class CatalogRepository implements CrudRepository<Item> {
   private static CatalogRepository sInstance;
   private final List<Item> mDataList = new ArrayList<>();
   private final MutableLiveData<List<Item>> mMutableList = new MutableLiveData<>();
   private String data;
   private final MutableLiveData<String> mValue = new MutableLiveData<>();
   private final String UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   private final DatabaseReference mInstanceFirebase = FirebaseDatabase.getInstance().getReference();

   // Singleton pattern
   public static CatalogRepository getInstance() {
      if (sInstance == null) {
         sInstance = new CatalogRepository();
      }
      return sInstance;
   }

   // Read Catalog
   public MutableLiveData<List<Item>> readList() {
      if (mDataList.size() == 0) {
         initialCatalogList();
      }
      mMutableList.setValue(mDataList);
      return mMutableList;
   }

   private void initialCatalogList() {
      mInstanceFirebase
          .child(Constants.NODE_ITEMS)
          .addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Item item = snapshot.getValue(Item.class);
            assert item != null;
            item.setId(snapshot.getKey());

            if (!mDataList.contains(item)) {
               mDataList.add(item);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Item item = snapshot.getValue(Item.class);
            assert item != null;
            item.setId(snapshot.getKey());
            if (mDataList.contains(item)) {
               mDataList.set(mDataList.indexOf(item), item);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Item item = snapshot.getValue(Item.class);
            assert item != null;
            item.setId(snapshot.getKey());
            if (mDataList.contains(item)) {
               mDataList.remove(item);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

         @Override
         public void onCancelled(@NonNull DatabaseError error) {}
      });
   }

   // Create Catalog Item
   public MutableLiveData<String> createItem() {
      if (data == null) {
         addCatalogItem();
      }
      mValue.setValue(data);
      return mValue;
   }

   private void addCatalogItem() {
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
      post.setDate(catalog.get("ru/mobile/beerhoven/data"));
      post.setTime(catalog.get("time"));
      post.setUrl(catalog.get("url"));
      post.setQuantity(catalog.get("quantity"));
      post.setPrice(price.get("catalog_price"));
      post.setTotal(price.get("catalog_total"));

      assert UID != null;
      mInstanceFirebase
          .child(Constants.NODE_CART)
          .child(UID)
          .child(Objects.requireNonNull(id.get("catalog_id")))
          .setValue(post);
   }

   // Delete Catalog Item
   public MutableLiveData<String> deleteItem() {
      if (data == null) deleteCatalogItem();
      mValue.setValue(data);
      return mValue;
   }

   private void deleteCatalogItem() {
      HashMap<String, String> pid = HashMapRepository.pushMap;
      mInstanceFirebase.child(Constants.NODE_ITEMS)
          .child(Objects.requireNonNull(pid.get("item_id")))
          .removeValue();
      FirebaseStorage.getInstance()
          .getReferenceFromUrl(Objects.requireNonNull(pid.get("image")))
          .delete();
   }
}

