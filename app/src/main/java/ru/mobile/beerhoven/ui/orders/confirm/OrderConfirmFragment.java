package ru.mobile.beerhoven.ui.orders.confirm;

import static java.util.Objects.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import info.hoang8f.widget.FButton;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.activity.MainActivity;
import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.data.remote.OrderConfirmRepository;
import ru.mobile.beerhoven.utils.CurrentDateTime;
import ru.mobile.beerhoven.utils.Randomizer;
import ru.mobile.beerhoven.utils.Validation;

public class OrderConfirmFragment extends Fragment {
   private FButton mAddOrderButton;
   private OrderConfirmViewModel mOrderConfirmViewModel;
   private String mData;
   private TextInputLayout mNameEditText;
   private TextInputLayout mPhoneEditText;
   private TextInputLayout mAddressEditText;

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      if (getActivity() != null) {
         assert getArguments() != null;
         OrderConfirmFragmentArgs args = OrderConfirmFragmentArgs.fromBundle(getArguments());
         mData = args.getCommon();
      }
   }

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mOrderConfirmViewModel = new OrderConfirmViewModel(new OrderConfirmRepository(),
          requireNonNull(getActivity()).getApplicationContext());

      View view = inflater.inflate(R.layout.fragment_order_confirm, container, false);
      mNameEditText = view.findViewById(R.id.confirm_name);
      mPhoneEditText = view.findViewById(R.id.confirm_number);
      mAddressEditText = view.findViewById(R.id.confirm_address);
      mAddOrderButton = view.findViewById(R.id.confirm_order);
      return view;
   }

   @SuppressLint({"CommitPrefEdits", "NewApi"})
   @Override
   public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      mAddOrderButton.setOnClickListener(v -> {
         if (!Validation.isValidNameField(mNameEditText) |
             !Validation.isValidPhoneNumber(mPhoneEditText) |
             !Validation.setValidateAddress(mAddressEditText)) {
            Toasty.error(requireActivity(), R.string.form_completed, Toast.LENGTH_LONG, true).show();
            return;
         }

         HashMap<String, String> map = MapStorage.productMap;
         map.put("address", requireNonNull(mAddressEditText.getEditText()).getText().toString());
         map.put("color", String.valueOf(Randomizer.getRandomColorMarker()));
         map.put("common", String.valueOf(Double.parseDouble(mData)));
         map.put("date", CurrentDateTime.getCurrentDate());
         map.put("name", requireNonNull((mNameEditText.getEditText()).getText()).toString());
         map.put("phone", String.valueOf(requireNonNull(mPhoneEditText.getEditText()).getText()));
         map.put("time", CurrentDateTime.getCurrentTime());

         mOrderConfirmViewModel.onCreateConfirmOrderOnDatabase();
         mOrderConfirmViewModel.onDeleteConfirmOrderOnDatabase();
         mOrderConfirmViewModel.onDeleteCartCounterFromStorage();
         Toasty.success(requireActivity(), "Ваш заказ успешно отправлен", Toast.LENGTH_LONG, true).show();

         toActivity(0);
      });
   }

   public void toActivity(int data) {
      Activity activity = getActivity();
      if (activity != null && !activity.isFinishing() && activity instanceof MainActivity) {
         ((MainActivity) activity).onUpdateCounterFromFragment(data);
      }
   }
}
