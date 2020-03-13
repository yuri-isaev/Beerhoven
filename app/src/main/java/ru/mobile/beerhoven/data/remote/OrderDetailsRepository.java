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
import ru.mobile.beerhoven.domain.repository.IOrderDetailsRepository;
import ru.mobile.beerhoven.domain.repository.IUserRepository;
import ru.mobile.beerhoven.utils.Constants;

public class OrderDetailsRepository implements IOrderDetailsRepository, IUserRepository {
   private final DatabaseReference mFirebaseRef;
   private final List<Product> mDataList;
   private final MutableLiveData<List<Product>> mMutableList;
   private final String mUserPhoneID;

   public OrderDetailsRepository() {
      this.mDataList = new ArrayList<>();
      this.mMutableList = new MutableLiveData<>();
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mUserPhoneID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   @Override
   public String getCurrentUserPhoneNumber() {
      return mUserPhoneID;
   }

   @Override
   public MutableLiveData<List<Product>> getOrderDetailsList() {
      if (mDataList.size() == 0) {
         String push = MapStorage.productMap.get("push_id");
         readOrderList(push);
      }
      mMutableList.setValue(mDataList);
      return mMutableList;
   }

   private void readOrderList(String pushID) {
      mFirebaseRef.child(Constants.NODE_ORDERS).child(mUserPhoneID).child(pushID).addChildEventListener(new ChildEventListener() {
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
