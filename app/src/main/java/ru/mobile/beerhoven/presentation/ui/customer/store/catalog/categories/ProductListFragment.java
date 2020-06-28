package ru.mobile.beerhoven.presentation.ui.customer.store.catalog.categories;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.ProductRepository;
import ru.mobile.beerhoven.domain.model.Category;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.listeners.AdapterPositionListener;
import ru.mobile.beerhoven.presentation.ui.customer.store.catalog.CatalogAdapter;
import ru.mobile.beerhoven.presentation.ui.customer.store.catalog.CatalogViewModel;

public class ProductListFragment extends Fragment {
   private CatalogAdapter mAdapter;
   private CatalogViewModel mViewModel;
   private RecyclerView mRecyclerView;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_product_list, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_product);
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mViewModel = new CatalogViewModel(new ProductRepository());
      mViewModel.getProductListByCategory(Category.PRODUCTS.getValue())
          .observe(getViewLifecycleOwner(), list -> mAdapter.notifyDataSetChanged());
      onInitRecyclerView();
   }

   @SuppressLint("NotifyDataSetChanged")
   private void onInitRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

      List<Product> productList = mViewModel
          .getProductListByCategory(Category.PRODUCTS.getValue())
          .getValue();

      mAdapter = new CatalogAdapter(productList, requireActivity(), new AdapterPositionListener() {
         @Override
         public void onInteractionAdd(Product product) {
            mViewModel.onAddProductToCartRepository(product);
         }

         @Override
         public void onInteractionDelete(Product product) {
            mViewModel.onDeleteProductFromRepository(product);
         }
      });
      mRecyclerView.setAdapter(mAdapter);
      mAdapter.notifyDataSetChanged();
   }
}