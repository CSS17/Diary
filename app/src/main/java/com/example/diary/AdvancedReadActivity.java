package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.graphics.Matrix;



public class AdvancedReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_read);
        Matrix matrix = new Matrix();
        getSupportActionBar().setTitle("Here is your Diary");
        ImageView imageView = findViewById(R.id.image_view_on_advanced);
        TextView viewdate,viewtitle,viewbody;
        RatingBar ratingBar;
        viewdate=findViewById(R.id.title);
        viewtitle=findViewById(R.id.title);
        viewbody=findViewById(R.id.body);
        ratingBar=findViewById(R.id.ratingBar);
        String rate,diary,date,title;
        Bitmap image;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                rate= null;
            } else {
                rate= extras.getString("rate");
            }
        } else {
            rate= (String) savedInstanceState.getSerializable("rate");
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                diary= null;
            } else {
                diary= extras.getString("diary");
            }
        } else {
            diary= (String) savedInstanceState.getSerializable("diary");
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                date= null;
            } else {
                date= extras.getString("date");
            }
        } else {
            date= (String) savedInstanceState.getSerializable("date");
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                title= null;
            } else {
                title= extras.getString("title");
            }
        } else {
            title= (String) savedInstanceState.getSerializable("title");
        }


        Bitmap imageBitMap = (Bitmap) getIntent().getExtras().get("bitmap");

        System.out.println(String.valueOf(imageBitMap));
        imageView.setImageBitmap(imageBitMap);
        viewtitle.setText(title);
        viewbody.setText(diary);
        viewdate.setText(date);
        ratingBar.setRating(Float.parseFloat(rate));



    }
}