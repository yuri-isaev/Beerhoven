package ru.mobile.beerhoven.presentation.ui.user.orders.details;

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
import ru.mobile.beerhoven.data.remote.OrderDetailRepository;
import ru.mobile.beerhoven.domain.model.Product;

public class OrderDetailListFragment extends Fragment {
   private OrderDetailListAdapter mOrderDetailsAdapter;
   private OrderDetailListViewModel mViewModel;
   private RecyclerView mRecyclerView;
   private String mOrderKey;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getArguments() != null) {
         OrderDetailListFragmentArgs argsOrder = OrderDetailListFragmentArgs.fromBundle(getArguments());
         mOrderKey = argsOrder.getOrderKey();
      }
   }

   @SuppressLint("MissingInflatedId")
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_order_detail_list, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_products);
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mViewModel = new OrderDetailListViewModel(new OrderDetailRepository());
      mViewModel.initOrderDetailList(mOrderKey);
      mViewModel.getOrderDetailList().observe(getViewLifecycleOwner(), (List<Product> list) -> {
         mOrderDetailsAdapter.notifyDataSetChanged();
      });
      initRecyclerView();
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      List<Product> list = requireNonNull(mViewModel.getOrderDetailList().getValue());
      mOrderDetailsAdapter = new OrderDetailListAdapter(list);
      mRecyclerView.setAdapter(mOrderDetailsAdapter);
      mOrderDetailsAdapter.notifyDataSetChanged();
   }
}
