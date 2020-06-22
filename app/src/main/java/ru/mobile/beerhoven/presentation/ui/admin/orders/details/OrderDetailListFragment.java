package ru.mobile.beerhoven.presentation.ui.admin.orders.details;

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
import ru.mobile.beerhoven.presentation.ui.customer.orders.details.OrderDetailListFragmentArgs;

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
      View view = inflater.inflate(R.layout.fragment_admin_order_detail_list, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_products_list);
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mViewModel = new OrderDetailListViewModel(new OrderDetailRepository());
      mViewModel.getOrderDetailListFromRepository(mOrderKey).observe(getViewLifecycleOwner(), list ->
         mOrderDetailsAdapter.notifyDataSetChanged());
      List<Product> list = requireNonNull(mViewModel.getOrderDetailListFromRepository(mOrderKey).getValue());
      initRecyclerView(list);
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView(List<Product> list) {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      mOrderDetailsAdapter = new OrderDetailListAdapter(list);
      mRecyclerView.setAdapter(mOrderDetailsAdapter);
      mOrderDetailsAdapter.notifyDataSetChanged();
   }
}
