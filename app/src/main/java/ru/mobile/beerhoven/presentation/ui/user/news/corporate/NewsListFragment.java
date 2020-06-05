package ru.mobile.beerhoven.presentation.ui.user.news.corporate;

import static java.util.Objects.requireNonNull;

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
import ru.mobile.beerhoven.data.remote.NewsRepository;
import ru.mobile.beerhoven.domain.model.News;

public class NewsListFragment extends Fragment {
   private NewsListAdapter mAdapter;
   private RecyclerView mRecyclerView;

   @SuppressLint("MissingInflatedId")
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_news_list, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view);
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      NewsListViewModel viewModel = new NewsListViewModel(requireContext(), new NewsRepository());
      viewModel.onDeleteNewsCounterToStorage();
      viewModel.getNewsListFromRepository().observe(getViewLifecycleOwner(),
          list -> mAdapter.notifyDataSetChanged());
      List<News> list = requireNonNull(viewModel.getNewsListFromRepository().getValue());
      initRecyclerView(list);
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView(List<News> list) {
      mRecyclerView.setHasFixedSize(true);
      LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
      layoutManager.setReverseLayout(true);
      layoutManager.setStackFromEnd(true);
      mRecyclerView.setLayoutManager(layoutManager);
      mAdapter = new NewsListAdapter(requireActivity(), requireNonNull(list));
      mRecyclerView.setAdapter(mAdapter);
      mAdapter.notifyDataSetChanged();
   }
}
