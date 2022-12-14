package com.example.diary;

import static android.content.Intent.ACTION_VIEW;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewClass>{
    ArrayList<String> edittxtArray;
    ArrayList<String> rateArray;
    ArrayList<String> diaryArray;
    ArrayList<String> dateArray;

    ArrayList<Bitmap> imageBitMapArray;
    Context context;
    public RecyclerViewAdapter(ArrayList<String> diaryArray,ArrayList<String> dateArray,ArrayList<String> edittxtArray , ArrayList<Bitmap> imageBitMapArray  ,ArrayList<String> rateArray,Context context) {
        this.diaryArray = diaryArray;
        this.dateArray = dateArray;
        this.edittxtArray = edittxtArray;
        this.imageBitMapArray = imageBitMapArray;
        this.rateArray = rateArray;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        MyViewClass myViewClass = new MyViewClass(view);

        return myViewClass;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewClass holder, int position) {
        holder.edittxt.setText(edittxtArray.get(position));
        holder.imageView.setImageBitmap(imageBitMapArray.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("CLİCKED ///////");
                System.out.println(rateArray.get(holder.getAdapterPosition())); // Rate
                System.out.println(edittxtArray.get(holder.getAdapterPosition())); // Title
                System.out.println(diaryArray.get(holder.getAdapterPosition()));
                System.out.println(dateArray.get(holder.getAdapterPosition()));

                Intent intent=new Intent(context.getApplicationContext(),AdvancedReadActivity.class);
                intent.putExtra("rate", rateArray.get(holder.getAdapterPosition()));
                intent.putExtra("diary", diaryArray.get(holder.getAdapterPosition()));
                intent.putExtra("date", dateArray.get(holder.getAdapterPosition()));
                intent.putExtra("title", edittxtArray.get(holder.getAdapterPosition()));
                intent.putExtra("bitmap", imageBitMapArray.get(position));

                context.startActivity(intent);


                System.out.println("//////////");
                }

        });
    }

    @Override
    public int getItemCount() {
        return  edittxtArray.size();
    }
    public class MyViewClass extends RecyclerView.ViewHolder {
        TextView edittxt;
        ImageView imageView;
        public MyViewClass(@NonNull View itemView) {
            super(itemView);
            edittxt= (TextView) itemView.findViewById(R.id.edittxt);
            imageView = (ImageView) itemView.findViewById(R.id.fetch_image_recycleview);


        }
    }
}