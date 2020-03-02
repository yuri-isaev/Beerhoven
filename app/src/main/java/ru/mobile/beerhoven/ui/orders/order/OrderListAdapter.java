package ru.mobile.beerhoven.ui.orders.order;

import static java.util.Objects.requireNonNull;
import static ru.mobile.beerhoven.ui.orders.order.OrderListAdapter.*;

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

import ru.mobile.beerhoven.data.remote.OrderRepository;
import ru.mobile.beerhoven.databinding.ItemOrderBinding;
import ru.mobile.beerhoven.domain.model.Order;

public class OrderListAdapter extends Adapter<OrderViewHolder> {
   private final List<Order> mOrderList;

   public OrderListAdapter(@NonNull List<Order> list) {
      this.mOrderList = list;
   }

   @NonNull
   @Override
   public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new OrderViewHolder(binding);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
      OrderViewModel orderViewModel = new OrderViewModel(new OrderRepository());
      Order order = mOrderList.get(position);

      // Get phone number current user aka user id from database
      String UID = orderViewModel.getCurrentUserPhone();

      // Binding view fields
      holder.binding.tvAddressOrder.setText(order.getAddress());
      holder.binding.tvDateOrder.setText(order.getDate());
      holder.binding.tvNameOrder.setText(order.getName());
      holder.binding.tvPhoneOrder.setText(order.getPhone());
      holder.binding.tvTimeOrder.setText(order.getTime());
      holder.binding.tvCommonOrder.setText(order.getTotal() + " руб.");

      // Get order key from database
      String orderKey = orderViewModel.gePushId();

      holder.itemView.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         OrderFragmentDirections.ActionNavOrderToNavOrderDetails action = OrderFragmentDirections.actionNavOrderToNavOrderDetails()
             .setID(requireNonNull(UID))
             .setPushID(requireNonNull(orderKey));
         navController.navigate(action);
      });

      holder.binding.tvDeleteOrder.setVisibility(View.INVISIBLE);

      // Remove order from on click delete button
      holder.binding.tvDeleteOrder.setOnClickListener(v -> orderViewModel.getId(order.getId()));
   }

   @Override
   public int getItemCount() {
      return mOrderList.size();
   }


   public static class OrderViewHolder extends ViewHolder implements OnClickListener {
      ItemOrderBinding binding;

      public OrderViewHolder(ItemOrderBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }

      @Override
      public void onClick(View v) {
         binding.tvDeleteOrder.setOnClickListener(this);
         itemView.setOnClickListener(this);
      }
   }
}
