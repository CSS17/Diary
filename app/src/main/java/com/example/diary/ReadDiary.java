package com.example.diary;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class ReadDiary extends AppCompatActivity {
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    ArrayList<String> edittxtArray=new ArrayList<>();//edittext
    ArrayList<Bitmap> imageBitMapArray=new ArrayList<>();//edittext
    ImageView imageView;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_diary);
        getSupportActionBar().setTitle("Read Your Diaries");
        System.out.println(Environment.getExternalStorageDirectory());
        imageView=(ImageView)findViewById(R.id.fetch_image);
        fetchImageCloud();
        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        //fetchImageLocal();
    }
    public void fetchImageCloud(){
        // Create a storage reference from our app
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("x.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //Load arrarys
                imageBitMapArray.add(bmp);
                edittxtArray.add("qweqwe");
                imageBitMapArray.add(bmp);
                edittxtArray.add("asadasd");


                Adapter adapter =new Adapter(edittxtArray,imageBitMapArray ,ReadDiary.this);
                recyclerView.setAdapter(adapter);

                System.out.println(edittxtArray.get(0));
                imageView.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });}
    public void fetchImageLocal(){
        String path = "/storage/emulated/0/Pictures/title.jpg";

        File imgFile = new File(path);
        if(imgFile.exists())
        {
            System.out.println("Dosya var");

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
    }
}