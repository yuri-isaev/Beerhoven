package ru.mobile.beerhoven.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.mobile.beerhoven.models.Item;
import ru.mobile.beerhoven.utils.Constants;

public class OrderRepository {
   private static OrderRepository instance;
   private final List<Item> dataList = new ArrayList<>();
   private final MutableLiveData<List<Item>> mutableList = new MutableLiveData<>();

   protected String UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();

   public static OrderRepository getInstance() {
      if (instance == null) {
         instance = new OrderRepository();
      }
      return instance;
   }

   //To retrieve data from a web service or online source
   public MutableLiveData<List<Item>> getOrderList() {
      if (dataList.size() == 0) { //for reload
         loadOrderList();
      }
      mutableList.setValue(dataList);
      return mutableList;
   }

   private void loadOrderList() {
      FirebaseDatabase.getInstance().getReference().child(Constants.NODE_CONFIRMS).child(UID).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Item order = snapshot.getValue(Item.class);
            assert order != null;
            order.setId(snapshot.getKey());
            if (!dataList.contains(order)) {
               dataList.add(order);
            }
            mutableList.postValue(dataList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Item order = snapshot.getValue(Item.class);
            assert order != null;
            order.setId(snapshot.getKey());
            if (dataList.contains(order)) {
               dataList.set(dataList.indexOf(order), order);
            }
            mutableList.postValue(dataList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Item order = snapshot.getValue(Item.class);
            assert order != null;
            order.setId(snapshot.getKey());
            dataList.remove(order);
            mutableList.postValue(dataList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {
         }
      });
   }
}
