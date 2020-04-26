package ru.mobile.beerhoven.presentation.ui.user.cart;

import static android.widget.Toast.LENGTH_LONG;
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
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.dmoral.toasty.Toasty;
import lombok.var;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.CartRepository;

public class CartListFragment extends Fragment implements CartListAdapter.Callback {
   private CartListAdapter mAdapter;
   private CartListViewModel mViewModel;
   private Button mConfirmButton;
   private RecyclerView mRecyclerView;
   private String mTotal;
   private TextView mOrderTotal;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_cart_list, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_cart);
      mConfirmButton = view.findViewById(R.id.btn_confirm);
      mOrderTotal = view.findViewById(R.id.cart_total);

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
      onPassData(mTotal);
      mViewModel = new CartListViewModel(new CartRepository());
      mViewModel.initCartList();
      mViewModel.getCartList().observe(getViewLifecycleOwner(), (list) -> mAdapter.notifyDataSetChanged());
      initRecyclerView();
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
            try {
               NavController navController = Navigation.findNavController(v);
               var action = CartListFragmentDirections
                   .actionNavCartToNavOrderConfirm()
                   .setTotal(mTotal);

               NavOptions options = new NavOptions.Builder()
                   .setLaunchSingleTop(true)
                   .setEnterAnim(R.anim.fade_in)
                   .setExitAnim(R.anim.fade_out)
                   .setPopExitAnim(R.anim.fade_out)
                   .build();
               navController.navigate(action, options);
            } catch (Exception e) {
               Toasty.warning(requireActivity(), R.string.cart_empty, LENGTH_LONG, true).show();
            }
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
