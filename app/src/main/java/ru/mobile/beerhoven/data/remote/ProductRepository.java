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
import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IProductRepository;
import ru.mobile.beerhoven.utils.Constants;

public class ProductRepository implements IProductRepository {
   private final DatabaseReference mFirebaseRef;
   private final List<Product> mProductList;
   private final MutableLiveData<List<Product>> mMutableList;
   private final MutableLiveData<String> mMutableData;
   private final String mUserPhoneId;

   public ProductRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mProductList = new ArrayList<>();
      this.mMutableList = new MutableLiveData<>();
      this.mMutableData = new MutableLiveData<>();
      this.mUserPhoneId = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   @Override
   public MutableLiveData<List<Product>> getProductList() {
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
            mProductList.remove(product);
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

         @Override
         public void onCancelled(@NonNull DatabaseError error) {}
      });
   }

   @Override
   public MutableLiveData<String> addCartProductToRepository(Product product) {
      addCatalogItem(product);
      mMutableData.setValue(null);
      return mMutableData;
   }

   private void addCatalogItem(@NonNull Product product) {
      assert mUserPhoneId != null;
      mFirebaseRef.child(Constants.NODE_CART).child(mUserPhoneId).child(product.getId()).setValue(product);
   }

   @Override
   public MutableLiveData<String> deleteProductFromRepository(Product product) {
      onDeleteProductById(product);
      mMutableData.setValue(null);
      return mMutableData;
   }

   private void onDeleteProductById(@NonNull Product product) {
      mFirebaseRef.child(Constants.NODE_PRODUCTS).child(product.getId()).removeValue();
      FirebaseStorage.getInstance().getReferenceFromUrl(product.getUrl()).delete();
   }
}
