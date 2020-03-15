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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IOrderRepository;
import ru.mobile.beerhoven.domain.repository.IUserRepository;
import ru.mobile.beerhoven.utils.Constants;

public class OrderRepository implements IOrderRepository, IUserRepository {
   private final DatabaseReference mFirebaseRef;
   private final List<Order> mDataList;
   private final MutableLiveData<String> mMutableOrderData;
   private final MutableLiveData<List<Order>> mMutableList;
   private String mOrderData;
   private final String mUserPhoneID;

   public OrderRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mDataList = new ArrayList<>();
      this.mMutableOrderData = new MutableLiveData<>();
      this.mMutableList = new MutableLiveData<>();
      this.mOrderData = null;
      this.mUserPhoneID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   // Get current user phone number
   @Override
   public String getCurrentUserPhoneNumber() {
      return this.mUserPhoneID;
   }

   // Get order confirm list
   @Override
   public MutableLiveData<List<Order>> getOrderMutableList() {
      if (mDataList.size() == 0) {
         readOrderConfirmList();
      }
      mMutableList.setValue(mDataList);
      return mMutableList;
   }

   @Override
   public MutableLiveData<String> getOrderMutableData() {
      if (mOrderData == null) {
         readOrderData();
      }
      mMutableOrderData.setValue(mOrderData);
      return mMutableOrderData;
   }

   private void readOrderData() {
      mFirebaseRef.child(Constants.NODE_ORDERS).child(mUserPhoneID).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot push : dataSnapshot.getChildren()) {
               mOrderData = push.getKey();
            }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {}
      });
   }

   @Override
   public void readOrderConfirmList() {
      assert mUserPhoneID != null;
      mFirebaseRef.child(Constants.NODE_CONFIRMS).child(mUserPhoneID).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Order confirm = snapshot.getValue(Order.class);
            assert confirm != null;
            confirm.setId(snapshot.getKey());
            if (!mDataList.contains(confirm)) {
               mDataList.add(confirm);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Order confirm = snapshot.getValue(Order.class);
            assert confirm != null;
            confirm.setId(snapshot.getKey());
            if (mDataList.contains(confirm)) {
               mDataList.set(mDataList.indexOf(confirm), confirm);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Order confirm = snapshot.getValue(Order.class);
            assert confirm != null;
            confirm.setId(snapshot.getKey());
            mDataList.remove(confirm);
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
      mFirebaseRef.child(Constants.NODE_CONFIRMS).child(mUserPhoneID).child(keyId).removeValue();
   }
}
