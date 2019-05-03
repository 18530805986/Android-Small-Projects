package com.blazeapps.fooddelivery.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blazeapps.fooddelivery.R;
import com.blazeapps.fooddelivery.interfaces.ItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tvOrderId)
    public TextView itemId;
    @BindView(R.id.tvOrderPhone)
    public TextView itemPhone;
    @BindView(R.id.tvOrderStatus)
    public TextView itemStatus;
    @BindView(R.id.tvOrderAddress)
    public TextView itemAddress;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
