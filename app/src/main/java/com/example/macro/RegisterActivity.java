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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText InputName,InputEmail,InputPassword,InputReEnteredPwd;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton=(Button) findViewById(R.id.register_btn);
        InputName=(EditText) findViewById(R.id.register_username_input);
        InputPassword=(EditText) findViewById(R.id.register_password_input);
        InputReEnteredPwd = (EditText) findViewById(R.id.editReRegPwd);
        InputEmail=(EditText)findViewById(R.id.register_email_input);

        loadingBar =new ProgressDialog(this );
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 createAccount();
            }
        });

    }

    public void createAccount(){
        String userName = InputName.getText().toString();
        String email = InputEmail.getText().toString();
        String password = InputPassword.getText().toString();
        String reEnteredPwd = InputReEnteredPwd.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this,"Please enter your username",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter your email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter your password",Toast.LENGTH_SHORT).show();
        }else if(!password.equals(reEnteredPwd)){
            Toast.makeText(this,"Password and Reentered Password is not equal.", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            String editedEmail=email.replace(".",",");
            validateEmail(userName, editedEmail, password);
        }
    }

    private void validateEmail(final String userName, final String email, final String password){
            final DatabaseReference Ref = FirebaseDatabase.getInstance().getReference();

            Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!(dataSnapshot.child("Customers").child(email).exists())){

                        HashMap<String, Object> userDataMap = new HashMap<>();

                        userDataMap.put("userName", userName);

                        userDataMap.put("email", email);
                        userDataMap.put("password", password);


                        Ref.child("Customers").child(email).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Your account is created..", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                }else{
                                    Toast.makeText(RegisterActivity.this,"Connection Error",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        Toast.makeText(RegisterActivity.this,"This " + email + " already exsits..",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                        Toast.makeText(RegisterActivity.this,"Please try again..",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }
}
