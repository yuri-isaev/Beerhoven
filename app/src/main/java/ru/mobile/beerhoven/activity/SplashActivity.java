package ru.mobile.beerhoven.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.authentication.SplashFragment;

public class SplashActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_splash);
      if (savedInstanceState == null) {
         getSupportFragmentManager().beginTransaction()
             .replace(R.id.container, new SplashFragment()).commitNow();
      }
   }
}