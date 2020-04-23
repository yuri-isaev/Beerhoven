package ru.mobile.beerhoven.presentation.ui.user.cart;

import static java.util.Objects.requireNonNull;

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
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.CartRepository;
import soup.neumorphism.NeumorphButton;

public class CartListFragment extends Fragment implements CartListAdapter.Callback {
   private CartListAdapter mAdapter;
   private CartListViewModel mViewModel;
   private NeumorphButton mConfirmButton;
   private RecyclerView mRecyclerView;
   private String mTotal;
   private TextView mOrderTotal;

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mViewModel = new CartListViewModel(new CartRepository());
      View view = inflater.inflate(R.layout.fragment_cart_list, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_cart);
      mConfirmButton = view.findViewById(R.id.btn_confirm);
      mOrderTotal = view.findViewById(R.id.cart_total);

      // Custom layout params
      ViewGroup.LayoutParams layoutParams = mRecyclerView.getLayoutParams();
      layoutParams.height = (int) (container.getHeight() * 0.8);
      mRecyclerView.setLayoutParams(layoutParams);

      mViewModel.initCartList();
      mViewModel.getCartList().observe(getViewLifecycleOwner(), (list) -> mAdapter.notifyDataSetChanged());
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

      mAdapter = new CartListAdapter(requireNonNull(mViewModel.getCartList().getValue()), (String total) -> {
         mTotal = total;
         mOrderTotal.setText("Сумма корзины:  " + total + " руб.");
      }, getContext());

      if (mViewModel.getCartList().getValue().size() == 0) {
         mConfirmButton.setClickable(true);

         mConfirmButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            CartListFragmentDirections.ActionNavCartToNavOrderConfirm action = CartListFragmentDirections
                .actionNavCartToNavOrderConfirm()
                .setTotal(mTotal);
            
            NavOptions options = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .setPopExitAnim(R.anim.fade_out)
                .build();

            navController.navigate(action, options);
         });

      } else {
         mConfirmButton.setClickable(false);
      }

      mRecyclerView.setAdapter(mAdapter);
   }

   @Override
   public void onPassData(String data) {
      mTotal = data;
   }
}
