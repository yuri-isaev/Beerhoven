package ru.mobile.beerhoven.presentation.ui.customer.store.sections;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ru.mobile.beerhoven.presentation.ui.customer.store.catalog.CatalogFragment;
import ru.mobile.beerhoven.presentation.ui.customer.store.catalog.categories.AlcoListFragment;
import ru.mobile.beerhoven.presentation.ui.customer.store.catalog.categories.ProductListFragment;

public class SectionPagerAdapter extends FragmentStateAdapter {

   public SectionPagerAdapter(@NonNull StoreFragment storeFragment) {
      super(storeFragment);
   }

   @NonNull
   @Override
   public Fragment createFragment(int position) {
      switch (position) {
         case 1:
            return new ProductListFragment();
         case 2:
            return new AlcoListFragment();
         default:
            return new CatalogFragment();
      }
   }

   @Override
   public int getItemCount() {
      return 3;
   }
}