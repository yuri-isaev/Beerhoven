package ru.mobile.beerhoven.ui.cart;

import static androidx.recyclerview.widget.RecyclerView.*;

import static ru.mobile.beerhoven.ui.cart.CartListAdapter.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import java.util.EventListener;
import java.util.List;

import ru.mobile.beerhoven.databinding.ItemCartBinding;
import ru.mobile.beerhoven.models.Item;
import ru.mobile.beerhoven.utils.Constants;

public class CartListAdapter extends Adapter<CartListViewHolder> {
   private final List<Item> mCartList;
   private final Callback mCallback;
   private final CartViewModel cartViewModel;
   private final Context mContext;
   private double mOverTotalPrice;
   private double oneTypeProductPrice;

   public interface Callback extends EventListener {
      void onPassData(String data);
   }

   public CartListAdapter(@NonNull List<Item> list, Context context, Callback listener) {
      this.mCartList = list;
      this.mContext = context;
      this.mCallback = listener;
      this.cartViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(CartViewModel.class);
   }

   @NonNull
   @Override
   public CartListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemCartBinding recyclerBinding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new CartListViewHolder(recyclerBinding);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull CartListViewHolder holder, int position) {
      Item model = mCartList.get(position);
      String positionID = model.getId();

      Glide.with(holder.recyclerBinding.tvName.getContext())
          .load(model.getUrl())
          .into(holder.recyclerBinding.tvImage);

      holder.recyclerBinding.tvName.setText(model.getName());
      holder.recyclerBinding.tvStyle.setText(model.getStyle());
      holder.recyclerBinding.tvFortress.setText(model.getFortress() + "%");
      holder.recyclerBinding.tvQuantity.setText(model.getQuantity());
      holder.recyclerBinding.tvPrice.setText(model.getPrice() + " руб.");
      holder.recyclerBinding.tvTotal.setText(model.getTotal() + " руб.");

      holder.recyclerBinding.tvDeleteItem.setOnClickListener(v -> {
         // Initialize cart list from database
         cartViewModel.deleteCartListItem(positionID);

         // The total price of the entire cart
         oneTypeProductPrice = model.getTotal();
         double sum = mOverTotalPrice + oneTypeProductPrice;
         mOverTotalPrice = Math.round(sum * 100.0) / 100.0;
         mCallback.onPassData(String.valueOf(mOverTotalPrice));
      });

      holder.recyclerBinding.tvContainer.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         CartFragmentDirections.ActionNavCartToNavDetails action = CartFragmentDirections.actionNavCartToNavDetails()
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
      ItemCartBinding recyclerBinding;

      public CartListViewHolder(ItemCartBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.recyclerBinding = recyclerBinding;
      }

      @Override
      public void onClick(View v) {
         recyclerBinding.tvDeleteItem.setOnClickListener(this);
         recyclerBinding.tvContainer.setOnClickListener(this);
      }
   }
}
