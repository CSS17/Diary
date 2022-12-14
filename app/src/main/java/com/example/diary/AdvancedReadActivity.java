package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class AdvancedReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_read);
        ImageView imageView = findViewById(R.id.image_view_on_advanced);
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

        System.out.println("Retrieve" + rate);
        System.out.println("Retrieve" + date);
        System.out.println("Retrieve" + diary);
        System.out.println("Retrieve" + title);
        Bitmap imageBitMap = (Bitmap) getIntent().getExtras().get("bitmap");
        System.out.println(String.valueOf(imageBitMap));
        imageView.setImageBitmap(imageBitMap);


    }
}