package ru.mobile.beerhoven.data.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.INotificationRepository;
import ru.mobile.beerhoven.utils.Constants;

public class NotificationRepository implements INotificationRepository {
   private final DatabaseReference mFirebaseRef;
   private final List<Order> mOrderList;
   private final MutableLiveData<List<Order>> mMutableList;

   public NotificationRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mMutableList = new MutableLiveData<>();
      this.mOrderList = new ArrayList<>();
   }

   @Override
   public MutableLiveData<List<Order>> getNotificationListFromDatabase() {
      if (mOrderList.size() == 0) {
         onGetConfirmList();
      }
      mMutableList.setValue(mOrderList);
      return mMutableList;
   }

   private void onGetConfirmList() {
      mFirebaseRef.child(Constants.NODE_CONFIRMS).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Order order = snapshot.getValue(Order.class);
            assert order != null;
            order.setId(snapshot.getKey());
            if (!mOrderList.contains(order)) {
               mOrderList.add(order);
            }
            mMutableList.postValue(mOrderList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Order order = snapshot.getValue(Order.class);
            assert order != null;
            order.setId(snapshot.getKey());
            if (mOrderList.contains(order)) {
               mOrderList.set(mOrderList.indexOf(order), order);
            }
            mMutableList.postValue(mOrderList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Order order = snapshot.getValue(Order.class);
            assert order != null;
            order.setId(snapshot.getKey());
            mOrderList.remove(order);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {}
      });
   }
}
