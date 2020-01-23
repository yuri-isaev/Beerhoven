package ru.mobile.beerhoven.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.custom.CustomTypeFaceSpan;
import ru.mobile.beerhoven.utils.Constants;

public class MainActivity extends AppCompatActivity {
   private AppBarConfiguration mAppBarConfiguration;
   private ImageView mIcon;
   private NavigationView mNavigationView;
   private SharedPreferences mSharedPref;
   private TextView mCounterText;
   private int mCounterValue;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);

      Toolbar toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      mSharedPref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
      mCounterValue = mSharedPref.getInt(Constants.COUNTER_VALUE, 0);

      DrawerLayout drawer = findViewById(R.id.drawer_layout);
      mNavigationView = findViewById(R.id.nav_view);

      mAppBarConfiguration = new AppBarConfiguration
          .Builder(R.id.nav_news, R.id.nav_store, R.id.nav_order, R.id.nav_cart, R.id.nav_map)
          .setDrawerLayout(drawer).build();

      NavController mNavigationController = Navigation.findNavController(this, R.id.nav_host_fragment);
      NavigationUI.setupActionBarWithNavController(this, mNavigationController, mAppBarConfiguration);
      NavigationUI.setupWithNavController(mNavigationView, mNavigationController);

      setFontToMenuItem();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.cart_counter, menu);
      return true;
   }

   @Override
   public boolean onSupportNavigateUp() {
      NavController mNavigationController = Navigation.findNavController(this, R.id.nav_host_fragment);
      return NavigationUI.navigateUp(mNavigationController, mAppBarConfiguration) || super.onSupportNavigateUp();
   }

   // Cart badge counter
   @Override
   public boolean onPrepareOptionsMenu(final Menu menu) {
      initBadgeCounter(menu);
      return super.onPrepareOptionsMenu(menu);
   }

   private void initBadgeCounter(Menu menu) {
      MenuItem menuItem = menu.findItem(R.id.navCartCounter);
      View counter = menuItem.getActionView();

      mIcon = counter.findViewById(R.id.badgeCart);
      mCounterText = counter.findViewById(R.id.badgeCounter);

      counter.setOnClickListener(v -> onOptionsItemSelected(menuItem));
      updateCounter(mCounterValue);
   }

   public void updateCounter(int newCounterValue) {
      if (mIcon == null || mCounterText == null) {
         return;
      }
      if (newCounterValue == 0) {
         mCounterText.setVisibility(View.GONE);
      } else {
         mCounterText.setVisibility(View.VISIBLE);
         mCounterText.setText(String.valueOf(newCounterValue));
         onCounterSave();
      }
   }

   public void onCounterSave() {
      SharedPreferences.Editor editor = mSharedPref.edit();
      editor.putInt(Constants.COUNTER_VALUE, mCounterValue).apply();
   }

   // Menu item font
   private void applyFontToMenuItem(MenuItem mi) {
      Typeface font = ResourcesCompat.getFont(this, R.font.catorze27_style_1);
      SpannableString title = new SpannableString(mi.getTitle());
      title.setSpan(new CustomTypeFaceSpan("", font), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
      mi.setTitle(title);
   }

   // Set menu item font
   private void setFontToMenuItem() {
      Menu menu = mNavigationView.getMenu();
      for (int i = 0; i < menu.size(); i++) {
         MenuItem menuItem = menu.getItem(i);
         // Applying a font to submenu
         SubMenu subMenu = menuItem.getSubMenu();
         if (subMenu != null && subMenu.size() > 0) {
            for (int j = 0; j < subMenu.size(); j++) {
               MenuItem subMenuItem = subMenu.getItem(j);
               applyFontToMenuItem(subMenuItem);
            }
         }
         applyFontToMenuItem(menuItem);
      }
   }
}
