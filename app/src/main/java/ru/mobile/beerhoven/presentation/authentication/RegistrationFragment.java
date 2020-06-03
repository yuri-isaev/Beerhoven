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
   private Button mUserRegistrationButton;
   private TextInputLayout mUserEmailInput;
   private TextInputLayout mUserNameInput;
   private TextInputLayout mUserPhoneNumberInput;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      FragmentRegistrationBinding binding = FragmentRegistrationBinding.inflate(inflater, container, false);
      mUserRegistrationButton = binding.btnRegistration;
      mUserEmailInput = binding.inputEmail;
      mUserNameInput = binding.inputName;
      mUserPhoneNumberInput = binding.inputPhoneNumber;

      // Set button alpha to zero.
      mUserRegistrationButton.setAlpha(0f);

      // Animate the alpha value to 1f and set duration as 800 ms.
      mUserRegistrationButton.animate().alpha(1f).setDuration(800);

      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      mUserRegistrationButton.setOnClickListener(v -> {
         if (  !Validation.isValidEmailField(mUserEmailInput)
             | !Validation.isValidTextField(mUserNameInput)
             | !Validation.isValidPhoneNumberField(mUserPhoneNumberInput)) {
            Toasty.error(requireActivity(), R.string.invalid_field);
            return;
         }

         String email = requireNonNull(mUserEmailInput.getEditText()).getText().toString();
         String name = requireNonNull(mUserNameInput.getEditText()).getText().toString();
         String phoneNumber = requireNonNull(mUserPhoneNumberInput.getEditText()).getText().toString();

         NavDirections action = RegistrationFragmentDirections.actionNavRegToNavAuth()
             .setEmail(email)
             .setName(name)
             .setPhone(phoneNumber);
         Navigation.findNavController(v).navigate(action);
      });
   }
}
