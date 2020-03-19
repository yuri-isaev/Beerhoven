package ru.mobile.beerhoven.presentation.authentication;

import static java.util.Objects.requireNonNull;

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
import ru.mobile.beerhoven.databinding.FragmentRegistrationBinding;
import ru.mobile.beerhoven.utils.Validation;

public class RegistrationFragment extends Fragment {
   private FragmentRegistrationBinding mFragmentBind;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      mFragmentBind = FragmentRegistrationBinding.inflate(inflater, container, false);

      TextInputLayout regName = mFragmentBind.regName;
      TextInputLayout regEmail = mFragmentBind.reqEmail;
      TextInputLayout regPhoneNumber = mFragmentBind.regPhoneNumber;
      Button btnRegister = mFragmentBind.regBtn;

      btnRegister.setOnClickListener(v -> {
         if (!Validation.isValidNameField(regName) | !Validation.isValidateEmail(regEmail) | !Validation.isValidPhoneNumber(regPhoneNumber)) {
            Toasty.error(requireActivity(), R.string.form_fill, Toast.LENGTH_LONG, true).show();
            return;
         }
         String name = requireNonNull(regName.getEditText()).getText().toString();
         String email = requireNonNull(regEmail.getEditText()).getText().toString();
         String phoneNumber = requireNonNull(regPhoneNumber.getEditText()).getText().toString();

         NavDirections action = RegistrationFragmentDirections.actionNavRegToNavAuth()
             .setName(name).setEmail(email).setPhone(phoneNumber);
         Navigation.findNavController(v).navigate(action);
      });

      return mFragmentBind.getRoot();
   }
}
