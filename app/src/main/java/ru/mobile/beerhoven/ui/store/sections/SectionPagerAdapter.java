package ru.mobile.beerhoven.ui.store.sections;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionPagerAdapter extends FragmentPagerAdapter {

   private final List<String> mTitleList;
   private final List<Fragment> mFragmentList;

   public SectionPagerAdapter(@NonNull FragmentManager fm) {
      super(fm);
      this.mTitleList = new ArrayList<>();
      this.mFragmentList = new ArrayList<>();
   }

   @NonNull
   @Override
   public Fragment getItem(int position) {
      return mFragmentList.get(position);
   }

   @Override
   public int getCount() {
      return mFragmentList.size();
   }

   @Override
   public CharSequence getPageTitle(int position) {
      return mTitleList.get(position);
   }

   public void addFragment(Fragment fragment, String title) {
      mFragmentList.add(fragment);
      mTitleList.add(title);
   }
}