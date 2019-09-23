package com.example.macro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SM_Update_Items extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sm__update__items);

    }

    public void pressUpdate(View view){
        Intent intent1 = new Intent(SM_Update_Items.this,Store_Management.class);
        startActivity(intent1);
    }

}
