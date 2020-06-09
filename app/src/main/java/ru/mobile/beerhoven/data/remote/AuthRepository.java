package ru.mobile.beerhoven.data.remote;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.mobile.beerhoven.domain.model.Customer;
import ru.mobile.beerhoven.domain.model.EmptyUser;
import ru.mobile.beerhoven.domain.model.User;
import ru.mobile.beerhoven.domain.repository.IAuthRepository;
import ru.mobile.beerhoven.utils.Constants;

public class AuthRepository implements IAuthRepository {
   private final DatabaseReference mFirebaseRef;
   private final FirebaseUser mCurrentUser;
   private final MutableLiveData<User> mMutableData;

   public AuthRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference(Constants.NODE_USERS);
      this.mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
      this.mMutableData = new MutableLiveData<>();
   }

   @SuppressLint("NewApi")
   public MutableLiveData<User> getCurrentUserFromDatabase() {
      if (mCurrentUser == null) {
         mMutableData.postValue(new EmptyUser());
         return mMutableData;
      }
      onGetCurrentUserObject();
      return mMutableData;
   }

   @SuppressLint("NewApi")
   public void onGetCurrentUserObject() {
      String uid = mCurrentUser.getPhoneNumber();
      assert uid != null;
      mFirebaseRef.child(uid);
      mFirebaseRef.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot child : dataSnapshot.getChildren()) {
               Customer user = child.getValue(Customer.class);
               mMutableData.postValue(user);
            }
         }

         @Override
         public void onCancelled(@NonNull DatabaseError error) {}
      });
   }

   @Override
   public void onCreateUserToDatabase(@NonNull User customer) {
      String key = mFirebaseRef.child(Constants.NODE_USERS).push().getKey();
      customer.setId(key);
      mFirebaseRef.child(customer.getPhone()).setValue(customer);
   }
}
