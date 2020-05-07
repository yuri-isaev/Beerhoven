package ru.mobile.beerhoven.presentation.ui.user.store.catalog;

import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.ProductRepository;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.interfaces.AdapterPositionListener;

public class ProductListFragment extends Fragment {
   private ProductListAdapter mProductListAdapter;
   private ProductListViewModel mViewModel;
   private RecyclerView mRecyclerView;

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setHasOptionsMenu(true);
   }

   @SuppressWarnings("unchecked")
   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_product_list, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view);
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mViewModel = new ProductListViewModel(new ProductRepository());
      mViewModel.getCatalogList().observe(getViewLifecycleOwner(), (List<Product> list) -> {
         mProductListAdapter.notifyDataSetChanged();
      });
      initRecyclerView();
   }

   @SuppressWarnings("unchecked")
   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      List<Product> list = requireNonNull(mViewModel.getCatalogList().getValue());
      mProductListAdapter = new ProductListAdapter(list, getContext(), new AdapterPositionListener() {
         @Override
         public void onInteractionAdd(Product product) {
            mViewModel.addProductCartToRepository(product).observe(getViewLifecycleOwner(), s -> {});
         }

         @SuppressLint("NotifyDataSetChanged")
         @Override
         public void onInteractionDelete(Product product) {
            mViewModel.deleteProductFromRepository(product).observe(getViewLifecycleOwner(), s -> {
               mProductListAdapter.notifyDataSetChanged();
            });
         }
      });

      mRecyclerView.setAdapter(mProductListAdapter);
      mProductListAdapter.notifyDataSetChanged();
   }

   @Override
   public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
      inflater.inflate(R.menu.search_main, menu);
      MenuItem menuItem = menu.findItem(R.id.action_search);
      SearchView searchView = (SearchView) menuItem.getActionView();
      searchView.setQueryHint("Поиск ...");
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
      super.onCreateOptionsMenu(menu, inflater);
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      int id = item.getItemId();
      if (id == R.id.action_search) {
         return true;
      }
      return super.onOptionsItemSelected(item);
   }

   @SuppressWarnings("unchecked")
   @SuppressLint("NotifyDataSetChanged")
   private void onItemSearch(String searchText) {
      List<Product> searchList = new ArrayList<>();
      List<Product> products = requireNonNull(mViewModel.getCatalogList().getValue());

      for (Product product : products) {
         if (product.getName() != null && !product.getName().isEmpty()) {
            if (product.getName().toLowerCase().contains(searchText.toLowerCase())) {
               searchList.add(product);
            }
         }
      }

      mProductListAdapter = new ProductListAdapter(searchList, getContext(), new AdapterPositionListener() {
         @Override
         public void onInteractionAdd(Product product) {
            mViewModel.addProductCartToRepository(product).observe(getViewLifecycleOwner(), s -> {});
         }
         @Override
         public void onInteractionDelete(Product product) {
            mViewModel.deleteProductFromRepository(product).observe(getViewLifecycleOwner(), s -> {
               mProductListAdapter.notifyDataSetChanged();
            });
         }
      });
      mRecyclerView.setAdapter(mProductListAdapter);
   }
}
