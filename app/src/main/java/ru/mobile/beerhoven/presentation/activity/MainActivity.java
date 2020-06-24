package ru.mobile.beerhoven.presentation.activity;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
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
import ru.mobile.beerhoven.data.remote.CartRepository;
import ru.mobile.beerhoven.data.remote.NewsRepository;
import ru.mobile.beerhoven.data.remote.NotificationRepository;
import ru.mobile.beerhoven.presentation.authentication.AuthViewModel;
import ru.mobile.beerhoven.presentation.ui.admin.notifications.NotificationListViewModel;
import ru.mobile.beerhoven.presentation.ui.customer.cart.CartListViewModel;
import ru.mobile.beerhoven.presentation.ui.customer.news.corporate.NewsListViewModel;
import ru.mobile.beerhoven.utils.TypeFaceSpan;

public class MainActivity extends AppCompatActivity {
   private AppBarConfiguration mAppBarConfiguration;
   private NavController mNavController;
   private NavigationView mNavigationView;

   // NewsList fragment activity counter
   private NewsListViewModel mNewsListViewModel;
   private int mNewsCounterValue;
   private TextView mNewsView;

   // CartList fragment activity counter
   private CartListViewModel mCartViewModel;
   private TextView mCartBadgeCounter;
   private TextView mCartView;
   private ImageView mCartBadgeIcon;
   private int mCartCounterValue;

   // NotificationList fragment activity counter
   private NotificationListViewModel mNotificationListViewModel;
   private TextView mNotificationView;
   private int mNotificationCounterValue;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      Toolbar toolbar = findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
      mNavigationView = findViewById(R.id.nav_view);

      mAppBarConfiguration = new AppBarConfiguration.Builder(
          R.id.nav_news,
          R.id.nav_products,
          R.id.nav_orders,
          R.id.nav_cart,
          R.id.nav_map,
          R.id.nav_add_product,
          R.id.nav_add_news,
          R.id.nav_admin_notifications)
          .setDrawerLayout(mDrawerLayout).build();

      mNewsView = (TextView) mNavigationView.getMenu()
          .findItem(R.id.nav_news)
          .getActionView();

      mCartView = (TextView) mNavigationView.getMenu()
          .findItem(R.id.nav_cart)
          .getActionView();

      mNotificationView = (TextView) mNavigationView.getMenu()
          .findItem(R.id.nav_admin_notifications)
          .getActionView();

      mCartViewModel = new CartListViewModel(getApplicationContext(),
          new CartRepository());

      mNewsListViewModel = new NewsListViewModel(getApplicationContext(),
          new NewsRepository());

      mNotificationListViewModel = new NotificationListViewModel(getApplicationContext(),
          new NotificationRepository());

      mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
      NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
      NavigationUI.setupWithNavController(mNavigationView, mNavController);

