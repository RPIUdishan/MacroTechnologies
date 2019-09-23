package com.example.macro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.macro.Model.Product;
import com.example.macro.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private Button addTocart;
    private TextView productPrice, productDescription, productName;
    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        productImage =(ImageView) findViewById(R.id.product_image_details);
        numberButton =(ElegantNumberButton) findViewById(R.id.number_button);

        productName = (TextView)findViewById(R.id.product_name_details);
        productPrice = (TextView)findViewById(R.id.product_price_details);
        productDescription = (TextView)findViewById(R.id.product_description_details);
        addTocart = (Button)findViewById(R.id.add_cart_button);

        getProductDetails(productID);

        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingToCartList();
            }
        });

    }

    private void addingToCartList() {

        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
        saveCurrentDate = currentdate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("MMM dd, YYYY");
        saveCurrentTime = currentdate.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());


        cartListRef.child("User View").child(Prevalent.currentCustomer.getEmail()).child("Products").child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    cartListRef.child("User View")
                            .child(Prevalent.currentCustomer.getEmail()).child("Products").child(productID)
                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(ProductDetailsActivity.this,"Added to the Cart", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }

                        }
                    }) ;

                    }

            }
        });



    }

    private void getProductDetails(final String productID){

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Product product = dataSnapshot.getValue(Product.class);

                    productName.setText(product.getPname());
                    productPrice.setText(product.getPrice());
                    productDescription.setText(product.getDescription());
                    Picasso.get().load(product.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
