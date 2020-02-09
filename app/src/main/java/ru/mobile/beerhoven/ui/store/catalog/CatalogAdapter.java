package ru.mobile.beerhoven.ui.store.catalog;

import static android.widget.PopupMenu.*;

import static ru.mobile.beerhoven.ui.store.catalog.CatalogAdapter.*;
import static ru.mobile.beerhoven.ui.store.sections.StoreFragmentDirections.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import ru.mobile.beerhoven.activity.MainActivity;
import ru.mobile.beerhoven.databinding.ProductCatalogBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.interfaces.InteractionListener;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.HashMapRepository;

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
          .setItemID(productId).setCountry(product.getCountry())
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

      // Navigate action for click product catalog card
      // holder.binding.tvSelectorProduct.setOnClickListener(v -> {
      //   PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
      //   popupMenu.inflate(R.menu.popup_menu);
      //   popupMenu.show();
      //   popupMenu.setOnMenuItemClickListener(item -> {
      //      switch (item.getItemId()) {
      //         case R.id.action_context_open:
      //            navController = Navigation.findNavController(v);
      //            navController.navigate(action);
      //            break;
      //         case R.id.action_context_delete:
      //            HashMapRepository.map.put("item_id", PID);
      //            HashMapRepository.map.put("image", image);
      //            mListener.onInteractionDelete(mAdapterList.get(position));
      //            break;
      //      }
      //      return true;
      //   });
      // });

      // Add product to cart when click catalog card element
      holder.binding.tvAddProduct.setOnClickListener(v -> {
         int defaultCountValue = 1;
         Calendar calForDate = Calendar.getInstance();

         @SuppressLint({"NewApi", "LocalSuppress", "SimpleDateFormat"})
         SimpleDateFormat currentDate = new SimpleDateFormat(Constants.CURRENT_DATA);

         @SuppressLint({"NewApi", "LocalSuppress"})
         String saveCurrentDate = currentDate.format(calForDate.getTime());

         @SuppressLint({"SimpleDateFormat", "NewApi", "LocalSuppress"})
         SimpleDateFormat currentTime = new SimpleDateFormat(Constants.CURRENT_TIME);

         @SuppressLint({"NewApi", "LocalSuppress"})
         String saveCurrentTime = currentTime.format(calForDate.getTime());

         HashMapRepository.idMap.put("productID", productId);
         HashMapRepository.priceMap.put("total", product.getPrice());
         HashMapRepository.priceMap.put("price", product.getPrice());

         HashMap<String, String> map = HashMapRepository.catalogMap;
         map.put("name", product.getName());
         map.put("country", product.getCountry());
         map.put("manufacture", product.getManufacture());
         map.put("style", product.getStyle());
         map.put("fortress", product.getFortress());
         map.put("density", product.getDensity());
         map.put("description", product.getDescription());
         map.put("url", product.getUrl());
         map.put("data", saveCurrentDate);
         map.put("time", saveCurrentTime);
         map.put("quantity", String.valueOf(defaultCountValue));

         mListener.onInteractionAdd(mAdapterList.get(position));

         // Access to the activation method through the Adapter
         if (mContext instanceof MainActivity) {
            ((MainActivity) mContext).onIncreaseCounterClick();
         }
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
