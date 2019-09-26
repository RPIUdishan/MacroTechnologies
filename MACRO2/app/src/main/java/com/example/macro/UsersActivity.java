package com.example.macro;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    RecyclerView recyclerView;
    AdapterUsers adapterUsers;

    DatabaseReference ref;

    ImageButton mSearchBtn;
    EditText mSearchField;
    String mSearch;

    List<ModelUser> userList;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        //init

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //ref = firebaseDatabase.getReference().child(getString(R.string.project_id));
        ref = FirebaseDatabase.getInstance().getReference("Users");

        mSearchField = findViewById(R.id.search);
        mSearchBtn = findViewById(R.id.search_btn);

        recyclerView = findViewById(R.id.users_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(UsersActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearch = mSearchField.getText().toString();
                searchUsers();
            }
        });


        //init user list
        userList = new ArrayList<>();


        //get all users
        getAllUsers();

    }

    private void searchUsers() {
        Toast.makeText(UsersActivity.this, "Started Searching", Toast.LENGTH_SHORT).show();

        Query search_query = ref.orderByChild("name").startAt(mSearch)
                .endAt(mSearch+"\uf8ff");

        FirebaseRecyclerAdapter<ModelUser, AdapterUsers.MyHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelUser, AdapterUsers.MyHolder>(
                ModelUser.class,
                R.layout.row_users,
                AdapterUsers.MyHolder.class,
                search_query

        ) {
            @Override
            protected void populateViewHolder(AdapterUsers.MyHolder myHolder, ModelUser modelUser, int position) {
                myHolder.setDetails(modelUser.getName(), modelUser.getEmail());

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void getAllUsers() {
        //get current user
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        //get path of database named "Users" containing users info


        //get all data from path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelUser modelUser = ds.getValue(ModelUser.class);

                    //get all users except currently signed in user
                    if(!modelUser.getUid().equals(fUser.getUid())){
                        userList.add(modelUser);
                    }

                    //adapter
                    adapterUsers = new AdapterUsers(UsersActivity.this, userList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterUsers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
