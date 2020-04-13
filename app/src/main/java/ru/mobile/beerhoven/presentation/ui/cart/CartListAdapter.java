package ru.mobile.beerhoven.presentation.ui.cart;

import static androidx.recyclerview.widget.RecyclerView.*;

import static ru.mobile.beerhoven.presentation.ui.cart.CartListAdapter.*;

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

import ru.mobile.beerhoven.data.remote.CartRepository;
import ru.mobile.beerhoven.databinding.ItemCartBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.utils.Constants;

public class CartListAdapter extends Adapter<CartListViewHolder> {
   private double mOverTotalPrice;
   private final Callback mCallback;
   private final Context mContext;
   private final List<Product> mCartList;

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
      ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new CartListViewHolder(binding);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull CartListViewHolder holder, int position) {
      Product product = mCartList.get(position);
      String productId = product.getId();

      Glide.with(holder.binding.cartName.getContext())
          .load(product.getUrl())
          .into(holder.binding.cartImage);

      // Binding view fields
      holder.binding.cartName.setText(product.getName());
      holder.binding.cartStyle.setText(product.getStyle());
      holder.binding.cartFortress.setText(product.getFortress() + "%");
      holder.binding.cartQuantity.setText(product.getQuantity());
      holder.binding.cartPrice.setText(product.getPrice() + " руб.");
      holder.binding.cartTotal.setText(product.getTotal() + " руб.");

      // The total price of the entire cart
      double oneTypeProductPrice = product.getTotal();
      double sum = mOverTotalPrice + oneTypeProductPrice;
      mOverTotalPrice = Math.round(sum * 100.0) / 100.0;
      mCallback.onPassData(String.valueOf(mOverTotalPrice));

      holder.binding.cartProductDelete.setOnClickListener(v -> {
         // Delete product from cart and database
         CartViewModel mCartViewModel = new CartViewModel(new CartRepository());
         mCartViewModel.onDeleteCartListItemToRepository(productId);

         // Decrease counter when delete product from cart and database
         ((MainActivity) mContext).onDecreaseCartDrawerCounter();

         // Update total price of the entire cart after decrease counter
         double dif = 0.0;
         mOverTotalPrice = Math.round(dif * 100.0) / 100.0;
         mCallback.onPassData(String.valueOf(mOverTotalPrice));
      });

      holder.binding.cartContainer.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         CartFragmentDirections.ActionNavCartToNavDetails action = CartFragmentDirections
             .actionNavCartToNavDetails()
             .setChange(Constants.OBJECT_RENAME)
             .setProductID(productId)
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
      private final ItemCartBinding binding;

      public CartListViewHolder(ItemCartBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }

      @Override
      public void onClick(View v) {
         binding.cartContainer.setOnClickListener(this);
         binding.cartProductDelete.setOnClickListener(this);
      }
   }
}
