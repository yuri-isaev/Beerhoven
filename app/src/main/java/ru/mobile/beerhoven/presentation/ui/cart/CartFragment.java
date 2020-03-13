package ru.mobile.beerhoven.presentation.ui.cart;

import static java.util.Objects.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import info.hoang8f.widget.FButton;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.CartRepository;
import ru.mobile.beerhoven.domain.model.Product;

public class CartFragment extends Fragment implements CartListAdapter.Callback {
   private RecyclerView mRecyclerView;
   private CartListAdapter mCartListAdapter;
   private CartViewModel mCartViewModel;
   private TextView mOrderTotal;
   private FButton mCartAddConfirmButton;
   private String mTotal;

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mCartViewModel = new CartViewModel(new CartRepository());
      View view = inflater.inflate(R.layout.fragment_cart, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_cart);

      // Custom layout params
      ViewGroup.LayoutParams layoutParams = mRecyclerView.getLayoutParams();
      layoutParams.height = (int) (container.getHeight() * 0.8);
      mRecyclerView.setLayoutParams(layoutParams);

      mCartAddConfirmButton = view.findViewById(R.id.placeConfirmButton);
      mOrderTotal = view.findViewById(R.id.tvTotalCart);

      // Default cart value
      mOrderTotal.setText("Сумма корзины: 0.0");

      // Initialize cart list from database
      mCartViewModel.initCartList();

      // Cart list adapter observer
      mCartViewModel.getCartList().observe(getViewLifecycleOwner(),
          (List<Product> list) -> mCartListAdapter.notifyDataSetChanged());

      // Initialize cart list adapter
      initRecyclerView();

      return view;
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      onPassData(mTotal);
   }

   @SuppressLint("SetTextI18n")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

      mCartListAdapter = new CartListAdapter(requireNonNull(mCartViewModel.getCartList().getValue()), (String total) -> {
         mTotal = total;
         mOrderTotal.setText("Сумма корзины:  " + total + " руб.");
      },
          getContext());

      if (mCartViewModel.getCartList().getValue().size() == 0) {
         mCartAddConfirmButton.setClickable(true);

         mCartAddConfirmButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            CartFragmentDirections.ActionNavCartToNavOrderConfirm action = CartFragmentDirections
                .actionNavCartToNavOrderConfirm()
                .setTotal(mTotal);
            navController.navigate(action);
         });

      } else {
         mCartAddConfirmButton.setClickable(false);
      }

      mRecyclerView.setAdapter(mCartListAdapter);
   }

   @Override
   public void onPassData(String data) {
      mTotal = data;
   }
}
