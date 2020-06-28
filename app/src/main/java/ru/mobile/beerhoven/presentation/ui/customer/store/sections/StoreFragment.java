package ru.mobile.beerhoven.presentation.ui.customer.store.sections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayoutMediator;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.domain.model.Category;

public class StoreFragment extends Fragment {
   private TabLayout mTabLayout;
   private ViewPager2 mViewPager;

   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_store, container, false);
      mTabLayout = view.findViewById(R.id.tab_layout);
      mViewPager = view.findViewById(R.id.view_pager);
      return view;
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      SectionPagerAdapter adapter = new SectionPagerAdapter(this);
      mViewPager.setAdapter(adapter);
      mViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

      mTabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
         @Override
         public void onTabSelected(TabLayout.Tab tab) {
            mViewPager.setCurrentItem(tab.getPosition());
         }

         @Override
         public void onTabUnselected(TabLayout.Tab tab) {}

         @Override
         public void onTabReselected(TabLayout.Tab tab) {}
      });

      new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> {
         switch (position) {
            case 0:
               tab.setText(Category.ALL.getValue());
               break;
            case 1:
               tab.setText(Category.PRODUCTS.getValue());
               break;
            case 2:
               tab.setText(Category.ALCOHOL.getValue());
               break;
         }
      }).attach();
   }
}
