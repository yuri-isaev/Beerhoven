package ru.mobile.beerhoven.presentation.ui.customer.orders.details;

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
   private final List<Product> mAdapterList;

   public OrderDetailListAdapter(@NonNull List<Product> list) {
      this.mAdapterList = list;
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
      Product product = mAdapterList.get(position);
      String productId = product.getId();
      holder.binding.tvCartItemName.setText(product.getName());
      holder.binding.tvCartItemStyle.setText(product.getStyle());
      holder.binding.tvCartItemFortress.setText(product.getFortress() + "%");
      holder.binding.tvCartItemQuantity.setText(product.getQuantity());
      holder.binding.tvCartItemPrice.setText(product.getPrice() + " руб.");
      holder.binding.tvCartItemTotal.setText(product.getTotal() + " руб.");
      holder.binding.ivCartItemDelete.setVisibility(INVISIBLE);

      Glide.with(holder.binding.ivCartItemImage.getContext())
          .load(product.getImage())
          .into(holder.binding.ivCartItemImage);

      holder.binding.llCartItemContainer.setOnClickListener(v -> {
         assert productId != null;
         NavDirections action = OrderDetailListFragmentDirections.actionNavOrderDetailsToNavProductDetails()
             .setCountry(product.getCountry())
             .setDensity(product.getDensity())
             .setDescription(product.getDescription())
             .setFortress(product.getFortress())
             .setImage(product.getImage())
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
      return mAdapterList.size();
   }

   public static class OrderDetailsViewHolder extends ViewHolder {
      ItemCartBinding binding;

      public OrderDetailsViewHolder(@NonNull ItemCartBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }
   }
}
