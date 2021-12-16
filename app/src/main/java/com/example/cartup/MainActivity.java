package com.example.cartup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.cartuplibrary.Cartup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Cartup(this, "", "", "", "").GetCartupEvent();
    }
}