      onSetNavHeaderUserName();
      onSetMenuItemFont();
   }

   public void onSetNavHeaderUserName() {
      View headerView = mNavigationView.getHeaderView(0);
      TextView navUserName = headerView.findViewById(R.id.tvNavUserName);
      AuthViewModel model = new AuthViewModel(getApplicationContext());
      String userName = model.getUserNameToStorage();
      navUserName.setText(String.format(" Hello,  %s", userName));
   }

   @Override
   public boolean onCreateOptionsMenu(@NonNull Menu menu) {
      getMenuInflater().inflate(R.menu.cart_counter, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      return NavigationUI.onNavDestinationSelected(item, mNavController) || super.onOptionsItemSelected(item);
   }

   @Override
   public boolean onSupportNavigateUp() {
      return NavigationUI.navigateUp(mNavController, mAppBarConfiguration) || super.onSupportNavigateUp();
   }

   // Menu item font
   private void onApplyMenuItemFont(@NonNull MenuItem item) {
      Typeface font = ResourcesCompat.getFont(this, R.font.catorze27_style1_semibold);
      SpannableString title = new SpannableString(item.getTitle());
      title.setSpan(new TypeFaceSpan("", font), 0, title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
      item.setTitle(title);
   }

   // Set menu item font
   private void onSetMenuItemFont() {
      Menu menu = mNavigationView.getMenu();
      for (int i = 0; i < menu.size(); i++) {
         MenuItem menuItem = menu.getItem(i);
         // Applying a font to submenu
         SubMenu subMenu = menuItem.getSubMenu();
         if (subMenu != null && subMenu.size() > 0) {
            for (int j = 0; j < subMenu.size(); j++) {
               MenuItem subMenuItem = subMenu.getItem(j);
               onApplyMenuItemFont(subMenuItem);
            }
         }
         onApplyMenuItemFont(menuItem);
      }
   }

   @Override
   public boolean onPrepareOptionsMenu(@NonNull final Menu menu) {
      initCounterMenu(menu);
      initCartCountDrawer();
      initNewsCountDrawer();
      initNotificationCountDrawer();
      return super.onPrepareOptionsMenu(menu);
   }

   private void initCounterMenu(@NonNull Menu menu) {
      MenuItem menuItem = menu.findItem(R.id.nav_cart);
      View counter = menuItem.getActionView();
      mCartBadgeIcon = counter.findViewById(R.id.ivBadgeCart);
      mCartBadgeCounter = counter.findViewById(R.id.tvBadgeCounterCart);

      counter.setOnClickListener(v -> onOptionsItemSelected(menuItem));
      updateCartCounter(mCartCounterValue);

      int cartCount = mCartViewModel.getCartCountFromStorage();
      int newsCount = mNewsListViewModel.getNewsCountFromStorage();
      int notificationCount = mNotificationListViewModel.getNotificationCountFromStorage();

      if (newsCount <= 0) {
         mNewsView.setVisibility(View.GONE);
      } else {
         mNewsView.setVisibility(View.VISIBLE);
         String value = Integer.toString(mNewsListViewModel.getNewsCountFromStorage());
         mNewsView.setText(value);
         mNewsCounterValue = mNewsListViewModel.getNewsCountFromStorage();
      }

      if (cartCount <= 0) {
         mCartView.setVisibility(View.GONE);
         mCartBadgeCounter.setVisibility(View.GONE);
      } else {
         mCartView.setVisibility(View.VISIBLE);
         String value = Integer.toString(mCartViewModel.getCartCountFromStorage());
         mCartView.setText(value);
         mCartBadgeCounter.setVisibility(View.VISIBLE);
         mCartBadgeCounter.setText(value);
         mCartCounterValue = mCartViewModel.getCartCountFromStorage();
      }

      if (notificationCount <= 0) {
         mNotificationView.setVisibility(View.GONE);
      } else {
         mNotificationView.setVisibility(View.VISIBLE);
         String value = Integer.toString(mNotificationListViewModel.getNotificationCountFromStorage());
         mNotificationView.setText(value);
         mNotificationCounterValue = mNotificationListViewModel.getNotificationCountFromStorage();
      }
   }

   public void onUpdateActivityCounter(int data) {
      updateCartCounter(data);
      mCartCounterValue = data;
   }

   @SuppressLint("UseCompatLoadingForDrawables")
   private void initCartCountDrawer() {
      mCartView.setBackground(getDrawable(R.drawable.circle_violet));
      mCartView.setGravity(Gravity.CENTER);
      mCartView.setTextColor(getResources().getColor(R.color.colorMainYellow));
      mCartView.setTypeface(null, Typeface.BOLD);
      mCartView.setTextSize(24);
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
      if (mCartBadgeIcon == null || mCartBadgeCounter == null || mCartView == null) {
         return;
      }
      if (counterValue == 0) {
         mCartView.setVisibility(View.GONE);
         mCartBadgeCounter.setVisibility(View.GONE);
      } else {
         mCartView.setVisibility(View.VISIBLE);
         mCartView.setText(String.valueOf(counterValue));
         mCartBadgeCounter.setVisibility(View.VISIBLE);
         mCartBadgeCounter.setText(String.valueOf(counterValue));
      }
      mCartViewModel.onSaveCartCounterToStorage(counterValue);
   }

   @SuppressLint("UseCompatLoadingForDrawables")
   private void initNewsCountDrawer() {
      mNewsView.setBackground(getDrawable(R.drawable.circle_violet));
      mNewsView.setGravity(Gravity.CENTER);
      mNewsView.setTextColor(getResources().getColor(R.color.colorMainYellow));
      mNewsView.setTextSize(24);
      mNewsView.setTypeface(null, Typeface.BOLD);
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

   private void updateNewsCounter(int counterValue) {
      if (mNewsView == null) {
         return;
      }
      if (counterValue == 0) {
         mNewsView.setVisibility(View.GONE);
      } else {
         mNewsView.setVisibility(View.VISIBLE);
         mNewsView.setText(String.valueOf(counterValue));
      }
      mNewsListViewModel.onSaveNewsCounterToStorage(counterValue);
   }

   @SuppressLint("UseCompatLoadingForDrawables")
   private void initNotificationCountDrawer() {
      mNotificationView.setBackground(getDrawable(R.drawable.circle_violet));
      mNotificationView.setGravity(Gravity.CENTER);
      mNotificationView.setTextColor(getResources().getColor(R.color.colorMainYellow));
      mNotificationView.setTextSize(24);
      mNotificationView.setTypeface(null, Typeface.BOLD);
   }

   public void onIncreaseNotificationCounter() {
      updateNotificationCounter(++mNotificationCounterValue);
   }

   public void onDecreaseNotificationCounter() {
      if (mNotificationCounterValue <= 0) {
         mNotificationCounterValue = 0;
      } else {
         updateNotificationCounter(--mNotificationCounterValue);
      }
   }

   private void updateNotificationCounter(int counterValue) {
      if (mNotificationView == null) {
         return;
      }
      if (counterValue == 0) {
         mNotificationView.setVisibility(View.GONE);
      } else {
         mNotificationView.setVisibility(View.VISIBLE);
         mNotificationView.setText(String.valueOf(counterValue));
      }
      mNotificationListViewModel.onSaveNotificationCounterToStorage(counterValue);
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
