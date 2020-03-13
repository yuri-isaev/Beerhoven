package ru.mobile.beerhoven.data.remote;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IOrderConfirmRepository;
import ru.mobile.beerhoven.utils.Constants;

public class OrderConfirmRepository implements IOrderConfirmRepository {
   private final String UID;
   private final DatabaseReference mFirebaseRef;

   public OrderConfirmRepository() {
      this.UID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
   }

   // Add order value to database
   public void createConfirmOrder() {
      HashMap<String, String> catalog = MapStorage.productMap;
      Order confirm = new Order();
      confirm.setAddress(catalog.get("address"));
      confirm.setColor(Float.parseFloat(requireNonNull(catalog.get("color"))));
      confirm.setTotal(Double.parseDouble(requireNonNull(catalog.get("total"))));
      confirm.setDate(catalog.get("date"));
      confirm.setName(catalog.get("name"));
      confirm.setPhone(catalog.get("phone"));
      confirm.setTime(catalog.get("time"));

      mFirebaseRef.child(Constants.NODE_CONFIRMS).child(UID).push().setValue(confirm);

      DatabaseReference databaseCartList = mFirebaseRef.child(Constants.NODE_CART).child(UID);
      DatabaseReference databaseOrderList = mFirebaseRef.child(Constants.NODE_ORDERS).child(UID).push();
      onTransferNodeDataBase(databaseCartList, databaseOrderList, confirm);
   }

   // Remove cart data
   public void removeConfirmOrder() {
      // Emptying the user's cart
      mFirebaseRef.child(Constants.NODE_CART).removeValue();
   }

   // Copying node from Cart List to Order List
   private void onTransferNodeDataBase(DatabaseReference fromPath, final DatabaseReference toPath, Order model) {
      fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
            toPath.setValue(dataSnapshot.getValue(Boolean.parseBoolean(model.getId())))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                 // Toasty.success(new MainActivity(), "Cкопирован успешно", Toast.LENGTH_LONG, true).show();
               }
            });
         }
         @Override
         public void onCancelled(DatabaseError databaseError) {}
      });
   }
}
