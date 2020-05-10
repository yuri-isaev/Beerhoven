package ru.mobile.beerhoven.presentation.ui.admin.notifications;

import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.NotificationRepository;
import ru.mobile.beerhoven.domain.model.Order;

public class NotificationListFragment extends Fragment {
   private NotificationListAdapter mAdapter;
   private NotificationListViewModel mViewModel;
   private RecyclerView mRecyclerView;

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_notification_list, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_notification);
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mViewModel = new NotificationListViewModel(new NotificationRepository());
      mViewModel.initNotificationList();
      mViewModel.getNotificationList().observe(getViewLifecycleOwner(), (List<Order> list) -> {
         mAdapter.notifyDataSetChanged();
      });
      initRecyclerView();
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      List<Order> list = requireNonNull(mViewModel.getNotificationList().getValue());
      mAdapter = new NotificationListAdapter(requireActivity(), list);
      mRecyclerView.setAdapter(mAdapter);
      mAdapter.notifyDataSetChanged();
   }
}
