package ru.mobile.beerhoven.ui.orders.order;

import static java.util.Objects.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.domain.model.Product;

public class OrderFragment extends Fragment {
   int count = 0;
   private RecyclerView mRecyclerView;
   private OrderAdapter mOrderAdapter;
   private OrderViewModel mOrderViewModel;

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      toastThis((++count) + " CATALOG Created");
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     mOrderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
      //mOrderViewModel = new OrderViewModel(new OrderRepository());

      View view = inflater.inflate(R.layout.fragment_order, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_order);

      mOrderViewModel.initOrderList();

      mOrderViewModel.getOrderList().observe(getViewLifecycleOwner(), (Observer<List<Product>>)
          list -> mOrderAdapter.notifyDataSetChanged());
      initRecyclerView();
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      mOrderAdapter = new OrderAdapter(requireNonNull(mOrderViewModel.getOrderList().getValue()));
      mRecyclerView.setAdapter(mOrderAdapter);
      mOrderAdapter.notifyDataSetChanged();
   }

   protected void toastThis(String message) {
      Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
   }
}