package ru.mobile.beerhoven.presentation.ui.customer.store.details;

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
import ru.mobile.beerhoven.presentation.ui.customer.cart.Cart;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.Toasty;

public class ProductDetailFragment extends Fragment {
   private double mTotalPrice;
   private FragmentProductDetailBinding mBinding;
   private int mValue = 1;
   private String mProductCapacity;
   private String mProductCountry;
   private String mProductDensity;
   private String mProductDescription;
   private String mProductFortress;
   private String mProductId;
   private String mProductImage;
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
         mProductCapacity = args.getCapacity();
         mProductCountry = args.getCountry();
         mProductDensity = args.getDensity();
         mProductDescription = args.getDescription();
         mProductFortress = args.getFortress();
         mProductId = args.getProductId();
         mProductImage = args.getImage();
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
      mBinding.tvProductDetailCountry.setText(mProductCountry);
      mBinding.tvProductDetailDensity.setText(mProductDensity + "%");
      mBinding.tvProductDetailDescription.setText(mProductDescription);
      mBinding.tvProductDetailFortress.setText(mProductFortress + "%");
      mBinding.tvProductDetailName.setText(mProductName);
      mBinding.tvProductCapacity.setText(mProductCapacity);
      mBinding.tvProductDetailPrice.setText(String.valueOf(mProductPrice));
      mBinding.tvProductDetailStyle.setText(mProductStyle);
      mBinding.tvProductDetailTotal.setText(String.valueOf(mTotalPrice));

      Glide.with(mBinding.tvProductDetailImage.getContext())
          .load(mProductImage)
          .into(mBinding.tvProductDetailImage);

      switch (mVisible) {
         case Constants.OBJECT_VISIBLE:
            mBinding.productTotalContainer.setVisibility(View.VISIBLE);
            break;
         case Constants.OBJECT_RENAME:
            mBinding.btnAddProductToCart.setVisibility(View.VISIBLE);
            mBinding.btnAddProductToCart.setText(R.string.product_info_update);
            break;
         case Constants.OBJECT_INVISIBLE:
            mBinding.productTotalContainer.setVisibility(View.INVISIBLE);
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
         if (!Cart.productSet.contains(mProductId)) {
            Cart.productSet.add(mProductId);
            ((MainActivity) requireActivity()).onIncreaseCartCounter();
            Toasty.success(requireActivity(), R.string.cart_add_success);
         }
         double cartPrice = Double.parseDouble(mProductPrice);
         mProductQuantity = String.valueOf(mValue);

         Product product = new Product();
         product.setCapacity(mProductCapacity);
         product.setCategory("null");
         product.setCountry(mProductCountry);
         product.setDescription(mProductDescription);
         product.setDensity(mProductDensity);
         product.setFortress(mProductFortress);
         product.setId(mProductId);
         product.setImage(mProductImage);
         product.setName(mProductName);
         product.setPrice(cartPrice);
         product.setQuantity(mProductQuantity);
         product.setStyle(mProductStyle);
         product.setTotal(mTotalPrice);

         mViewModel.onAddCartProductToRepository(product);
         v.setClickable(false);
      });
   }

   // Product count constrains
   private void onCountListener() {
      mBinding.includeFragmentCounter.tvCounterPlus.setOnClickListener(v -> {
         if (mValue != 15) {
            mValue++;
         }
         onCalculateValue();
      });

      mBinding.includeFragmentCounter.tvCounterMinus.setOnClickListener(v -> {
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
      mBinding.includeFragmentCounter.tvCounterValue.setText(String.valueOf(mValue));
      double cost = (Double.parseDouble(mProductPrice) * mValue);
      mTotalPrice = Math.round(cost * 100.0) / 100.0;
      mBinding.tvProductDetailTotal.setText(String.valueOf(mTotalPrice));
   }
}
