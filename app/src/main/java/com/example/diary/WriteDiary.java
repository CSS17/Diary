package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;

public class WriteDiary extends AppCompatActivity {
    RatingBar ratingBar;
    int rate;
    String title,diary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);
        getSupportActionBar().setTitle("Write Your Diary");
        ratingBar=findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
               rate=(int)v;
                Log.d("MOON","your rate is "+rate);
            }
        });
    }

    public void TakePhoto(View view) {
        //This Method will trigger when user click Take Photo Button
        Log.d("MOON","Take Photo Button Clicked");
    }
    public void LocalSave(View view) {
        //This Method will trigger when user click Local Save Button
        Log.d("MOON","Local Save Button Clicked");
    }
    public void CloudSave(View view) {
        //This Method will trigger when user click CloudSave Button
        Log.d("MOON","Cloud Save Button Clicked");
    }
}