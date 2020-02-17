package ru.mobile.beerhoven.data.remote;

import android.net.Uri;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IAddProductRepository;
import ru.mobile.beerhoven.utils.Constants;

public class AddProductRepository implements IAddProductRepository {
   private boolean res;
   private Uri uriImage;
   private final MutableLiveData<Boolean> value = new MutableLiveData<>();

   @Override
   public MutableLiveData<Boolean> getListImageCamera() {
      if (!res) {
         addImageFromCamera();
      }
      value.setValue(res);
      return value;
   }

   public void addImageFromCamera() {
      uriImage = MapStorage.uriMap.get("camera");
      res = false;
   }

   @Override
   public MutableLiveData<Boolean> getListImageGallery() {
      if (!res) {
         addImageFromGallery();
      }
      value.setValue(res);
      return value;
   }

   private void addImageFromGallery() {
      uriImage = MapStorage.uriMap.get("gallery");
      res = false;
   }

   @Override
   public MutableLiveData<Boolean> getListDataBase() {
      if (!res) {
         createPost();
      }
      value.setValue(res);
      return value;
   }

   public void createPost() {
      StorageReference storageRef = FirebaseStorage.getInstance().getReference();
      StorageReference folderRef = storageRef.child(Constants.PRODUCT_IMG);
      StorageReference photoRef = folderRef.child(new Date().toString());

      photoRef.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
         @Override
         public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

            while ((!uriTask.isComplete()));
            Uri downloadUri = uriTask.getResult();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference(Constants.NODE_PRODUCTS);

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
            reference.push().setValue(post);
         }
      });
   }
}
