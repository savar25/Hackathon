package com.example.hackathon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.models.DesignView;
import com.example.models.ImageModel;
import com.example.models.ScaleDrag;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class designer extends AppCompatActivity {

    ImageView main;
    ScaleDrag scaleDrag;
    DesignView designView;
    ImageButton doodle,imAdd,teAdd;
    public static  Bitmap map2;
    public static AlertDialog alertDialog;
    public static Bitmap map1;
    private static final String TAG = "designer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer);

        main=findViewById(R.id.mainImage);
        scaleDrag=findViewById(R.id.imageViewDesign);
        designView=findViewById(R.id.designView);
        doodle=findViewById(R.id.doodleBtn);
        imAdd=findViewById(R.id.imageAdd);
        teAdd=findViewById(R.id.textAdd);

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;

        String filepath=getIntent().getStringExtra("filePath");
        Bitmap map= BitmapFactory.decodeFile(filepath,options);
        Picasso.get().load("file://"+filepath).resize(300,300).into(main);
        map1=Bitmap.createScaledBitmap(map,800,800,true);

        doodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.setVisibility(View.GONE);
                scaleDrag.setVisibility(View.GONE);
                designView.setVisibility(View.VISIBLE);
                designView.setBitmap(map1);

            }
        });

        imAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.setVisibility(View.VISIBLE);
                designView.setVisibility(View.GONE);
                scaleDrag.setVisibility(View.VISIBLE);

                LayoutInflater li = LayoutInflater.from(designer.this);
                final View promptsView = li.inflate(R.layout.rec_layout, null);


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        designer.this);

                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder.setTitle("Choose Image");

                RecyclerView rec=promptsView.findViewById(R.id.rec1);
                rec.setLayoutManager(new LinearLayoutManager(designer.this));
                ListImageAdapter imageAdapter=new ListImageAdapter(fn_imagespath(),designer.this);
                rec.setAdapter(imageAdapter);

                alertDialog=alertDialogBuilder.create();
                alertDialog.show();
                Log.d(TAG, "onClick: "+map2);
                scaleDrag.setBitmap(map2);


            }



        });


    }

    public ArrayList<ImageModel> fn_imagespath() {
        ArrayList<ImageModel> al_images=new ArrayList<>();
        Boolean boolean_folder=false;

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            for (int i = 0; i < al_images.size(); i++) {
                if (al_images.get(i).getStr_folder().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }


            if (boolean_folder) {

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(al_images.get(int_position).getAl_imagepath());
                al_path.add(absolutePathOfImage);
                al_images.get(int_position).setAl_imagepath(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                ImageModel obj_model = new ImageModel();
                obj_model.setStr_folder(cursor.getString(column_index_folder_name));
                obj_model.setAl_imagepath(al_path);

                al_images.add(obj_model);


            }


        }


        for (int i = 0; i < al_images.size(); i++) {
            Log.e("FOLDER", al_images.get(i).getStr_folder());
            for (int j = 0; j < al_images.get(i).getAl_imagepath().size(); j++) {
                Log.e("FILE", al_images.get(i).getAl_imagepath().get(j));
            }
        }


        return al_images;
    }




}