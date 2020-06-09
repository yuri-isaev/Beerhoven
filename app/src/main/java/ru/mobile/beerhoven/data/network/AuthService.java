package ru.mobile.beerhoven.data.network;

import static com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken;
import static com.google.firebase.auth.PhoneAuthProvider.getCredential;
import static com.google.firebase.auth.PhoneAuthProvider.verifyPhoneNumber;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;

import java.util.concurrent.TimeUnit;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.domain.repository.IAuthService;
import ru.mobile.beerhoven.utils.Toasty;

/**
 * OTP authentication by user phone number.
 * One time password - it is a password that is valid for only one authentication session.
 */
public class AuthService implements IAuthService {
   private final Activity mActivity;
   private final FirebaseAuth mFirebaseAuth;
   private final MutableLiveData<Boolean> mMutableData;

   // String for storing our verification ID.
   private String verificationId;

   public AuthService(Activity activity) {
      this.mActivity = activity;
      this.mFirebaseAuth = FirebaseAuth.getInstance();
      this.mMutableData = new MutableLiveData<>();
   }

   // Method is used for getting OTP on user phone number.
   @Override
   public MutableLiveData<Boolean> sendVerificationCodeToUser(String phoneNumber) {
      PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mFirebaseAuth)
          .setPhoneNumber(phoneNumber)
          .setTimeout(60L, TimeUnit.SECONDS)
          .setActivity(mActivity)
          .setCallbacks(mCallbacks)
          .build();
      verifyPhoneNumber(options);
      return mMutableData;
   }

   // Callback method is called on phone auth provider.
   private final OnVerificationStateChangedCallbacks
       // Initializing our callbacks for on verification callback method.
       mCallbacks = new OnVerificationStateChangedCallbacks() {

      // Used when OTP is sent from Firebase.
      @Override
      public void onCodeSent(@NonNull String s, @NonNull ForceResendingToken token) {
         super.onCodeSent(s, token);
         // When we receive the OTP it contains a unique id which,
         // we are storing in our string which we have already created.
         verificationId = s;
      }

      @Override
      public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
         // Used for getting OTP code which is sent in phone auth credentials.
         final String otp = credential.getSmsCode();

         if (otp != null) {
            verifyCode(otp);
         }
      }

      @Override
      public void onVerificationFailed(@NonNull FirebaseException e) {
         if (e instanceof FirebaseAuthInvalidCredentialsException) {
            Toasty.error(mActivity, R.string.auth_phone_number_wrong);
         } else if (e instanceof FirebaseTooManyRequestsException) {
            Toasty.warning(mActivity, R.string.auth_quota_exceeded);
         }
      }
   };

   private void verifyCode(String otp) {
      // Getting credentials from our verification id and code.
      PhoneAuthCredential credential = getCredential(verificationId, otp);
      signInTheUserByCredential(credential);
   }

   private void signInTheUserByCredential(PhoneAuthCredential credential) {
      mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(mActivity, task -> {
         if (task.isSuccessful()) {
            mMutableData.postValue(true);
         } else {
            mMutableData.postValue(false);
         }
      });
   }
}
