package com.hackbulgaria.antoan.drawablebrush;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CustomView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private Bitmap mBrush;
    private List<Bitmap> mBrushes;
    private int mCurrentBrush;

    public final static int BRUSH_PLUS = 0;
    public final static int BRUSH_PRESENT = 1;
    public final static int BRUSH_STAR = 2;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setBrush(int brush) {
        mCurrentBrush = brush;
        mBrush = mBrushes.get(mCurrentBrush);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAlpha(128);

        mBrushes = new ArrayList<Bitmap>();
        mBrushes.add(BitmapFactory.decodeResource(getResources(), R.drawable.plus));
        mBrushes.add(BitmapFactory.decodeResource(getResources(), R.drawable.present));
        mBrushes.add(BitmapFactory.decodeResource(getResources(), R.drawable.star));
        setBrush(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0f, 0f, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mCanvas.drawBitmap(mBrush, event.getX() - mBrush.getWidth() * 0.5f, event.getY() - mBrush.getHeight() * 0.5f, mPaint);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }
}
