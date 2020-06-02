package ru.mobile.beerhoven.data.remote;

import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IProductRepository;
import ru.mobile.beerhoven.utils.Constants;

public class ProductRepository implements IProductRepository {
   private final DatabaseReference mFirebaseRef;
   private final List<Product> mProductList;
   private final MutableLiveData<List<Product>> mMutableList;
   private final MutableLiveData<Boolean> mMutableResponse;
   private final StorageReference mStorageRef;
   private final String mUserPhoneNumber;
   private static final String TAG = "ProductRepository";

   public ProductRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mProductList = new ArrayList<>();
      this.mMutableList = new MutableLiveData<>();
      this.mMutableResponse = new MutableLiveData<>();
      this.mStorageRef = FirebaseStorage.getInstance().getReference();
      this.mUserPhoneNumber = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   @SuppressLint("NewApi")
   @Override
   public MutableLiveData<List<Product>> getProductListFromDatabase() {
      if (mProductList.size() == 0) {
         onGetProductList();
      }
      mMutableList.setValue(mProductList);
      return mMutableList;
   }

   private void onGetProductList() {
      mFirebaseRef.child(Constants.NODE_PRODUCTS).addChildEventListener(new ChildEventListener() {
         @SuppressLint("NewApi")
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
         public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {
         }
      });
   }

   @Override
   public MutableLiveData<List<Product>> getProductListByCategoryFromDatabase(String category) {
      if (mProductList.size() == 0) {
         onGetProductListByCategory(category);
      }
      mMutableList.setValue(mProductList);
      return mMutableList;
   }

   public void onGetProductListByCategory(String category) {
      mFirebaseRef.child(Constants.NODE_PRODUCTS).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

               Product product = noteDataSnapshot.getValue(Product.class);
                  assert product != null;
                  if (!mProductList.contains(product)) {
                     if(product.getCategory().equals(category)) {
                     mProductList.add(product);
                     }
                  }
                  mMutableList.postValue(mProductList);

            }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, error.getMessage());
         }
      });
   }

   @Override
   public void onAddProductToCartDatabase(@NonNull Product product) {
      assert mUserPhoneNumber != null;
      mFirebaseRef
          .child(Constants.NODE_CART)
          .child(mUserPhoneNumber)
          .child(product.getId())
          .setValue(product);
   }

   @Override
   public void onDeleteProductFromDatabase(@NonNull Product product) {
      mFirebaseRef
          .child(Constants.NODE_PRODUCTS)
          .child(product.getId())
          .removeValue();

      FirebaseStorage.getInstance()
          .getReferenceFromUrl(product.getUri())
          .delete();
   }

   public MutableLiveData<Boolean> onAddProductToDatabase(@NonNull Product product) {
      mStorageRef.child(Constants.FOLDER_PRODUCT_IMG)
          .child(new Date().toString())
          .putFile(Uri.parse(product.getUri()))
          .addOnSuccessListener((taskSnapshot) -> {
             Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
             uriTask.addOnSuccessListener(uri -> {
                Uri downloadUri = uriTask.getResult();
                product.setUri(downloadUri.toString());

                mFirebaseRef.child(Constants.NODE_PRODUCTS).push().setValue(product)
                    .addOnSuccessListener(unused -> Log.i(TAG, "Product data added to database"))
                    .addOnFailureListener(e -> Log.i(TAG, e.getMessage()));

                mMutableResponse.setValue(true);
                Log.i(TAG, "Product image added to database storage");
             });
          })
          .addOnFailureListener(e -> {
             mMutableResponse.setValue(false);
             Log.e(TAG, e.getMessage());
          });
      return mMutableResponse;
   }
}
