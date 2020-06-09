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
   private Button mRegistrationButton;
   private TextInputLayout mEmailInput;
   private TextInputLayout mNameInput;
   private TextInputLayout mPhoneNumberInput;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      FragmentRegistrationBinding binding = FragmentRegistrationBinding.inflate(inflater, container, false);
      mRegistrationButton = binding.btnRegistration;
      mEmailInput = binding.inputEmail;
      mNameInput = binding.inputName;
      mPhoneNumberInput = binding.inputPhoneNumber;

      // Set button alpha to zero.
      mRegistrationButton.setAlpha(0f);

      // Animate the alpha value to 1f and set duration as 800 ms.
      mRegistrationButton.animate().alpha(1f).setDuration(800);

      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      mRegistrationButton.setOnClickListener(v -> {
         if (  !Validation.isValidEmailField(mEmailInput)
             | !Validation.isValidTextField(mNameInput)
             | !Validation.isValidPhoneNumberField(mPhoneNumberInput)) {
            Toasty.error(requireActivity(), R.string.invalid_field);
            return;
         }

         String email = requireNonNull(mEmailInput.getEditText()).getText().toString();
         String name = requireNonNull(mNameInput.getEditText()).getText().toString();
         String phoneNumber = requireNonNull(mPhoneNumberInput.getEditText()).getText().toString();

         NavDirections action = RegistrationFragmentDirections.actionNavRegToNavAuth()
             .setEmail(email)
             .setName(name)
             .setPhone(phoneNumber);
         Navigation.findNavController(v).navigate(action);
      });
   }
}
