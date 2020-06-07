package ru.mobile.beerhoven.presentation.ui.user.store.details;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.ProductRepository;
import ru.mobile.beerhoven.databinding.FragmentProductDetailBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.presentation.ui.user.cart.CartSet;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.Toasty;

public class ProductDetailFragment extends Fragment {
   private double mTotalPrice;
   private FragmentProductDetailBinding mBinding;
   private int mValue = 1;
   private String mProductCountry;
   private String mProductDensity;
   private String mProductDescription;
   private String mProductFortress;
   private String mProductId;
   private String mProductImage;
   private String mProductManufacture;
   private String mProductName;
   private String mProductPrice;
   private String mProductQuantity;
   private String mProductStyle;
   private String mVisible;
   private ProductDetailViewModel mViewModel;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (getActivity() != null) {
         assert getArguments() != null;
         ProductDetailFragmentArgs args = ProductDetailFragmentArgs.fromBundle(getArguments());
         mProductCountry = args.getCountry();
         mProductDensity = args.getDensity();
         mProductDescription = args.getDescription();
         mProductFortress = args.getFortress();
         mProductId = args.getProductId();
         mProductImage = args.getImage();
         mProductManufacture = args.getManufacture();
         mProductName = args.getName();
         mProductPrice = args.getPrice();
         mProductStyle = args.getStyle();
         mTotalPrice = Double.parseDouble(args.getPrice());
         mVisible = args.getVisible();
      }
   }

   @SuppressLint("SetTextI18n")
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mBinding = FragmentProductDetailBinding.inflate(inflater, container, false);
      mBinding.tvProductCountry.setText(mProductCountry);
      mBinding.tvProductDensity.setText(mProductDensity + "%");
      mBinding.tvProductDescription.setText(mProductDescription);
      mBinding.tvProductFortress.setText(mProductFortress + "%");
      mBinding.tvProductName.setText(mProductName);
      mBinding.tvProductManufacture.setText(mProductManufacture);
      mBinding.tvProductPrice.setText(String.valueOf(mProductPrice));
      mBinding.tvProductStyle.setText(mProductStyle);
      mBinding.tvProductTotal.setText(String.valueOf(mTotalPrice));

      Glide.with(mBinding.tvProductImage.getContext())
          .load(mProductImage)
          .into(mBinding.tvProductImage);

      switch (mVisible) {
         case Constants.OBJECT_VISIBLE:
            mBinding.btnContainer.setVisibility(View.VISIBLE);
            break;
         case Constants.OBJECT_RENAME:
            mBinding.btnAddProductToCart.setVisibility(View.VISIBLE);
            mBinding.btnAddProductToCart.setText(R.string.product_info_update);
            break;
         case Constants.OBJECT_INVISIBLE:
            mBinding.btnContainer.setVisibility(View.INVISIBLE);
            break;
      }
      return mBinding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mViewModel = new ProductDetailViewModel(new ProductRepository());
      onCountListener();
      onAddProductToCart();
   }

   public void onAddProductToCart() {
      mBinding.btnAddProductToCart.setOnClickListener(v -> {
         if (!CartSet.cartProducts.contains(mProductId)) {
            CartSet.cartProducts.add(mProductId);
            ((MainActivity) requireActivity()).onIncreaseCartCounter();
            Toasty.success(requireActivity(), R.string.cart_add_success);
         }
         double cartPrice = Double.parseDouble(mProductPrice);
         mProductQuantity = String.valueOf(mValue);

         Product product = new Product();
         product.setCategory("null");
         product.setCountry(mProductCountry);
         product.setDescription(mProductDescription);
         product.setDensity(mProductDensity);
         product.setFortress(mProductFortress);
         product.setId(mProductId);
         product.setManufacture(mProductManufacture);
         product.setName(mProductName);
         product.setPrice(cartPrice);
         product.setQuantity(mProductQuantity);
         product.setStyle(mProductStyle);
         product.setTotal(mTotalPrice);
         product.setImage(mProductImage);

         mViewModel.onAddCartProductToRepository(product);
         v.setClickable(false);
      });
   }

   // Product count constrains
   private void onCountListener() {
      mBinding.includeFragmentCounter.iCounterPlus.setOnClickListener(v -> {
         if (mValue != 15) {
            mValue++;
         }
         onCalculateValue();
      });

      mBinding.includeFragmentCounter.iCounterMinus.setOnClickListener(v -> {
         if (mValue <= 1) {
            mValue = 1;
         } else {
            mValue--;
         }
         onCalculateValue();
      });
   }

   // Calculate product total
   private void onCalculateValue() {
      mBinding.includeFragmentCounter.iCounterValue.setText(String.valueOf(mValue));
      double cost = (Double.parseDouble(mProductPrice) * mValue);
      mTotalPrice = Math.round(cost * 100.0) / 100.0;
      mBinding.tvProductTotal.setText(String.valueOf(mTotalPrice));
   }
}
