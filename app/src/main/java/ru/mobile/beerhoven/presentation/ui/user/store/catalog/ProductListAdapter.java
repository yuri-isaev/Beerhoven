package ru.mobile.beerhoven.presentation.ui.user.store.catalog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.mobile.beerhoven.databinding.ItemProductBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.presentation.interfaces.AdapterPositionListener;
import ru.mobile.beerhoven.presentation.ui.user.cart.CartSet;
import ru.mobile.beerhoven.presentation.ui.user.store.sections.StoreFragmentDirections;
import ru.mobile.beerhoven.utils.Constants;

public class ProductListAdapter extends Adapter<ProductListAdapter.ProductListViewHolder> {
   private final Context mContext;
   private final List<Product> mAdapterList;
   private final AdapterPositionListener mListener;

   public ProductListAdapter(List<Product> list, Context context, AdapterPositionListener mListener) {
      this.mAdapterList = list;
      this.mContext = context;
      this.mListener = mListener;
   }

   @NonNull
   @Override
   public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemProductBinding binding = ItemProductBinding
          .inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new ProductListViewHolder(binding);
   }

   @RequiresApi(api = Build.VERSION_CODES.N)
   @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
   @Override
   public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
      Product product = mAdapterList.get(position);
      String productId = product.getId();
      String productImage = product.getUrl();

      Glide.with(holder.binding.image.getContext())
          .load(product.getUrl())
          .into(holder.binding.image);
      
      holder.binding.productName.setText(product.getName());
      holder.binding.productPrice.setText((product.getPrice() + " руб."));
      holder.binding.productStyle.setText(product.getStyle());
      holder.binding.productFortress.setText((product.getFortress() + "%"));
      holder.binding.productContainer.setOnClickListener(v -> {
         NavDirections action = StoreFragmentDirections.actionNavProductListToNavProductDetails()
             .setProductId(productId)
             .setCountry(product.getCountry())
             .setManufacture(product.getManufacture())
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
      holder.binding.selectorAddProduct.setOnClickListener(v -> {
         int productQuantity = 1;

         // Counter value control
         if (mContext instanceof MainActivity && !CartSet.cartProducts.contains(productId)) {
            CartSet.cartProducts.add(productId);
            ((MainActivity) mContext).onIncreaseCartCounter();
         } 
         
         Product item = mAdapterList.get(position);
         item.setQuantity(String.valueOf(productQuantity));
         item.setTotal(Double.parseDouble(String.valueOf(item.getPrice())));

         // Add product list position to cart
         mListener.onInteractionAdd(product);
      });
   }

   @Override
   public int getItemCount() {
      return mAdapterList.size();
   }

   public static class ProductListViewHolder extends ViewHolder {
      ItemProductBinding binding;

      public ProductListViewHolder(@NonNull ItemProductBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }
   }
}
