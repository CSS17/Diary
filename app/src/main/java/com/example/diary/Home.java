package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
    }

    public void WriteDiary(View view) {
        Intent intent=new Intent(getApplicationContext(),WriteDiary.class);
        startActivity(intent);
    }

    public void ReadDiary(View view) {
        Intent intent=new Intent(getApplicationContext(),ReadDiary.class);
        startActivity(intent);
    }

    public void TakePhoto(View view) {
        Intent intent=new Intent(getApplicationContext(),TakePhoto.class);
        startActivity(intent);
    }

    public void ViewPhotos(View view) {
        Intent intent=new Intent(getApplicationContext(),ViewPhoto.class);
        startActivity(intent);
    }
}