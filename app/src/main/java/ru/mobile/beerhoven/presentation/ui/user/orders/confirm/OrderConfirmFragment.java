package ru.mobile.beerhoven.presentation.ui.user.orders.confirm;

import static android.widget.Toast.LENGTH_LONG;
import static java.util.Objects.requireNonNull;
import static ru.mobile.beerhoven.utils.Validation.isValidAddress;
import static ru.mobile.beerhoven.utils.Validation.isValidName;
import static ru.mobile.beerhoven.utils.Validation.isValidPhoneNumber;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

import es.dmoral.toasty.Toasty;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.local.PreferencesStorage;
import ru.mobile.beerhoven.data.network.PushMessagingService;
import ru.mobile.beerhoven.data.remote.OrderConfirmRepository;
import ru.mobile.beerhoven.databinding.FragmentOrderConfirmBinding;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.utils.CurrentDateTime;
import ru.mobile.beerhoven.utils.Randomizer;

public class OrderConfirmFragment extends Fragment {
   private Button mAddOrderButton;
   private OrderConfirmViewModel mViewModel;
   private String mTotal;
   private TextInputLayout mNameText;
   private TextInputLayout mPhoneText;
   private TextInputLayout mAddressText;

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getActivity() != null) {
         assert getArguments() != null;
         OrderConfirmFragmentArgs args = OrderConfirmFragmentArgs.fromBundle(getArguments());
         mTotal = args.getTotal();
      }
   }

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      FragmentOrderConfirmBinding binding = FragmentOrderConfirmBinding.inflate(inflater, container, false);
      mAddressText = binding.confirmAddress;
      mNameText = binding.confirmName;
      mPhoneText = binding.confirmNumber;
      mAddOrderButton = binding.btnConfirmOrder;
      return binding.getRoot();
   }

   @SuppressLint({"CommitPrefEdits", "NewApi"})
   @Override
   public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      mViewModel = new OrderConfirmViewModel(new OrderConfirmRepository(), new PushMessagingService(), new PreferencesStorage((Application) requireActivity().getApplicationContext()));

      mAddOrderButton.setOnClickListener(v -> {
         if (!isValidName(mNameText) | !isValidAddress(mAddressText) | !isValidPhoneNumber(mPhoneText)) {
            Toasty.error(requireActivity(), R.string.valid_form, LENGTH_LONG, true).show();
            return;
         }

         Order order = new Order();
         order.setAddress(requireNonNull(mAddressText.getEditText()).getText().toString());
         order.setColor(String.valueOf(Randomizer.getRandomColorMarker()));
         order.setDate(CurrentDateTime.getCurrentDate());
         order.setName(String.valueOf(requireNonNull(mNameText.getEditText()).getText()));
         order.setPhone(String.valueOf(requireNonNull(mPhoneText.getEditText()).getText()));
         order.setTime(CurrentDateTime.getCurrentTime());
         order.setTotal(Double.parseDouble(String.valueOf(Double.parseDouble(mTotal))));

         sendConfirmOnOrderList(order);
         toActivity(0);
         navigateFragment(view);
         sendPushNotification(requireActivity());
      });
   }

   public void sendConfirmOnOrderList(Order order) {
      try {
         mViewModel.onCreateConfirmOrderToRepository(order);
         mViewModel.onDeleteConfirmOrderToRepository();
         mViewModel.onDeleteCartCounterToStorage();
         Toasty.success(requireActivity(), R.string.order_sent_success, LENGTH_LONG, true).show();
      } catch (Exception e) {
         e.printStackTrace();
         Toasty.error(requireActivity(), R.string.order_sent_failed, LENGTH_LONG, true).show();
      }
   }

   public void toActivity(int data) {
      Activity activity = getActivity();
      if (activity != null && !activity.isFinishing() && activity instanceof MainActivity) {
         ((MainActivity) activity).onUpdateCounterFromFragment(data);
      }
   }

   public void navigateFragment(View view) {
      NavDirections action = OrderConfirmFragmentDirections.actionNavOrderConfirmToNavOrderNotify();
      NavOptions options = new NavOptions.Builder()
          .setLaunchSingleTop(true)
          .setEnterAnim(R.anim.fade_in)
          .setExitAnim(R.anim.fade_out)
          .setPopExitAnim(R.anim.fade_out)
          .build();
      Navigation.findNavController(view).navigate(action, options);
   }

   public void sendPushNotification(FragmentActivity activity) {
      mViewModel.onSendPushNotificationToService(activity);
   }
}
