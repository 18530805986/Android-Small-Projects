package com.blazeapps.fooddelivery.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blazeapps.fooddelivery.R;
import com.blazeapps.fooddelivery.interfaces.ItemClickListener;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvMenuName;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public MenuViewHolder(View itemView) {
        super(itemView);

        tvMenuName = itemView.findViewById(R.id.menuName);
        imageView = itemView.findViewById(R.id.menuImage);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false );
    }
}
