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
   private final DatabaseReference mFirebaseRef;
   private final String mUserPhoneId;

   public OrderConfirmRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mUserPhoneId = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   // Add order value to database
   public void onCreateConfirmOrder() {
      HashMap<String, String> catalog = MapStorage.productMap;
      Order confirm = new Order();
      confirm.setAddress(catalog.get("address"));
      confirm.setColor(Float.parseFloat(requireNonNull(catalog.get("color"))));
      confirm.setTotal(Double.parseDouble(requireNonNull(catalog.get("total"))));
      confirm.setDate(catalog.get("date"));
      confirm.setName(catalog.get("name"));
      confirm.setPhone(catalog.get("phone"));
      confirm.setTime(catalog.get("time"));

      mFirebaseRef.child(Constants.NODE_CONFIRMS).child(mUserPhoneId).push().setValue(confirm);

      DatabaseReference databaseCartList = mFirebaseRef.child(Constants.NODE_CART).child(mUserPhoneId);
      DatabaseReference databaseOrderList = mFirebaseRef.child(Constants.NODE_ORDERS).child(mUserPhoneId).push();
      onTransferNodeDataBase(databaseCartList, databaseOrderList, confirm);
   }

   // Emptying the user's cart
   public void onRemoveConfirmOrder() {
      mFirebaseRef.child(Constants.NODE_CART).removeValue();
   }

   // Copying node from cart list to order list
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
