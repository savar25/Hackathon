package com.example.hackathon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.models.ImageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Holder> {

    ArrayList<ImageModel> models=new ArrayList<>();
    Context context;

    public ImageAdapter(ArrayList<ImageModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        Picasso.get().load("file://images/" + models.get(position).getAl_imagepath().get(0)).centerCrop().resize(200,200).into(holder.listImage);
        holder.listImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,designer.class);
                intent.putExtra("filePath",models.get(position).getAl_imagepath().get(0));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
       ImageView listImage;

        public Holder(@NonNull View itemView) {
            super(itemView);

            listImage=itemView.findViewById(R.id.imageViewStore);
        }
    }
}
