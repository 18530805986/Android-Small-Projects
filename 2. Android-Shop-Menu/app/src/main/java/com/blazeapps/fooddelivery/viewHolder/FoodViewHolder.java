package com.blazeapps.fooddelivery.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blazeapps.fooddelivery.R;
import com.blazeapps.fooddelivery.interfaces.ItemClickListener;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvFoodName;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);

        tvFoodName = itemView.findViewById(R.id.foodName);
        imageView = itemView.findViewById(R.id.foodImage);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
