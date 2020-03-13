package ru.mobile.beerhoven.presentation.authentication;

import static java.util.Objects.requireNonNull;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.utils.Constants;

public class SplashFragment extends Fragment {

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_splash, container, false);
   }

   @Override
   public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      new Handler().postDelayed(() -> {

         FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

         if (currentUser != null) {
            Toast.makeText(getActivity(), R.string.auth_user_success, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
         } else {
            Toast.makeText(getActivity(), R.string.auth_user_failed, Toast.LENGTH_LONG).show();

            if ((requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId()) == R.id.nav_splash) {
               Navigation.findNavController(view).navigate(R.id.action_nav_splash_to_nav_reg);
            }
         }
      }, Constants.SPLASH_DISPLAY_LENGTH);
   }
}