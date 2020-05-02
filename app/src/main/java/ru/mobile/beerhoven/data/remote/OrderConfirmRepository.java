package ru.mobile.beerhoven.data.remote;

import static java.util.Objects.requireNonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

   public void onCreateOrderConfirm(Order order) {
      DatabaseReference confirm = mFirebaseRef.child(Constants.NODE_CONFIRMS).child(mUserId).push();
      confirm.setValue(order);
      String key = confirm.getKey();
      mFirebaseRef.child(Constants.NODE_ORDERS).child(mUserId).child(requireNonNull(key));
   }

   public void onDeleteOrderCart() {
      mFirebaseRef.child(Constants.NODE_CART).removeValue();
   }
}
