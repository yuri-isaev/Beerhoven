package ru.mobile.beerhoven.presentation.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.network.AuthService;
import ru.mobile.beerhoven.data.remote.AuthRepository;
import ru.mobile.beerhoven.domain.model.Customer;
import ru.mobile.beerhoven.domain.model.User;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.utils.Toasty;

public class AuthFragment extends Fragment {
   private AuthViewModel mViewModel;
   private String mEmail;
   private String mName;
   private String mPhone;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getActivity() != null) {
         assert getArguments() != null;
         AuthFragmentArgs args = AuthFragmentArgs.fromBundle(getArguments());
         mEmail = args.getEmail();
         mName = args.getName();
         mPhone = args.getPhone();
      }
   }

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_auth, container, false);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mViewModel = new AuthViewModel(requireContext(), new AuthRepository(), new AuthService(requireActivity()));
      mViewModel.onSaveNameToStorage(mName);
      mViewModel.onAuthenticationConfirm(mPhone).observe(getViewLifecycleOwner(), this::onSignIn);
   }

   private void onSignIn(@NonNull Boolean response) {
      if (response) {
         User user = new Customer(mEmail, "null", mName, mPhone);
         mViewModel.onCreateUserToRepository(user);
         Intent intent = new Intent(requireContext(), MainActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(intent);
         requireActivity().finish();
         Toasty.success(requireContext(), R.string.reg_account_success);
      } else {
         Toasty.error(requireContext(), R.string.auth_failed);
      }
   }
}
