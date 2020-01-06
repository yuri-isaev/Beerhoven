package ru.mobile.beerhoven.data.repository;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import ru.mobile.beerhoven.data.storage.CrudRepository;
import ru.mobile.beerhoven.models.Item;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.HashMapRepository;

public class DetailsRepository {
   private MutableLiveData<String> mLiveData;
   private final DatabaseReference mInstanceFirebase;
   private final String UID;

   @SuppressLint("StaticFieldLeak")
   private static Context context;

   public DetailsRepository(Context context) {
      DetailsRepository.context = context;
      mLiveData = new MutableLiveData<>();
      mInstanceFirebase = FirebaseDatabase.getInstance().getReference();
      UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   // Create an instance of a product to the cart
   public MutableLiveData<String> addItemInOrder() {
      if (mLiveData == null) {
         mLiveData = new MutableLiveData<>();
         createItemOrder();
      }
      return mLiveData;
   }

   public void createItemOrder() {
      HashMap<String, String> id = HashMapRepository.idMap;
      HashMap<String, String> map = HashMapRepository.detailsMap;
      HashMap<String, Double> price = HashMapRepository.priceMap;

      Item post = new Item();
      post.setName(map.get("name"));
      post.setCountry(map.get("country"));
      post.setManufacture(map.get("manufacture"));
      post.setStyle(map.get("style"));
      post.setFortress(map.get("fortress"));
      post.setDensity(map.get("density"));
      post.setDescription(map.get("description"));
      post.setDate(map.get("data"));
      post.setTime(map.get("time"));
      post.setUrl(map.get("url"));
      post.setQuantity(map.get("quantity"));
      post.setPrice(price.get("details_price"));

      assert UID != null;
      mInstanceFirebase.child(Constants.NODE_CART).child(UID)
          .child(Objects.requireNonNull(id.get("details_id")))
          .setValue(post);
   }
}
