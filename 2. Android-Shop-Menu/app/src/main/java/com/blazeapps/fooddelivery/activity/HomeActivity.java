package com.blazeapps.fooddelivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blazeapps.fooddelivery.R;
import com.blazeapps.fooddelivery.common.Common;
import com.blazeapps.fooddelivery.interfaces.ItemClickListener;
import com.blazeapps.fooddelivery.model.Category;
import com.blazeapps.fooddelivery.viewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.signin.SignIn;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;

    TextView tvName;

    RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    public static final String CATEGORY_CODE = "CategoryId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu);
        setSupportActionBar(toolbar);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Name for user
        View headerView = navigationView.getHeaderView(0);
        tvName = headerView.findViewById(R.id.navHeaderText);
        tvName.setText(Common.currentUser.getName());

        //Load menu
        recyclerMenu = findViewById(R.id.recycler_menu);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        recyclerMenu.setAdapter(getAdapter());

    }

    private FirebaseRecyclerAdapter<Category, MenuViewHolder> getAdapter() {
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(category, Category.class)
                        .setLifecycleOwner(this)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Category model) {
                holder.tvMenuName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.imageView);
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(HomeActivity.this, FoodListActivity.class);
                        intent.putExtra(CATEGORY_CODE, adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MenuViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_menu, parent, false));
            }
        };
        return adapter;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {

        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(HomeActivity.this,OrderStatusActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_log_out) {
            Intent intent = new Intent(HomeActivity.this,SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
