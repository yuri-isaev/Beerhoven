package ru.mobile.beerhoven.ui.orders.details;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.utils.Constants;

public class OrderDetailsRepository {
   private static OrderDetailsRepository instance;
   private final List<Product> dataList = new ArrayList<>();
   private final MutableLiveData<List<Product>> mutableList = new MutableLiveData<>();
   private final String UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   public final HashMap<String, Object> stateMap = new HashMap<>();

   public static OrderDetailsRepository getInstance() {
      if (instance == null) {
         instance = new OrderDetailsRepository();
      }
      return instance;
   }

   public MutableLiveData<List<Product>> getOrderDetailsList() {
      if (dataList.size() == 0) {
         loadOrderList((String) stateMap.get("push_id"));
      }
      mutableList.setValue(dataList);
      return mutableList;
   }


   private void loadOrderList(String pushID) {
      FirebaseDatabase.getInstance().getReference().child(Constants.NODE_ORDERS)
          .child(UID).child(pushID).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Product order = snapshot.getValue(Product.class);
            assert order != null;
            order.setId(snapshot.getKey());
            if (!dataList.contains(order)) {
               dataList.add(order);
            }
            mutableList.postValue(dataList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Product order = snapshot.getValue(Product.class);
            assert order != null;
            order.setId(snapshot.getKey());
            if (dataList.contains(order)) {
               dataList.set(dataList.indexOf(order), order);
            }
            mutableList.postValue(dataList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Product order = snapshot.getValue(Product.class);
            assert order != null;
            order.setId(snapshot.getKey());
            dataList.remove(order);
            mutableList.postValue(dataList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

         @Override
         public void onCancelled(@NonNull DatabaseError error) {}
      });
   }
}
