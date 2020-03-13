package ru.mobile.beerhoven.presentation.ui.orders.confirm;

import static java.util.Objects.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import info.hoang8f.widget.FButton;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.data.remote.OrderConfirmRepository;
import ru.mobile.beerhoven.databinding.FragmentAddProductBinding;
import ru.mobile.beerhoven.databinding.FragmentOrderConfirmBinding;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.CurrentDateTime;
import ru.mobile.beerhoven.utils.Randomizer;
import ru.mobile.beerhoven.utils.Validation;

public class OrderConfirmFragment extends Fragment {
   private FragmentOrderConfirmBinding mFragmentBind;
   private FButton mAddOrderButton;
   private OrderConfirmViewModel mOrderConfirmViewModel;
   private String mTotal;
   private TextInputLayout mNameEditText;
   private TextInputLayout mPhoneEditText;
   private TextInputLayout mAddressEditText;

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
      mOrderConfirmViewModel = new OrderConfirmViewModel(new OrderConfirmRepository(),
          requireNonNull(getActivity()).getApplicationContext());

      mFragmentBind = FragmentOrderConfirmBinding.inflate(inflater, container, false);
      mNameEditText = mFragmentBind.confirmName;
      mPhoneEditText = mFragmentBind.confirmNumber;
      mAddressEditText = mFragmentBind.confirmAddress;
      mAddOrderButton = mFragmentBind.btnConfirmOrder;

      return mFragmentBind.getRoot();
   }

   @SuppressLint({"CommitPrefEdits", "NewApi"})
   @Override
   public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      mAddOrderButton.setOnClickListener(v -> {
         if (!Validation.isValidNameField(mNameEditText) |
             !Validation.isValidPhoneNumber(mPhoneEditText) |
             !Validation.setValidateAddress(mAddressEditText)) {
            Toasty.error(requireActivity(), R.string.form_fill, Toast.LENGTH_LONG, true).show();
            return;
         }

         HashMap<String, String> map = MapStorage.productMap;
         map.put("address", requireNonNull(mAddressEditText.getEditText()).getText().toString());
         map.put("color", String.valueOf(Randomizer.getRandomColorMarker()));
         map.put("date", CurrentDateTime.getCurrentDate());
         map.put("name", requireNonNull((mNameEditText.getEditText()).getText()).toString());
         map.put("phone", String.valueOf(requireNonNull(mPhoneEditText.getEditText()).getText()));
         map.put("time", CurrentDateTime.getCurrentTime());
         map.put("total", String.valueOf(Double.parseDouble(mTotal)));

         sendConfirmOnOrderList();
         toActivity(0);
         navigateFragment(view);
         sendPushNotification();
      });
   }

   public void sendConfirmOnOrderList() {
      mOrderConfirmViewModel.onCreateConfirmOrderToRepository();
      mOrderConfirmViewModel.onDeleteConfirmOrderToRepository();
      mOrderConfirmViewModel.onDeleteCartCounterToStorage();
      Toasty.success(requireActivity(), R.string.order_sent_success, Toast.LENGTH_LONG, true).show();
   }

   public void toActivity(int data) {
      Activity activity = getActivity();
      if (activity != null && !activity.isFinishing() && activity instanceof MainActivity) {
         ((MainActivity) activity).onUpdateCounterFromFragment(data);
      }
   }

   public void navigateFragment(View view) {
      NavDirections action = OrderConfirmFragmentDirections.actionNavOrderConfirmToNavOrderNotify();
      Navigation.findNavController(view).navigate(action);
   }

   public void sendPushNotification() {
      RequestQueue requestQue = Volley.newRequestQueue(requireNonNull(getActivity()));
      FirebaseMessaging.getInstance().subscribeToTopic("news");
      JSONObject json = new JSONObject();

      try {
         json.put("to", "/topics/" + "news");

         JSONObject notificationObj = new JSONObject();
         notificationObj.put("title", "Получен новый заказ");
         notificationObj.put("body", "от Имени");

         JSONObject extraData = new JSONObject();
         extraData.put("brandId", "brand");
         extraData.put("category", "logo");
         json.put("notification", notificationObj);
         json.put("data", extraData);

         JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.GOOGLE_API, json,
             response -> Log.d("MUR", "onResponse: "),
             error -> Log.d("MUR", "onError: " + error.networkResponse)) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
               Map<String, String> header = new HashMap<>();
               header.put("content-type", "application/json");
               header.put("authorization", getString(R.string.server_key));
               return header;
            }
         };

         requestQue.add(request);

      } catch (JSONException e) {
         e.printStackTrace();
      }
   }
}
