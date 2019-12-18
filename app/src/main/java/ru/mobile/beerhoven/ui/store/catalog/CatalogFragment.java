package ru.mobile.beerhoven.ui.store.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.interfaces.InteractionListener;
import ru.mobile.beerhoven.models.Item;

import static java.util.Objects.requireNonNull;

public class CatalogFragment extends Fragment {

   private RecyclerView mRecyclerView;
   private CatalogAdapter mCatalogAdapter;
   private CatalogViewModel mCatalogViewModel;
   private CatalogRepository mCatalogRepository;

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

      // Add on store cart observer.
      mCatalogAdapter = new CatalogAdapter((List<Item>) mCatalogViewModel.getCatalogList().getValue(), getContext(), new InteractionListener() {
         @Override
         public void onInteractionAdd(Item model) {
            mCatalogViewModel.getReportAddCart().observe(getViewLifecycleOwner(), (Observer<String>) s ->
                Toasty.success(requireActivity(), "Товар добавлен в корзину!!", Toast.LENGTH_SHORT, true).show());
         }
         @Override
         public void onInteractionDelete(Item model) {
            mCatalogViewModel.getResponseDeleteItem().observe(getViewLifecycleOwner(), s -> {
               Toasty.success(requireActivity(), "Товар удален!!", Toast.LENGTH_SHORT, true).show();
               mCatalogAdapter.notifyDataSetChanged();
            });
         }
      });
      mRecyclerView.setAdapter(mCatalogAdapter);
      mCatalogAdapter.notifyDataSetChanged();
   }
}