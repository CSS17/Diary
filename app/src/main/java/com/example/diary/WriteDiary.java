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
import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class WriteDiary extends AppCompatActivity {
    RatingBar ratingBar;
    EditText titleText;
    EditText contextText;
    TextView status;
    int rate;
    String title,diary;
    ImageView imageView;
    Bitmap bitmap;
    private StorageReference storageReference = null;
    FirebaseFirestore db = null;
    DBHelper DB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Craeted");

        //Firebase init
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        db = FirebaseFirestore.getInstance();
        DB=new DBHelper(this);
        setContentView(R.layout.activity_write_diary);
        getSupportActionBar().setTitle("Write Your Diary");

        //Get value
        ratingBar = findViewById(R.id.ratingBar);
        imageView = findViewById(R.id.image_view_on_write);
        titleText= findViewById(R.id.title);
        contextText = findViewById(R.id.diary);
        status=findViewById(R.id.status);
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
        String title=titleText.getText().toString();
        String body=contextText.getText().toString();
        LocalDate myObj = null; // Create a date object
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myObj = LocalDate.now();
        }
        System.out.println(myObj); // Display the current date
        String date=myObj.toString();
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate=(int)v;
            }
        });

        Boolean checkinsertData=DB.InsertData(title,body,rate,date);
        if(checkinsertData==true){
            Toast.makeText(WriteDiary.this,"Added to SQLite DB",Toast.LENGTH_SHORT);
        }
        else{
            Toast.makeText(WriteDiary.this,"Failed to Insert",Toast.LENGTH_SHORT);
        }

    }
    public void CloudSave(View view) {

        //This Method will trigger when user click CloudSave Button
        if(ContextCompat.checkSelfPermission(WriteDiary.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            String fireBaseFilePath = "images/"+ UUID.randomUUID().toString();
            //Firestorda , strogedeki dosya ismi tutulur.
            saveImageCloud(saveImageLocal(bitmap),fireBaseFilePath);
            saveOnCloud(fireBaseFilePath);
            status.setText("Saved On Cloud!");
            status.setVisibility(View.VISIBLE);
        }
        else{
            ActivityCompat.requestPermissions(WriteDiary.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }
        Log.d("MOON","Cloud Save Button Clicked");
    }





    public  void saveOnCloud(String fireBaseFilePath){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("title", String.valueOf(titleText.getText()));
        user.put("diary", String.valueOf(contextText.getText()));
        user.put("rating", rate);
        user.put("photoUrl",fireBaseFilePath);

// Add a new document with a generated ID
        db.collection("diary")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });

    } public  void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        System.out.println("onRequestPermission");
        if(requestCode == 1){
            System.out.println("STATUS CODE 1");
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                System.out.println("BİTMAP CHECKED");
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
        System.out.println("on Activity request code  :"  + String.valueOf(requestCode));
        if(requestCode == 101){
            bitmap = (Bitmap) data.getExtras().get("data");
            saveImageLocal(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
    private void saveImageCloud(Uri filePath,String firebaseFilePath) {
        firebaseFilePath = firebaseFilePath.split("images/")[1];
        if(filePath != null) {
//            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            StorageReference ref = storageReference.child(firebaseFilePath);
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
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "ne" + ".jpeg");
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
    public void StoreImage(View view){
        if(ContextCompat.checkSelfPermission(WriteDiary.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            saveImageLocal(bitmap);
            status.setText("Saved On Local!");
            status.setVisibility(View.VISIBLE);
        }
        else{
            ActivityCompat.requestPermissions(WriteDiary.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }
    }
}