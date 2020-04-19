package ru.mobile.beerhoven.presentation.ui.user.store.details;

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
import ru.mobile.beerhoven.databinding.FragmentProductDetailBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.CurrentDateTime;

public class ProductDetailFragment extends Fragment {
   private double total;
   private FragmentProductDetailBinding mBinding;
   private int mValue = 1;
   private String id;
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
   private String visible;
   private ProductDetailViewModel mViewModel;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getActivity() != null) {
         assert getArguments() != null;
         ProductDetailFragmentArgs args = ProductDetailFragmentArgs.fromBundle(getArguments());
         id = args.getProductId();
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
         visible = args.getChange();
      }
   }

   @SuppressLint({"NewApi", "SetTextI18n"})
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mBinding = FragmentProductDetailBinding.inflate(inflater, container, false);
      mBinding.productName.setText(name);
      mBinding.productCountry.setText(country);
      mBinding.productManufacture.setText(manufacture);
      mBinding.productPrice.setText(String.valueOf(price));
      mBinding.productTotal.setText(String.valueOf(total));
      mBinding.productStyle.setText(style);
      mBinding.productFortress.setText(fortress + "%");
      mBinding.productDensity.setText(density + "%");
      mBinding.productDescription.setText(description);
      Glide.with(mBinding.productItemFrame.getContext()).load(image).into(mBinding.productItemFrame);

      switch (visible) {
         case Constants.OBJECT_VISIBLE:
            mBinding.btnAddProductToCart.setVisibility(View.VISIBLE);
            break;
         case Constants.OBJECT_RENAME:
            mBinding.btnAddProductToCart.setVisibility(View.VISIBLE);
            mBinding.btnAddProductToCart.setText("Обновить данные товара");
            break;
         case Constants.OBJECT_INVISIBLE:
            mBinding.btnAddProductToCart.setVisibility(View.INVISIBLE);
            break;
      }
      return mBinding.getRoot();
   }

   @SuppressLint({"SetTextI18n", "NewApi"})
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
      countListener();
      addProductToCart();
      }

   @RequiresApi(api = Build.VERSION_CODES.N)
   public void addProductToCart() {
      mBinding.btnAddProductToCart.setOnClickListener(v -> {
         if (!MapStorage.productMap.containsValue(id)) {
            MapStorage.productMap.put("productId", id);
            ((MainActivity) requireActivity()).onIncreaseCartCounter();
         }

         double cartPrice = Double.parseDouble(price);
         quantity = String.valueOf(mValue);
         HashMap<String, String> map = MapStorage.productMap;
         map.put("date", CurrentDateTime.getCurrentDate());
         map.put("time", CurrentDateTime.getCurrentTime());

         Product product = new Product(id, country, description, density, fortress, manufacture, name, cartPrice, total, quantity, style, image);

         mViewModel.addCartProductToRepository(product).observe(getViewLifecycleOwner(), s ->
             Toasty.success(requireActivity(), R.string.cart_add_success, Toast.LENGTH_SHORT, true).show());

         v.setClickable(false);
      });
   }

   // Product count constrains
   private void countListener() {
      mBinding.includeFragmentCounter.iCounterPlus.setOnClickListener(v -> {
         if (mValue != 15) {
            mValue++;
         }
         calculateValue();
      });

      mBinding.includeFragmentCounter.iCounterMinus.setOnClickListener(v -> {
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
      mBinding.includeFragmentCounter.iCounterValue.setText(String.valueOf(mValue));
      double cost = (Double.parseDouble(price) * mValue);
      total = Math.round(cost * 100.0) / 100.0;
      mBinding.productTotal.setText(String.valueOf(total));
   }
}
