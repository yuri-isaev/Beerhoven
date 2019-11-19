package ru.mobile.beerhoven.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.custom.CustomTypeFaceSpan;

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

      changeFont();
   }

   private void changeFont() {
      Menu menu = mNavigationView.getMenu();
      for (int i = 0; i < menu.size(); i++) {
         MenuItem menuItem = menu.getItem(i);
         // For applying a font to subMenu.
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

   private void applyFontToMenuItem(MenuItem mi) {
      Typeface font = ResourcesCompat.getFont(this, R.font.catorze27_style_1);
      SpannableString mNewTitle = new SpannableString(mi.getTitle());
      mNewTitle.setSpan(new CustomTypeFaceSpan("", font), 0, mNewTitle.length(),
          Spannable.SPAN_INCLUSIVE_INCLUSIVE);
      mi.setTitle(mNewTitle);
   }
}