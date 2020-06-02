package ru.mobile.beerhoven.presentation.ui.user.store.catalog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.ProductRepository;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.listeners.AdapterPositionListener;

public class CatalogFragment extends Fragment implements MenuProvider {
   private List<Product> mCatalog;
   private CatalogAdapter mAdapter;
   private CatalogViewModel mViewModel;
   private RecyclerView mRecyclerView;

   @SuppressLint("MissingInflatedId")
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_catalog, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_catalog);
      requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mViewModel = new CatalogViewModel(new ProductRepository());
      mViewModel.getProductListToRepository().observe(getViewLifecycleOwner(),
          list -> mAdapter.notifyDataSetChanged());
      onInitRecyclerView();
   }

   @SuppressLint("NotifyDataSetChanged")
   private void onInitRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
      mCatalog = mViewModel.getProductListToRepository().getValue();
      mAdapter = new CatalogAdapter(mCatalog, requireActivity(), new AdapterPositionListener() {
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

   @Override
   public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
      menuInflater.inflate(R.menu.search_main, menu);
      MenuItem menuItem = menu.findItem(R.id.action_search);
      SearchView searchView = (SearchView) menuItem.getActionView();
      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         @Override
         public boolean onQueryTextSubmit(String query) {
            onItemSearch(query);
            return false;
         }

         @Override
         public boolean onQueryTextChange(String newText) {
            onItemSearch(newText);
            return false;
         }
      });
   }

   @Override
   public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
      return menuItem.getItemId() == R.id.action_search;
   }

   @SuppressLint("NotifyDataSetChanged")
   private void onItemSearch(String searchText) {
      List<Product> searchList = new ArrayList<>();
      for (Product product : mCatalog) {
         if (product.getName() != null && !product.getName().isEmpty()) {
            if (product.getName().toLowerCase().contains(searchText.toLowerCase())) {
               searchList.add(product);
            }
         }
      }

      mAdapter = new CatalogAdapter(searchList, getContext(), new AdapterPositionListener() {
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
   }
}
