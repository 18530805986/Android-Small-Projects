package com.blazeapps.fooddelivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blazeapps.fooddelivery.R;
import com.blazeapps.fooddelivery.interfaces.ItemClickListener;
import com.blazeapps.fooddelivery.model.Food;
import com.blazeapps.fooddelivery.viewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class FoodListActivity extends AppCompatActivity {

    @BindView(R.id.recycler_food_list)
    RecyclerView recyclerView;
    MaterialSearchBar materialSearchBar;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    private String categoryId;

    public static final String FOOD_ID_CODE = "foodId";

    //search functionality
    List<String> suggestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        ButterKnife.bind(this);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra(HomeActivity.CATEGORY_CODE);
        }
        if (categoryId != null && !categoryId.isEmpty()) {
            recyclerView.setAdapter(getFoodListAdapter(categoryId,"MenuId"));
        }

        //search
        //TODO search works a bit stupid, need to be more friendly
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.addTextChangeListener(textWatcher);
        materialSearchBar.setHint("Enter food name");
        loadSuggests();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.setOnSearchActionListener(searchActionListener);
    }

    private void loadSuggests() {
        foodList.orderByChild("MenuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Food item = postSnapshot.getValue(Food.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private FirebaseRecyclerAdapter<Food, FoodViewHolder> getFoodListAdapter(String id,String sortBy) {
        FirebaseRecyclerOptions<Food> options =
                new FirebaseRecyclerOptions.Builder<Food>()
                        .setQuery(foodList.orderByChild(sortBy).equalTo(id), Food.class)
                        .setLifecycleOwner(this)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.tvFoodName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(FoodListActivity.this, FoodDetailsActivity.class);
                        intent.putExtra(FOOD_ID_CODE, adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FoodViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_food, parent, false));
            }
        };
        return adapter;
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            List<String> suggest = new ArrayList<>();
            for (String search:suggestList){
                if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                    suggest.add(search);
                }
            }
            materialSearchBar.setLastSuggestions(suggest);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    MaterialSearchBar.OnSearchActionListener searchActionListener = new MaterialSearchBar.OnSearchActionListener() {
        @Override
        public void onSearchStateChanged(boolean enabled) {
            if(!enabled){
                recyclerView.setAdapter(getFoodListAdapter(categoryId,"MenuId"));
            }
        }

        @Override
        public void onSearchConfirmed(CharSequence text) {
            //when search finish show result of search adapter
            recyclerView.setAdapter(getFoodListAdapter(text.toString(),"Name"));

        }

        @Override
        public void onButtonClicked(int buttonCode) {

        }
    };

}
