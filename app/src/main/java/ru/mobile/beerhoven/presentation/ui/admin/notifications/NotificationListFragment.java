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
   private RecyclerView mRecyclerView;

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_admin_notification_list, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_notification);
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      NotificationListViewModel viewModel = new NotificationListViewModel(new NotificationRepository());
      viewModel.getNotificationListFromRepository().observe(getViewLifecycleOwner(), list ->
          mAdapter.notifyDataSetChanged());
      List<Order> list = requireNonNull(viewModel.getNotificationListFromRepository().getValue());
      initRecyclerView(list);
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView(List<Order> list) {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      mAdapter = new NotificationListAdapter(requireActivity(), list);
      mRecyclerView.setAdapter(mAdapter);
      mAdapter.notifyDataSetChanged();
   }
}
