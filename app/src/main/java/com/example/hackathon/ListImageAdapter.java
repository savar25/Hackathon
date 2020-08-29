package com.example.hackathon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.models.ImageModel;
import com.example.models.ScaleDrag;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.hackathon.designer.map2;

public class ListImageAdapter extends RecyclerView.Adapter<ListImageAdapter.Holder> {

    ArrayList<ImageModel> models=new ArrayList<>();
    Context context;

    public ListImageAdapter(ArrayList<ImageModel> models, Context context) {
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
        Picasso.get().load("file://images/" + models.get(position).getAl_imagepath().get(0)).centerCrop().resize(200,200).into(holder.im);
        holder.im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScaleDrag.bitmap2=Bitmap.createScaledBitmap(BitmapFactory.decodeFile(models.get(position).getAl_imagepath().get(0)),800,800,true);
                designer.alertDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        ImageView im;
        public Holder(@NonNull View itemView) {
            super(itemView);

            im=itemView.findViewById(R.id.imageViewStore);
        }
    }
}
