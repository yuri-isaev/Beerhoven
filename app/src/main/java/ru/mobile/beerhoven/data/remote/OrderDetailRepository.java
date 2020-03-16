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

import java.util.ArrayList;
import java.util.List;

import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IOrderDetailRepository;
import ru.mobile.beerhoven.domain.repository.IUserRepository;
import ru.mobile.beerhoven.utils.Constants;

public class OrderDetailRepository implements IOrderDetailRepository, IUserRepository {
   private final DatabaseReference mFirebaseRef;
   private final List<Product> mProductList;
   private final MutableLiveData<List<Product>> mMutableList;
   private final String mUserPhoneId;

   public OrderDetailRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mProductList = new ArrayList<>();
      this.mMutableList = new MutableLiveData<>();
      this.mUserPhoneId = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   @Override
   public String getCurrentUserPhoneNumber() {
      return mUserPhoneId;
   }

   @Override
   public MutableLiveData<List<Product>> getOrderDetailsList() {
      if (mProductList.size() == 0) {
         String push = MapStorage.productMap.get("push_id");
         readOrderList(push);
      }
      mMutableList.setValue(mProductList);
      return mMutableList;
   }

   private void readOrderList(String pushID) {
      mFirebaseRef.child(Constants.NODE_ORDERS).child(mUserPhoneId).child(pushID).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Product order = snapshot.getValue(Product.class);
            assert order != null;
            order.setId(snapshot.getKey());

            if (!mProductList.contains(order)) {
               mProductList.add(order);
            }
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Product order = snapshot.getValue(Product.class);
            assert order != null;
            order.setId(snapshot.getKey());

            if (mProductList.contains(order)) {
               mProductList.set(mProductList.indexOf(order), order);
            }
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Product order = snapshot.getValue(Product.class);
            assert order != null;
            order.setId(snapshot.getKey());
            mProductList.remove(order);
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

         @Override
         public void onCancelled(@NonNull DatabaseError error) {}
      });
   }
}
