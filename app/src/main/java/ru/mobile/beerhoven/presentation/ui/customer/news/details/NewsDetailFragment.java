package ru.mobile.beerhoven.presentation.ui.customer.news.details;

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
      
      binding.tvNewsDetailTitle.setText(mNewsTitle);
      binding.tvNewsDetailDesc.setText(mNewsDesc);
      binding.tvNewsDetailDate.setText(mNewsDateTime);

      Glide.with(binding.ivNewsDetailImage.getContext())
          .load(mNewsImage)
          .into(binding.ivNewsDetailImage);

      return binding.getRoot();
   }
}
