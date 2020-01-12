package ru.mobile.beerhoven.ui.cart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import info.hoang8f.widget.FButton;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.ui.store.details.DetailsViewModel;

public class CartFragment extends Fragment implements CartListAdapter.Callback {
   private RecyclerView mRecyclerView;
   private CartListAdapter mCartListAdapter;
   private CartViewModel mCartViewModel;
   private TextView mOrderTotal;
   private FButton mCartAddConfirmButton;
   private String common;

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mCartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
      View view = inflater.inflate(R.layout.fragment_cart, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_cart);

      // Custom Layout Params
      ViewGroup.LayoutParams layoutParams = mRecyclerView.getLayoutParams();
      layoutParams.height = (int) (container.getHeight() * 0.8);
      mRecyclerView.setLayoutParams(layoutParams);

      mCartAddConfirmButton = view.findViewById(R.id.placeConfirmButton);
      mOrderTotal = view.findViewById(R.id.tvTotalCart);

      // Default cart value
      mOrderTotal.setText("Сумма корзины: 0.0");

      // Initialize cart list from database
      mCartViewModel.initList();

      // Cart list adapter observer
      mCartViewModel.getCartList().observe(getViewLifecycleOwner(),
          list -> mCartListAdapter.notifyDataSetChanged());

      // Initialize cart list adapter
      initRecyclerView();

      return view;
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      onPassData(common);
   }

   @SuppressLint("SetTextI18n")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      mRecyclerView.setAdapter(mCartListAdapter);
   }

   @Override
   public void onPassData(String data) {
      common = data;
   }
}
