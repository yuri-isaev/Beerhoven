package ru.mobile.beerhoven.presentation.ui.admin.orders;

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
import ru.mobile.beerhoven.data.remote.OrderRepository;
import ru.mobile.beerhoven.domain.model.Order;

public class OrderListFragment extends Fragment {
   private OrderListAdapter mOrderListAdapter;
   private RecyclerView mRecyclerView;

   @SuppressLint({"NotifyDataSetChanged", "MissingInflatedId"})
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_admin_order_list, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_order_list);
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      OrderListViewModel viewModel = new OrderListViewModel(new OrderRepository());
      viewModel.getOrderListFromRepository().observe(getViewLifecycleOwner(), list ->
          mOrderListAdapter.notifyDataSetChanged());
      List<Order> list = requireNonNull(viewModel.getOrderListFromRepository().getValue());
      onInitRecyclerView(list);
   }

   @SuppressLint("NotifyDataSetChanged")
   private void onInitRecyclerView(List<Order> list) {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      mOrderListAdapter = new OrderListAdapter(requireNonNull(list));
      mRecyclerView.setAdapter(mOrderListAdapter);
      mOrderListAdapter.notifyDataSetChanged();
   }
}
