package ru.mobile.beerhoven.ui.store.details;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import ru.mobile.beerhoven.databinding.FragmentDetailsBinding;

public class DetailsFragment extends Fragment {
   private int mValue = 1;
   private String itemID;
   private String name;
   private String country;
   private String manufacture;
   private String style;
   private String fortress;
   private String density;
   private String description;
   private String image;
   private String price;
   private double total;
   private FragmentDetailsBinding fragmentBind;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      DetailsViewModel mDetailsViewModel = new ViewModelProvider(requireActivity(), getDefaultViewModelProviderFactory()).get(DetailsViewModel.class);
      fragmentBind = FragmentDetailsBinding.inflate(inflater, container, false);
      countListener();
      return fragmentBind.getRoot();
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      if (getActivity() != null) {
         assert getArguments() != null;
         DetailsFragmentArgs args = DetailsFragmentArgs.fromBundle(getArguments());
         itemID = args.getItemID();
         name = args.getName();
         country = args.getCountry();
         manufacture = args.getManufacture();
         price = args.getPrice();
         total = Double.parseDouble(args.getPrice());
         style = args.getStyle();
         fortress = args.getFortress();
         density = args.getDensity();
         description = args.getDescription();
         image = args.getImage();

         fragmentBind.tvProductName.setText(name);
         fragmentBind.tvCountry.setText(country);
         fragmentBind.tvManufacture.setText(manufacture);
         fragmentBind.tvPrice.setText(String.valueOf(price));
         fragmentBind.tvTotalAmount.setText(String.valueOf(total));
         fragmentBind.tvStyle.setText(style);
         fragmentBind.tvFortrDes.setText(fortress + "%");
         fragmentBind.tvDensityDes.setText(density + "%");
         fragmentBind.tvFullDescription.setText(description);

         Glide.with(fragmentBind.tvItemFrame.getContext()).load(args.getImage()).into(fragmentBind.tvItemFrame);
      }
   }

   /**
    * Price calculator
    */
   private void countListener() {
      fragmentBind.includeFragmentCounter.iCounterPlus.setOnClickListener(v -> {
         if (mValue != 15) {
            mValue++;
         }
         updateValue();
      });

      fragmentBind.includeFragmentCounter.iCounterMinus.setOnClickListener(v -> {
         if (mValue <= 1) {
            mValue = 1;
         } else {
            mValue--;
         }
         updateValue();
      });
   }

   private void updateValue() {
      fragmentBind.includeFragmentCounter.iCounterValue.setText(String.valueOf(mValue));
      double cost = (Double.parseDouble(price) * mValue);
      total = Math.round(cost * 100.0) / 100.0;
      fragmentBind.tvTotalAmount.setText(String.valueOf(total));
   }
}
