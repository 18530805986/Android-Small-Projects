package com.blazeapps.fooddelivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blazeapps.fooddelivery.R;
import com.blazeapps.fooddelivery.common.Common;
import com.blazeapps.fooddelivery.interfaces.ItemClickListener;
import com.blazeapps.fooddelivery.model.Category;
import com.blazeapps.fooddelivery.model.Request;
import com.blazeapps.fooddelivery.viewHolder.MenuViewHolder;
import com.blazeapps.fooddelivery.viewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderStatusActivity extends AppCompatActivity {

    @BindView(R.id.listOrders)
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        ButterKnife.bind(this);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(getAdapter());

    }

    public FirebaseRecyclerAdapter<Request, OrderViewHolder> getAdapter() {
        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(requests.orderByChild("phone").equalTo(Common.currentUser.getPhone()), Request.class)
                        .setLifecycleOwner(this)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Request model) {
                holder.itemId.setText(adapter.getRef(position).getKey());
                holder.itemAddress.setText(model.getAddress());
                holder.itemPhone.setText(model.getPhone());
                holder.itemStatus.setText(convertCodeToStatus(model.getStatus()));
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new OrderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_order, parent, false));
            }
        };
        return adapter;
    }

    private String convertCodeToStatus(String status) {
        switch (status){
            case Request.PLACED_STATUS: return "Placed";
            case Request.PROCESS_STATUS: return "On my way";
            case Request.SHIPPED_STATUS: return "Shipped";
            default: return "NOPE";
        }
    }
}
