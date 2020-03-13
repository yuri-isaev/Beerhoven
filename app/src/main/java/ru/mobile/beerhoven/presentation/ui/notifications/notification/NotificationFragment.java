package ru.mobile.beerhoven.presentation.ui.notifications.notification;

import static java.util.Objects.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.NotificationRepository;
import ru.mobile.beerhoven.domain.model.Order;

public class NotificationFragment extends Fragment {
   private NotificationListAdapter mNotificationAdapter;
   private NotificationViewModel mNotificationViewModel;
   private RecyclerView mRecyclerView;

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mNotificationViewModel = new NotificationViewModel(new NotificationRepository());

      View view = inflater.inflate(R.layout.fragment_notification, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_notification);

      mNotificationViewModel.initNotificationList();

      mNotificationViewModel.getNotificationList().observe(getViewLifecycleOwner(),
          (List<Order> list) -> mNotificationAdapter.notifyDataSetChanged());

      initRecyclerView();

      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      mNotificationAdapter = new NotificationListAdapter(requireNonNull(mNotificationViewModel.getNotificationList().getValue()));
      mRecyclerView.setAdapter(mNotificationAdapter);
      mNotificationAdapter.notifyDataSetChanged();
   }
}