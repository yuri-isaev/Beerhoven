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
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IOrderConfirmRepository;
import ru.mobile.beerhoven.utils.Constants;

public class OrderConfirmRepository implements IOrderConfirmRepository {
   private final DatabaseReference mFirebaseRef;
   private final String mUserPhoneNumber;

   public OrderConfirmRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mUserPhoneNumber = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   public void onCreateOrderConfirm(@NonNull Order order) {
      DatabaseReference confirmRef = mFirebaseRef
          .child(Constants.NODE_CONFIRMS)
          .child(mUserPhoneNumber)
          .push();
      String confirmKey = confirmRef.getKey();
      order.setId(confirmKey);
      confirmRef.setValue(order);

      DatabaseReference cartList = mFirebaseRef
          .child(Constants.NODE_CART)
          .child(mUserPhoneNumber);

      DatabaseReference orderList = mFirebaseRef
          .child(Constants.NODE_ORDERS)
          .child(mUserPhoneNumber)
          .child(requireNonNull(confirmKey));

      onTransferNodeDatabase(cartList, orderList, new Product());
   }

   private void onTransferNodeDatabase(@NonNull DatabaseReference fromPath, final DatabaseReference toPath, Product product) {
      fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            toPath.setValue(dataSnapshot.getValue(Boolean.parseBoolean(product.getId())));
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {}
      });
   }

   public void onDeleteOrderCart() {
      mFirebaseRef.child(Constants.NODE_CART).removeValue();
   }
}
