package ru.mobile.beerhoven.presentation.ui.user.orders.details;

import static android.view.View.INVISIBLE;
import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.mobile.beerhoven.databinding.ItemCartBinding;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.utils.Constants;

public class OrderDetailListAdapter extends Adapter<OrderDetailListAdapter.OrderDetailsViewHolder> {
   private final List<Product> mOrderDetails;

   public OrderDetailListAdapter(@NonNull List<Product> list) {
      this.mOrderDetails = list;
   }

   @NonNull
   @Override
   public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemCartBinding binding = ItemCartBinding
          .inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new OrderDetailsViewHolder(binding);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {
      Product product = mOrderDetails.get(position);
      String productId = product.getId();
      holder.binding.cartName.setText(product.getName());
      holder.binding.cartStyle.setText(product.getStyle());
      holder.binding.cartFortress.setText(product.getFortress() + "%");
      holder.binding.cartQuantity.setText(product.getQuantity());
      holder.binding.cartPrice.setText(product.getPrice() + " руб.");
      holder.binding.cartTotal.setText(product.getTotal() + " руб.");
      holder.binding.cartProductDelete.setVisibility(INVISIBLE);

      Glide.with(holder.binding.cartImage.getContext())
          .load(product.getUrl())
          .into(holder.binding.cartImage);

      holder.binding.cartContainer.setOnClickListener(v -> {
         assert productId != null;
         NavDirections action = OrderDetailListFragmentDirections.actionNavOrderDetailsToNavProductDetails()
             .setCountry(product.getCountry())
             .setDensity(product.getDensity())
             .setDescription(product.getDescription())
             .setFortress(product.getFortress())
             .setImage(product.getUrl())
             .setManufacture(product.getManufacture())
             .setName(product.getName())
             .setProductId(productId)
             .setPrice(String.valueOf(product.getPrice()))
             .setStyle(product.getStyle())
             .setVisible(Constants.OBJECT_INVISIBLE);
         Navigation.findNavController(v).navigate(action);
      });
   }

   @Override
   public int getItemCount() {
      return mOrderDetails.size();
   }

   public static class OrderDetailsViewHolder extends ViewHolder {
      ItemCartBinding binding;

      public OrderDetailsViewHolder(@NonNull ItemCartBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }
   }
}
