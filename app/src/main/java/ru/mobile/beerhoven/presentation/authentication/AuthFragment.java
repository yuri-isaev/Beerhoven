package ru.mobile.beerhoven.presentation.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.AuthRepository;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.domain.model.User;

public class AuthFragment extends Fragment {
   private AuthViewModel mViewModel;
   private FirebaseAuth mAuthInstance;
   private String mName;
   private String mEmail;
   private String mPhoneNumber;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_auth, container, false);
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      if (getActivity() != null) {
         assert getArguments() != null;

         mAuthInstance = FirebaseAuth.getInstance();

         AuthFragmentArgs args = AuthFragmentArgs.fromBundle(getArguments());
         mName = args.getName();
         mEmail = args.getEmail();
         mPhoneNumber = args.getPhone();

         mViewModel = new AuthViewModel(new AuthRepository(), getActivity().getApplicationContext());
         mViewModel.onSaveNameToStorage(mName);

         sendVerificationCodeToUser(mPhoneNumber);
      }
   }

   private void sendVerificationCodeToUser(String phoneNumber) {
      PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuthInstance)
          .setPhoneNumber(phoneNumber)       // Phone number to verify
          .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
          .setActivity(requireActivity())    // Activity (for callback binding)
          .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
          .build();
      PhoneAuthProvider.verifyPhoneNumber(options);
   }

   private final OnVerificationStateChangedCallbacks mCallbacks = new OnVerificationStateChangedCallbacks() {
      @Override
      public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
         super.onCodeSent(s, forceResendingToken);
         Toast.makeText(getActivity(), R.string.reg_success, Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
         signInTheUserByCredentials(credential);
      }

      @Override
      public void onVerificationFailed(@NonNull FirebaseException e) {
         if (e instanceof FirebaseAuthInvalidCredentialsException) {
            Toasty.error(requireActivity(), R.string.auth_phone_number_wrong, Toast.LENGTH_LONG).show();
         } else if (e instanceof FirebaseTooManyRequestsException) {
            Toasty.warning(requireActivity(), R.string.auth_quota_exceeded, Toast.LENGTH_LONG).show();
         }
      }
   };

   private void signInTheUserByCredentials(PhoneAuthCredential credential) {
      mAuthInstance.signInWithCredential(credential).addOnCompleteListener(requireActivity(), task -> {
         if (task.isSuccessful()) {
            Toast.makeText(getActivity(), R.string.reg_account_success, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
         } else {
            Toasty.error(requireActivity(), R.string.auth_failed, Toast.LENGTH_LONG).show();
         }

         User user = new User(mName, mEmail, mPhoneNumber);
         mViewModel.onCreateUserToRepository(user);
      });
   }
}
