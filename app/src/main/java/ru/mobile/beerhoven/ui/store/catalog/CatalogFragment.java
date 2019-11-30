package ru.mobile.beerhoven.ui.store.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.models.Item;

public class CatalogFragment extends Fragment {

   private RecyclerView mRecyclerView;
   private CatalogAdapter mCatalogAdapter;
   private CatalogViewModel mCatalogViewModel;

   public CatalogFragment() {}

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
      mCatalogViewModel = new ViewModelProvider(this).get(CatalogViewModel.class);

      View view = inflater.inflate(R.layout.fragment_catalog, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view);

      mCatalogViewModel.initList();

      mCatalogViewModel.getCatalogList().observe(getViewLifecycleOwner(), new Observer<List<Item>>() {
         @Override
         public void onChanged(List<Item> list) {
            mCatalogAdapter.notifyDataSetChanged();
         }
      });

      initRecyclerView();
      return view;
   }

   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      mCatalogAdapter = new CatalogAdapter(mCatalogViewModel.getCatalogList().getValue());
      mRecyclerView.setAdapter(mCatalogAdapter);
      mCatalogAdapter.notifyDataSetChanged();
   }
}