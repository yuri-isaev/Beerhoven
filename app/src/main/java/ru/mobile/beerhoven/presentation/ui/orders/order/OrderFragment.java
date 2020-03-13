package ru.mobile.beerhoven.presentation.ui.orders.order;

import static java.util.Objects.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.OrderRepository;
import ru.mobile.beerhoven.domain.model.Order;

public class OrderFragment extends Fragment {
   private OrderListAdapter mOrderListAdapter;
   private OrderViewModel mOrderViewModel;
   private RecyclerView mRecyclerView;

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mOrderViewModel = new OrderViewModel(new OrderRepository());

      View view = inflater.inflate(R.layout.fragment_order, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_order);

      // Initialize order list without return value
      mOrderViewModel.initOrderList();

      // Get order list and order adapter aka observe
      mOrderViewModel.getOrderList().observe(getViewLifecycleOwner(),
          (Observer<List<Order>>) list -> mOrderListAdapter.notifyDataSetChanged());

      initRecyclerView();

      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      mOrderListAdapter = new OrderListAdapter(requireNonNull(mOrderViewModel.getOrderList().getValue()));
      mRecyclerView.setAdapter(mOrderListAdapter);
      mOrderListAdapter.notifyDataSetChanged();
   }
}
