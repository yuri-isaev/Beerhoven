package ru.mobile.beerhoven.authentication;

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

import java.util.Objects;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.activity.MainActivity;

public class SplashFragment extends Fragment {

   private static final short SPLASH_DISPLAY_LENGTH = 4000;

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
            Toast.makeText(getActivity(), "User not null", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
         } else {
            Toast.makeText(getActivity(), "User null", Toast.LENGTH_LONG).show();

            if ((Objects.requireNonNull(Navigation.findNavController(view).getCurrentDestination()).getId()) == R.id.nav_splash) {
               Navigation.findNavController(view).navigate(R.id.action_nav_splash_to_nav_reg);
            }
         }
      }, SPLASH_DISPLAY_LENGTH);
   }
}