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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.mobile.beerhoven.data.storage.IOrderRepository;
import ru.mobile.beerhoven.data.storage.IUserStateRepository;
import ru.mobile.beerhoven.models.Item;
import ru.mobile.beerhoven.utils.Constants;

public class OrderRepository implements IOrderRepository, IUserStateRepository {
   private final List<Item> mDataList;
   private final MutableLiveData<List<Item>> mMutableList;
   private String UID;
   private final DatabaseReference mFirebaseRef;

   public OrderRepository() {
      this.mDataList = new ArrayList<>();
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mMutableList = new MutableLiveData<>();
   }

   // Get current user phone number
   @Override
   public String getCurrentUserPhoneNumber() {
      return this.UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   // Get order id key
   @Override
   public String readOrderId() {
      return mFirebaseRef.child(Constants.NODE_ORDERS).child(UID).push().getKey();
   }

   // Get order confirm list
   @Override
   public MutableLiveData<List<Item>> getOrderMutableList() {
      if (mDataList.size() == 0) {
         readOrderConfirmList();
      }
      mMutableList.setValue(mDataList);
      return mMutableList;
   }

   @Override
   public void readOrderConfirmList() {
      mFirebaseRef.child(Constants.NODE_CONFIRMS).child(UID).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Item order = snapshot.getValue(Item.class);
            assert order != null;
            order.setId(snapshot.getKey());
            if (!mDataList.contains(order)) {
               mDataList.add(order);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Item order = snapshot.getValue(Item.class);
            assert order != null;
            order.setId(snapshot.getKey());
            if (mDataList.contains(order)) {
               mDataList.set(mDataList.indexOf(order), order);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Item order = snapshot.getValue(Item.class);
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
