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

import ru.mobile.beerhoven.models.Item;
import ru.mobile.beerhoven.utils.Constants;

public class CartRepository {
   private final List<Item> mDataList;
   private final MutableLiveData<List<Item>> mMutableList;
   private final String UID;
   private final DatabaseReference mFirebaseRef;

   public CartRepository() {
      mDataList = new ArrayList<>();
      mMutableList = new MutableLiveData<>();
      UID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
      mFirebaseRef = FirebaseDatabase.getInstance().getReference();
   }

   public MutableLiveData<List<Item>> getCartList() {
      if (mDataList.size() == 0) {
         readCartList();
      }
      mMutableList.setValue(mDataList);
      return mMutableList;
   }

   // Read cart product list
   private void readCartList() {
      assert UID != null;
      mFirebaseRef.child(Constants.NODE_CART).child(UID).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Item order = dataSnapshot.getValue(Item.class);
            assert order != null;
            order.setId(dataSnapshot.getKey());
            if (!mDataList.contains(order)) {
               mDataList.add(order);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Item order = dataSnapshot.getValue(Item.class);
            assert order != null;
            order.setId(dataSnapshot.getKey());
            if (mDataList.contains(order)) {
               mDataList.set(mDataList.indexOf(order), order);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            Item order = dataSnapshot.getValue(Item.class);
            assert order != null;
            order.setId(dataSnapshot.getKey());
            mDataList.remove(order);
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {}
      });
   }

   // Delete cart list item by position
   public void deleteCartItem(String position) {
      assert UID != null;
      mFirebaseRef.child(Constants.NODE_CART).child(UID)
          .child(position).removeValue();
   }
}
