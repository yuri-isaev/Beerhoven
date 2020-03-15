package ru.mobile.beerhoven.data.remote;

import static java.util.Objects.*;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.utils.Constants;

public class CatalogDetailRepository {
   private final DatabaseReference mInstanceFirebase;
   private final MutableLiveData<String> mLiveData;
   private final String mUserPhoneID;
   private String data;

   public CatalogDetailRepository() {
      this.mInstanceFirebase = FirebaseDatabase.getInstance().getReference();
      this.mLiveData = new MutableLiveData<>();
      this.mUserPhoneID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   // Create an instance of a product to the cart
   public MutableLiveData<String> createProductToCart() {
      if (data == null) {
         addPostToCartList();
      }
      mLiveData.setValue(data);
      return mLiveData;
   }

   private void addPostToCartList() {
      HashMap<String, String> map = MapStorage.productMap;
      HashMap<String, Double> price = MapStorage.priceMap;

      Product post = new Product();
      post.setCountry(map.get("country"));
      post.setDensity(map.get("density"));
      post.setDescription(map.get("description"));
      post.setFortress(map.get("fortress"));
      post.setManufacture(map.get("manufacture"));
      post.setName(map.get("name"));
      post.setPrice(price.get("price"));
      post.setQuantity(map.get("quantity"));
      post.setStyle(map.get("style"));
      post.setTotal(price.get("total"));
      post.setUrl(map.get("url"));

      assert mUserPhoneID != null;
      mInstanceFirebase.child(Constants.NODE_CART).child(mUserPhoneID)
          .child(requireNonNull(MapStorage.productMap.get("productID")))
          .setValue(post);
   }
}
