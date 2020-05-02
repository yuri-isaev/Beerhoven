package ru.mobile.beerhoven.presentation.ui.user.orders;

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
   private OrderListViewModel mViewModel;
   private RecyclerView mRecyclerView;

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_order_list, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_order);
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mViewModel = new OrderListViewModel(new OrderRepository());
      mViewModel.initOrderList();
      mViewModel.getOrderList().observe(getViewLifecycleOwner(), (List<Order> list) -> {
         mOrderListAdapter.notifyDataSetChanged();
      });
      initRecyclerView();
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      List<Order> list = requireNonNull(mViewModel.getOrderList().getValue());
      mOrderListAdapter = new OrderListAdapter(requireNonNull(list));
      mRecyclerView.setAdapter(mOrderListAdapter);
      mOrderListAdapter.notifyDataSetChanged();
   }
}
