package ru.mobile.beerhoven.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

import es.dmoral.toasty.Toasty;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.configs.Utils;

public class RegistrationFragment extends Fragment {

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_registration, container, false);
      TextInputLayout regName = view.findViewById(R.id.reg_name);
      TextInputLayout regEmail = view.findViewById(R.id.req_email);
      TextInputLayout regPhoneNo = view.findViewById(R.id.reg_phone_number);
      Button btnRegister = view.findViewById(R.id.reg_btn);

      btnRegister.setOnClickListener(v -> {
         Utils validate = Utils.getInstance();
         if (!validate.isValidNameField(regName) | !validate.isValidateEmail(regEmail) | !validate.isValidPhoneNumber(regPhoneNo)) {
            Toasty.error(requireActivity(), "Заполните форму!", Toast.LENGTH_LONG, true).show();
            return;
         }
         String name = regName.getEditText().getText().toString();
         String email = regEmail.getEditText().getText().toString();
         String phoneNo = regPhoneNo.getEditText().getText().toString();

         NavDirections action = RegistrationFragmentDirections.actionNavRegToNavAuth()
             .setName(name).setPhone(phoneNo).setEmail(email);
         Navigation.findNavController(v).navigate(action);
      });

      return view;
   }
}
