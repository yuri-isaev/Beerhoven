package ru.mobile.beerhoven.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

import ru.mobile.beerhoven.R;

public class MainActivity extends AppCompatActivity {

   private NavigationView mNavigationView;
   private AppBarConfiguration mAppBarConfiguration;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      DrawerLayout drawer = findViewById(R.id.drawer_layout);
      mNavigationView = findViewById(R.id.nav_view);

      mAppBarConfiguration = new AppBarConfiguration
          .Builder(R.id.nav_news, R.id.nav_store, R.id.nav_order, R.id.nav_cart, R.id.nav_map)
          .setDrawerLayout(drawer).build();
   }
}