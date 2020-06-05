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
   private final DatabaseReference mFirebaseRef;
   private final List<News> mNewsList;
   private final MutableLiveData<List<News>> mMutableList;
   private final StorageReference mStorageRef;
   private static final String TAG = "NewsRepository";

   public NewsRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mStorageRef = FirebaseStorage.getInstance().getReference();
      this.mMutableList = new MutableLiveData<>();
      this.mNewsList = new ArrayList<>();
   }

   @Override
   public void onAddNewsWithoutImageToDatabase(@NonNull News post) {
      post.setImage(Constants.IMAGE_DEFAULT);
      mFirebaseRef.child(Constants.NODE_NEWS).push().setValue(post)
          .addOnFailureListener(e -> Log.i(TAG, e.getMessage()))
          .addOnSuccessListener(unused -> Log.i(TAG, "News data added to database"));
   }

   @Override
   public void onAddNewsToDatabase(@NonNull News post) {
      mStorageRef.child(Constants.FOLDER_NEWS_IMG).child(new Date().toString())
          .putFile(Uri.parse(post.getImage()))
          .addOnSuccessListener((taskSnapshot) -> {
             Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
             uriTask
                 .addOnFailureListener(e -> Log.e(TAG, e.getMessage()))
                 .addOnSuccessListener(uri -> {
                    Uri downloadUri = uriTask.getResult();
                    post.setImage(downloadUri.toString());

                    mFirebaseRef.child(Constants.NODE_NEWS).push().setValue(post)
                        .addOnFailureListener(e -> Log.i(TAG, e.getMessage()))
                        .addOnSuccessListener(unused -> Log.i(TAG, "News data added to database"));

                    Log.i(TAG, "News image added to database storage");
             });
          });
   }

   @Override
   public MutableLiveData<List<News>> getNewsListFromDatabase() {
      if (mNewsList.size() == 0) {
         onGetNewsList();
      }
      mMutableList.setValue(mNewsList);
      return mMutableList;
   }

   private void onGetNewsList() {
         mFirebaseRef.child(Constants.NODE_NEWS).addChildEventListener(new ChildEventListener() {
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
