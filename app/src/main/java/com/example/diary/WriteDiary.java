package com.example.diary;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Output;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;

public class WriteDiary extends AppCompatActivity {
    RatingBar ratingBar;
    int rate;
    String title,diary;
    ImageView imageView;
    Bitmap bitmap;
    private StorageReference storageReference = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


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
        if(ContextCompat.checkSelfPermission(WriteDiary.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            saveImageLocal(bitmap);
        }
        else{
            ActivityCompat.requestPermissions(WriteDiary.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }
    }
    public void CloudSave(View view) {
        //This Method will trigger when user click CloudSave Button
        saveImageCloud(saveImageLocal(bitmap));
        Log.d("MOON","Cloud Save Button Clicked");
    }








    public  void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveImageLocal(bitmap);
            }else{
                Toast.makeText(WriteDiary.this,"Please provide permission",Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 101){
            bitmap = (Bitmap) data.getExtras().get("data");
            saveImageLocal(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

    private void saveImageCloud(Uri filePath) {
        if(filePath != null) {
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //databaseReference.push().setValue(uri.toString());
                            Toast.makeText(WriteDiary.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(WriteDiary.this, "Image uploaded failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public Uri saveImageLocal(Bitmap bitmap) {
        Uri images;
        ContentResolver contentResolver = getContentResolver();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }else{
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "resmin_ismi_burda_agam" + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"images/*");
        Uri uri = contentResolver.insert(images,contentValues);
        try {
            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            Objects.requireNonNull(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return uri;

    }
}