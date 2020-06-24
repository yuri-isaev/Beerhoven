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
import ru.mobile.beerhoven.utils.Constants;

public class OrderRepository implements IOrderRepository {
   private final DatabaseReference mFirebaseRef;
   private final List<Order> mOrderList;
   private final MutableLiveData<List<Order>> mMutableList;
   private final String mUserPhoneNumber;

   public OrderRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mOrderList = new ArrayList<>();
      this.mMutableList = new MutableLiveData<>();
      this.mUserPhoneNumber = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   @Override
   public MutableLiveData<List<Order>> getOrderListFromDatabase() {
      if (mOrderList.size() == 0) {
         onGetOrderConfirmList();
      }
      mMutableList.setValue(mOrderList);
      return mMutableList;
   }

   public void onGetOrderConfirmList() {
      assert mUserPhoneNumber != null;
      mFirebaseRef.child(Constants.NODE_CONFIRMS).child(mUserPhoneNumber).addChildEventListener(new ChildEventListener() {
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

   @Override
   public void onDeleteOrderByIdToDatabase(String orderId) {
      mFirebaseRef
          .child(Constants.NODE_CONFIRMS)
          .child(mUserPhoneNumber)
          .child(orderId)
          .removeValue();
   }
}
