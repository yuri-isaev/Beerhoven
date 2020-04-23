package ru.mobile.beerhoven.presentation.ui.user.cart;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.ViewHolder;
import static ru.mobile.beerhoven.presentation.ui.user.cart.CartListAdapter.CartListViewHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import java.util.EventListener;
import java.util.List;

import ru.mobile.beerhoven.R;
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

      // Binding view fields
      holder.binding.cartName.setText(product.getName());
      holder.binding.cartStyle.setText(product.getStyle());
      holder.binding.cartFortress.setText(product.getFortress() + "%");
      holder.binding.cartQuantity.setText(product.getQuantity());
      holder.binding.cartPrice.setText(product.getPrice() + " руб.");
      holder.binding.cartTotal.setText(product.getTotal() + " руб.");
      Glide.with(holder.binding.cartName.getContext()).load(product.getUrl()).into(holder.binding.cartImage);

      // The total price of the entire cart
      double oneTypeProductPrice = product.getTotal();
      double sum = mOverTotalPrice + oneTypeProductPrice;
      mOverTotalPrice = Math.round(sum * 100.0) / 100.0;
      mCallback.onPassData(String.valueOf(mOverTotalPrice));

      holder.binding.cartProductDelete.setOnClickListener(v -> {
         // Delete product from cart and database
         CartListViewModel mViewModel = new CartListViewModel(new CartRepository());
         mViewModel.onDeleteCartItemFromRepository(productId);

         // Decrease counter when delete product from cart and database
         ((MainActivity) mContext).onDecreaseCartDrawerCounter();

         // Update total price of the entire cart after decrease counter
         double dif = 0.0;
         mOverTotalPrice = Math.round(dif * 100.0) / 100.0;
         mCallback.onPassData(String.valueOf(mOverTotalPrice));
      });

      holder.binding.cartContainer.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         CartListFragmentDirections.ActionNavCartToNavDetails action = CartListFragmentDirections
             .actionNavCartToNavDetails()
             .setChange(Constants.OBJECT_RENAME)
             .setProductId(productId)
             .setCountry(product.getCountry())
             .setManufacture(product.getManufacture())
             .setName(product.getName())
             .setPrice(String.valueOf(product.getPrice()))
             .setStyle(product.getStyle())
             .setFortress(product.getFortress())
             .setDensity(product.getDensity())
             .setDescription(product.getDescription())
             .setImage(product.getUrl());

         NavOptions options = new NavOptions.Builder()
             .setLaunchSingleTop(true)
             .setEnterAnim(R.anim.fade_in)
             .setExitAnim(R.anim.fade_out)
             .setPopExitAnim(R.anim.fade_out)
             .build();

         navController.navigate(action, options);
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
