package ru.mobile.beerhoven.presentation.ui.user.cart;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import java.util.EventListener;
import java.util.List;

import ru.mobile.beerhoven.data.remote.CartRepository;
import ru.mobile.beerhoven.databinding.ItemCartBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.utils.Constants;

public class CartListAdapter extends Adapter<CartListAdapter.CartListViewHolder> {
   private double mOverTotalPrice;
   private final Callback mCallback;
   private final Context mContext;
   private final List<Product> mCartList;

   @FunctionalInterface
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
      ItemCartBinding binding = ItemCartBinding
          .inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new CartListViewHolder(binding);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull CartListViewHolder holder, int position) {
      Product product = mCartList.get(position);
      String productId = product.getId();
      holder.binding.tvCartItemName.setText(product.getName());
      holder.binding.tvCartItemStyle.setText(product.getStyle());
      holder.binding.tvCartItemFortress.setText(product.getFortress() + "%");
      holder.binding.tvCartItemQuantity.setText(product.getQuantity());
      holder.binding.tvCartItemPrice.setText(product.getPrice() + " руб.");
      holder.binding.tvCartItemTotal.setText(product.getTotal() + " руб.");

      Glide.with(holder.binding.tvCartItemName.getContext())
          .load(product.getImage())
          .into(holder.binding.ivCartItemImage);

      // The total price of the entire products cart
      double oneTypeProductPrice = product.getTotal();
      double sum = mOverTotalPrice + oneTypeProductPrice;
      mOverTotalPrice = Math.round(sum * 100.0) / 100.0;
      mCallback.onPassData(String.valueOf(mOverTotalPrice));

      holder.binding.ivCartItemDelete.setOnClickListener(v -> {
         // Delete product from database
         CartListViewModel mViewModel = new CartListViewModel(new CartRepository());
         mViewModel.onDeleteCartItemFromRepository(productId);

         // Decrease counter when delete product from cart
         ((MainActivity) mContext).onDecreaseCartDrawerCounter();

         // Update total price of the entire cart after delete product
         mOverTotalPrice = Math.round(0 * 100.0) / 100.0;
         mCallback.onPassData(String.valueOf(mOverTotalPrice));

         // Delete product from cart set
         CartSet.cartProducts.remove(productId);
      });

      holder.binding.llCartItemContainer.setOnClickListener(v -> {
         NavDirections action = CartListFragmentDirections.actionNavCartToNavDetails()
             .setCountry(product.getCountry())
             .setDensity(product.getDensity())
             .setDescription(product.getDescription())
             .setFortress(product.getFortress())
             .setImage(product.getImage())
             .setName(product.getName())
             .setManufacture(product.getManufacture())
             .setProductId(productId)
             .setPrice(String.valueOf(product.getPrice()))
             .setStyle(product.getStyle())
             .setVisible(Constants.OBJECT_RENAME);
         Navigation.findNavController(v).navigate(action);
      });
   }

   @Override
   public int getItemCount() {
      return mCartList.size();
   }

   public static class CartListViewHolder extends ViewHolder {
      private final ItemCartBinding binding;

      public CartListViewHolder(@NonNull ItemCartBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }
   }
}
