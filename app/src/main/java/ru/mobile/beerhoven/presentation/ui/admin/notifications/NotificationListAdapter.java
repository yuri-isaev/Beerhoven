package ru.mobile.beerhoven.presentation.ui.admin.notifications;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.List;

import ru.mobile.beerhoven.databinding.ItemNotificationBinding;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.presentation.activity.MainActivity;

public class NotificationListAdapter extends Adapter<NotificationListAdapter.NotificationViewHolder> {
   private final FragmentActivity mActivity;
   private final List<Order> mAdapterList;

   public NotificationListAdapter(FragmentActivity activity, @NonNull List<Order> list) {
      this.mActivity = activity;
      this.mAdapterList = list;
   }

   @NonNull
   @Override
   public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemNotificationBinding binding = ItemNotificationBinding
          .inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new NotificationViewHolder(binding);
   }

   @Override
   public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
      Order order = mAdapterList.get(position);
      String userPhoneNumber = order.getId();

      holder.binding.tvNotificationOrderPhone.setText(userPhoneNumber);
      holder.itemView.setOnClickListener(v -> {
         NavDirections action = NotificationListFragmentDirections.actionNavAdminNotificationsToNavAdminOrders()
             .setOrderId(userPhoneNumber);
         Navigation.findNavController(v).navigate(action);
         ((MainActivity) mActivity).onDecreaseNotificationCounter();
      });
   }

   @Override
   public int getItemCount() {
      return mAdapterList.size();
   }

   public static class NotificationViewHolder extends RecyclerView.ViewHolder {
      ItemNotificationBinding binding;

      public NotificationViewHolder(@NonNull ItemNotificationBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }
   }
}
