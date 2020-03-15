package ru.mobile.beerhoven.presentation.ui.orders.details;

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
import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.data.remote.OrderDetailRepository;
import ru.mobile.beerhoven.domain.model.Product;

public class OrderDetailFragment extends Fragment {
   private OrderDetailAdapter mOrderDetailsAdapter;
   private OrderDetailViewModel mViewModel;
   private RecyclerView mRecyclerView;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      if (getArguments() != null) {
         OrderDetailFragmentArgs argsOrder = OrderDetailFragmentArgs.fromBundle(getArguments());
         String pushId = argsOrder.getOrderKey();
         MapStorage.productMap.put("push_id", pushId);
      }
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mViewModel = new OrderDetailViewModel(new OrderDetailRepository());

      View view = inflater.inflate(R.layout.fragment_order_details, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_cart);

      mViewModel.initOrderDetailsList();

      // Get order details list and order adapter aka observe
      mViewModel.getOrderDetailsList().observe(getViewLifecycleOwner(),
          (List<Product> list) -> mOrderDetailsAdapter.notifyDataSetChanged());

      initRecyclerView();

      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      mOrderDetailsAdapter = new OrderDetailAdapter(requireNonNull(mViewModel.getOrderDetailsList().getValue()));
      mRecyclerView.setAdapter(mOrderDetailsAdapter);
      mOrderDetailsAdapter.notifyDataSetChanged();
   }
}
