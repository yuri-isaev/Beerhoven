package ru.mobile.beerhoven.ui.store.catalog;

import static android.widget.PopupMenu.*;

import static ru.mobile.beerhoven.ui.store.catalog.CatalogAdapter.*;
import static ru.mobile.beerhoven.ui.store.sections.StoreFragmentDirections.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.activity.MainActivity;
import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.databinding.ProductCatalogBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.interfaces.InteractionListener;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.CurrentDateTime;

public class CatalogAdapter extends Adapter<ItemViewHolder> implements OnMenuItemClickListener {
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
   public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ProductCatalogBinding binding = ProductCatalogBinding
          .inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new ItemViewHolder(binding);
   }

   @RequiresApi(api = Build.VERSION_CODES.N)
   @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
   @Override
   public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
      Product product = mAdapterList.get(position);
      String productId = product.getId();
      String productImage = product.getUrl();

      Glide.with(holder.binding.tvImageProduct.getContext())
          .load(product.getUrl())
          .into(holder.binding.tvImageProduct);

      // Set navigate action args
      ActionNavStoreToNavDetails action = actionNavStoreToNavDetails()
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
      holder.binding.tvNameProduct.setText(product.getName());
      holder.binding.tvPriceProduct.setText((product.getPrice() + " руб."));
      holder.binding.tvStyleProduct.setText(product.getStyle());
      holder.binding.tvFortressProduct.setText((product.getFortress() + "%"));

      // Navigate action for click product catalog card
      holder.binding.tvContainer.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         navController.navigate(action);
      });

      // Manager functional
      // Navigate action for click product catalog card
      holder.binding.tvSelectorProduct.setOnClickListener(v -> {
         PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
         popupMenu.inflate(R.menu.popup_menu);
         popupMenu.show();
         popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
               case R.id.action_context_open:
                  NavController navController = Navigation.findNavController(v);
                  navController.navigate(action);
                  break;
               case R.id.action_context_delete:
                  MapStorage.productMap.put("productID", productId);
                  MapStorage.productMap.put("image", productImage);
                  // Delete product list position from cart
                  mListener.onInteractionDelete(mAdapterList.get(position));
                  break;
            }
            return true;
         });
      });

      // Add product to cart when click catalog card element
      holder.binding.tvAddProduct.setOnClickListener(v -> {
         int defaultCountValue = 1;

         // Counter value control
         if (mContext instanceof MainActivity && !MapStorage.productMap.containsValue(productId)) {
            ((MainActivity) mContext).onIncreaseCounterClick();
            MapStorage.productMap.put("productID", productId);
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
         //map.put("data", CurrentDateTime.getCurrentDate());
         //map.put("time", CurrentDateTime.getCurrentTime());
         map.put("quantity", String.valueOf(defaultCountValue));

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

   public static class ItemViewHolder extends ViewHolder implements OnClickListener, OnMenuItemClickListener {
      ProductCatalogBinding binding;

      public ItemViewHolder(ProductCatalogBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }

      @Override
      public void onClick(View v) {
         // binding.tvSelectorProduct.setOnClickListener(this);
         binding.tvContainer.setOnClickListener(this);
         binding.tvAddProduct.setOnClickListener(this);
      }

      @Override
      public boolean onMenuItemClick(MenuItem item) {
         return false;
      }
   }
}
