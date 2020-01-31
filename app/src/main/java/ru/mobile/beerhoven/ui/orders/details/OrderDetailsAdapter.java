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

import ru.mobile.beerhoven.R;
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
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
      return new OrderDetailsViewHolder(view);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {
      Product order = mOrderDetails.get(position);
      String keyID = order.getId();

      Glide.with(holder.binding.tvImage.getContext())
          .load(order.getUrl())
          .into(holder.binding.tvImage);

      // Binding view fields
      holder.binding.tvNameCart.setText(order.getName());
      holder.binding.tvStyleCart.setText(order.getStyle());
      holder.binding.tvFortressCart.setText(order.getFortress() + "%");
      holder.binding.tvQuantityCart.setText(order.getQuantity());
      holder.binding.tvPriceCart.setText(order.getPrice() + " руб.");
      holder.binding.tvTotalCart.setText(order.getTotal() + " руб.");
      holder.binding.tvDeleteItemCart.setVisibility(INVISIBLE);

      holder.binding.tvContainerCart.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         assert keyID != null;
         ActionNavOrderDetailsToNavDetails action = actionNavOrderDetailsToNavDetails()
             .setChange(Constants.OBJECT_INVISIBLE)
             .setItemID(keyID)
             .setCountry(order.getCountry())
             .setManufacture(order.getManufacture())
             .setName(order.getName())
             .setPrice(String.valueOf(order.getPrice()))
             .setStyle(order.getStyle())
             .setFortress(order.getFortress())
             .setDensity(order.getDensity())
             .setDescription(order.getDescription())
             .setImage(order.getUrl());
         navController.navigate(action);
      });
   }

   @Override
   public int getItemCount() {
      return mOrderDetails.size();
   }


   public static class OrderDetailsViewHolder extends ViewHolder implements OnClickListener {
      ItemCartBinding binding;

      public OrderDetailsViewHolder(@NonNull View itemView) {
         super(itemView);
      }

      @Override
      public void onClick(View v) {
         itemView.setOnClickListener(this);
         binding.tvContainerCart.setOnClickListener(this);
      }
   }
}
