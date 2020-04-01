package ru.mobile.beerhoven.data.remote;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IAddProductRepository;
import ru.mobile.beerhoven.utils.Constants;

public class AddProductRepository implements IAddProductRepository {
   private boolean mResponse;
   private final MutableLiveData<Boolean> mMutableData;
   private Uri mUriImage;

   public AddProductRepository() {
      this.mMutableData = new MutableLiveData<>();
   }

   @Override
   public MutableLiveData<Boolean> onGetCameraResponse() {
      if (!mResponse) {
         addPhotoFromCamera();
      }
      mMutableData.setValue(mResponse);
      return mMutableData;
   }

   public void addPhotoFromCamera() {
      mUriImage = MapStorage.uriMap.get("camera");
      mResponse = false;
   }

   @Override
   public MutableLiveData<Boolean> onGetImageResponse() {
      if (!mResponse) {
         getImageFromGallery();
      }
      mMutableData.setValue(mResponse);
      return mMutableData;
   }

   private void getImageFromGallery() {
      mUriImage = MapStorage.uriMap.get("gallery");
      mResponse = false;
   }

   @Override
   public MutableLiveData<Boolean> onGetCreatePostResponse() {
      if (!mResponse) {
         createPost();
      }
      mMutableData.setValue(mResponse);
      return mMutableData;
   }

   public void createPost() {
      StorageReference storageRef = FirebaseStorage.getInstance().getReference();
      StorageReference photoRef = storageRef.child(Constants.FOLDER_PRODUCT_IMG).child(new Date().toString());

      photoRef.putFile(mUriImage).addOnSuccessListener(taskSnapshot -> {
         Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

         while ((!uriTask.isComplete())) ;
         Uri downloadUri = uriTask.getResult();

         Product post = new Product();
         post.setName(MapStorage.productMap.get("name"));
         post.setCountry(MapStorage.productMap.get("country"));
         post.setManufacture(MapStorage.productMap.get("manufacture"));
         post.setStyle(MapStorage.productMap.get("style"));
         post.setFortress(MapStorage.productMap.get("fortress"));
         post.setDensity(MapStorage.productMap.get("density"));
         post.setDescription(MapStorage.productMap.get("description"));
         post.setPrice(Double.parseDouble(MapStorage.productMap.get("price")));
         post.setUrl(downloadUri.toString());

         DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.NODE_PRODUCTS);
         reference.push().setValue(post);
      });
   }
}
