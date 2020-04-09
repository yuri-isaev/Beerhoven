package ru.mobile.beerhoven.presentation.ui.user.news.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import ru.mobile.beerhoven.databinding.FragmentNewsDetailBinding;

public class NewsDetailFragment extends Fragment {
   private FragmentNewsDetailBinding mFragmentBind;
   private String mNewsTitle;
   private String mNewsImage;
   private String mNewsDesc;
   private String mNewsDateTime;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getArguments() != null) {
         NewsDetailFragmentArgs args = NewsDetailFragmentArgs.fromBundle(getArguments());
         mNewsTitle = args.getNewsTitle();
         mNewsImage = args.getNewsImage();
         mNewsDesc = args.getNewsDesc();
         mNewsDateTime = args.getNewsDateTime();
      }
   }

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mFragmentBind = FragmentNewsDetailBinding.inflate(inflater, container, false);
      return mFragmentBind.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mFragmentBind.title.setText(mNewsTitle);
      mFragmentBind.desc.setText(mNewsDesc);
      mFragmentBind.newsPublishedAt.setText(mNewsDateTime);
      Glide.with(mFragmentBind.mealThumb.getContext()).load(mNewsImage).into(mFragmentBind.mealThumb);
   }
}
