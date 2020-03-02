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

import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IOrderRepository;
import ru.mobile.beerhoven.domain.repository.IUserStateRepository;
import ru.mobile.beerhoven.utils.Constants;

public class OrderRepository implements IOrderRepository, IUserStateRepository {
   private final DatabaseReference mFirebaseRef;
   private final List<Order> mDataList;
   private final MutableLiveData<List<Order>> mMutableList;
   private final String UID;

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
   public MutableLiveData<List<Order>> getOrderMutableList() {
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
      mFirebaseRef.child(Constants.NODE_CONFIRMS).child(UID).child(keyId).removeValue();
   }
}
