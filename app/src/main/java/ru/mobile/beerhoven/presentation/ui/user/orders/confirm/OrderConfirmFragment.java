package ru.mobile.beerhoven.presentation.ui.user.orders.confirm;

import static java.util.Objects.requireNonNull;
import static ru.mobile.beerhoven.utils.Validation.isValidAddressField;
import static ru.mobile.beerhoven.utils.Validation.isValidPhoneNumberField;
import static ru.mobile.beerhoven.utils.Validation.isValidTextField;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.network.PushMessagingService;
import ru.mobile.beerhoven.data.remote.OrderConfirmRepository;
import ru.mobile.beerhoven.databinding.FragmentOrderConfirmBinding;
import ru.mobile.beerhoven.domain.model.Order;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.utils.CurrentDateTime;
import ru.mobile.beerhoven.utils.Randomizer;
import ru.mobile.beerhoven.utils.Toasty;

public class OrderConfirmFragment extends Fragment {
   private Button mAddOrderButton;
   private OrderConfirmViewModel mViewModel;
   private String mTotal;
   private TextInputLayout mNameInput;
   private TextInputLayout mPhoneInput;
   private TextInputLayout mAddressInput;

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
      mAddOrderButton = binding.btnAddDatabase;
      mAddressInput = binding.ilConfirmAddress;
      mNameInput = binding.ilConfirmName;
      mPhoneInput = binding.ilConfirmNumber;
      return binding.getRoot();
   }

   @SuppressLint({"CommitPrefEdits", "NewApi"})
   @Override
   public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      mViewModel = new OrderConfirmViewModel(requireActivity().getApplicationContext(),
          new OrderConfirmRepository(), new PushMessagingService());

      mAddOrderButton.setOnClickListener(v -> {
         if (!isValidTextField(mNameInput) | !isValidAddressField(mAddressInput) | !isValidPhoneNumberField(mPhoneInput)) {
            Toasty.error(requireActivity(), R.string.invalid_form);
            return;
         }

         Order order = new Order();
         order.setAddress(requireNonNull(mAddressInput.getEditText()).getText().toString());
         order.setColor(String.valueOf(Randomizer.getRandomColorMarker()));
         order.setDate(CurrentDateTime.getCurrentDate());
         order.setContactName(String.valueOf(requireNonNull(mNameInput.getEditText()).getText()));
         order.setPhone(String.valueOf(requireNonNull(mPhoneInput.getEditText()).getText()));
         order.setTime(CurrentDateTime.getCurrentTime());
         order.setTotal(Double.parseDouble(String.valueOf(Double.parseDouble(mTotal))));

         onSendOrderConfirmList(order);
         onFragmentActivity(0);
         navigateFragment(view);
         sendPushNotification(requireActivity());
      });
   }

   @SuppressLint("CheckResult")
   public void onSendOrderConfirmList(Order order) {
         mViewModel.onCreateConfirmOrderToRepository(order);
         mViewModel.onDeleteOrderCartToRepository();
         mViewModel.onDeleteCartCounterToStorage();
         Toasty.success(requireActivity(), R.string.order_sent_success);
   }

   public void onFragmentActivity(int data) {
      Activity activity = getActivity();
      if (activity != null && !activity.isFinishing() && activity instanceof MainActivity) {
         ((MainActivity) activity).onUpdateActivityCounter(data);
      }
   }

   public void navigateFragment(View view) {
      NavDirections action = OrderConfirmFragmentDirections.actionNavOrderConfirmToNavOrderNotify();
      Navigation.findNavController(view).navigate(action);
   }

   public void sendPushNotification(FragmentActivity activity) {
      mViewModel.onSendPushNotificationToService(activity);
      ((MainActivity) activity).onIncreaseNotificationCounter();
   }
}
