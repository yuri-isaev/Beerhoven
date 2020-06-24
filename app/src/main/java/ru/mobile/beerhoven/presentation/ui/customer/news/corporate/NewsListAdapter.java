package ru.mobile.beerhoven.presentation.ui.customer.news.corporate;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.mobile.beerhoven.databinding.ItemNewsBinding;
import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.utils.CurrentDateTime;

public class NewsListAdapter extends Adapter<NewsListAdapter.NewsListViewHolder> {
   private final FragmentActivity mActivity;
   private final List<News> mAdapterList;

   public NewsListAdapter(FragmentActivity activity, @NonNull List<News> list) {
      this.mActivity = activity;
      this.mAdapterList = list;
   }

   @NonNull
   @Override
   public NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemNewsBinding binding = ItemNewsBinding
          .inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new NewsListViewHolder(binding);
   }

   @Override
   public void onBindViewHolder(@NonNull NewsListViewHolder holder, int position) {
      News post = mAdapterList.get(position);
      String postDescription = post.getDescription();
      String postId = post.getId();
      String postImage = post.getImage();
      String postTitle = post.getTitle();

      Glide.with(holder.binding.ivNewsImage.getContext())
          .load(postImage)
          .into(holder.binding.ivNewsImage);

      String postDateTime = CurrentDateTime.parseDateTime(post.getTime());

      holder.binding.tvNewsTitle.setText(postTitle);
      holder.binding.tvNewsPublishedAt.setText(postDateTime);
      holder.binding.newsContainer.setOnClickListener(v -> {
         NavDirections action = NewsListFragmentDirections.actionNavNewsListToNavNewsDetail()
             .setNewsDateTime(postDateTime)
             .setNewsDesc(postDescription)
             .setNewsId(postId)
             .setNewsImage(postImage)
             .setNewsTitle(postTitle);
            ((MainActivity) mActivity).onDecreaseNewsCounter();
         NavController navController = Navigation.findNavController(v);
         navController.navigate(action);
      });
   }

   @Override
   public int getItemCount() {
      return mAdapterList.size();
   }

   public static class NewsListViewHolder extends ViewHolder {
      ItemNewsBinding binding;

      public NewsListViewHolder(@NonNull ItemNewsBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }
   }
}
