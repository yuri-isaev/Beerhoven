package ru.mobile.beerhoven.presentation.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import es.dmoral.toasty.Toasty;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.network.AuthVerificationService;
import ru.mobile.beerhoven.data.remote.AuthRepository;
import ru.mobile.beerhoven.domain.model.Customer;
import ru.mobile.beerhoven.domain.model.User;
import ru.mobile.beerhoven.presentation.activity.MainActivity;

public class AuthFragment extends Fragment {
   private AuthViewModel mViewModel;
   //private final Context mContext = requireContext();
   private String mName;
   private String mEmail;
   private String mPhoneNumber;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getActivity() != null) {
         assert getArguments() != null;
         AuthFragmentArgs args = AuthFragmentArgs.fromBundle(getArguments());
         mName = args.getName();
         mEmail = args.getEmail();
         mPhoneNumber = args.getPhone();
      }
   }

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle state) {
      return inflater.inflate(R.layout.fragment_auth, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
      super.onViewCreated(view, state);
      mViewModel = new AuthViewModel(requireContext(), new AuthRepository(), new AuthVerificationService(requireContext()));
      mViewModel.onSaveNameToStorage(mName);
      mViewModel.onSendVerificationCodeToUser(mPhoneNumber).observe(getViewLifecycleOwner(), this::signInTheUser);
   }

   // Sign in
   private void signInTheUser(@NonNull Boolean response) {
      if (response) {
         User user = new Customer("", mName, mEmail, mPhoneNumber);
         mViewModel.onCreateUserToRepository(user);
         Toast.makeText(requireContext(), R.string.reg_account_success, Toast.LENGTH_LONG).show();
         Intent intent = new Intent(requireContext(), MainActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(intent);
         requireActivity().finish();
      } else {
         Toasty.error(requireContext(), R.string.auth_failed).show();
      }
   }
}
