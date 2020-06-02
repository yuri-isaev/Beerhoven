package ru.mobile.beerhoven.presentation.ui.admin.post;

import static android.provider.MediaStore.Images.Media.getBitmap;
import static androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import static java.util.Objects.requireNonNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.io.IOException;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.domain.enums.Category;
import ru.mobile.beerhoven.utils.Permission;

public class PostFragment extends Fragment {
   private Activity mActivity;
   private AlertDialog mAlertDialog;
   private MutableLiveData<Bitmap> mMutableData;
   protected Uri mUriImage;

   @Override
   public void onStart() {
      super.onStart();
      mActivity = requireActivity();
   }

   public final LiveData<Bitmap> onShowImagePickDialog() {
      mMutableData = new MutableLiveData<>();
      AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
      View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_add_image, null);
      builder.setView(view);
      mAlertDialog = builder.create();
      Permission permission = new Permission(mActivity);

      view.findViewById(R.id.btn_add_camera).setOnClickListener(v -> {
         if (!permission.checkCameraPermission()) {
            permission.requestCameraPermission();
            onPickImageFromCamera();
         } else {
            onPickImageFromCamera();
         }
      });

      view.findViewById(R.id.btn_add_gallery).setOnClickListener(v -> {
         if (!permission.checkStoragePermission()) {
            permission.requestStoragePermission();
            onPickImageFromGallery();
         } else {
            onPickImageFromGallery();
         }
      });

      view.findViewById(R.id.btn_cancel_container).setOnClickListener(v -> mAlertDialog.cancel());

      if (mAlertDialog.getWindow() != null) {
         mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
      }

      mAlertDialog.show();
      return mMutableData;
   }

   @NonNull
   public final LiveData<String> onShowCategoryPickDialog() {
      TextView title = new TextView(mActivity);
      title.setGravity(Gravity.CENTER);
      title.setPadding(10, 30, 10,10);
      title.setText(R.string.product_category_select);
      title.setTextColor(Color.WHITE);

      MutableLiveData<String> mutableData = new MutableLiveData<>();
      final String[] categories = Category.getValues();
      
      AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.AlertDialog);
      builder.setCancelable(false);
      builder.setCustomTitle(title);
      builder.setNegativeButton("OK", (dialog, id) -> dialog.cancel());
      builder.setSingleChoiceItems(categories, -1, (dialog, i) -> mutableData.postValue(categories[i]));
      AlertDialog alert = builder.create();
      alert.show();
      return mutableData;
   }

   private void onPickImageFromCamera() {
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      File photo = new File(requireNonNull(mActivity).getExternalFilesDir(null), "test.jpg");
      mUriImage = FileProvider.getUriForFile(mActivity, mActivity.getPackageName() + ".provider", photo);
      intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriImage);
      launchCameraResult.launch(intent);
      mAlertDialog.dismiss();
   }

   private void onPickImageFromGallery() {
      Intent intent = new Intent(Intent.ACTION_PICK);
      intent.setType("image/*");
      launchGalleryResult.launch(intent);
      mAlertDialog.dismiss();
   }

   ActivityResultLauncher<Intent> launchGalleryResult = registerForActivityResult(
       new StartActivityForResult(), result -> {
          if (result.getResultCode() == Activity.RESULT_OK) {
             Intent data = result.getData();
             if (data != null) {
                mUriImage = data.getData();
                try {
                   Bitmap bitmap = getBitmap(mActivity.getContentResolver(), mUriImage);
                   mMutableData.postValue(bitmap);
                } catch (IOException e) {
                   e.printStackTrace();
                }
             }
          }
       });

   ActivityResultLauncher<Intent> launchCameraResult = registerForActivityResult(
       new StartActivityForResult(), result -> {
          if (result.getResultCode() == Activity.RESULT_OK) {
             Bitmap bitmap = BitmapFactory.decodeFile(mActivity.getExternalFilesDir(null) + "/test.jpg");
             mMutableData.postValue(bitmap);
          }
       });
}
