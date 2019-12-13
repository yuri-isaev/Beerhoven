package ru.mobile.beerhoven.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.activity.MainActivity;

public class AuthenticationFragment extends Fragment {

   private FirebaseAuth mAuth;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getActivity() != null) {
         assert getArguments() != null;
         mAuth = FirebaseAuth.getInstance();
      }
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_auth, container, false);
   }

   private void sendVerificationCodeToUser(String phoneNo) {
      PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNo, 60, TimeUnit.SECONDS, (Activity) TaskExecutors.MAIN_THREAD, mCallbacks);
   }

   private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
      @Override
      public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
         super.onCodeSent(s, forceResendingToken);
         Toast.makeText(getActivity(), "code sent!", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
         signInTheUserByCredentials(credential);
      }

      @Override
      public void onVerificationFailed(@NonNull FirebaseException e) {
         if (e instanceof FirebaseAuthInvalidCredentialsException) {
            Toasty.error(requireActivity(), "Неверный номер телефона", Toast.LENGTH_LONG).show();
         } else if (e instanceof FirebaseTooManyRequestsException) {
            Toasty.warning(requireActivity(), "Превышение квоты доступа", Toast.LENGTH_LONG).show();
         }
      }
   };

   private void signInTheUserByCredentials(PhoneAuthCredential credential) {
      mAuth.signInWithCredential(credential).addOnCompleteListener(requireActivity(), task -> {
         if (task.isSuccessful()) {
            Toast.makeText(getActivity(), "Ваш аккаунт успешно создан!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
         } else {
            Toasty.error(requireActivity(), "Проверка подлинности не удалась", Toast.LENGTH_LONG).show();
         }
      });
   }
}