package com.example.macro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.macro.Model.Product;
import com.example.macro.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    private Button searchBtn;
    private EditText inputTxt;
    private RecyclerView searchList;
    private String searchInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        inputTxt=findViewById(R.id.search_name);
        searchBtn=findViewById(R.id.search_btn);
        searchList=findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput=inputTxt.getText().toString();
                onStart();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Products");
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(ref.orderByChild("pname").startAt(searchInput),Product.class).build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter=new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int i, @NonNull final Product model) {
                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price : "+model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(SearchActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("pid", model.getPid());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);

                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        searchList.setAdapter(adapter);
        adapter.startListening();

    }
}