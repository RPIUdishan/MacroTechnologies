package com.example.macro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.macro.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrder extends AppCompatActivity {

    private EditText nameEditTxt, phoneEditTxt, addressEditTxt;
    private Button btnConfirmOrder;

    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(ConfirmOrder.this,"Total Price is "+totalAmount, Toast.LENGTH_SHORT).show();

        nameEditTxt = findViewById(R.id.your_name_confirm);
        phoneEditTxt = findViewById(R.id.your_phone_confirm);
        addressEditTxt = findViewById(R.id.your_address_confirm);

        btnConfirmOrder = findViewById(R.id.confirm_button);

        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });
    }

    private void Check() {

        if (TextUtils.isEmpty(nameEditTxt.getText().toString())){
            Toast.makeText(this, "Provide Name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phoneEditTxt.getText().toString())){
            Toast.makeText(this, "Provide PhoneNumber", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(addressEditTxt.getText().toString())){
            Toast.makeText(this, "Provide Address", Toast.LENGTH_SHORT).show();
        }else{
            confirmOrder();
        }
    }

    private void confirmOrder() {

        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, YYYY");
        saveCurrentDate = currentdate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("MMM dd, YYYY");
        saveCurrentTime = currentdate.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentCustomer.getEmail());


        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("name", nameEditTxt.getText().toString());
        orderMap.put("phone", phoneEditTxt.getText().toString());
        orderMap.put("address", addressEditTxt.getText().toString());
        orderMap.put("time", saveCurrentTime);
        orderMap.put("date", saveCurrentDate);
        orderMap.put("state" , "not shipped");

        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentCustomer.getEmail()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ConfirmOrder.this,"Order completed Succesfully.",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmOrder.this,HomeActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }

            }
        });


    }
}
