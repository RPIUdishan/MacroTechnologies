package com.example.macro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CategoryAdapter adapter;

    List<Category> categoryList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryList=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.recycle_view2);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //adding some items to our list
        categoryList.add(
                new Category("Lap Tops",1));
        categoryList.add(
                new Category("Mobile Phones",2));
        categoryList.add(
                new Category("Chagers",3));
        categoryList.add(
                new Category("Lap Tops",4));
        categoryList.add(
                new Category("Chagers",5));
        categoryList.add(
                new Category("Chagers",6));
        categoryList.add(
                new Category("Chagers",7));
        categoryList.add(
                new Category("Chagers",8));
        categoryList.add(
                new Category("Chagers",9));
        categoryList.add(
                new Category("Chagers",10));
        categoryList.add(
                new Category("Other",5));
        adapter=new CategoryAdapter(this,categoryList);
        recyclerView.setAdapter(adapter);
    }

}
