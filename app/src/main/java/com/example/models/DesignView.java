package com.example.models;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.hackathon.R;

import androidx.annotation.Nullable;

public class DesignView extends View {

    Bitmap bitmap,map;
    Canvas canvas1;
    Paint brush,bitmapPaint;
    Path path=new Path();
    private static final String TAG = "DesignView";


    public DesignView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        brush=new Paint();
        brush.setColor(Color.BLACK);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeJoin(Paint.Join.MITER);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeWidth(5f);

        bitmapPaint=new Paint(Paint.DITHER_FLAG);
        bitmapPaint.setAntiAlias(false);
        bitmapPaint.setColor(Color.parseColor("#eeeeee"));
        bitmapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bitmapPaint.setStrokeWidth(10f);

        TypedArray a= context.getTheme().obtainStyledAttributes(attrs, R.styleable.DesignView, 0, 0);

        try {
            if(a.getInteger(R.styleable.DesignView_bitmap,3)==0){
                this.bitmap=Bitmap.createBitmap(300,300, Bitmap.Config.ARGB_8888);
            }

        }finally {
            a.recycle();
        }
    }

    public DesignView(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvas1=new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.parseColor("#040404"));
        canvas.drawBitmap(bitmap,0,0,bitmapPaint);

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap =bitmap;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x_pos=event.getX();
        float y_pos=event.getY();

        Log.d(TAG, "onTouchEvent: called");
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x_pos,y_pos);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x_pos,y_pos);
                canvas1.drawPath(path,brush);
                break;
            case MotionEvent.ACTION_UP:
                canvas1.drawPath(path,brush);
                path.reset();
                break;
            default:
                return false;
        }

        invalidate();

        return true;

    }
}
