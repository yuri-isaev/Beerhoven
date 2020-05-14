package ru.mobile.beerhoven.presentation.authentication;

import static java.util.Objects.requireNonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.databinding.FragmentRegistrationBinding;
import ru.mobile.beerhoven.utils.Toasty;
import ru.mobile.beerhoven.utils.Validation;

public class RegistrationFragment extends Fragment {
   Button btnRegistration;
   TextInputLayout mInputEmail;
   TextInputLayout mInputName;
   TextInputLayout mInputPhoneNumber;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      FragmentRegistrationBinding binding = FragmentRegistrationBinding.inflate(inflater, container, false);
      btnRegistration = binding.btnRegistration;
      mInputEmail = binding.inputEmail;
      mInputName = binding.inputName;
      mInputPhoneNumber = binding.inputPhoneNumber;

      // Set button alpha to zero.
      btnRegistration.setAlpha(0f);

      // Animate the alpha value to 1f and set duration as 800 ms.
      btnRegistration.animate().alpha(1f).setDuration(800);

      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      btnRegistration.setOnClickListener(v -> {
         if (!Validation.isValidEmail(mInputEmail) | !Validation.isValidName(mInputName) |
             !Validation.isValidPhoneNumber(mInputPhoneNumber)) {
            Toasty.error(requireActivity(), R.string.invalid_field);
            return;
         }

         String email = requireNonNull(mInputEmail.getEditText()).getText().toString();
         String name = requireNonNull(mInputName.getEditText()).getText().toString();
         String phoneNumber = requireNonNull(mInputPhoneNumber.getEditText()).getText().toString();

         NavDirections action = RegistrationFragmentDirections.actionNavRegToNavAuth()
             .setEmail(email)
             .setName(name)
             .setPhone(phoneNumber);
         Navigation.findNavController(v).navigate(action);
      });
   }
}
