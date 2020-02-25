package ru.mobile.beerhoven.ui.orders.notify;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.utils.Constants;

public class OrderNotifyFragment extends Fragment {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_order_notify, container, false);

      new Handler().postDelayed(() -> {

         FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

         if (currentUser != null) {
            Toast.makeText(getActivity(), "Заказ получен", Toast.LENGTH_LONG).show();

            NavDirections action = OrderNotifyFragmentDirections.actionNavOrderNotifyToNavStore();
            Navigation.findNavController(view).navigate(action);
         } else {
            Toast.makeText(getActivity(), "Заказ уже получен", Toast.LENGTH_LONG).show();
         }
      },
          Constants.SPLASH_DISPLAY_LENGTH);

      return view;
   }
}
