package ru.mobile.beerhoven.ui.store.catalog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.interfaces.InteractionListener;
import ru.mobile.beerhoven.models.Item;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ItemHolder> {

   protected List<Item> mAdapterList;

   public CatalogAdapter(List<Item> list) {
      this.mAdapterList = list;
   }

   @NonNull
   @Override
   public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catalog, parent, false);
      return new ItemHolder(view);
   }

   @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
   @Override
   public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
      Item model = mAdapterList.get(position);
      holder.mItemTitle.setText(model.getTitle());
      holder.mItemPrice.setText(model.getPrice() + " руб.");
      holder.mItemStyle.setText(model.getStyle());
      holder.mItemFortress.setText(model.getFortress() + "%");
   }

   @Override
   public int getItemCount() {
      return mAdapterList.size();
   }

   public static class ItemHolder extends RecyclerView.ViewHolder {
      private final LinearLayout mItemContainer;
      private final TextView mItemTitle;
      private final TextView mItemPrice;
      private final TextView mItemStyle;
      private final TextView mItemFortress;

      public ItemHolder(@NonNull View itemView) {
         super(itemView);
         this.mItemContainer = itemView.findViewById(R.id.item_container);
         this.mItemTitle = itemView.findViewById(R.id.item_title);
         this.mItemPrice = itemView.findViewById(R.id.item_price);
         this.mItemStyle = itemView.findViewById(R.id.item_style);
         this.mItemFortress = itemView.findViewById(R.id.item_fortress);
      }
   }
}
