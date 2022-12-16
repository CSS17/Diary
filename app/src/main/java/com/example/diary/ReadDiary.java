package com.example.diary;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

public class ReadDiary extends AppCompatActivity {
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    ArrayList<String> edittxtArray=new ArrayList<>();//Title
    ArrayList<Bitmap> imageBitMapArray=new ArrayList<>();//edittext
    ArrayList<String> rateArray=new ArrayList<String>();//Rating
    ArrayList<String> diaryArray=new ArrayList<String>();//Diary
    ArrayList<String> dateArray = new ArrayList<String>(); // Date

    ArrayList<String> firebaseDiaryIds=new ArrayList<>();
    ArrayList<String> firebaseDiaryRatings=new ArrayList<>();
    ArrayList<String> firebaseDiary=new ArrayList<>();
    ArrayList<String> firebaseDiaryTitle=new ArrayList<>();
    ArrayList<String> firebaseDiaryPhotoUrl=new ArrayList<>();

    String tempRate;
    String  tempDiary;
    String  tempDate;

    ImageView imageView;
    RecyclerView recyclerView;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DB=new DBHelper(this);

        setContentView(R.layout.activity_read_diary);
        getSupportActionBar().setTitle("Read Your Diaries");
        imageView=(ImageView)findViewById(R.id.fetch_image);

        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        fetchAllID();
        fetchLocal();

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
                        tempRate = String.valueOf(document.getData().get("rating"));
                        tempDiary = String.valueOf(document.getData().get("diary"));
                        tempDate = String.valueOf(document.getData().get("date"));

                        fetchImageCloud(tempDate,tempDiary,tempRate,String.valueOf(document.getData().get("photoUrl")),String.valueOf(document.getData().get("title")),String.valueOf(document.getData().get("rate")));
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
    public void fetchImageCloud(String tempDate,String tempDiary,String tempRate,String photourl, String title,String rate){
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
                rateArray.add(tempRate);
                diaryArray.add(tempDiary);
                dateArray.add(tempDate);

                RecyclerViewAdapter adapter = new RecyclerViewAdapter(diaryArray,dateArray,edittxtArray,imageBitMapArray ,rateArray,ReadDiary.this);
                recyclerView.setAdapter(adapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });}
    public void fetchLocal(){
        Cursor cursorCourses = DB.getData();
        System.out.println("LOCALL////////////////");
        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.


                System.out.println(cursorCourses.getString(0));
                String title = cursorCourses.getString(0);
                String diary = cursorCourses.getString(1);
                String rate =  cursorCourses.getString(2);
                String date =  cursorCourses.getString(3);
                String photourl = cursorCourses.getString(4);

                //File imgFile = new File("/storage/emulated/0/Pictures/ne.jpeg");
                System.out.println("Fetching photo url " + photourl);
                File imgFile = new File("/storage/emulated/0/Pictures/"+photourl+".jpeg");


                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                //imageView.setImageBitmap(myBitmap);

                System.out.println(photourl);
                edittxtArray.add(title);
                rateArray.add(rate);
                diaryArray.add(diary);
                dateArray.add(date);
                imageBitMapArray.add(myBitmap);
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(diaryArray,dateArray,edittxtArray,imageBitMapArray ,rateArray,ReadDiary.this);
                recyclerView.setAdapter(adapter);

            } while (cursorCourses.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorCourses.close();

        System.out.println("LOCALLEND////////////////");
    }
    public void fetchImageLocal(){
        String path = "";

        File imgFile = new File(path);
        if(imgFile.exists())
        {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
    }
}