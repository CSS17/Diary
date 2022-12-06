package com.example.diary;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteDiary extends AppCompatActivity {
    RatingBar ratingBar;
    int rate;
    String title,diary;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);
        getSupportActionBar().setTitle("Write Your Diary");
        ratingBar=findViewById(R.id.ratingBar);
        imageView = findViewById(R.id.image_view);
        if (ContextCompat.checkSelfPermission(WriteDiary.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(WriteDiary.this,new String[]{Manifest.permission.CAMERA},101);
        }
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,101);

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 101){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            saveImage(bitmap);
            imageView.setImageBitmap(StringToBitMap(BitMapToString(bitmap)));
        }
    }
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public static void saveImage(Bitmap bitmapImage) {

        File root = new File(Environment.getExternalStorageDirectory(), "Identidata");

        if (!root.exists()) {
            root.mkdirs();
        }

        File mypath = new File(root,"debug.jpg");


        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}