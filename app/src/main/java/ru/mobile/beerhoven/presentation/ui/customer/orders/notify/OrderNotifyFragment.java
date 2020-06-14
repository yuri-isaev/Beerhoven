package ru.mobile.beerhoven.presentation.ui.customer.orders.notify;

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

public class OrderNotifyFragment extends Fragment {

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_order_notify, container, false);
   }

   @Override
   public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      new Handler().postDelayed(() -> {
         NavDirections action = OrderNotifyFragmentDirections.actionNavOrderNotifyToNavProductList();
         Navigation.findNavController(view).navigate(action);
      }, 5000);
   }
}
