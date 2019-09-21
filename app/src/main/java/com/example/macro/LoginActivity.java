package com.example.macro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.macro.Model.Customer;

import com.example.macro.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private Button LoginButton;
    private EditText inputEmail, inputPassword;
    private ProgressDialog loadingBar;
    private String parentDbName="Customers";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton=findViewById(R.id.login_btn);
        loadingBar = new ProgressDialog(this);

        inputEmail = findViewById(R.id.login_email_input);
        inputPassword = findViewById(R.id.login_password_input);



        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
    }

    private void LoginUser(){

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter Your Email..", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter Your Password..", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Plese wait..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            String editedEmail=email.replace(".",",");
            AllowAccessToAccount(editedEmail,password);
        }

    }

    private void AllowAccessToAccount(final String email, final String password){
        final DatabaseReference Ref;
        Ref= FirebaseDatabase.getInstance().getReference();

        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDbName).child(email).exists()){

                    Customer customerData = dataSnapshot.child(parentDbName).child(email).getValue(Customer.class);

                    if (customerData.getEmail().equals(email)){
                        if (customerData.getPassword().equals(password)){
                            Toast.makeText(LoginActivity.this, "Sign in successfully..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                           Prevalent.currentCustomer = customerData;
                           Toast.makeText(LoginActivity.this, ".." +Prevalent.currentCustomer.getUserName(), Toast.LENGTH_SHORT ).show();
                            startActivity(intent);


                        }else{
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();



                        }
                    }
                }else{
                    Toast.makeText(LoginActivity.this,"This " +email+ " email doea not exists..", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
