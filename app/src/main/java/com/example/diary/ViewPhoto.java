package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ViewPhoto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        getSupportActionBar().setTitle("View Your Photos");
    }
}