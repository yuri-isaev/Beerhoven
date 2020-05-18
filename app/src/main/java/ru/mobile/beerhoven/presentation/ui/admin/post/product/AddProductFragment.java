package ru.mobile.beerhoven.presentation.ui.admin.post.product;

import static android.provider.MediaStore.Images.Media.getBitmap;
import static java.util.Objects.requireNonNull;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.ProductRepository;
import ru.mobile.beerhoven.databinding.FragmentAddProductBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.Toasty;
import ru.mobile.beerhoven.utils.Validation;

public class AddProductFragment extends Fragment {
   private Activity mActivity;
   private AlertDialog mAlertDialog;
   private AddProductViewModel mAddProductViewModel;
   private FragmentAddProductBinding mFragmentBind;
   private Button btnAddDatabase;
   private TextInputLayout mInputCountry;
   private TextInputLayout mInputDensity;
   private TextInputLayout mInputDescription;
   private TextInputLayout mInputFortress;
   private TextInputLayout mInputManufacture;
   private TextInputLayout mInputName;
   private TextInputLayout mInputPrice;
   private TextInputLayout mInputStyle;
   private String[] cameraPermissions;
   private String[] storagePermissions;
   private Uri mUriImage;
   private static final String TAG = "AddNewsFragment";

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mFragmentBind = FragmentAddProductBinding.inflate(inflater, container, false);
      btnAddDatabase = mFragmentBind.btnAddDatabase;
      mInputCountry = mFragmentBind.productCountry;
      mInputDensity = mFragmentBind.productDensity;
      mInputDescription = mFragmentBind.productDescription;
      mInputFortress = mFragmentBind.productFortress;
      mInputManufacture = mFragmentBind.productManufacture;
      mInputName = mFragmentBind.productName;
      mInputPrice = mFragmentBind.productPrice;
      mInputStyle = mFragmentBind.productStyle;
      cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
      storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
      return mFragmentBind.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      mAddProductViewModel = new AddProductViewModel(new ProductRepository());

      mFragmentBind.selectorAddImage.setOnClickListener(v -> onShowImagePickDialog());

      btnAddDatabase.setOnClickListener(v -> {
         if (!Validation.isValidName(mInputCountry) |
             !Validation.isValidName(mInputDensity) |
             !Validation.isValidName(mInputDescription) |
             !Validation.isValidName(mInputFortress) |
             !Validation.isValidName(mInputManufacture) |
             !Validation.isValidName(mInputName) |
             !Validation.isValidName(mInputStyle)) {
            Toasty.error(mActivity, R.string.invalid_form);
         } else if (mUriImage == null) {
            Toasty.error(mActivity, R.string.add_image);
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
            product.setUri(String.valueOf(mUriImage));

            mAddProductViewModel.onAddProductToRepository(product).observe(getViewLifecycleOwner(),
                (Boolean res) -> Toasty.success(mActivity, R.string.product_add_database));
         }
      });
   }

   @Override
   public void onStart() {
      super.onStart();
      mActivity = getActivity();
      if (isAdded() && mActivity != null) {
         Log.i(TAG, "activity created");
      }
   }

   private void onShowImagePickDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
      View view = LayoutInflater.from(mActivity).inflate(R.layout.custom_alert_dialog, null);
      builder.setView(view);
      mAlertDialog = builder.create();

      view.findViewById(R.id.btn_add_camera).setOnClickListener(v -> {
         if (!checkCameraPermission()) {
            requestCameraPermission();
            onPickFromCamera();
         } else {
            onPickFromCamera();
         }
      });

      view.findViewById(R.id.btn_add_gallery).setOnClickListener(v -> {
         if (!checkStoragePermission()) {
            requestStoragePermission();
            onPickFromGallery();
         } else {
            onPickFromGallery();
         }
      });

      view.findViewById(R.id.btn_cancel_container).setOnClickListener(v -> mAlertDialog.cancel());

      if (mAlertDialog.getWindow() != null) {
         mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
      }
      mAlertDialog.show();
   }

   private boolean checkStoragePermission() {
      return ContextCompat.checkSelfPermission(requireActivity(),
          Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
   }

   private void requestStoragePermission() {
      ActivityCompat.requestPermissions(mActivity, storagePermissions, Constants.STORAGE_REQUEST_CODE);
   }

   private boolean checkCameraPermission() {
      return ContextCompat.checkSelfPermission(mActivity,
          Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED)
          && ContextCompat.checkSelfPermission(mActivity,
          Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
   }

   private void requestCameraPermission() {
      ActivityCompat.requestPermissions(mActivity, cameraPermissions, Constants.CAMERA_REQUEST_CODE);
   }

   private void onPickFromCamera() {
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      File photo = new File(requireNonNull(mActivity).getExternalFilesDir(null), "test.jpg");
      mUriImage = FileProvider.getUriForFile(mActivity, mActivity.getPackageName() + ".provider", photo);
      intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriImage);
      launchCameraResult.launch(intent);
      mAlertDialog.dismiss();
   }

   private void onPickFromGallery() {
      Intent intent = new Intent(Intent.ACTION_PICK);
      intent.setType("image/*");
      launchGalleryResult.launch(intent);
      mAlertDialog.dismiss();
   }

   ActivityResultLauncher<Intent> launchGalleryResult = registerForActivityResult(
       new ActivityResultContracts.StartActivityForResult(), result -> {
      if (result.getResultCode() == Activity.RESULT_OK) {
         Intent data = result.getData();
         if (data != null) {
            mUriImage = data.getData();
            try {
               Bitmap bitmap = getBitmap(mActivity.getContentResolver(), mUriImage);
               mFragmentBind.productImage.setImageBitmap(bitmap);
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   });

   ActivityResultLauncher<Intent> launchCameraResult = registerForActivityResult(
       new ActivityResultContracts.StartActivityForResult(), result -> {
      if (result.getResultCode() == Activity.RESULT_OK) {
         Bitmap bitmap = BitmapFactory.decodeFile(mActivity.getExternalFilesDir(null) + "/test.jpg");
         mFragmentBind.productImage.setImageBitmap(bitmap);
      }
   });
}
