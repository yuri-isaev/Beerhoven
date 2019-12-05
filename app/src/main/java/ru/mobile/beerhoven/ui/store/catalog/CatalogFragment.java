package ru.mobile.beerhoven.ui.store.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mobile.beerhoven.R;

public class CatalogFragment extends Fragment {

   private RecyclerView mRecyclerView;
   private CatalogAdapter mCatalogAdapter;
   private CatalogViewModel mCatalogViewModel;
   private CatalogRepository mCatalogRepository;

   public CatalogFragment() {}

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.fragment_catalog, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view);

      mCatalogViewModel = new CatalogViewModel(mCatalogRepository);
      mCatalogViewModel.getCatalogList().observe(getViewLifecycleOwner(), list -> mCatalogAdapter.notifyDataSetChanged());

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