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

import com.google.android.material.textfield.TextInputLayout;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.ProductRepository;
import ru.mobile.beerhoven.databinding.FragmentAddProductBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.ui.admin.post.PostFragment;
import ru.mobile.beerhoven.utils.Toasty;
import ru.mobile.beerhoven.utils.Validation;

public class AddProductFragment extends PostFragment {
   private Button mAddDatabaseButton;
   private ImageView mSelectorAddImage;
   private TextInputLayout mInputCountry;
   private TextInputLayout mInputDensity;
   private TextInputLayout mInputDescription;
   private TextInputLayout mInputFortress;
   private TextInputLayout mInputManufacture;
   private TextInputLayout mInputName;
   private TextInputLayout mInputPrice;
   private TextInputLayout mInputStyle;
   private ImageView mProductImage;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      FragmentAddProductBinding binding = FragmentAddProductBinding.inflate(inflater, container, false);
      mAddDatabaseButton = binding.btnAddDatabase;
      mInputCountry = binding.productCountry;
      mInputDensity = binding.productDensity;
      mInputDescription = binding.productDescription;
      mInputFortress = binding.productFortress;
      mInputManufacture = binding.productManufacture;
      mInputName = binding.productName;
      mInputPrice = binding.productPrice;
      mInputStyle = binding.productStyle;
      mProductImage = binding.productImage;
      mSelectorAddImage = binding.selectorAddImage;
      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      AddProductViewModel viewModel = new AddProductViewModel(new ProductRepository());

      mSelectorAddImage.setOnClickListener(v -> super.onShowImagePickDialog()
          .observe(getViewLifecycleOwner(), bitmap -> mProductImage.setImageBitmap(bitmap)));

      mAddDatabaseButton.setOnClickListener(v -> {
         if (!Validation.isValidName(mInputCountry) |
             !Validation.isValidName(mInputDensity) |
             !Validation.isValidName(mInputDescription) |
             !Validation.isValidName(mInputFortress) |
             !Validation.isValidName(mInputManufacture) |
             !Validation.isValidName(mInputName) |
             !Validation.isValidName(mInputStyle)) {
            Toasty.error(requireActivity(), R.string.invalid_form);
         } else if (super.mUriImage == null) {
            Toasty.error(requireActivity(), R.string.add_image);
         } else {
            String country = requireNonNull(mInputCountry.getEditText()).getText().toString();
            String density = requireNonNull(mInputDensity.getEditText()).getText().toString();
            String description = requireNonNull(mInputDescription.getEditText()).getText().toString();
            String fortress = requireNonNull(mInputFortress.getEditText()).getText().toString();
            String manufacture = requireNonNull(mInputManufacture.getEditText()).getText().toString();
            String name = requireNonNull(mInputName.getEditText()).getText().toString();
            String price = requireNonNull(mInputPrice.getEditText()).getText().toString();
            String style = requireNonNull(mInputStyle.getEditText()).getText().toString();

            Product product = new Product();
            product.setCountry(country);
            product.setDensity(density);
            product.setDescription(description);
            product.setFortress(fortress);
            product.setId("null");
            product.setManufacture(manufacture);
            product.setName(name);
            product.setPrice(Double.parseDouble(price));
            product.setStyle(style);
            product.setUri(String.valueOf(super.mUriImage));

            viewModel.onAddProductToRepository(product).observe(getViewLifecycleOwner(),
                b -> Toasty.success(requireActivity(), R.string.product_add_database));
         }
      });
   }
}
