package ru.mobile.beerhoven.data.remote;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.domain.repository.IOrderConfirmRepository;
import ru.mobile.beerhoven.utils.Constants;

public class OrderConfirmRepository implements IOrderConfirmRepository {
   private final DatabaseReference mFirebaseRef;
   private final String mUserId;

   public OrderConfirmRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mUserId = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   // Add order value to database
   public void onCreateConfirmOrder(Order order) {
      mFirebaseRef.child(Constants.NODE_CONFIRMS).child(mUserId).push().setValue(order);
      DatabaseReference databaseCartList = mFirebaseRef.child(Constants.NODE_CART).child(mUserId);
      DatabaseReference databaseOrderList = mFirebaseRef.child(Constants.NODE_ORDERS).child(mUserId).push();
      onTransferNodeDataBase(databaseCartList, databaseOrderList, order);
   }

   // Emptying the user's cart
   public void onRemoveConfirmOrder() {
      mFirebaseRef.child(Constants.NODE_CART).removeValue();
   }

   // Copying node from cart list to order list
   private void onTransferNodeDataBase(@NonNull DatabaseReference fromPath, final DatabaseReference toPath, Order order) {
      fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            toPath
                .setValue(dataSnapshot.getValue(Boolean.parseBoolean(order.getId())))
                .addOnCompleteListener(task -> {});
         }
         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {}
      });
   }
}
