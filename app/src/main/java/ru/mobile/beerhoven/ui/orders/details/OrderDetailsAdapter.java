package ru.mobile.beerhoven.ui.orders.details;

import static android.view.View.*;
import static androidx.recyclerview.widget.RecyclerView.*;

import static ru.mobile.beerhoven.ui.orders.details.OrderDetailsAdapter.*;
import static ru.mobile.beerhoven.ui.orders.details.OrderDetailsFragmentDirections.*;

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

public class OrderDetailsAdapter extends Adapter<OrderDetailsViewHolder> {
   private final List<Product> mOrderDetails;

   public OrderDetailsAdapter(@NonNull List<Product> list) {
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

      Glide.with(holder.binding.tvImage.getContext())
          .load(product.getUrl())
          .into(holder.binding.tvImage);

      // Binding view fields
      holder.binding.tvNameCart.setText(product.getName());
      holder.binding.tvStyleCart.setText(product.getStyle());
      holder.binding.tvFortressCart.setText(product.getFortress() + "%");
      holder.binding.tvQuantityCart.setText(product.getQuantity());
      holder.binding.tvPriceCart.setText(product.getPrice() + " руб.");
      holder.binding.tvTotalCart.setText(product.getTotal() + " руб.");
      holder.binding.tvDeleteProductCart.setVisibility(INVISIBLE);

      // Navigate action on order details
      holder.binding.tvContainerCart.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         assert productId != null;
         ActionNavOrderDetailsToNavDetails action = actionNavOrderDetailsToNavDetails()
             .setChange(Constants.OBJECT_INVISIBLE)
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
         binding.tvContainerCart.setOnClickListener(this);
      }
   }
}
