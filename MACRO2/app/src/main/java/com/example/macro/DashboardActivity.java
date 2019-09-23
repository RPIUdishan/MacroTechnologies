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
    TextView mDashboardTv;
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

        //bottom navigation
//        BottomNavigationView navigationView = findViewById(R.id.navigation);
//        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //home fragment transaction
        //actionBar.setTitle("Home");
//        HomeFragment fragment1 = new HomeFragment();
//        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
//        ft1.replace(R.id.content, fragment1, "");
//        ft1.commit();

        //init views
        mDashboardTv = findViewById(R.id.emailtxt);
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                    //handle item clicks
//                    switch (menuItem.getItemId()){
//                        case R.id.nav_home:
//                            //home fragment transaction
//                           // actionBar.setTitle("Home");
//                            HomeFragment fragment1 = new HomeFragment();
//                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
//                            ft1.replace(R.id.content, fragment1, "");
//                            ft1.commit();
//                            return true;
//                        case R.id.nav_profile:
//                            //profile fragment transaction
//                            //actionBar.setTitle("Profile");
//                            ProfileFragment fragment2 = new ProfileFragment();
//                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
//                            ft2.replace(R.id.content, fragment2, "");
//                            ft2.commit();
//                            return true;
//                        case R.id.nav_users:
//                            //users fragment transaction
//                            //profile fragment transaction
//                            //actionBar.setTitle("Users");
//                            UsersFragment fragment3 = new UsersFragment();
//                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
//                            ft3.replace(R.id.content, fragment3, "");
//                            ft3.commit();
//                            return true;
//                    }
//                    return false;
//                }
//            };

    private void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            //user is signed in stay here
            //set email of logged in user
            mDashboardTv.setText(user.getEmail());
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
