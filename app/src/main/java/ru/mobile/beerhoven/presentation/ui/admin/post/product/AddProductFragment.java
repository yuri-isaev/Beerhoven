package ru.mobile.beerhoven.presentation.ui.admin.post.product;

import static java.util.Objects.requireNonNull;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.ProductRepository;
import ru.mobile.beerhoven.databinding.FragmentAdminAddProductBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.ui.admin.post.PostFragment;
import ru.mobile.beerhoven.utils.Toasty;
import ru.mobile.beerhoven.utils.Validation;

public class AddProductFragment extends PostFragment {
   private Button mAddDatabaseButton;
   private ImageView mAddImageSelector;
   private ImageView mProductImage;
   private TextInputEditText mCategoryInputText;
   private TextInputLayout mCategoryInput;
   private TextInputLayout mCountryInput;
   private TextInputLayout mDensityInput;
   private TextInputLayout mDescriptionInput;
   private TextInputLayout mFortressInput;
   private TextInputLayout mCapacityInput;
   private TextInputLayout mNameInput;
   private TextInputLayout mPriceInput;
   private TextInputLayout mStyleInput;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      FragmentAdminAddProductBinding binding = FragmentAdminAddProductBinding.inflate(inflater, container, false);
      mAddDatabaseButton = binding.btnAddDatabase;
      mCapacityInput = binding.inputProductCapacity;
      mCountryInput = binding.inputProductCountry;
      mDensityInput = binding.inputProductDensity;
      mDescriptionInput = binding.inputProductDescription;
      mFortressInput = binding.inputProductFortress;
      mNameInput = binding.inputProductName;
      mPriceInput = binding.inputProductPrice;
      mStyleInput = binding.inputProductStyle;
      mProductImage = binding.ivProductImage;
      mCategoryInput = binding.inputProductCategory;
      mCategoryInputText = binding.etProductCategory;
      mAddImageSelector = binding.ivSelectorAddImage;
      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      AddProductViewModel viewModel = new AddProductViewModel(new ProductRepository());

      mAddImageSelector.setOnClickListener(v -> super
          .onShowImagePickDialog()
          .observe(getViewLifecycleOwner(), bitmap -> mProductImage.setImageBitmap(bitmap)));

      mCategoryInputText.setOnClickListener(v -> super
          .onShowCategoryPickDialog()
          .observe(getViewLifecycleOwner(), s -> mCategoryInputText.setText(s)));

      mAddDatabaseButton.setOnClickListener(v -> {
         if (  !Validation.isValidTextField(mCapacityInput)
             | !Validation.isValidTextField(mCategoryInput)
             | !Validation.isValidTextField(mCountryInput)
             | !Validation.isValidTextField(mDescriptionInput)
             | !Validation.isValidTextField(mNameInput)
             | !Validation.isValidTextField(mPriceInput)
             | !Validation.isValidTextField(mStyleInput)) {
            Toasty.error(requireActivity(), R.string.invalid_form);
         } else if (super.mUriImage == null) {
            Toasty.error(requireActivity(), R.string.add_image);
         } else {
            String capacity = requireNonNull(mCapacityInput.getEditText()).getText().toString();
            String category = requireNonNull(mCategoryInput.getEditText()).getText().toString();
            String country = requireNonNull(mCountryInput.getEditText()).getText().toString();
            String density = requireNonNull(mDensityInput.getEditText()).getText().toString();
            String description = requireNonNull(mDescriptionInput.getEditText()).getText().toString();
            String fortress = requireNonNull(mFortressInput.getEditText()).getText().toString();
            String name = requireNonNull(mNameInput.getEditText()).getText().toString();
            String price = requireNonNull(mPriceInput.getEditText()).getText().toString();
            String style = requireNonNull(mStyleInput.getEditText()).getText().toString();

            Product product = new Product();
            product.setCapacity(capacity);
            product.setCategory(category);
            product.setCountry(country);
            product.setDensity(density);
            product.setDescription(description);
            product.setFortress(fortress);
            product.setName(name);
            product.setPrice(Double.parseDouble(price));
            product.setStyle(style);
            product.setImage(String.valueOf(super.mUriImage));

            viewModel.onAddProductToRepository(product).observe(getViewLifecycleOwner(), b ->
                Toasty.success(requireActivity(), R.string.product_add_database));
         }
      });
   }
}
