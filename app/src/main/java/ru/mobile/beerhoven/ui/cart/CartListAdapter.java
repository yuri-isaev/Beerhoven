package ru.mobile.beerhoven.ui.cart;

import static androidx.recyclerview.widget.RecyclerView.*;

import static ru.mobile.beerhoven.ui.cart.CartFragmentDirections.*;
import static ru.mobile.beerhoven.ui.cart.CartListAdapter.*;

import android.annotation.SuppressLint;
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
import ru.mobile.beerhoven.databinding.ItemCartBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.utils.Constants;

public class CartListAdapter extends Adapter<CartListViewHolder> {
   private final List<Product> mCartList;
   private final Callback mCallback;
   private final CartViewModel mCartViewModel;
   private double mOverTotalPrice;

   public interface Callback extends EventListener {
      void onPassData(String data);
   }

   public CartListAdapter(@NonNull List<Product> list, Callback listener) {
      this.mCartList = list;
      this.mCallback = listener;
      this.mCartViewModel = new CartViewModel(new MainActivity().getApplication());
   }

   @NonNull
   @Override
   public CartListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemCartBinding recyclerBinding = ItemCartBinding
          .inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new CartListViewHolder(recyclerBinding);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull CartListViewHolder holder, int position) {
      Product model = mCartList.get(position);
      String positionID = model.getId();

      Glide.with(holder.binding.tvNameCart.getContext())
          .load(model.getUrl())
          .into(holder.binding.tvImage);

      holder.binding.tvNameCart.setText(model.getName());
      holder.binding.tvStyleCart.setText(model.getStyle());
      holder.binding.tvFortressCart.setText(model.getFortress() + "%");
      holder.binding.tvQuantityCart.setText(model.getQuantity());
      holder.binding.tvPriceCart.setText(model.getPrice() + " руб.");
      holder.binding.tvTotalCart.setText(model.getTotal() + " руб.");

      holder.binding.tvDeleteItemCart.setOnClickListener(v -> {
         // Initialize cart list from database
         mCartViewModel.deleteCartListItem(positionID);

         // The total price of the entire cart
         double sum = mOverTotalPrice + model.getTotal();
         mOverTotalPrice = Math.round(sum * 100.0) / 100.0;
         mCallback.onPassData(String.valueOf(mOverTotalPrice));
      });

      holder.binding.tvContainerCart.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         ActionNavCartToNavDetails action = actionNavCartToNavDetails()
             .setChange(Constants.OBJECT_RENAME)
             .setItemID(positionID)
             .setCountry(model.getCountry())
             .setManufacture(model.getManufacture())
             .setName(model.getName())
             .setPrice(String.valueOf(model.getPrice()))
             .setStyle(model.getStyle())
             .setFortress(model.getFortress())
             .setDensity(model.getDensity())
             .setDescription(model.getDescription())
             .setImage(model.getUrl());
         navController.navigate(action);
      });
   }

   @Override
   public int getItemCount() {
      return mCartList.size();
   }


   public static class CartListViewHolder extends ViewHolder implements OnClickListener {
      // Recycler binding
      ItemCartBinding binding;

      public CartListViewHolder(ItemCartBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }

      @Override
      public void onClick(View v) {
         binding.tvContainerCart.setOnClickListener(this);
         binding.tvDeleteItemCart.setOnClickListener(this);
      }
   }
}
