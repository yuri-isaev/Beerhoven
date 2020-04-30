package ru.mobile.beerhoven.presentation.ui.user.orders.notify;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.OrderRepository;
import ru.mobile.beerhoven.presentation.ui.orders.order.OrderViewModel;

public class OrderNotifyFragment extends Fragment {

   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_order_notify, container, false);
   }

   @SuppressLint("ShowToast")
   @Override
   public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      new Handler().postDelayed(() -> {
         OrderViewModel viewModel = new OrderViewModel(new OrderRepository());
         String currentUser = viewModel.getCurrentUserPhoneToRepository();

         assert currentUser != null;
         NavDirections action = OrderNotifyFragmentDirections.actionNavOrderNotifyToNavProductList();
         Navigation.findNavController(view).navigate(action);
      }, 5000);
   }
}
