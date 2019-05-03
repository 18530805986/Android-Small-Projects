package com.blazeapps.fooddelivery.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blazeapps.fooddelivery.R;
import com.blazeapps.fooddelivery.database.Database;
import com.blazeapps.fooddelivery.model.Food;
import com.blazeapps.fooddelivery.model.Order;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tvFoodName)
    TextView foodName;
    @BindView(R.id.tvFoodPrice)
    TextView foodPrice;
    @BindView(R.id.tvDescription)
    TextView foodDescription;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.img_food)
    ImageView imageView;
    @BindView(R.id.btnNumber)
    ElegantNumberButton numberButton;

    String foodId = "";
    private Food food;

    FirebaseDatabase database;
    DatabaseReference foods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        ButterKnife.bind(this);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        if (getIntent() != null) {
            foodId = getIntent().getStringExtra(FoodListActivity.FOOD_ID_CODE);
        }
        if (!foodId.isEmpty()) {
            getFoodDetails();
        }

    }

    private void getFoodDetails() {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                food = dataSnapshot.getValue(Food.class);

                Picasso.get().load(food.getImage()).into(imageView);
                collapsingToolbarLayout.setTitle(food.getName());
                foodPrice.setText(food.getPrice());
                foodName.setText(food.getName());
                foodDescription.setText(food.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.btnCart)
    public void btnCardClick(View view) {
        new Database(getBaseContext()).addToCart(new Order(
                foodId,
                food.getName(),
                numberButton.getNumber(),
                food.getPrice(),
                food.getDescription()));
        Snackbar.make(view,"Added to Cart", Snackbar.LENGTH_SHORT).show();
    }
}
