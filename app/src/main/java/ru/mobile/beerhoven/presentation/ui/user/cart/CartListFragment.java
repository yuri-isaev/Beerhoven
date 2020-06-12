package ru.mobile.beerhoven.presentation.ui.user.cart;

import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.CartRepository;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.utils.Toasty;

public class CartListFragment extends Fragment implements CartListAdapter.Callback {
   private CartListAdapter mAdapter;
   private Button mConfirmButton;
   private RecyclerView mRecyclerView;
   private String mTotal = "0.0";
   private TextView mOrderTotal;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_cart_list, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_cart);
      mConfirmButton = view.findViewById(R.id.btnOrderConfirm);
      mOrderTotal = view.findViewById(R.id.tvCartTotal);

      // Custom layout params
      ViewGroup.LayoutParams layoutParams = mRecyclerView.getLayoutParams();
      layoutParams.height = (int) (container.getHeight() * 0.8);
      mRecyclerView.setLayoutParams(layoutParams);
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      CartListViewModel viewModel = new CartListViewModel(new CartRepository());
      viewModel.getCartListFromRepository().observe(getViewLifecycleOwner(), list ->
          mAdapter.notifyDataSetChanged());
      List<Product> list = viewModel.getCartListFromRepository().getValue();
      initRecyclerView(list);
   }

   @SuppressLint("SetTextI18n")
   private void initRecyclerView(List<Product> list) {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

      mAdapter = new CartListAdapter(requireNonNull(list), total -> {
         mTotal = total;
         mOrderTotal.setText("Сумма корзины:  " + total + " руб.");
      }, getContext());

      if (list.size() == 0) {
         mConfirmButton.setClickable(true);
         mConfirmButton.setOnClickListener(v -> {
            if (!(mTotal.equals("0.0"))) {
               NavDirections action = CartListFragmentDirections
                   .actionNavCartToNavOrderConfirm()
                   .setTotal(mTotal);
               Navigation.findNavController(v).navigate(action);
            } else {
               Toasty.error(requireActivity(), R.string.cart_empty);
            }
         });
      } else {
         mConfirmButton.setClickable(false);
      }
      mRecyclerView.setAdapter(mAdapter);
   }

   @Override
   public void onPassData(String total) {
      mTotal = total;
   }
}
