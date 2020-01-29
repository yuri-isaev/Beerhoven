package ru.mobile.beerhoven.data.repository;

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
import java.util.HashMap;
import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IOrderDetailsRepository;
import ru.mobile.beerhoven.domain.repository.IUserStateRepository;
import ru.mobile.beerhoven.utils.Constants;

public class OrderDetailsRepository implements IOrderDetailsRepository, IUserStateRepository {
   private final List<Product> mDataList;
   private final MutableLiveData<List<Product>> mMutableList;
   private String UID;
   public final HashMap<String, Object> mStateMap;
   private final DatabaseReference mFirebaseRef;

   public OrderDetailsRepository() {
      this.mDataList = new ArrayList<>();
      this.mMutableList = new MutableLiveData<>();
      this.mStateMap = new HashMap<>();
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
   }

   @Override
   public String getCurrentUserPhoneNumber() {
      return this.UID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   @Override
   public MutableLiveData<List<Product>> getOrderDetailsList() {
      if (mDataList.size() == 0) {
         readOrderList((String) mStateMap.get("push_id"));
      }
      mMutableList.setValue(mDataList);
      return mMutableList;
   }

   private void readOrderList(String pushID) {
      mFirebaseRef.child(Constants.NODE_ORDERS).child(UID).child(pushID).addChildEventListener(new ChildEventListener() {
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
}
