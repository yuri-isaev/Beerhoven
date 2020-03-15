package ru.mobile.beerhoven.presentation.ui.store.details;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.databinding.FragmentDetailsBinding;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.CurrentDateTime;

public class ProductDetailFragment extends Fragment {
   private double total;
   private FragmentDetailsBinding mFragmentBind;
   private int mValue = 1;
   private String productId;
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
   private ProductDetailViewModel mViewModel;

   @SuppressLint("NewApi")
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
      mFragmentBind = FragmentDetailsBinding.inflate(inflater, container, false);
      countListener();
      addProductToCartList();
      return mFragmentBind.getRoot();
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

      if (getActivity() != null) {
         assert getArguments() != null;
         ProductDetailFragmentArgs args = ProductDetailFragmentArgs.fromBundle(getArguments());
         productId = args.getProductID();
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

         mFragmentBind.productName.setText(name);
         mFragmentBind.productCountry.setText(country);
         mFragmentBind.productManufacture.setText(manufacture);
         mFragmentBind.productPrice.setText(String.valueOf(price));
         mFragmentBind.productTotal.setText(String.valueOf(total));
         mFragmentBind.productStyle.setText(style);
         mFragmentBind.productFortress.setText(fortress + "%");
         mFragmentBind.productDensity.setText(density + "%");
         mFragmentBind.productDescription.setText(description);

         Glide.with(mFragmentBind.productItemFrame.getContext())
             .load(args.getImage())
             .into(mFragmentBind.productItemFrame);

         String change = args.getChange();
         switch (change) {
            case Constants.OBJECT_VISIBLE:
               mFragmentBind.btnAddProductToCart.setVisibility(View.VISIBLE);
               break;
            case Constants.OBJECT_RENAME:
               mFragmentBind.btnAddProductToCart.setVisibility(View.VISIBLE);
               mFragmentBind.btnAddProductToCart.setText("Обновить данные товара");
               break;
            case Constants.OBJECT_INVISIBLE:
               mFragmentBind.btnAddProductToCart.setVisibility(View.INVISIBLE);
               break;
         }
      }
   }

   // Adding an instance of a product to the cart
   @RequiresApi(api = Build.VERSION_CODES.N)
   public void addProductToCartList() {
      mFragmentBind.btnAddProductToCart.setOnClickListener(v -> {
         // Counter value control
         if (!MapStorage.productMap.containsValue(productId)) {
            ((MainActivity) requireActivity()).onIncreaseCounterClick();
            MapStorage.productMap.put("productID", productId);
         }

         double cartPrice = Double.parseDouble(price);
         quantity = String.valueOf(mValue);

         assert total != 0;
         MapStorage.priceMap.put("total", total);

         assert cartPrice != 0;
         MapStorage.priceMap.put("price", cartPrice);

         HashMap<String, String> map = MapStorage.productMap;
         map.put("name", name);
         map.put("country", country);
         map.put("manufacture", manufacture);
         map.put("style", style);
         map.put("fortress", fortress);
         map.put("density", density);
         map.put("description", description);
         map.put("data", CurrentDateTime.getCurrentDate());
         map.put("time", CurrentDateTime.getCurrentTime());
         map.put("url", image);
         map.put("quantity", quantity);

         // Details view model observer
         mViewModel.addCartProductToRepository().observe(getViewLifecycleOwner(), s ->
             Toasty.success(requireActivity(), R.string.cart_add_success, Toast.LENGTH_SHORT, true).show());

         v.setClickable(false);
      });
   }

   // Product count constrains
   private void countListener() {
      mFragmentBind.includeFragmentCounter.iCounterPlus.setOnClickListener(v -> {
         if (mValue != 15) {
            mValue++;
         }
         calculateValue();
      });

      mFragmentBind.includeFragmentCounter.iCounterMinus.setOnClickListener(v -> {
         if (mValue <= 1) {
            mValue = 1;
         } else {
            mValue--;
         }
         calculateValue();
      });
   }

   // Calculate product total
   private void calculateValue() {
      mFragmentBind.includeFragmentCounter.iCounterValue.setText(String.valueOf(mValue));
      double cost = (Double.parseDouble(price) * mValue);
      total = Math.round(cost * 100.0) / 100.0;
      mFragmentBind.productTotal.setText(String.valueOf(total));
   }
}
