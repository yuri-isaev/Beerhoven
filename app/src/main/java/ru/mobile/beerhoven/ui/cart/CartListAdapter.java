package ru.mobile.beerhoven.ui.cart;

import static androidx.recyclerview.widget.RecyclerView.*;

import static ru.mobile.beerhoven.ui.cart.CartFragmentDirections.*;
import static ru.mobile.beerhoven.ui.cart.CartListAdapter.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import java.util.EventListener;
import java.util.List;

import ru.mobile.beerhoven.activity.MainActivity;
import ru.mobile.beerhoven.data.remote.CartRepository;
import ru.mobile.beerhoven.databinding.ProductCartBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.utils.Constants;

public class CartListAdapter extends Adapter<CartListViewHolder> {
   private final List<Product> mCartList;
   private final Callback mCallback;
   private final Context mContext;
   private double mOverTotalPrice;
   protected double oneTypeProductPrice;

   public interface Callback extends EventListener {
      void onPassData(String data);
   }

   public CartListAdapter(@NonNull List<Product> list, Callback listener, Context context) {
      this.mCartList = list;
      this.mCallback = listener;
      this.mContext = context;
   }

   @NonNull
   @Override
   public CartListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ProductCartBinding binding = ProductCartBinding
          .inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new CartListViewHolder(binding);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull CartListViewHolder holder, int position) {
      Product product = mCartList.get(position);
      String productId = product.getId();

      Glide.with(holder.binding.tvNameCart.getContext())
          .load(product.getUrl())
          .into(holder.binding.tvImage);

      // Binding view fields
      holder.binding.tvNameCart.setText(product.getName());
      holder.binding.tvStyleCart.setText(product.getStyle());
      holder.binding.tvFortressCart.setText(product.getFortress() + "%");
      holder.binding.tvQuantityCart.setText(product.getQuantity());
      holder.binding.tvPriceCart.setText(product.getPrice() + " руб.");
      holder.binding.tvTotalCart.setText(product.getTotal() + " руб.");

      // The total price of the entire cart
      oneTypeProductPrice = product.getTotal();
      double sum = mOverTotalPrice + oneTypeProductPrice;
      mOverTotalPrice = Math.round(sum * 100.0) / 100.0;
      mCallback.onPassData(String.valueOf(mOverTotalPrice));

      holder.binding.tvDeleteProductCart.setOnClickListener(v -> {
         // Delete product from cart and database
         CartViewModel mCartViewModel = new CartViewModel(new CartRepository());
         mCartViewModel.onDeleteCartListItem(productId);

         // Decrease counter when delete product from cart and database
         ((MainActivity) mContext).onDecreaseCounterClick();

         // Update total price of the entire cart after decrease counter
         double dif = 0.0;
         mOverTotalPrice = Math.round(dif * 100.0) / 100.0;
         mCallback.onPassData(String.valueOf(mOverTotalPrice));
      });

      holder.binding.tvContainerCart.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         ActionNavCartToNavDetails action = actionNavCartToNavDetails()
             .setChange(Constants.OBJECT_RENAME)
             .setItemID(productId)
             .setCountry(product.getCountry())
             .setManufacture(product.getManufacture())
             .setName(product.getName())
             .setPrice(String.valueOf(product.getPrice()))
             .setStyle(product.getStyle())
             .setFortress(product.getFortress())
             .setDensity(product.getDensity())
             .setDescription(product.getDescription())
             .setImage(product.getUrl());
         navController.navigate(action);
      });
   }

   @Override
   public int getItemCount() {
      return mCartList.size();
   }


   public static class CartListViewHolder extends ViewHolder implements OnClickListener {
      private final ProductCartBinding binding;

      public CartListViewHolder(ProductCartBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }

      @Override
      public void onClick(View v) {
         binding.tvContainerCart.setOnClickListener(this);
         binding.tvDeleteProductCart.setOnClickListener(this);
      }
   }
}
