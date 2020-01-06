package ru.mobile.beerhoven.ui.store.details;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import ru.mobile.beerhoven.databinding.FragmentDetailsBinding;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.HashMapRepository;

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
   private String quantity;
   private String change;
   private double total;
   private FragmentDetailsBinding fragmentBind;
   private DetailsViewModel mDetailsViewModel;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mDetailsViewModel = new ViewModelProvider(requireActivity(), getDefaultViewModelProviderFactory())
          .get(DetailsViewModel.class);
      fragmentBind = FragmentDetailsBinding.inflate(inflater, container, false);
      countListener();
      addCartList();
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

         change = args.getChange();
         switch (change) {
            case Constants.OBJECT_VISIBLE:
               fragmentBind.addCartButton.setVisibility(View.VISIBLE);
               break;
            case Constants.OBJECT_RENAME:
               fragmentBind.addCartButton.setVisibility(View.VISIBLE);
               fragmentBind.addCartButton.setText("Обновить данные товара");
               break;
            case Constants.OBJECT_INVISIBLE:
               fragmentBind.addCartButton.setVisibility(View.INVISIBLE);
               break;
         }
      }
   }

   // Adding an instance of a product to the cart
   @SuppressLint({"NewApi", "LocalSuppress", "SimpleDateFormat"})
   public void addCartList() {
      fragmentBind.addCartButton.setOnClickListener(v -> {
         String saveCurrentDate, saveCurrentTime;
         Calendar calForDate = Calendar.getInstance();
         SimpleDateFormat currentDate = new SimpleDateFormat(Constants.CURRENT_DATA);
         saveCurrentDate = currentDate.format(calForDate.getTime());
         SimpleDateFormat currentTime = new SimpleDateFormat(Constants.CURRENT_TIME);
         saveCurrentTime = currentTime.format(calForDate.getTime());
         double cartPrice = Double.parseDouble(price);
         quantity = String.valueOf(mValue);

         HashMapRepository.idMap.put("details_id", itemID);
         HashMapRepository.priceMap.put("details_total", total);
         HashMapRepository.priceMap.put("details_price", cartPrice);

         HashMap<String, String> map = HashMapRepository.detailsMap;
         map.put("name", name);
         map.put("country", country);
         map.put("manufacture", manufacture);
         map.put("style", style);
         map.put("fortress", fortress);
         map.put("density", density);
         map.put("description", description);
         map.put("data", saveCurrentDate);
         map.put("time", saveCurrentTime);
         map.put("url", image);
         map.put("quantity", quantity);

         // Details view model observer
         mDetailsViewModel.addItemOrder().observe(getViewLifecycleOwner(), s ->
             Toasty.success(requireActivity(), "Товар добавлен в корзину", Toast.LENGTH_SHORT, true).show());

         v.setClickable(false);
      });
   }

   /**
    * Use case
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
