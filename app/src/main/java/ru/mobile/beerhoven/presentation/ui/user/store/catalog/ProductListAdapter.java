package ru.mobile.beerhoven.presentation.ui.user.store.catalog;

import static ru.mobile.beerhoven.presentation.ui.user.store.catalog.ProductListAdapter.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.local.MapStorage;
import ru.mobile.beerhoven.databinding.ItemProductBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.presentation.interfaces.IAdapterPositionListener;
import ru.mobile.beerhoven.presentation.ui.user.store.sections.StoreFragmentDirections;
import ru.mobile.beerhoven.utils.Constants;

public class ProductListAdapter extends Adapter<ProductListViewHolder> {
   private final List<Product> mAdapterList;
   private final Context mContext;
   private final IAdapterPositionListener mListener;

   public ProductListAdapter(List<Product> list, Context context, IAdapterPositionListener mListener) {
      this.mAdapterList = list;
      this.mContext = context;
      this.mListener = mListener;
   }

   @NonNull
   @Override
   public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new ProductListViewHolder(binding);
   }

   @RequiresApi(api = Build.VERSION_CODES.N)
   @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
   @Override
   public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
      Product item = mAdapterList.get(position);
      String productId = item.getId();
      String productImage = item.getUrl();

      // Binding view fields
      Glide.with(holder.binding.productImage.getContext()).load(item.getUrl()).into(holder.binding.productImage);
      holder.binding.productName.setText(item.getName());
      holder.binding.productPrice.setText((item.getPrice() + " руб."));
      holder.binding.productStyle.setText(item.getStyle());
      holder.binding.productFortress.setText((item.getFortress() + "%"));

      // Set navigate action args
      StoreFragmentDirections.ActionNavProductListToNavProductDetails action = StoreFragmentDirections.actionNavProductListToNavProductDetails()
          .setChange(Constants.OBJECT_VISIBLE)
          .setProductId(productId)
          .setCountry(item.getCountry())
          .setManufacture(item.getManufacture())
          .setName(item.getName())
          .setPrice(String.valueOf(item.getPrice()))
          .setStyle(item.getStyle())
          .setFortress(item.getFortress())
          .setDensity(item.getDensity())
          .setDescription(item.getDescription())
          .setImage(productImage);

      NavOptions options = new NavOptions.Builder()
          .setLaunchSingleTop(true)
          .setEnterAnim(R.anim.fade_in)
          .setExitAnim(R.anim.fade_out)
          .setPopExitAnim(R.anim.fade_out)
          .build();

      // Navigate action for click product catalog card
      holder.binding.productContainer.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         navController.navigate(action, options);
      });

      // Add product to cart when click catalog card element
      holder.binding.cardAddProduct.setOnClickListener(v -> {
         int productQuantity = 1;

         // Counter value control
         if (mContext instanceof MainActivity && !MapStorage.productMap.containsValue(productId)) {
            MapStorage.productMap.put("id", productId);
            ((MainActivity) mContext).onIncreaseCartCounter();
         } 
         
         Product product = mAdapterList.get(position);
         product.setQuantity(String.valueOf(productQuantity));
         MapStorage.priceMap.put("total", product.getPrice());

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
