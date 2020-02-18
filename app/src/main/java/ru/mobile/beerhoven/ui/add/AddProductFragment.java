package ru.mobile.beerhoven.ui.add;

import static java.util.Objects.*;

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
import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.data.remote.AddProductRepository;
import ru.mobile.beerhoven.databinding.FragmentAddProductBinding;
import ru.mobile.beerhoven.utils.Validation;

public class AddProductFragment extends Fragment {

   // System constants will be used as request code
   public static final int CODE_CAMERA = 21;
   public static final int CODE_GALLERY = 22;

   private Uri mUriImage;
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

      mFragmentBind.btnAddDataBase.setOnClickListener(v -> {
         if (!Validation.isValidNameField(mFragmentBind.addProductName) |
             !Validation.isValidNameField(mFragmentBind.addProductCountry) |
             !Validation.isValidNameField(mFragmentBind.addProductManufacture) |
             !Validation.isValidNameField(mFragmentBind.addProductStyle) |
             !Validation.isValidNameField(mFragmentBind.addProductFortress) |
             !Validation.isValidNameField(mFragmentBind.addProductDensity) |
             !Validation.isValidNameField(mFragmentBind.addProductDescription)) {
            Toasty.error(requireActivity(), "Заполните форму", Toast.LENGTH_LONG, true).show();
         } else if (mUriImage == null) {
            Toasty.error(requireActivity(), "Добавьте изображение", Toast.LENGTH_LONG, true).show();
         } else {
            country = requireNonNull(mFragmentBind.addProductCountry.getEditText()).getText().toString();
            manufacture = requireNonNull(mFragmentBind.addProductManufacture.getEditText()).getText().toString();
            name = requireNonNull(mFragmentBind.addProductName.getEditText()).getText().toString();
            price = requireNonNull(mFragmentBind.addProductPrice.getEditText()).getText().toString();
            style = requireNonNull(mFragmentBind.addProductStyle.getEditText()).getText().toString();
            fortress = requireNonNull(mFragmentBind.addProductFortress.getEditText()).getText().toString();
            density = requireNonNull(mFragmentBind.addProductDensity.getEditText()).getText().toString();
            description = requireNonNull(mFragmentBind.addProductDescription.getEditText()).getText().toString();

            MapStorage.productMap.put("name", name);
            MapStorage.productMap.put("country", country);
            MapStorage.productMap.put("manufacture", manufacture);
            MapStorage.productMap.put("price", price);
            MapStorage.productMap.put("style", style);
            MapStorage.productMap.put("fortress", fortress);
            MapStorage.productMap.put("density", density);
            MapStorage.productMap.put("description", description);

            mAddProductViewModel.getReportAddDataBase().observe(getViewLifecycleOwner(), aBoolean ->
                Toasty.success(requireActivity(), "Товар добавлен в базу данных", Toast.LENGTH_SHORT, true).show());
         }
      });

      mFragmentBind.btnGallery.setOnClickListener(v -> {
         Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
         startActivityForResult(intent, CODE_GALLERY);
      });

      mFragmentBind.btnCamera.setOnClickListener(v -> {
         Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
         File photo = new File(requireNonNull(getActivity()).getExternalFilesDir(null), "test.jpg");

         mUriImage = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", photo);
         intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriImage);

         startActivityForResult(intent, CODE_CAMERA);
         MapStorage.uriMap.put("camera", mUriImage);

         mAddProductViewModel.getReportImageCamera().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
               Toasty.warning(requireActivity(), "Добавьте снимок", Toast.LENGTH_LONG, true).show();
               mAddProductViewModel.getReportImageCamera().removeObserver(this);
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
               mAddProductViewModel.getReportImageGallery().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                  @Override
                  public void onChanged(Boolean aBoolean) {
                     Toasty.success(requireActivity(), "Изображение добавлено", Toast.LENGTH_SHORT, true).show();
                     mAddProductViewModel.getReportImageGallery().removeObserver(this);
                  }
               });
               try {
                  Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireNonNull(getActivity()).getContentResolver(), mUriImage);
                  mFragmentBind.addImage.setImageBitmap(bitmap);
               } catch (IOException e) {
                  e.printStackTrace();
               }
            }
            break;
         case CODE_CAMERA:
            Bitmap bitmap = BitmapFactory.decodeFile(requireNonNull(getActivity()).getExternalFilesDir(null) + "/test.jpg");
            mFragmentBind.addImage.setImageBitmap(bitmap);
            break;
      }
   }
}
