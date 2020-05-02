package ru.mobile.beerhoven.presentation.ui.notifications.notification;

import static ru.mobile.beerhoven.presentation.ui.notifications.notification.NotificationListAdapter.*;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.List;

import ru.mobile.beerhoven.databinding.ItemNotificationBinding;
import ru.mobile.beerhoven.domain.model.Order;

public class NotificationListAdapter extends Adapter<NotificationViewHolder> {
   private final List<Order> mConfirmOrderList;

   public NotificationListAdapter(@NonNull List<Order> list) {
      this.mConfirmOrderList = list;
   }

   @NonNull
   @Override
   public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new NotificationViewHolder(binding);
   }

   @Override
   public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
      Order order = mConfirmOrderList.get(position);

      // Phone number that is registered in the system and from which the order form was confirmed
      String confirmID = order.getId();

      holder.binding.tvDateOrder.setText(order.getDate());
      holder.binding.tvPhoneOrder.setText(confirmID);
      holder.itemView.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         NavDirections action = NotificationFragmentDirections.actionNavNotificationsToNavOrders()
             .setId(confirmID);;
         navController.navigate(action);
      });
   }

   @Override
   public int getItemCount() {
      return mConfirmOrderList.size();
   }


   public static class NotificationViewHolder extends RecyclerView.ViewHolder {
      ItemNotificationBinding binding;

      public NotificationViewHolder(ItemNotificationBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }
   }
}