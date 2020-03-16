package ru.mobile.beerhoven.data.remote;

import static java.util.Objects.*;

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

import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.ICatalogRepository;
import ru.mobile.beerhoven.utils.Constants;

public class CatalogRepository implements ICatalogRepository {
   private final DatabaseReference mFirebaseRef;
   private final List<Product> mProductList;
   private final MutableLiveData<List<Product>> mMutableList;
   private final MutableLiveData<String> mMutableData;
   private final String mUserPhoneId;

   public CatalogRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mProductList = new ArrayList<>();
      this.mMutableList = new MutableLiveData<>();
      this.mMutableData = new MutableLiveData<>();
      this.mUserPhoneId = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   // Read store catalog
   @Override
   public MutableLiveData<List<Product>> readProductList() {
      if (mProductList.size() == 0) {
         readCatalogList();
      }
      mMutableList.setValue(mProductList);
      return mMutableList;
   }

   private void readCatalogList() {
      mFirebaseRef.child(Constants.NODE_PRODUCTS).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Product product = snapshot.getValue(Product.class);
            assert product != null;
            product.setId(snapshot.getKey());

            if (!mProductList.contains(product)) {
               mProductList.add(product);
            }
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Product product = snapshot.getValue(Product.class);
            assert product != null;
            product.setId(snapshot.getKey());
            if (mProductList.contains(product)) {
               mProductList.set(mProductList.indexOf(product), product);
            }
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Product product = snapshot.getValue(Product.class);
            assert product != null;
            product.setId(snapshot.getKey());
            if (mProductList.contains(product)) {
               mProductList.remove(product);
            }
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

         @Override
         public void onCancelled(@NonNull DatabaseError error) {}
      });
   }

   // Create store catalog item
   @Override
   public MutableLiveData<String> addProductToCart() {
      addCatalogItem();
      mMutableData.setValue(null);
      return mMutableData;
   }

   private void addCatalogItem() {
      HashMap<String, String> catalog = MapStorage.productMap;
      HashMap<String, Double> price = MapStorage.priceMap;

      Product post = new Product();
      post.setName(catalog.get("name"));
      post.setCountry(catalog.get("country"));
      post.setManufacture(catalog.get("manufacture"));
      post.setStyle(catalog.get("style"));
      post.setFortress(catalog.get("fortress"));
      post.setDensity(catalog.get("density"));
      post.setDescription(catalog.get("description"));
      post.setUrl(catalog.get("url"));
      post.setQuantity(catalog.get("quantity"));
      post.setPrice(price.get("price"));
      post.setTotal(price.get("total"));

      assert mUserPhoneId != null;
      mFirebaseRef.child(Constants.NODE_CART).child(mUserPhoneId)
          .child(requireNonNull(MapStorage.productMap.get("productID"))).setValue(post);
   }

   // Delete store catalog item
   @Override
   public MutableLiveData<String> deleteProductFromCart() {
      deleteCatalogItem();
      mMutableData.setValue(null);
      return mMutableData;
   }

   private void deleteCatalogItem() {
      HashMap<String, String> map = MapStorage.productMap;

      mFirebaseRef.child(Constants.NODE_PRODUCTS)
          .child(requireNonNull(map.get("productID")))
          .removeValue();
      FirebaseStorage.getInstance()
          .getReferenceFromUrl(requireNonNull(map.get("image")))
          .delete();
   }
}
