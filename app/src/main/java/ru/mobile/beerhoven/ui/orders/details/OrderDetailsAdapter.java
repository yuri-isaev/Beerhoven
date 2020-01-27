package ru.mobile.beerhoven.ui.orders.details;

import static android.view.View.*;
import static androidx.recyclerview.widget.RecyclerView.*;

import static ru.mobile.beerhoven.ui.orders.details.OrderDetailsAdapter.*;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.mobile.beerhoven.R;
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
      Glide.with(holder.mImageTv.getContext()).load(order.getUrl()).into(holder.mImageTv);

      // Binding view fields
      holder.mNameTv.setText(order.getName());
      holder.mStyleTv.setText(order.getStyle());
      holder.mFortressTv.setText(order.getFortress() + "%");
      holder.mQuantityTv.setText(order.getQuantity());
      holder.mPriceTv.setText(order.getPrice() + " руб.");
      holder.mTotalTv.setText(order.getTotal() + " руб.");
      holder.mDeleteTv.setVisibility(INVISIBLE);

      holder.mViewContainer.setOnClickListener(v -> {
         NavController navController = Navigation.findNavController(v);
         assert keyID != null;
         OrderDetailsFragmentDirections.ActionNavOrderDetailsToNavDetails action = OrderDetailsFragmentDirections.actionNavOrderDetailsToNavDetails()
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
      private final LinearLayout mViewContainer;
      private final CircleImageView mImageTv;
      private final TextView mNameTv;
      private final TextView mPriceTv;
      private final TextView mStyleTv;
      private final TextView mFortressTv;
      private final TextView mQuantityTv;
      private final TextView mTotalTv;
      private final ImageView mDeleteTv;

      public OrderDetailsViewHolder(@NonNull View itemView) {
         super(itemView);
         this.mNameTv = itemView.findViewById(R.id.tvName);
         this.mPriceTv = itemView.findViewById(R.id.tvPrice);
         this.mStyleTv = itemView.findViewById(R.id.tvStyle);
         this.mFortressTv = itemView.findViewById(R.id.tvFortress);
         this.mImageTv = itemView.findViewById(R.id.tvImage);
         this.mDeleteTv = itemView.findViewById(R.id.tvDeleteItem);
         this.mViewContainer = itemView.findViewById(R.id.tvContainer);
         this.mQuantityTv = itemView.findViewById(R.id.tvQuantity);
         this.mTotalTv = itemView.findViewById(R.id.tvTotal);
      }

      @Override
      public void onClick(View v) {
         mViewContainer.setOnClickListener(this);
         itemView.setOnClickListener(this);
      }
   }
}
