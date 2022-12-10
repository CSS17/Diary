package com.example.diary;

import android.content.Context;
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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewClass>{
    ArrayList<String> edittxtArray;

    ArrayList<Bitmap> imageBitMapArray;
    Context context;
    public RecyclerViewAdapter(ArrayList<String> edittxtArray , ArrayList<Bitmap> imageBitMapArray , Context context) {
        this.edittxtArray = edittxtArray;
        this.imageBitMapArray = imageBitMapArray;
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

                Toast.makeText(context,"Item Clicked" + edittxtArray.get(holder.getAdapterPosition())+"" ,Toast.LENGTH_LONG).show();
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
