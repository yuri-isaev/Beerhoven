package ru.mobile.beerhoven.presentation.ui.user.news.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import ru.mobile.beerhoven.databinding.FragmentNewsDetailBinding;

public class NewsDetailFragment extends Fragment {
   private String mNewsDateTime;
   private String mNewsDesc;
   private String mNewsImage;
   private String mNewsTitle;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getArguments() != null) {
         NewsDetailFragmentArgs args = NewsDetailFragmentArgs.fromBundle(getArguments());
         mNewsDateTime = args.getNewsDateTime();
         mNewsDesc = args.getNewsDesc();
         mNewsImage = args.getNewsImage();
         mNewsTitle = args.getNewsTitle();
      }
   }

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      FragmentNewsDetailBinding binding = FragmentNewsDetailBinding
          .inflate(inflater, container, false);
      
      binding.tvNewsTitle.setText(mNewsTitle);
      binding.tvNewsDesc.setText(mNewsDesc);
      binding.tvDate.setText(mNewsDateTime);

      Glide.with(binding.image.getContext())
          .load(mNewsImage)
          .into(binding.image);

      return binding.getRoot();
   }
}
