package ru.mobile.beerhoven.ui.orders.order;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.databinding.ItemOrderBinding;
import ru.mobile.beerhoven.models.Item;
import ru.mobile.beerhoven.utils.Constants;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

   private String PID;
   private final List<Item> mOrderList;

   public OrderAdapter(@NonNull List<Item> list) {
      this.mOrderList = list;
   }

   @NonNull
   @Override
   public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
      return new OrderViewHolder(view);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
      Item model = mOrderList.get(position);
      String keyID = model.getId();
      String UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();

      holder.recyclerBinding.tvTimeOrder.setText(model.getTime());
      holder.recyclerBinding.tvDateOrder.setText(model.getDate());
      holder.recyclerBinding.tvNameOrder.setText(model.getName());
      holder.recyclerBinding.tvPhoneOrder.setText(model.getPhone());
      holder.recyclerBinding.tvAddressOrder.setText(model.getAddress());
      holder.recyclerBinding.tvCommonOrder.setText(model.getCommon() + " руб.");

      DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
      Query query = rootRef.child(Constants.NODE_ORDERS).child(UID);
      ValueEventListener valueEventListener = new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot push : dataSnapshot.getChildren()) {
               PID = push.getKey();
            }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {
         }
      };

      query.addListenerForSingleValueEvent(valueEventListener);

      holder.recyclerBinding.tvDeleteOrder.setVisibility(View.INVISIBLE);

      holder.recyclerBinding.tvDeleteOrder.setOnClickListener(v -> FirebaseDatabase.getInstance()
          .getReference()
          .child(Constants.NODE_CONFIRMS)
          .child(UID)
          .child(keyID)
          .removeValue());
   }

   @Override
   public int getItemCount() {
      return mOrderList.size();
   }


   public static class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
      ItemOrderBinding recyclerBinding;

      public OrderViewHolder(@NonNull View itemView) {
         super(itemView);
      }

      @Override
      public void onClick(View v) {
         recyclerBinding.tvDeleteOrder.setOnClickListener(this);
         itemView.setOnClickListener(this);
      }
   }
}
