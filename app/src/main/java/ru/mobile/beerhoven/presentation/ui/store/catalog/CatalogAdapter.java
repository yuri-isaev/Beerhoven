package ru.mobile.beerhoven.presentation.ui.store.catalog;

import static android.widget.PopupMenu.*;

import static ru.mobile.beerhoven.presentation.ui.store.catalog.CatalogAdapter.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.databinding.ItemCatalogBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.presentation.interfaces.InteractionListener;
import ru.mobile.beerhoven.presentation.ui.store.sections.StoreFragmentDirections;
import ru.mobile.beerhoven.presentation.ui.store.sections.StoreFragmentDirections.ActionNavStoreToNavDetails;
import ru.mobile.beerhoven.utils.Constants;

public class CatalogAdapter extends Adapter<CatalogViewHolder> implements OnMenuItemClickListener {
   private final List<Product> mAdapterList;
   private final Context mContext;
   private final InteractionListener mListener;

   public CatalogAdapter(List<Product> list, Context context, InteractionListener mListener) {
      this.mAdapterList = list;
      this.mContext = context;
      this.mListener = mListener;
   }

   @NonNull
   @Override
   public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemCatalogBinding binding = ItemCatalogBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new CatalogViewHolder(binding);
   }

   @RequiresApi(api = Build.VERSION_CODES.N)
   @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
   @Override
   public void onBindViewHolder(@NonNull CatalogViewHolder holder, int position) {
      Product product = mAdapterList.get(position);
      String productId = product.getId();
      String productImage = product.getUrl();

      Glide.with(holder.binding.tvImageProduct.getContext())
          .load(product.getUrl())
          .into(holder.binding.tvImageProduct);

      // Set navigate action args
      ActionNavStoreToNavDetails action = StoreFragmentDirections.actionNavStoreToNavDetails()
          .setChange(Constants.OBJECT_VISIBLE)
          .setProductID(productId)
          .setCountry(product.getCountry())
          .setManufacture(product.getManufacture())
          .setName(product.getName())
          .setPrice(String.valueOf(product.getPrice()))
          .setStyle(product.getStyle())
          .setFortress(product.getFortress())
          .setDensity(product.getDensity())
          .setDescription(product.getDescription())
          .setImage(productImage);

      // Binding view fields
      holder.binding.productName.setText(product.getName());
      holder.binding.productPrice.setText((product.getPrice() + " руб."));
      holder.binding.productStyle.setText(product.getStyle());
      holder.binding.productFortress.setText((product.getFortress() + "%"));

      // Navigate action for click product catalog card
      holder.binding.productContainer.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         navController.navigate(action);
      });

      // Add product to cart when click catalog card element
      holder.binding.cardAddProduct.setOnClickListener(v -> {
         int productQuantity = 1;

         // Counter value control
         if (mContext instanceof MainActivity && !MapStorage.productMap.containsValue(productId)) {
            MapStorage.productMap.put("productID", productId);
            ((MainActivity) mContext).onIncreaseCartCounter();
         } else {
            productQuantity += 1;
         }

         MapStorage.priceMap.put("total", product.getPrice());
         MapStorage.priceMap.put("price", product.getPrice());
         HashMap<String, String> map = MapStorage.productMap;
         map.put("name", product.getName());
         map.put("country", product.getCountry());
         map.put("manufacture", product.getManufacture());
         map.put("style", product.getStyle());
         map.put("fortress", product.getFortress());
         map.put("density", product.getDensity());
         map.put("description", product.getDescription());
         map.put("url", product.getUrl());
         map.put("quantity", String.valueOf(productQuantity));

         // Add product list position to cart
         mListener.onInteractionAdd(mAdapterList.get(position));
      });
   }

   @Override
   public int getItemCount() {
      return mAdapterList.size();
   }

   @Override
   public boolean onMenuItemClick(MenuItem menuItem) {
      return false;
   }

   public static class CatalogViewHolder extends ViewHolder implements OnClickListener, OnMenuItemClickListener {
      ItemCatalogBinding binding;

      public CatalogViewHolder(ItemCatalogBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }

      @Override
      public void onClick(View v) {
         // binding.tvSelectorProduct.setOnClickListener(this);
         binding.productContainer.setOnClickListener(this);
         binding.cardAddProduct.setOnClickListener(this);
      }

      @Override
      public boolean onMenuItemClick(MenuItem item) {
         return false;
      }
   }
}
