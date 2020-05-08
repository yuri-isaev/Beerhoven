package ru.mobile.beerhoven.data.remote;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.ICartRepository;
import ru.mobile.beerhoven.utils.Constants;

public class CartRepository implements ICartRepository {
   private final DatabaseReference mFirebaseRef;
   private final List<Product> mProductList;
   private final MutableLiveData<List<Product>> mMutableList;
   private final String mUserPhoneNumber;

   public CartRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mProductList = new ArrayList<>();
      this.mMutableList = new MutableLiveData<>();
      this.mUserPhoneNumber = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   @Override
   public MutableLiveData<List<Product>> getCartMutableList() {
      if (mProductList.size() == 0) {
         onGetCartList();
      }
      mMutableList.setValue(mProductList);
      return mMutableList;
   }

   private void onGetCartList() {
      assert mUserPhoneNumber != null;
      mFirebaseRef.child(Constants.NODE_CART).child(mUserPhoneNumber).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Product product = dataSnapshot.getValue(Product.class);
            requireNonNull(product).setId(dataSnapshot.getKey());
            if (!mProductList.contains(product)) {
               mProductList.add(product);
            }
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Product product = dataSnapshot.getValue(Product.class);
            requireNonNull(product).setId(dataSnapshot.getKey());
            if (mProductList.contains(product)) {
               mProductList.set(mProductList.indexOf(product), product);
            }
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            Product product = dataSnapshot.getValue(Product.class);
            requireNonNull(product).setId(dataSnapshot.getKey());
            mProductList.remove(product);
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {
         }
      });
   }

   @Override
   public void onDeleteCartItem(String item) {
      mFirebaseRef.child(Constants.NODE_CART).child(mUserPhoneNumber).child(item).removeValue();
   }

   @Override
   public void onDeleteUserCartList() {
      mFirebaseRef.child(Constants.NODE_CART).child(mUserPhoneNumber).removeValue();
   }
}
