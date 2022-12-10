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
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadDiary extends AppCompatActivity {
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    ArrayList<String> edittxtArray=new ArrayList<>();//imageviews
    ArrayList<Bitmap> imageBitMapArray=new ArrayList<>();//edittext

    ArrayList<String> firebaseDiaryIds=new ArrayList<>();
    ArrayList<String> firebaseDiaryRatings=new ArrayList<>();
    ArrayList<String> firebaseDiary=new ArrayList<>();
    ArrayList<String> firebaseDiaryTitle=new ArrayList<>();
    ArrayList<String> firebaseDiaryPhotoUrl=new ArrayList<>();

    ImageView imageView;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_diary);
        getSupportActionBar().setTitle("Read Your Diaries");
        imageView=(ImageView)findViewById(R.id.fetch_image);

        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        fetchAllID();
        //fetchImageLocal();
    }
    public  void fetchAllID(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("diary").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //document.getId() tekrar için tekrar sorgu atılmalı.
                        firebaseDiaryIds.add(document.getId());
                        fetchInfo(document.getId());
                    }
                    System.out.println("liste" + firebaseDiaryIds.toString());

                } else {
                    System.out.println("yokk" );
                }
            }
        });
    }
    public void fetchInfo(String id){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("diary").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println("DATA VAR" + String.valueOf(document.getData().get("rating")));
                        firebaseDiaryTitle.add( String.valueOf(document.getData().get("title")));
                        firebaseDiaryRatings.add( String.valueOf(document.getData().get("rating")));
                        firebaseDiary.add( String.valueOf(document.getData().get("diary")));
                        firebaseDiaryPhotoUrl.add(String.valueOf(document.getData().get("photoUrl")));
                        fetchImageCloud(String.valueOf(document.getData().get("photoUrl")),String.valueOf(document.getData().get("title")));
                    } else {
                        System.out.println("document yok");
                        Log.d("TAG", "No such document");
                    }
                } else {

                    System.out.println("error var");
                    Log.d("TAG", "get failed with ", task.getException());
                }

                // Mount işlemi

            }
        });
    }
    public void fetchImageCloud(String photourl, String title){
        // Create a storage reference from our app
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child(photourl);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //Load arrarys
                imageBitMapArray.add(bmp);
                edittxtArray.add(title);

                RecyclerViewAdapter adapter = new RecyclerViewAdapter(edittxtArray,imageBitMapArray ,ReadDiary.this);
                recyclerView.setAdapter(adapter);


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

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
    }
}