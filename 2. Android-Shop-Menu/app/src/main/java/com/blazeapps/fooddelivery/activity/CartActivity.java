package com.blazeapps.fooddelivery.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blazeapps.fooddelivery.R;
import com.blazeapps.fooddelivery.common.Common;
import com.blazeapps.fooddelivery.database.Database;
import com.blazeapps.fooddelivery.model.Order;
import com.blazeapps.fooddelivery.model.Request;
import com.blazeapps.fooddelivery.viewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends AppCompatActivity {

    @BindView(R.id.listCart)
    RecyclerView listCart;
    RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.tvTotal)
    TextView tvTotal;

    FirebaseDatabase database;
    DatabaseReference requests;

    List<Order> orderList = new ArrayList<>();

    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        listCart.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listCart.setLayoutManager(layoutManager);

        loadCart();

    }

    private void loadCart() {
        orderList = new Database(this).getCarts();
        cartAdapter = new CartAdapter(orderList, this);
        listCart.setAdapter(cartAdapter);

        //Total price

        int total = 0;
        Locale locale = Locale.getDefault();
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        for (Order order : orderList) {
            total += (Integer.parseInt(order.getPrice()) * Integer.parseInt(order.getQuantity()));
        }

        tvTotal.setText(fmt.format(total));

    }

    @OnClick(R.id.btnPlaceOrder)
    public void onViewClicked() {
        showAlertDialog();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        alertDialog.setTitle("One more step...");
        alertDialog.setMessage("Enter your address");

        final EditText edtAddress = new EditText(CartActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAddress.getText().toString(),
                        tvTotal.getText().toString(),
                        orderList
                );
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new Database(getBaseContext()).cleanCart();
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
