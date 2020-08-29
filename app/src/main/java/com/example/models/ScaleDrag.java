package com.example.models;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.hackathon.R;
import com.example.hackathon.designer;

import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class ScaleDrag extends View {

    Paint bitmapPaint;
    public static Bitmap bitmap1,bitmap2;
    public float scaleFactor=1.f;
    ScaleGestureDetector detector1;
    float mLastTouchX,mLastTouchY,mPosX=0,mPosY=0;
    int mActivePointerId;
    Canvas canvas1;
    Bitmap bitmap;
    private static final String TAG = "ScaleDrag";

    public ScaleDrag(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a= context.getTheme().obtainStyledAttributes(attrs, R.styleable.DesignView, 0, 0);

        try {
            if(a.getInteger(R.styleable.DesignView_bitmap,3)==0){
                this.bitmap2= Bitmap.createBitmap(300,300, Bitmap.Config.ARGB_8888);
            }

        }finally {
            a.recycle();
        }

        bitmapPaint=new Paint(Paint.DITHER_FLAG);
        bitmapPaint.setAntiAlias(false);
        bitmapPaint.setColor(Color.parseColor("#eeeeee"));
        bitmapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bitmapPaint.setStrokeWidth(10f);


        detector1=new ScaleGestureDetector(context,new ScaleDetector());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.drawBitmap(bitmap,new Matrix(),bitmapPaint);
        canvas.scale(scaleFactor,scaleFactor);

        if(bitmap2!=null) {
            canvas.drawBitmap(bitmap2, mPosX, mPosY, bitmapPaint);
        }


        canvas.restore();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap=Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        this.bitmap2=Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        canvas1 = new Canvas(bitmap2);

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
            // Let the ScaleGestureDetector inspect all events.
            detector1.onTouchEvent(ev);

            final int action = MotionEventCompat.getActionMasked(ev);

            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                    final float x = MotionEventCompat.getX(ev, pointerIndex);
                    final float y = MotionEventCompat.getY(ev, pointerIndex);
                    // Remember where we started (for dragging)
                    mLastTouchX = x;
                    mLastTouchY = y;
                    // Save the ID of this pointer (for dragging)
                    mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    // Find the index of the active pointer and fetch its position
                    final int pointerIndex =
                            MotionEventCompat.findPointerIndex(ev, mActivePointerId);

                    final float x = MotionEventCompat.getX(ev, pointerIndex);
                    final float y = MotionEventCompat.getY(ev, pointerIndex);

                    // Calculate the distance moved
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;
                    canvas1.drawBitmap(bitmap2,mPosX,mPosY,bitmapPaint);
                    bitmap=bitmapOverlayToCenter(bitmap,bitmap2);

                    invalidate();

                    // Remember this touch position for the next move event
                    mLastTouchX = x;
                    mLastTouchY = y;

                    break;
                }

                case MotionEvent.ACTION_UP: {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_CANCEL: {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_POINTER_UP: {

                    final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                    final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                        mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                        mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                    }
                    break;
                }
            }
            invalidate();

            return true;
        }


    public class ScaleDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scaleFactor*= detector.getScaleFactor();
            scaleFactor=Math.max(0.1f,Math.min(scaleFactor,5.0f));
            invalidate();
            return true;

        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap2=bitmap2;

        invalidate();
    }

    public Bitmap bitmapOverlayToCenter(Bitmap bitmap1, Bitmap overlayBitmap) {
        int bitmap1Width = bitmap1.getWidth();
        int bitmap1Height = bitmap1.getHeight();
        int bitmap2Width = overlayBitmap.getWidth();
        int bitmap2Height = overlayBitmap.getHeight();


        Bitmap finalBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bitmap1.getConfig());
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(overlayBitmap, mPosX, mPosY, null);
        designer.map1=bitmapOverlayToCenter1(finalBitmap, designer.map1);
        return finalBitmap;
    }

    public Bitmap bitmapOverlayToCenter1(Bitmap bitmap1, Bitmap overlayBitmap) {

        Log.d(TAG, "bitmapOverlayToCenter1: called");
        int bitmap1Width = bitmap1.getWidth();
        int bitmap1Height = bitmap1.getHeight();
        int bitmap2Width = overlayBitmap.getWidth();
        int bitmap2Height = overlayBitmap.getHeight();
        float marginLeft = (float) (bitmap1Width * 0.5 - bitmap2Width * 0.5);
        float marginTop = (float) (bitmap1Height * 0.5 - bitmap2Height * 0.5);

        Bitmap finalBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bitmap1.getConfig());
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(overlayBitmap, marginLeft, marginLeft, null);
        return finalBitmap;
    }
}
