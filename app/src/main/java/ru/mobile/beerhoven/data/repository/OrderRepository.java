package ru.mobile.beerhoven.data.repository;

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
import ru.mobile.beerhoven.domain.repository.IOrderRepository;
import ru.mobile.beerhoven.domain.repository.IUserStateRepository;
import ru.mobile.beerhoven.utils.Constants;

public class OrderRepository implements IOrderRepository, IUserStateRepository {
   private final List<Product> mDataList;
   private final MutableLiveData<List<Product>> mMutableList;
   private final String UID;
   private final DatabaseReference mFirebaseRef;

   public OrderRepository() {
      this.mDataList = new ArrayList<>();
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mMutableList = new MutableLiveData<>();
      this.UID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   // Get current user phone number
   @Override
   public String getCurrentUserPhoneNumber() {
      return this.UID;
   }

   // Get order id key
   @Override
   public String readOrderId() {
      return mFirebaseRef.child(Constants.NODE_ORDERS).child(UID).push().getKey();
   }

   // Get order confirm list
   @Override
   public MutableLiveData<List<Product>> getOrderMutableList() {
      if (mDataList.size() == 0) {
         readOrderConfirmList();
      }
      mMutableList.setValue(mDataList);
      return mMutableList;
   }

   @Override
   public void readOrderConfirmList() {
      assert UID != null;
      mFirebaseRef.child(Constants.NODE_CONFIRMS).child(UID).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Product order = snapshot.getValue(Product.class);
            assert order != null;
            order.setId(snapshot.getKey());
            if (!mDataList.contains(order)) {
               mDataList.add(order);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Product order = snapshot.getValue(Product.class);
            assert order != null;
            order.setId(snapshot.getKey());
            if (mDataList.contains(order)) {
               mDataList.set(mDataList.indexOf(order), order);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Product order = snapshot.getValue(Product.class);
            assert order != null;
            order.setId(snapshot.getKey());
            mDataList.remove(order);
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

         @Override
         public void onCancelled(@NonNull DatabaseError error) {}
      });
   }

   // Get order confirm by id key
   @Override
   public void deleteOrderById(String keyId) {
      mFirebaseRef.child(Constants.NODE_CONFIRMS).child(UID).child(keyId).removeValue();
   }
}
