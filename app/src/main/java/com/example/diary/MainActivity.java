package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        CountDownTimer waitTimer;
        waitTimer = new CountDownTimer(2000, 300) {

            public void onTick(long millisUntilFinished) {
                //called every 300 milliseconds, which could be used to
                //send messages or some other action
            }

            public void onFinish() {
                //After 60000 milliseconds (60 sec) finish current
                //if you would like to execute something when time finishes
                Intent intent=new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
                finish();
            }
        }.start();




    }

}