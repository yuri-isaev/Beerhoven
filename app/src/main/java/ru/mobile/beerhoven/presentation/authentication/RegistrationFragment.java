package ru.mobile.beerhoven.presentation.authentication;

import static java.util.Objects.requireNonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.databinding.FragmentRegistrationBinding;
import ru.mobile.beerhoven.utils.Toasty;
import ru.mobile.beerhoven.utils.Validation;

public class RegistrationFragment extends Fragment {

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      FragmentRegistrationBinding binding = FragmentRegistrationBinding.inflate(inflater, container, false);
      Button btnRegister = binding.regBtn;
      TextInputLayout inputName = binding.regName;
      TextInputLayout inputEmail = binding.reqEmail;
      TextInputLayout inputPhoneNumber = binding.regPhoneNumber;

      btnRegister.setOnClickListener(v -> {
         if (!Validation.isValidName(inputName) |
             !Validation.isValidEmail(inputEmail) |
             !Validation.isValidPhoneNumber(inputPhoneNumber)) {
            Toasty.error(requireActivity(), R.string.valid_field);
            return;
         }

         String name = requireNonNull(inputName.getEditText()).getText().toString();
         String email = requireNonNull(inputEmail.getEditText()).getText().toString();
         String phoneNumber = requireNonNull(inputPhoneNumber.getEditText()).getText().toString();

         NavDirections action = RegistrationFragmentDirections.actionNavRegToNavAuth()
             .setName(name)
             .setEmail(email)
             .setPhone(phoneNumber);
         Navigation.findNavController(v).navigate(action);
      });

      return binding.getRoot();
   }
}
