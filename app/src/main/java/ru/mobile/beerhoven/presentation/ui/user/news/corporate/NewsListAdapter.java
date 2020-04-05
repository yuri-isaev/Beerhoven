package ru.mobile.beerhoven.presentation.ui.user.news.corporate;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.databinding.ItemNewsBinding;
import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.utils.CurrentDateTime;

public class NewsListAdapter extends Adapter<NewsListAdapter.NewsListViewHolder> {
   private final List<News> mAdapterList;

   public NewsListAdapter(@NonNull List<News> list) {
      this.mAdapterList = list;
   }

   @NonNull
   @Override
   public NewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemNewsBinding binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new NewsListViewHolder(binding);
   }

   @SuppressLint({"NonConstantResourceId", "SetTextI18n", "NewApi"})
   @Override
   public void onBindViewHolder(@NonNull NewsListViewHolder holder, int position) {
      News post = mAdapterList.get(position);
      String postImage = post.getUri();
      String title = post.getTitle();
      Glide.with(holder.binding.itemNewsImage.getContext()).load(postImage).into(holder.binding.itemNewsImage);
      holder.binding.itemNewsTitle.setText(title);
      String data = CurrentDateTime.parseDataTime(post.getTime());
      holder.binding.itemNewsPublishedAt.setText(data);

      // Set navigate action args
      NewsListFragmentDirections.ActionNavNewsListToNavNewsDetail action = NewsListFragmentDirections
          .actionNavNewsListToNavNewsDetail()
          .setNewsId(post.getId())
          .setNewsTitle(post.getTitle())
          .setNewsDesc(post.getDescription())
          .setNewsDateTime(data)
          .setNewsImage(postImage);

      // Navigate action for click news list card
      holder.binding.itemNewsContainer.setOnClickListener(v -> {
         NavOptions options = new NavOptions.Builder()
             .setLaunchSingleTop(true)
             .setEnterAnim(R.anim.fade_in)
             .setExitAnim(R.anim.fade_out)
             .setPopExitAnim(R.anim.fade_out)
             .build();

         NavController navController = Navigation.findNavController(v);
         navController.navigate(action, options);
      });
   }

   @Override
   public int getItemCount() {
      return mAdapterList.size();
   }

   public static class NewsListViewHolder extends ViewHolder {
      ItemNewsBinding binding;

      public NewsListViewHolder(ItemNewsBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }
   }
}
