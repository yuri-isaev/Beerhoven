package ru.mobile.beerhoven.presentation.ui.customer.store.catalog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.mobile.beerhoven.databinding.ItemProductBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.presentation.listeners.AdapterPositionListener;
import ru.mobile.beerhoven.presentation.ui.customer.cart.Cart;
import ru.mobile.beerhoven.presentation.ui.customer.store.sections.StoreFragmentDirections;
import ru.mobile.beerhoven.utils.Constants;

public class CatalogAdapter extends Adapter<CatalogAdapter.CatalogViewHolder> {
   private final Context mContext;
   private final List<Product> mAdapterList;
   private final AdapterPositionListener iPositionListener;

   public CatalogAdapter(List<Product> list, Context ctx, AdapterPositionListener listener) {
      this.mAdapterList = list;
      this.mContext = ctx;
      this.iPositionListener = listener;
   }

   @NonNull
   @Override
   public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemProductBinding binding = ItemProductBinding
          .inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new CatalogViewHolder(binding);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull CatalogViewHolder holder, int position) {
      Product product = mAdapterList.get(position);
      String productId = product.getId();
      String productImage = product.getImage();

      Glide.with(holder.binding.ivProductImage.getContext())
          .load(product.getImage())
          .into(holder.binding.ivProductImage);
      
      holder.binding.tvProductName.setText(product.getName());
      holder.binding.tvProductPrice.setText((product.getPrice() + " руб."));
      holder.binding.tvProductStyle.setText(product.getStyle());
      //holder.binding.tvProductFortress.setText((product.getFortress() + "%"));
      holder.binding.llProductContainer.setOnClickListener(v -> {
         NavDirections action = StoreFragmentDirections.actionNavProductListToNavProductDetails()
             .setProductId(productId)
             .setCapacity(product.getCapacity())
             .setCountry(product.getCountry())
             .setName(product.getName())
             .setPrice(String.valueOf(product.getPrice()))
             .setStyle(product.getStyle())
             .setFortress(product.getFortress())
             .setDensity(product.getDensity())
             .setDescription(product.getDescription())
             .setImage(productImage)
             .setVisible(Constants.OBJECT_VISIBLE);
         Navigation.findNavController(v).navigate(action);
      });

      // Add product to cart when click on selector
      holder.binding.ivSelectorAddProduct.setOnClickListener(v -> {
         int productQuantity = 1;

         // Counter value control
         if (mContext instanceof MainActivity && !Cart.productSet.contains(productId)) {
            Cart.productSet.add(productId);
            ((MainActivity) mContext).onIncreaseCartCounter();
         } 
         
         Product item = mAdapterList.get(position);
         item.setQuantity(String.valueOf(productQuantity));
         item.setTotal(Double.parseDouble(String.valueOf(item.getPrice())));

         // Add product list position to cart
         iPositionListener.onInteractionAdd(product);
      });
   }

   @Override
   public int getItemCount() {
      return mAdapterList.size();
   }

   public static class CatalogViewHolder extends ViewHolder {
      ItemProductBinding binding;

      public CatalogViewHolder(@NonNull ItemProductBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }
   }
}
