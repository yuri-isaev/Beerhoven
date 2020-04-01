package ru.mobile.beerhoven.data.remote;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.repository.INewsRepository;
import ru.mobile.beerhoven.utils.Constants;

public class NewsRepository implements INewsRepository {
   private final DatabaseReference mDatabaseRef;
   private final StorageReference mStorageRef;
   private static final String TAG = "NewsRepository";

   public NewsRepository() {
      this.mDatabaseRef = FirebaseDatabase.getInstance().getReference();
      this.mStorageRef = FirebaseStorage.getInstance().getReference();
   }

   @Override
   public void addNewsDataToDatabase(News model) {
      model.setUri(Constants.URI_LOGO);
      mDatabaseRef.child(Constants.NODE_NEWS).push().setValue(model)
          .addOnSuccessListener(unused -> Log.i(TAG, "News data added to database"))
          .addOnFailureListener(e -> Log.i(TAG, e.getMessage()));
   }

   @Override
   public void addNewsPostToDatabase(News model) {
      mStorageRef.child(Constants.FOLDER_NEWS_IMG).child(new Date().toString())
          .putFile(Uri.parse(model.getUri()))
          .addOnSuccessListener((taskSnapshot) -> {
             Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
             while (!uriTask.isComplete());
             Uri downloadUri = uriTask.getResult();
             model.setUri(downloadUri.toString());

             mDatabaseRef.child(Constants.NODE_NEWS).push().setValue(model)
                 .addOnSuccessListener(unused -> Log.i(TAG, "News data added to database"))
                 .addOnFailureListener(e -> Log.i(TAG, e.getMessage()));

             Log.i(TAG, "News image added to database storage");
      })
          .addOnFailureListener(e -> Log.i(TAG, e.getMessage()));
   }
}
