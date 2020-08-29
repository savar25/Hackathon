package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.models.DesignView;
import com.example.models.ScaleDrag;
import com.squareup.picasso.Picasso;

public class designer extends AppCompatActivity {

    ImageView main;
    ScaleDrag scaleDrag;
    DesignView designView;
    ImageButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer);

        main=findViewById(R.id.mainImage);
        scaleDrag=findViewById(R.id.imageViewDesign);
        designView=findViewById(R.id.designView);

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;

        String filepath=getIntent().getStringExtra("filePath");
        Bitmap map= BitmapFactory.decodeFile(filepath,options);
        main.setImageBitmap(map);




       ScaleDrag view=findViewById(R.id.imageViewDesign);
        Bitmap map1=Bitmap.createScaledBitmap(map,800,800,true);
       view.setBitmap(map1);





    }


}