package com.blazeapps.fooddelivery.viewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.blazeapps.fooddelivery.R;
import com.blazeapps.fooddelivery.interfaces.ItemClickListener;
import com.blazeapps.fooddelivery.model.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tvCartName)
    TextView itemName;
    @BindView(R.id.tvCartPrice)
    TextView itemPrice;
    @BindView(R.id.cartItemCount)
    ImageView itemCount;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> items = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder().buildRound(
                "" + items.get(position).getQuantity(), Color.RED);
        holder.itemCount.setImageDrawable(drawable);
        Locale locale = Locale.getDefault();
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(items.get(position).getPrice()) * Integer.parseInt(items.get(position).getQuantity()));
        holder.itemPrice.setText(fmt.format(price));
        holder.itemName.setText(items.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
