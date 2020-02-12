package ru.mobile.beerhoven.ui.store.catalog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.CatalogRepository;
import ru.mobile.beerhoven.interfaces.InteractionListener;
import ru.mobile.beerhoven.domain.model.Product;

public class CatalogFragment extends Fragment {
   private RecyclerView mRecyclerView;
   private CatalogAdapter mCatalogAdapter;
   private CatalogViewModel mCatalogViewModel;

   @SuppressWarnings("unchecked")
   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_catalog, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view);
      mCatalogViewModel = new CatalogViewModel(new CatalogRepository());

      // Catalog adapter observer
      mCatalogViewModel.getCatalogList().observe(getViewLifecycleOwner(),
          list -> mCatalogAdapter.notifyDataSetChanged());

      initRecyclerView();
      return view;
   }

   @SuppressWarnings("unchecked")
   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

      // Store cart observer
      mCatalogAdapter = new CatalogAdapter((List<Product>) mCatalogViewModel.getCatalogList().getValue(),
          getContext(),
          new InteractionListener() {
             @Override
             public void onInteractionAdd(Product product) {
                mCatalogViewModel.addProductToCart().observe(getViewLifecycleOwner(), s ->
                    Toasty.success(requireActivity(), "Товар добавлен в корзину", Toast.LENGTH_SHORT, true).show());
             }

             @SuppressLint("NotifyDataSetChanged")
             @Override
             public void onInteractionDelete(Product product) {
                mCatalogViewModel.removeProductFromCart().observe(getViewLifecycleOwner(), s -> {
                   Toasty.success(requireActivity(), "Товар удален", Toast.LENGTH_SHORT, true).show();
                   mCatalogAdapter.notifyDataSetChanged();
                });
             }
          }
      );
      mRecyclerView.setAdapter(mCatalogAdapter);
      mCatalogAdapter.notifyDataSetChanged();
   }
}