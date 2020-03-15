package ru.mobile.beerhoven.presentation.ui.store.catalog;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.CatalogRepository;
import ru.mobile.beerhoven.presentation.interfaces.InteractionListener;
import ru.mobile.beerhoven.domain.model.Product;

public class CatalogFragment extends Fragment {
   private RecyclerView mRecyclerView;
   private CatalogAdapter mCatalogAdapter;
   private CatalogViewModel mViewModel;

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setHasOptionsMenu(true);
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
      mViewModel = new CatalogViewModel(new CatalogRepository());

      View view = inflater.inflate(R.layout.fragment_catalog, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view);

      // Catalog adapter observer
      mViewModel.getCatalogList().observe(getViewLifecycleOwner(), res ->
          mCatalogAdapter.notifyDataSetChanged());

      initRecyclerView();

      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

      // Store cart observer
      mCatalogAdapter = new CatalogAdapter((List<Product>) mViewModel.getCatalogList().getValue(),
          getContext(),
          new InteractionListener() {
         @Override
         public void onInteractionAdd(Product product) {
            mViewModel.addProductToCartToRepository().observe(getViewLifecycleOwner(), s ->
                Toasty.success(requireActivity(), R.string.product_add_cart, Toast.LENGTH_SHORT, true).show());
         }

         @SuppressLint("NotifyDataSetChanged")
         @Override
         public void onInteractionDelete(Product product) {
            mViewModel.removeProductFromCartToRepository().observe(getViewLifecycleOwner(), s -> {
               Toasty.success(requireActivity(), R.string.product_catalog_delete, Toast.LENGTH_SHORT, true).show();
               mCatalogAdapter.notifyDataSetChanged();
            });
         }
      });
      mRecyclerView.setAdapter(mCatalogAdapter);
      mCatalogAdapter.notifyDataSetChanged();
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

   @SuppressLint("NotifyDataSetChanged")
   private void onItemSearch(String searchText) {
      List<Product> searchList = new ArrayList<>();
      List<Product> products = (List<Product>) requireNonNull(mViewModel.getCatalogList().getValue());

      for (Product product : products) {
         if (product.getName() != null && !product.getName().isEmpty())
            if (product.getName().toLowerCase().contains(searchText.toLowerCase())) {
               searchList.add(product);
            }
      }

      mCatalogAdapter = new CatalogAdapter(searchList, getContext(), new InteractionListener() {
         @Override
         public void onInteractionDelete(Product model) {
            mViewModel.removeProductFromCartToRepository().observe(getViewLifecycleOwner(), s -> {
               Toasty.success(requireActivity(), R.string.product_catalog_delete, Toast.LENGTH_SHORT, true).show();
               mCatalogAdapter.notifyDataSetChanged();
            });
         }

         @Override
         public void onInteractionAdd(Product model) {
            mViewModel.addProductToCartToRepository().observe(getViewLifecycleOwner(), s ->
               Toasty.success(requireActivity(), R.string.product_add_cart, Toast.LENGTH_SHORT, true).show());
         }
      });
      mRecyclerView.setAdapter(mCatalogAdapter);
   }
}
