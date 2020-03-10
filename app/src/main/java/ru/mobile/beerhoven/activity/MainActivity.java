package ru.mobile.beerhoven.activity;

import android.annotation.SuppressLint;
import android.app.Application;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.custom.CustomTypeFaceSpan;
import ru.mobile.beerhoven.data.remote.CartRepository;
import ru.mobile.beerhoven.ui.cart.CartViewModel;
import ru.mobile.beerhoven.utils.Constants;

public class MainActivity extends AppCompatActivity {
   private AppBarConfiguration mAppBarConfiguration;
   private CartViewModel viewModel;
   private ImageView mIcon;
   private NavController mNavController;
   private NavigationView mNavigationView;
   private TextView mCounterText;
   private int mCounterValue;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_main);

      Toolbar toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      // Set cart count value by shared preferences
      viewModel = new CartViewModel(new CartRepository(), (Application) getApplicationContext());
      viewModel.onSetCartCount();

      DrawerLayout drawer = findViewById(R.id.drawer_layout);
      mNavigationView = findViewById(R.id.nav_view);

      mAppBarConfiguration = new AppBarConfiguration
          .Builder(R.id.nav_news, R.id.nav_store, R.id.nav_order, R.id.nav_cart, R.id.nav_map, R.id.nav_add_product)
          .setDrawerLayout(drawer).build();

      mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
      NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
      NavigationUI.setupWithNavController(mNavigationView, mNavController);

      setFontToMenuItem();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.cart_counter, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      return NavigationUI.onNavDestinationSelected(item, mNavController) || super.onOptionsItemSelected(item);
   }

   @Override
   public boolean onSupportNavigateUp() {
      return NavigationUI.navigateUp(mNavController, mAppBarConfiguration) || super.onSupportNavigateUp();
   }

   // Cart badge counter
   @Override
   public boolean onPrepareOptionsMenu(final Menu menu) {
      initBadgeCounter(menu);
      return super.onPrepareOptionsMenu(menu);
   }

   private void initBadgeCounter(Menu menu) {
      MenuItem menuItem = menu.findItem(R.id.cart_counter_bar);
      View counter = menuItem.getActionView();

      mIcon = counter.findViewById(R.id.badge_cart);
      mCounterText = counter.findViewById(R.id.badge_counter_cart);

      counter.setOnClickListener(v -> onOptionsItemSelected(menuItem));
      updateCounter(mCounterValue);
   }

   public void onIncreaseCounterClick() {
      updateCounter(++mCounterValue);
   }

   public void onDecreaseCounterClick() {
      if (mCounterValue <= 0) {
         mCounterValue = 0;
      } else {
         updateCounter(--mCounterValue);
      }
   }

   // Save cart count value by shared preferences
   public void onCounterSave() {
      viewModel.onCounterCartSave();
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

   public void onUpdateCounterFromFragment(int data) {
      mCounterValue = data;
      updateCounter(data);
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

   @SuppressLint("CommitPrefEdits")
   @Override
   protected void onDestroy() {
      super.onDestroy();
      // Toast.makeText(this, ": Destroy", Toast.LENGTH_LONG).show();

      // Delete cart list values when destroy
      viewModel.onDeleteCartList();

      // Delete shared preferences values when destroy
      // Delete cart counter when destroy
      viewModel.onDeleteCartCounter();
   }
}
