package ru.mobile.beerhoven.presentation.ui.user.store.details;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import ru.mobile.beerhoven.data.remote.ProductRepository;
import ru.mobile.beerhoven.databinding.FragmentProductDetailBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.presentation.ui.user.cart.CartSet;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.Toasty;

public class ProductDetailFragment extends Fragment {
   private double total;
   private FragmentProductDetailBinding mBinding;
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
   private String visible;
   private ProductDetailViewModel mViewModel;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getActivity() != null) {
         assert getArguments() != null;
         ProductDetailFragmentArgs args = ProductDetailFragmentArgs.fromBundle(getArguments());
         country = args.getCountry();
         density = args.getDensity();
         description = args.getDescription();
         fortress = args.getFortress();
         image = args.getImage();
         manufacture = args.getManufacture();
         name = args.getName();
         price = args.getPrice();
         productId = args.getProductId();
         style = args.getStyle();
         total = Double.parseDouble(args.getPrice());
         visible = args.getVisible();
      }
   }

   @SuppressLint({"NewApi", "SetTextI18n"})
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mBinding = FragmentProductDetailBinding.inflate(inflater, container, false);
      mBinding.productCountry.setText(country);
      mBinding.productDensity.setText(density + "%");
      mBinding.productDescription.setText(description);
      mBinding.productFortress.setText(fortress + "%");
      mBinding.productName.setText(name);
      mBinding.productManufacture.setText(manufacture);
      mBinding.productPrice.setText(String.valueOf(price));
      mBinding.productStyle.setText(style);
      mBinding.productTotal.setText(String.valueOf(total));

      Glide.with(mBinding.productImage.getContext()).load(image).into(mBinding.productImage);

      switch (visible) {
         case Constants.OBJECT_VISIBLE:
            mBinding.btnContainer.setVisibility(View.VISIBLE);
            break;
         case Constants.OBJECT_RENAME:
            mBinding.btnAddProductToCart.setVisibility(View.VISIBLE);
            mBinding.btnAddProductToCart.setText("Обновить данные товара");
            break;
         case Constants.OBJECT_INVISIBLE:
            mBinding.btnContainer.setVisibility(View.INVISIBLE);
            break;
      }
      return mBinding.getRoot();
   }

   @SuppressLint({"SetTextI18n", "NewApi"})
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mViewModel = new ProductDetailViewModel(new ProductRepository());
      countListener();
      addProductToCart();
   }

   @RequiresApi(api = Build.VERSION_CODES.N)
   public void addProductToCart() {
      mBinding.btnAddProductToCart.setOnClickListener(v -> {
         if (!CartSet.cartProducts.contains(productId)) {
            CartSet.cartProducts.add(productId);
            ((MainActivity) requireActivity()).onIncreaseCartCounter();
            Toasty.success(requireActivity(), "Товар добавлен в корзину");
         }
         double cartPrice = Double.parseDouble(price);
         quantity = String.valueOf(mValue);

         Product product = new Product(productId, country, description, density, fortress,
             manufacture, name, cartPrice, total, quantity, style, image);

         mViewModel.onAddCartProductToRepository(product);

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
