package ru.mobile.beerhoven.ui.cart;

import static androidx.recyclerview.widget.RecyclerView.*;

import static ru.mobile.beerhoven.ui.cart.CartListAdapter.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;

import java.util.EventListener;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.models.Item;

public class CartListAdapter extends Adapter<CartViewHolder> {
   private final Context mContext;
   private double mOverTotalPrice;
   protected double oneTypeProductPrice;
   protected List<Item> mCartList;
   private final Callback mCallback;

   public interface Callback extends EventListener {
      void onPassData(String data);
   }

   public CartListAdapter(@NonNull List<Item> list, Context context, Callback listener) {
      this.mCartList = list;
      this.mContext = context;
      this.mCallback = listener;
   }

   @NonNull
   @Override
   public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
      return new CartViewHolder(view);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
      Item model = mCartList.get(position);
      Glide.with(holder.mImageTv.getContext()).load(model.getUrl()).into(holder.mImageTv);

      holder.mNameTv.setText(model.getName());
      holder.mStyleTv.setText(model.getStyle());
      holder.mFortressTv.setText(model.getFortress() + "%");
      holder.mQuantityTv.setText(model.getQuantity());
      holder.mPriceTv.setText(model.getPrice() + " руб.");
      holder.mTotalTv.setText(model.getTotal() + " руб.");

      // The total price of the entire cart
      oneTypeProductPrice = model.getTotal();
      double sum = mOverTotalPrice + oneTypeProductPrice;
      mOverTotalPrice = Math.round(sum * 100.0) / 100.0;
      mCallback.onPassData(String.valueOf(mOverTotalPrice));
   }

   @Override
   public int getItemCount() {
      return mCartList.size();
   }


   public static class CartViewHolder extends ViewHolder implements OnClickListener {
      private final LinearLayout mViewContainer;
      private final CircleImageView mImageTv;
      private final TextView mNameTv;
      private final TextView mPriceTv;
      private final TextView mStyleTv;
      private final TextView mFortressTv;
      private final TextView mQuantityTv;
      private final TextView mTotalTv;
      private final ImageView mDeleteTv;

      public CartViewHolder(@NonNull View itemView) {
         super(itemView);
         this.mNameTv = itemView.findViewById(R.id.tvName);
         this.mPriceTv = itemView.findViewById(R.id.tvPrice);
         this.mStyleTv = itemView.findViewById(R.id.tvStyle);
         this.mFortressTv = itemView.findViewById(R.id.tvFortress);
         this.mImageTv = itemView.findViewById(R.id.tvImage);
         this.mDeleteTv = itemView.findViewById(R.id.tvDeleteItem);
         this.mViewContainer = itemView.findViewById(R.id.tvContainer);
         this.mQuantityTv = itemView.findViewById(R.id.tvProductQuantity);
         this.mTotalTv = itemView.findViewById(R.id.tvTotal);
      }

      @Override
      public void onClick(View v) {
         mDeleteTv.setOnClickListener(this);
         mViewContainer.setOnClickListener(this);
      }
   }
}
