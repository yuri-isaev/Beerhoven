package ru.mobile.beerhoven.ui.store.catalog;

import static android.widget.PopupMenu.*;

import static ru.mobile.beerhoven.ui.store.catalog.CatalogAdapter.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.activity.MainActivity;
import ru.mobile.beerhoven.databinding.ItemCatalogBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.interfaces.InteractionListener;
import ru.mobile.beerhoven.ui.store.sections.StoreFragmentDirections;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.HashMapRepository;

public class CatalogAdapter extends Adapter<ItemViewHolder> implements OnMenuItemClickListener {
   protected List<Product> mAdapterList;
   private final InteractionListener mListener;
   private final Context mContext;
   private NavController navController;

   public CatalogAdapter(List<Product> list, Context context, InteractionListener mListener) {
      this.mAdapterList = list;
      this.mListener = mListener;
      this.mContext = context;
   }

   @NonNull
   @Override
   public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemCatalogBinding recyclerBinding = ItemCatalogBinding
          .inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new ItemViewHolder(recyclerBinding);
   }

   @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
   @Override
   public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
      Product model = mAdapterList.get(position);
      String PID = model.getId();
      String image = model.getUrl();

      Glide.with(holder.recyclerBinding.itemImage.getContext())
          .load(model.getUrl())
          .into(holder.recyclerBinding.itemImage);

      StoreFragmentDirections.ActionNavStoreToNavDetails action = StoreFragmentDirections.actionNavStoreToNavDetails()
          .setChange(Constants.OBJECT_VISIBLE)
          .setItemID(PID).setCountry(model.getCountry())
          .setManufacture(model.getManufacture())
          .setName(model.getName())
          .setPrice(String.valueOf(model.getPrice()))
          .setStyle(model.getStyle())
          .setFortress(model.getFortress())
          .setDensity(model.getDensity())
          .setDescription(model.getDescription())
          .setImage(image);

      // Binding view fields
      holder.recyclerBinding.itemName.setText(model.getName());
      holder.recyclerBinding.itemPrice.setText((model.getPrice() + " руб."));
      holder.recyclerBinding.itemStyle.setText(model.getStyle());
      holder.recyclerBinding.itemFortress.setText((model.getFortress() + "%"));

      // Navigate action for click product catalog card
      holder.recyclerBinding.itemContainer.setOnClickListener(v -> {
         navController = Navigation.findNavController(v);
         navController.navigate(action);
      });

      // Navigate action for click product catalog card
      holder.recyclerBinding.itemSelector.setOnClickListener(v -> {
         PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
         popupMenu.inflate(R.menu.popup_menu);
         popupMenu.show();

         popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
               case R.id.action_context_open:
                  navController = Navigation.findNavController(v);
                  navController.navigate(action);
                  break;
               case R.id.action_context_delete:
                  HashMapRepository.map.put("item_id", PID);
                  HashMapRepository.map.put("image", image);
                  mListener.onInteractionDelete(mAdapterList.get(position));
                  break;
            }
            return true;
         });
      });

      // Add product to cart when click catalog card element
      holder.recyclerBinding.itemPlus.setOnClickListener(v -> {
         int mValue = 1;
         Calendar calForDate = Calendar.getInstance();

         @SuppressLint({"NewApi", "LocalSuppress", "SimpleDateFormat"})
         SimpleDateFormat currentDate = new SimpleDateFormat(Constants.CURRENT_DATA);

         @SuppressLint({"NewApi", "LocalSuppress"})
         String saveCurrentDate = currentDate.format(calForDate.getTime());

         @SuppressLint({"SimpleDateFormat", "NewApi", "LocalSuppress"})
         SimpleDateFormat currentTime = new SimpleDateFormat(Constants.CURRENT_TIME);

         @SuppressLint({"NewApi", "LocalSuppress"})
         String saveCurrentTime = currentTime.format(calForDate.getTime());

         HashMapRepository.idMap.put("catalog_id", model.getId());
         HashMapRepository.priceMap.put("catalog_total", model.getPrice());
         HashMapRepository.priceMap.put("catalog_price", model.getPrice());

         HashMap<String, String> map = HashMapRepository.catalogMap;
         map.put("name", model.getName());
         map.put("country", model.getCountry());
         map.put("manufacture", model.getManufacture());
         map.put("style", model.getStyle());
         map.put("fortress", model.getFortress());
         map.put("density", model.getDensity());
         map.put("description", model.getDescription());
         map.put("url", model.getUrl());
         map.put("data", saveCurrentDate);
         map.put("time", saveCurrentTime);
         map.put("quantity", String.valueOf(mValue));

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
      ItemCatalogBinding recyclerBinding;

      public ItemViewHolder(ItemCatalogBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.recyclerBinding = recyclerBinding;
      }

      @Override
      public void onClick(View v) {
         recyclerBinding.itemSelector.setOnClickListener(this);
         recyclerBinding.itemContainer.setOnClickListener(this);
         recyclerBinding.itemPlus.setOnClickListener(this);
      }

      @Override
      public boolean onMenuItemClick(MenuItem item) {
         return false;
      }
   }
}
