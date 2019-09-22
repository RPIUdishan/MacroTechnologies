package com.example.macro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EmployeeActivity extends AppCompatActivity {

    //views
    Button mCreateBtn, mListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        //init Views
//        mCreateBtn = findViewById(R.id.create_btn);
//        mListBtn = findViewById(R.id.list_btn);
//
//        //handle create button click
//        mCreateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(EmployeeActivity.this, CreateActivity.class));
//            }
//        });

        mCreateBtn = findViewById(R.id.create_btn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

//        mCreateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //start RegisterActivity
//                Intent intent = new Intent(EmployeeActivity.this, CreateActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}
