package com.example.macro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<CartItem> cartItemList;
    CartItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        cartItemList=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.recycle_view3);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItemList.add(
                new CartItem(
                        1,
                        "Apple MacBook Air Core i5 5th Gen - (8 GB/128 GB SSD/Mac OS Sierra)",
                        3,
                        60000,
                        R.drawable.item1));
        cartItemList.add(
                new CartItem(
                        2,
                        "Apple MacBook Air Core i5 5th Gen - (8 GB/128 GB SSD/Mac OS Sierra)",
                        8,
                        50000,
                        R.drawable.item2));

        adapter=new CartItemAdapter(this,cartItemList);
        recyclerView.setAdapter(adapter);


    }
}
