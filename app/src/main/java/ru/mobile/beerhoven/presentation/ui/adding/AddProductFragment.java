package ru.mobile.beerhoven.presentation.ui.adding;

import static android.provider.MediaStore.Images.Media.getBitmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import java.io.File;
import java.io.IOException;

import es.dmoral.toasty.Toasty;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.data.remote.AddProductRepository;
import ru.mobile.beerhoven.databinding.FragmentAddProductBinding;

public class AddProductFragment extends Fragment {
   public static final int CODE_CAMERA = 21;
   public static final int CODE_GALLERY = 22;

   private AddProductViewModel mAddProductViewModel;
   private FragmentAddProductBinding mFragmentBind;
   private String name;
   private String country;
   private String manufacture;
   private String price;
   private String style;
   private String fortress;
   private String density;
   private String description;
   private Uri mUriImage;

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setHasOptionsMenu(true);
   }

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mAddProductViewModel = new AddProductViewModel(new AddProductRepository());
      mFragmentBind = FragmentAddProductBinding.inflate(inflater, container, false);
      return mFragmentBind.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);

//      mFragmentBind.btnAddDatabase.setOnClickListener(v -> {
//         if (Validation.isValidNameField(mFragmentBind.productName) | Validation.isValidNameField(mFragmentBind.productCountry) | Validation.isValidNameField(mFragmentBind.productManufacture) | Validation.isValidNameField(mFragmentBind.productStyle) | Validation.isValidNameField(mFragmentBind.productFortress) | Validation.isValidNameField(mFragmentBind.productDensity) | Validation.isValidNameField(mFragmentBind.productDescription)) {
//            Toasty.error(requireActivity(), R.string.valid_form, Toast.LENGTH_LONG, true).show();
//         } else if (mUriImage == null) {
//            Toasty.error(requireActivity(), R.string.add_image, Toast.LENGTH_LONG, true).show();
//         } else {
//            country = requireNonNull(mFragmentBind.productCountry.getEditText()).getText().toString();
//            manufacture = requireNonNull(mFragmentBind.productManufacture.getEditText()).getText().toString();
//            name = requireNonNull(mFragmentBind.productName.getEditText()).getText().toString();
//            price = requireNonNull(mFragmentBind.productPrice.getEditText()).getText().toString();
//            style = requireNonNull(mFragmentBind.productStyle.getEditText()).getText().toString();
//            fortress = requireNonNull(mFragmentBind.productFortress.getEditText()).getText().toString();
//            density = requireNonNull(mFragmentBind.productDensity.getEditText()).getText().toString();
//            description = requireNonNull(mFragmentBind.productDescription.getEditText()).getText().toString();
//
//            MapStorage.productMap.put("name", name);
//            MapStorage.productMap.put("country", country);
//            MapStorage.productMap.put("manufacture", manufacture);
//            MapStorage.productMap.put("price", price);
//            MapStorage.productMap.put("style", style);
//            MapStorage.productMap.put("fortress", fortress);
//            MapStorage.productMap.put("density", density);
//            MapStorage.productMap.put("description", description);
//
//            mAddProductViewModel.onGetPostResponseToRepository().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//               @Override
//               public void onChanged(Boolean res) {
//                  Toasty.success(AddProductFragment.this.requireActivity(), R.string.product_add_database, Toast.LENGTH_SHORT, true).show();
//               }
//            });
//         }
//      });

      mFragmentBind.btnGallery.setOnClickListener(v -> {
         Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
         startActivityForResult(intent, CODE_GALLERY);
      });

      mFragmentBind.btnCamera.setOnClickListener(v -> {
         Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
         File photo = new File(requireActivity().getExternalFilesDir(null), "test.jpg");

         mUriImage = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", photo);
         intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriImage);

         startActivityForResult(intent, CODE_CAMERA);
         MapStorage.uriMap.put("camera", mUriImage);

         mAddProductViewModel.onGetCameraResponseToRepository().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
               Toasty.warning(requireActivity(), R.string.add_photo, Toast.LENGTH_LONG, true).show();
               mAddProductViewModel.onGetCameraResponseToRepository().removeObserver(this);
            }
         });
      });
   }

   @Override
   public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      // Pop-up menu adding an image
      switch (requestCode) {
         case CODE_GALLERY:
            if (data != null) {
               mUriImage = data.getData();
               MapStorage.uriMap.put("gallery", mUriImage);
               mAddProductViewModel.onGetImageResponseToRepository().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                  @Override
                  public void onChanged(Boolean aBoolean) {
                     Toasty.success(requireActivity(), R.string.add_image_inside, Toast.LENGTH_SHORT, true).show();
                     mAddProductViewModel.onGetImageResponseToRepository().removeObserver(this);
                  }
               });
               try {
                  Bitmap bitmap = getBitmap(requireActivity().getContentResolver(), mUriImage);
                  mFragmentBind.image.setImageBitmap(bitmap);
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
            break;
         case CODE_CAMERA:
            Bitmap bitmap = BitmapFactory.decodeFile(requireActivity().getExternalFilesDir(null) + "/test.jpg");
            mFragmentBind.image.setImageBitmap(bitmap);
            break;
      }
   }
}
