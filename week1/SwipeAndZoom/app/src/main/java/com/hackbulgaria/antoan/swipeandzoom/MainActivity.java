package com.hackbulgaria.antoan.swipeandzoom;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private ArrayList<Drawable> mImages;
    private GestureDetector mDetector;
    private int mCurrentImageIndex;
    private ImageView mImage;
    private TextView mText;
    private boolean mZoomed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImages = new ArrayList<Drawable>();

        mImage = (ImageView) findViewById(R.id.image);
        mText = (TextView) findViewById(R.id.text);

        TypedArray arr = getResources().obtainTypedArray(R.array.images);
        for (int i = 0; i < arr.length(); i++) {
            Drawable d = arr.getDrawable(i);
            mImages.add(d);
        }

        arr.recycle();

        mCurrentImageIndex = 0;
        mZoomed = false;
        mDetector = new GestureDetector(this, new MyGestureListener());

        displayImage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }



    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            if(!mZoomed) {
                mImage.setScaleX(2);
                mImage.setScaleY(2);
            }
            else {
                mImage.setScaleX(1);
                mImage.setScaleY(1);
            }

            mZoomed = !mZoomed;

            return super.onDoubleTap(e);
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if(velocityX < 0)
                showNextImage();
            else
                showPrevImage();

            return true;
        }
    }

    private void showPrevImage() {
        if(mCurrentImageIndex - 1 >= 0) {
            mCurrentImageIndex --;
            displayImage();
        }
    }

    private void showNextImage() {
        if(mCurrentImageIndex + 1 < mImages.size()) {
            mCurrentImageIndex ++;
            displayImage();
        }
    }

    private void displayImage() {
        if(mZoomed) {
            mZoomed = false;
            mImage.setScaleX(1);
            mImage.setScaleY(1);
        }

        mImage.setImageDrawable(mImages.get(mCurrentImageIndex));
        mText.setText((mCurrentImageIndex + 1) + " / " + mImages.size());
    }
}
