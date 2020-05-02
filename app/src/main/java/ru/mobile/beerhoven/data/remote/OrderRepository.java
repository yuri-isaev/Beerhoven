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
   private final List<Order> mOrderList;
   private final List<String> mPushOrderList;
   private final MutableLiveData<String> mMutableData;
   private final MutableLiveData<List<Order>> mMutableList;
   private final MutableLiveData<List<String>> mMutablePushOrderList;
   private String mOrderData;
   private final String mUserPhoneId;

   public OrderRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
   
      this.mOrderList = new ArrayList<>();
      this.mMutableData = new MutableLiveData<>();
      this.mMutableList = new MutableLiveData<>();
      this.mUserPhoneId = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
      mPushOrderList = new ArrayList<>();
      mMutablePushOrderList = new MutableLiveData<>();
   }

   // Get current user phone number
   @Override
   public String getCurrentUserPhoneNumber() {
      return this.mUserPhoneId;
   }

   @Override
   public MutableLiveData<List<Order>> getOrderMutableList() {
      if (mOrderList.size() == 0) {
         onGetOrderConfirmList();
      }
      mMutableList.setValue(mOrderList);
      return mMutableList;
   }

   @Override
   public MutableLiveData<String> getOrderMutableData() {
      if (mOrderData == null) {
         getPushOrderKey();
      }
      mMutableData.setValue(mOrderData);
      return mMutableData;
   }

   private void getPushOrderKey() {
      mFirebaseRef.child(Constants.NODE_ORDERS).child(mUserPhoneId).addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot push : dataSnapshot.getChildren()) {
               mOrderData = push.getKey();
               mMutableData.postValue(mOrderData);
            }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {}
      });
   }

   @Override
   public void onGetOrderConfirmList() {
      assert mUserPhoneId != null;
      mFirebaseRef.child(Constants.NODE_CONFIRMS).child(mUserPhoneId).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Order confirm = snapshot.getValue(Order.class);
            assert confirm != null;
            confirm.setId(snapshot.getKey());
            if (!mOrderList.contains(confirm)) {
               mOrderList.add(confirm);
            }
            mMutableList.postValue(mOrderList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Order confirm = snapshot.getValue(Order.class);
            assert confirm != null;
            confirm.setId(snapshot.getKey());
            if (mOrderList.contains(confirm)) {
               mOrderList.set(mOrderList.indexOf(confirm), confirm);
            }
            mMutableList.postValue(mOrderList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Order confirm = snapshot.getValue(Order.class);
            assert confirm != null;
            confirm.setId(snapshot.getKey());
            mOrderList.remove(confirm);
            mMutableList.postValue(mOrderList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

         @Override
         public void onCancelled(@NonNull DatabaseError error) {}
      });
   }

   // Get order confirm by id key
   @Override
   public void onDeleteOrderById(String keyId) {
      mFirebaseRef.child(Constants.NODE_CONFIRMS).child(mUserPhoneId).child(keyId).removeValue();
   }
}
