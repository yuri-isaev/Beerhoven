package ru.mobile.beerhoven.presentation.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.presentation.authentication.SplashFragment;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_splash);
      if (savedInstanceState == null) {
         getSupportFragmentManager()
             .beginTransaction()
             .replace(R.id.container, new SplashFragment())
             .commitNow();
      }
   }
}