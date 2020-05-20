package ru.mobile.beerhoven.presentation.ui.admin.post;

import static android.provider.MediaStore.Images.Media.getBitmap;
import static java.util.Objects.requireNonNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.io.IOException;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.utils.Permission;

public class PostFragment extends Fragment {
   private Activity mActivity;
   private AlertDialog mAlertDialog;
   //private String mPostImage;
   protected Uri mUriImage;
   private  MutableLiveData<Bitmap> mMutableData;

   private static final String TAG = "PostFragment";


   @Override
   public void onStart() {
      super.onStart();
      mActivity = getActivity();
      if (isAdded() && mActivity != null) {
         Log.i(TAG, "activity created");
      }
   }

   public LiveData<Bitmap> onShowImagePickDialog() {
      mMutableData = new MutableLiveData<>();
      AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
      View view = LayoutInflater.from(mActivity).inflate(R.layout.custom_alert_dialog, null);
      builder.setView(view);
      mAlertDialog = builder.create();
      Permission permission = new Permission(mActivity);

      view.findViewById(R.id.btn_add_camera).setOnClickListener(v -> {
         if (!permission.checkCameraPermission()) {
            permission.requestCameraPermission();
            onPickFromCamera();
         } else {
            onPickFromCamera();
         }
      });

      view.findViewById(R.id.btn_add_gallery).setOnClickListener(v -> {
         if (!permission.checkStoragePermission()) {
            permission.requestStoragePermission();
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
      return mMutableData;
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
                   mMutableData.postValue(bitmap);
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
             mMutableData.postValue(bitmap);
          }
       });
}
