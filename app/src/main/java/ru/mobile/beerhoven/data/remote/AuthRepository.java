package ru.mobile.beerhoven.data.remote;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import ru.mobile.beerhoven.domain.model.User;
import ru.mobile.beerhoven.domain.repository.IAuthRepository;
import ru.mobile.beerhoven.utils.Constants;

public class AuthRepository implements IAuthRepository {
   private final DatabaseReference mFirebaseRef;
   private final FirebaseUser mCurrentUser;

   public AuthRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference(Constants.NODE_USERS);
      this.mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
   }

   @Override
   public Object getCurrentUser() {
      return this.mCurrentUser;
   }

   @Override
   public void onCreateUser(User user) {
      mFirebaseRef.child((Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())).getUid()).setValue(user);
   }
}
