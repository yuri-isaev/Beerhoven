package ru.mobile.beerhoven.presentation.ui.orders.details;

import static android.view.View.*;
import static androidx.recyclerview.widget.RecyclerView.*;

import static ru.mobile.beerhoven.presentation.ui.orders.details.OrderDetailAdapter.*;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.mobile.beerhoven.databinding.ItemCartBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.utils.Constants;

public class OrderDetailAdapter extends Adapter<OrderDetailsViewHolder> {
   private final List<Product> mOrderDetails;

   public OrderDetailAdapter(@NonNull List<Product> list) {
      this.mOrderDetails = list;
   }

   @NonNull
   @Override
   public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new OrderDetailsViewHolder(binding);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {
      Product product = mOrderDetails.get(position);
      String productId = product.getId();

      Glide.with(holder.binding.cartImage.getContext())
          .load(product.getUrl())
          .into(holder.binding.cartImage);

      // Binding view fields
      holder.binding.cartName.setText(product.getName());
      holder.binding.cartStyle.setText(product.getStyle());
      holder.binding.cartFortress.setText(product.getFortress() + "%");
      holder.binding.cartQuantity.setText(product.getQuantity());
      holder.binding.cartPrice.setText(product.getPrice() + " руб.");
      holder.binding.cartTotal.setText(product.getTotal() + " руб.");
      holder.binding.cartProductDelete.setVisibility(INVISIBLE);

      // Navigate action on order details
      holder.binding.cartContainer.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         assert productId != null;
         OrderDetailFragmentDirections.ActionNavOrderDetailsToNavDetails action = OrderDetailFragmentDirections
             .actionNavOrderDetailsToNavDetails()
             .setChange(Constants.OBJECT_INVISIBLE)
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
         navController.navigate(action);
      });
   }

   @Override
   public int getItemCount() {
      return mOrderDetails.size();
   }


   public static class OrderDetailsViewHolder extends ViewHolder implements OnClickListener {
      ItemCartBinding binding;

      public OrderDetailsViewHolder(ItemCartBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }

      @Override
      public void onClick(View v) {
         itemView.setOnClickListener(this);
         binding.cartContainer.setOnClickListener(this);
      }
   }
}
