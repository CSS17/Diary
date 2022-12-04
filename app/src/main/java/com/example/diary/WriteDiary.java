package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class WriteDiary extends AppCompatActivity {
    RatingBar ratingBar;
    private Button saveLocalButton ;
    int rate;
    String title,diary;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFireStore ;
    private HashMap<String, Object> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFireStore = FirebaseFirestore.getInstance();
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
        Log.d("Rate", String.valueOf(rate));
        EditText contentField = findViewById(R.id.diary);
        String context = String.valueOf(contentField.getText());
        EditText titleField = findViewById(R.id.title);
        title = String.valueOf(contentField.getText());

        mData = new HashMap<>();
        mData.put("Content",context);
        mData.put("Rating",String.valueOf(rate));
        mData.put("Title",title);

        mFireStore.collection("diary").document("idgelcekhmmmm").set(mData);
    }
}