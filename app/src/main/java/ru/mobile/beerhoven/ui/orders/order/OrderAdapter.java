package ru.mobile.beerhoven.ui.orders.order;

import static java.util.Objects.requireNonNull;
import static ru.mobile.beerhoven.ui.orders.order.OrderAdapter.*;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.repository.OrderRepository;
import ru.mobile.beerhoven.databinding.ItemOrderBinding;
import ru.mobile.beerhoven.domain.model.Product;

public class OrderAdapter extends Adapter<OrderViewHolder> {
   private final List<Product> mOrderList;

   public OrderAdapter(@NonNull List<Product> list) {
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
      OrderViewModel orderViewModel = new OrderViewModel(new OrderRepository());
      Product model = mOrderList.get(position);

      // Get phone number current user aka user id from database
      String UID = orderViewModel.getCurrentUserPhone();

      // Binding view fields
      holder.recyclerBinding.tvTimeOrder.setText(model.getTime());
      holder.recyclerBinding.tvDateOrder.setText(model.getDate());
      holder.recyclerBinding.tvNameOrder.setText(model.getName());
      holder.recyclerBinding.tvPhoneOrder.setText(model.getPhone());
      holder.recyclerBinding.tvAddressOrder.setText(model.getAddress());
      holder.recyclerBinding.tvCommonOrder.setText(model.getCommon() + " руб.");

      // Get order key from database
      String orderKey = orderViewModel.gePushId();

      holder.itemView.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         OrderFragmentDirections.ActionNavOrderToNavOrderDetails action = OrderFragmentDirections.actionNavOrderToNavOrderDetails()
             .setID(requireNonNull(UID))
             .setPushID(requireNonNull(orderKey));
         navController.navigate(action);
      });

      holder.recyclerBinding.tvDeleteOrder.setVisibility(View.INVISIBLE);

      // Remove order from on click delete button
      holder.recyclerBinding.tvDeleteOrder.setOnClickListener(v -> orderViewModel.getId(model.getId()));
   }

   @Override
   public int getItemCount() {
      return mOrderList.size();
   }


   public static class OrderViewHolder extends ViewHolder implements OnClickListener {
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
