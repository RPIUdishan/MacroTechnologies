package com.example.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    RecyclerView recyclerView;
    AdapterItem adapterItem;
    List<ModelItem> itemList;



    TextView name, price, date, status, contactNo, location;


//    final FirebaseUser fItem = FirebaseAuth.getInstance().getCurrentUser();
    //get path of database named "Users" containing users info
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders");
    private Button button, orderBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.cName);
        price = (findViewById(R.id.price));
        date =  findViewById(R.id.date);
        status = findViewById(R.id.status);
        location = findViewById(R.id.locationTo);
        contactNo = findViewById(R.id.contactNo);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.items_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //init user list
        itemList = new ArrayList<>();


        button = findViewById(R.id.orderComBtn);
        orderBtn = findViewById(R.id.OrderBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllCompletOrder();

            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getAllOrder();
            }
        });






    }

    private void getAllOrder() {
        final ModelItem modelItem = new ModelItem();

        //get all data from path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelItem modelItem = ds.getValue(ModelItem.class);
                    itemList.add(modelItem);
                    //adapter
                    adapterItem = new AdapterItem(MainActivity.this, itemList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAllCompletOrder() {


        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Deliver");

        //get all data from path
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelItem modelOrder = ds.getValue(ModelItem.class);
                    itemList.add(modelOrder);
                    //adapter
                    adapterItem = new AdapterItem(MainActivity.this, itemList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
