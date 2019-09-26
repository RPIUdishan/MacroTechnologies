package com.example.macro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    //Views
    ImageView employeeBtn, proPic;
    TextView mDashboardTv, mDashNameTv;
    //firebase auth
    FirebaseAuth firebaseAuth;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Action bar and title
         actionBar = getSupportActionBar();
//        actionBar.setTitle("Profile");

        //handle create button click
        employeeBtn = findViewById(R.id.emp_btn);

        employeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, EmployeeActivity.class);
                startActivity(intent);
            }
        });

        proPic = findViewById(R.id.propic);
        proPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        //init
        firebaseAuth = FirebaseAuth.getInstance();


        //init views
        mDashboardTv = findViewById(R.id.emailtxt);
        mDashNameTv = findViewById(R.id.dName);
    }



    private void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            //user is signed in stay here
            //set email of logged in user
            mDashboardTv.setText(user.getEmail());
            mDashNameTv.setText(user.getDisplayName());
        }
        else{
            //user not signed in, go to main activity
            startActivity(new Intent(DashboardActivity.this, EmployeeActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    protected void onStart() {
        //check on start of app
        checkUserStatus();
        super.onStart();
    }
}
