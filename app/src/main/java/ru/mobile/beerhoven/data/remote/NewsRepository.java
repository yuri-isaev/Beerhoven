package ru.mobile.beerhoven.data.remote;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.domain.repository.INewsRepository;
import ru.mobile.beerhoven.utils.Constants;

public class NewsRepository implements INewsRepository {
   private final DatabaseReference mDatabaseRef;
   private final StorageReference mStorageRef;
   private final MutableLiveData<List<News>> mMutableList;
   private final List<News> mNewsList;
   private static final String TAG = "NewsRepository";

   public NewsRepository() {
      this.mDatabaseRef = FirebaseDatabase.getInstance().getReference();
      this.mStorageRef = FirebaseStorage.getInstance().getReference();
      this.mMutableList = new MutableLiveData<>();
      this.mNewsList = new ArrayList<>();
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

   @Override
   public MutableLiveData<List<News>> getNewsList() {
      if (mNewsList.size() == 0) {
         onGetNewsListFromDatabase();
      }
      mMutableList.setValue(mNewsList);
      return mMutableList;
   }

   private void onGetNewsListFromDatabase() {
      mDatabaseRef.child(Constants.NODE_NEWS).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            News post = snapshot.getValue(News.class);
            assert post != null;
            post.setId(snapshot.getKey());
            if (!mNewsList.contains(post)) {
               mNewsList.add(post);
            }
            mMutableList.postValue(mNewsList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            News post = snapshot.getValue(News.class);
            assert post != null;
            post.setId(snapshot.getKey());
            if (mNewsList.contains(post)) {
               mNewsList.set(mNewsList.indexOf(post), post);
            }
            mMutableList.postValue(mNewsList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            News post = snapshot.getValue(News.class);
            assert post != null;
            post.setId(snapshot.getKey());
            mNewsList.remove(post);
            mMutableList.postValue(mNewsList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

         @Override
         public void onCancelled(@NonNull DatabaseError error) {}
      });
   }
}
