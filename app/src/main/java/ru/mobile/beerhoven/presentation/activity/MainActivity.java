package ru.mobile.beerhoven.presentation.activity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.CartRepository;
import ru.mobile.beerhoven.data.remote.NewsRepository;
import ru.mobile.beerhoven.presentation.authentication.AuthViewModel;
import ru.mobile.beerhoven.presentation.ui.user.news.corporate.NewsListViewModel;
import ru.mobile.beerhoven.utils.TypeFaceSpan;
import ru.mobile.beerhoven.presentation.ui.cart.CartViewModel;

public class MainActivity extends AppCompatActivity {
   private AppBarConfiguration mAppBarConfiguration;
   private CartViewModel mCartViewModel;
   private NewsListViewModel mNewsListViewModel;
   private int mCartCounterValue;
   private ImageView mIcon;
   private int mNewsCounterValue;
   private NavController mNavController;
   private NavigationView mNavigationView;

   private TextView mBadgeCounter;
   private TextView mNewsCounter;
   private TextView mNews;
   private TextView mCartCounter;
   private TextView mCart;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      Toolbar toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
      mNavigationView = findViewById(R.id.nav_view);

      mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_news_list, R.id.nav_product_list,
          R.id.nav_order, R.id.nav_cart, R.id.nav_map, R.id.nav_add_product, R.id.nav_add_news)
          .setDrawerLayout(mDrawerLayout).build();

      mNews = (TextView) MenuItemCompat.getActionView(mNavigationView.getMenu().findItem(R.id.nav_news_list));
      mCart = (TextView) MenuItemCompat.getActionView(mNavigationView.getMenu().findItem(R.id.nav_cart));

      mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
      NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
      NavigationUI.setupWithNavController(mNavigationView, mNavController);

      mCartViewModel = new CartViewModel(new CartRepository(), (Application) getApplicationContext());
      mNewsListViewModel = new NewsListViewModel(new NewsRepository(), (Application) getApplicationContext());

      setNavHeaderUserName();
      setFontToMenuItem();
   }

   public void setNavHeaderUserName() {
      AuthViewModel model = new AuthViewModel(getApplicationContext());
      String userName = model.getUserNameToStorage();
      View headerView = mNavigationView.getHeaderView(0);
      TextView navUserName = headerView.findViewById(R.id.nav_user_name);
      navUserName.setText(String.format(" Hello,  %s", userName));
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

   @Override
   public boolean onPrepareOptionsMenu(final Menu menu) {
      initBadgeCartCounter(menu);
      initCartCountDrawer();
      initNewsCountDrawer();
      return super.onPrepareOptionsMenu(menu);
   }

   private void initBadgeCartCounter(Menu menu) {
      MenuItem menuItem = menu.findItem(R.id.cart_counter_bar);
      View counter = menuItem.getActionView();
      mIcon = counter.findViewById(R.id.badge_cart);
      mBadgeCounter = counter.findViewById(R.id.badge_counter_cart);
      counter.setOnClickListener(v -> onOptionsItemSelected(menuItem));
      updateCartCounter(mCartCounterValue);

      int cartCount = mCartViewModel.getCartCountFromStorage();
      int newsCount = mNewsListViewModel.getNewsCountFromStorage();

      if (newsCount <= 0) {
         mNews.setVisibility(View.GONE);
      } else {
         mNews.setVisibility(View.VISIBLE);
         String value = Integer.toString(mNewsListViewModel.getNewsCountFromStorage());
         mNews.setText(value);
         mNewsCounterValue = mNewsListViewModel.getNewsCountFromStorage();
      }

      if (cartCount <= 0) {
         mCart.setVisibility(View.GONE);
         mBadgeCounter.setVisibility(View.GONE);
      } else {
         mCart.setVisibility(View.VISIBLE);
         String value = Integer.toString(mCartViewModel.getCartCountFromStorage());
         mCart.setText(value);
         mBadgeCounter.setVisibility(View.VISIBLE);
         mBadgeCounter.setText(value);
         mCartCounterValue = mCartViewModel.getCartCountFromStorage();
      }
   }

   public void onUpdateCounterFromFragment(int data) {
      updateCartCounter(data);
   }

   @SuppressLint("UseCompatLoadingForDrawables")
   private void initCartCountDrawer() {
      mCart.setGravity(Gravity.CENTER);
      mCart.setTypeface(null, Typeface.BOLD);
      mCart.setTextColor(getResources().getColor(R.color.colorAccent));
      mCart.setTextSize(24);
      mCart.setBackground(getDrawable(R.drawable.circle_violet));
      mCartCounter = mCart;
   }

   private void onSaveCartCounter(int counterValue) {
      mCartViewModel.onSaveCartCounterToStorage(counterValue);
   }

   public void onIncreaseCartCounter() {
      updateCartCounter(++mCartCounterValue);
   }

   public void onDecreaseCartDrawerCounter() {
      if (mCartCounterValue <= 0) {
         mCartCounterValue = 0;
      } else {
         updateCartCounter(--mCartCounterValue);
      }
   }

   private void updateCartCounter(int counterValue) {
      if (mIcon == null || mBadgeCounter == null || mCartCounter == null) {
         return;
      }
      if (counterValue == 0) {
         mCartCounter.setVisibility(View.GONE);
         mBadgeCounter.setVisibility(View.GONE);
         onSaveCartCounter(counterValue);
      } else {
         mCartCounter.setVisibility(View.VISIBLE);
         mCartCounter.setText(String.valueOf(counterValue));
         mBadgeCounter.setVisibility(View.VISIBLE);
         mBadgeCounter.setText(String.valueOf(counterValue));
         onSaveCartCounter(counterValue);
      }
   }

   @SuppressLint("UseCompatLoadingForDrawables")
   private void initNewsCountDrawer() {
      mNews.setGravity(Gravity.CENTER);
      mNews.setTypeface(null, Typeface.BOLD);
      mNews.setTextColor(getResources().getColor(R.color.colorAccent));
      mNews.setTextSize(24);
      mNews.setBackground(getDrawable(R.drawable.circle_violet));
      mNewsCounter = mNews;
   }

   private void updateNewsCounter(int counterValue) {
      if (mNewsCounter == null) {
         return;
      }
      if (counterValue == 0) {
         mNewsCounter.setVisibility(View.GONE);
         onSaveNewsCount(counterValue);
      } else {
         mNewsCounter.setVisibility(View.VISIBLE);
         mNewsCounter.setText(String.valueOf(counterValue));
         onSaveNewsCount(counterValue);
      }
   }

   private void onSaveNewsCount(int counterValue) {
      mNewsListViewModel.onSaveNewsCounterToStorage(counterValue);
   }

   public void onIncreaseNewsCounter() {
      updateNewsCounter(++mNewsCounterValue);
   }

   public void onDecreaseNewsCounter() {
      if (mNewsCounterValue <= 0) {
         mNewsCounterValue = 0;
      } else {
         updateNewsCounter(--mNewsCounterValue);
      }
   }

   // Menu item font
   private void applyFontToMenuItem(MenuItem mi) {
      Typeface font = ResourcesCompat.getFont(this, R.font.catorze27_style_1);
      SpannableString title = new SpannableString(mi.getTitle());
      title.setSpan(new TypeFaceSpan("", font), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
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

      // Delete cart list values when destroy
      mCartViewModel.onDeleteCartListToRepository();

      // Delete shared preferences values when destroy
      // Delete cart counter when destroy
      mCartViewModel.onDeleteCartCounterToStorage();
   }
}
