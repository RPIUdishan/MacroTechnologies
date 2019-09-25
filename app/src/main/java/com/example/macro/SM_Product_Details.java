package com.example.macro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.macro.Model.SM_Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.ImageView;
import com.squareup.picasso.Picasso;

public class SM_Product_Details extends AppCompatActivity {
    EditText editTextCategory, editTextPname, editTextPrice, editTextDescription;
    //ImageView imageView;
    Button buttonSumbimt;
    private String productID = "";
    private DatabaseReference productsRef;
    private DatabaseReference productsRef2;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        loadingBar = new ProgressDialog(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sm__product__details);

        productID = getIntent().getStringExtra("pid");

        editTextCategory = findViewById(R.id.editText_category);
        editTextPname = findViewById(R.id.editTxt_pName);
        editTextPrice = findViewById(R.id.editTxt_price);
        editTextDescription = findViewById(R.id.editTxt_description);
        //imageView = findViewById(R.id.image_Upload);


        getProductDetails(productID);

        buttonSumbimt = findViewById(R.id.btn_submit);

        buttonSumbimt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productsRef2 = FirebaseDatabase.getInstance().getReference().child("Products_n");
                productsRef2.child(productID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            loadingBar.setTitle("Updating the product");
                            loadingBar.setMessage("Please wait. while we are update a product");
                            loadingBar.setCanceledOnTouchOutside(false);
                            loadingBar.show();


                            SM_Products products = new SM_Products();
                            SM_Products E_product = dataSnapshot.getValue(SM_Products.class);

                            products.setPid(productID);
                            products.setCategory(editTextCategory.getText().toString().trim());
                            products.setPname(editTextPname.getText().toString().trim());
                            products.setPrice(editTextPrice.getText().toString().trim());
                            products.setDescription(editTextDescription.getText().toString().trim());

                            products.setDate(E_product.getDate());
                            products.setTime(E_product.getTime());
                            products.setImage(E_product.getImage());

                            productsRef2.child(productID).setValue(products);
                            Toast.makeText(SM_Product_Details.this, "Item update successfully.",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SM_Product_Details.this, Store_Management.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void getProductDetails(String productID) {
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products_n");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    SM_Products products = dataSnapshot.getValue(SM_Products.class);

                    editTextCategory.setText(products.getCategory()+"");
                    editTextPname.setText(products.getPname()+"");
                    editTextPrice.setText(products.getPrice()+"");
                    editTextDescription.setText(products.getDescription()+"");
                    //Picasso.get().load(products.getImage()).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
