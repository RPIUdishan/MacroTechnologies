package com.example.macro;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.macro.Model.SM_Products;
import com.example.macro.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;




import android.content.Intent;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;
import android.widget.Toast;


public class Store_Management extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

        private DatabaseReference databaseReference;
        private RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;


        /*ListView listView;
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter;*/

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_store__management);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Products_n");





            recyclerView = findViewById(R.id.recycle_menu);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);


            DrawerLayout drawer = findViewById(R.id.SM_drawer_layout);
            NavigationView navigationView = findViewById(R.id.SM_nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
        }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<SM_Products> options =
                new FirebaseRecyclerOptions.Builder<SM_Products>()
                .setQuery(databaseReference, SM_Products.class)
                .build();

        FirebaseRecyclerAdapter<SM_Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<SM_Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, int i, @NonNull final SM_Products sm_products) {
                        productViewHolder.txtProductName.setText(sm_products.getPname());
                        productViewHolder.txtProductDescription.setText(sm_products.getDescription());
                        productViewHolder.txtProductPrice.setText(sm_products.getPrice());
                        Picasso.get().load(sm_products.getImage()).into(productViewHolder.imageView);

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]{
                                        "Edit",
                                        "Remove"
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(Store_Management.this);
                                builder.setTitle("Choose Options:");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i == 0){
                                            Intent intent = new Intent(Store_Management.this, SM_Product_Details.class);
                                            intent.putExtra("pid", sm_products.getPid());
                                            startActivity(intent);
                                        }

                                        if (i == 1){
                                            databaseReference.child(sm_products.getPid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(Store_Management.this, "Item removed successfully.",Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.show();
                            }

                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sm_product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return  holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
        public void onBackPressed() {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.store__management, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_store_sm) {
                Intent intent = new Intent(Store_Management.this, Store_Management.class);
                startActivity(intent);
            } else if (id == R.id.nav_add_sm) {
                Intent intent = new Intent(Store_Management.this, SM_Add_Items.class);
                startActivity(intent);
            } else if (id == R.id.nav_setting) {

            } else if (id == R.id.nav_logout) {

            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }


        public void btnPressAdd(View view) {
            Intent intent2 = new Intent(Store_Management.this, SM_Add_Items.class);
            startActivity(intent2);
        }


}

