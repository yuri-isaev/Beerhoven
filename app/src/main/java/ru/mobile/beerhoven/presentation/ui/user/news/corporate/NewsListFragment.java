package ru.mobile.beerhoven.presentation.ui.user.news.corporate;

import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.NewsRepository;
import ru.mobile.beerhoven.domain.model.News;

public class NewsListFragment extends Fragment {
   private NewsListAdapter mAdapter;
   private NewsListViewModel mViewModel;
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
   public void onActivityCreated(@Nullable Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      mViewModel = new NewsListViewModel(new NewsRepository());
      mViewModel.initNewsList();
      mViewModel.getNewsList().observe(getViewLifecycleOwner(), (List<News> list) -> mAdapter.notifyDataSetChanged());
      initRecyclerView();
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      mAdapter = new NewsListAdapter(requireNonNull(mViewModel.getNewsList().getValue()));
      mRecyclerView.setAdapter(mAdapter);
      mAdapter.notifyDataSetChanged();
   }
}
