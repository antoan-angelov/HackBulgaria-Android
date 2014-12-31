package com.hackbulgaria.antoan.flappy.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Antoan on 09-Nov-14.
 */
public abstract class GameObject implements GameClock.GameClockListener {

    protected Bitmap mBitmap;
    protected final PointF mPosition = new PointF();
    protected int mWidth;
    protected int mHeight;
    protected Context mContext;
    protected final RectF mCollisionRect = new RectF();

    protected GameObject(Bitmap bitmap, float initialX, float initialY, int width, int height) {
        this.mBitmap = bitmap;
        mPosition.set(initialX, initialY);
        mWidth = width;
        mHeight = height;

        mCollisionRect.set(0, 0, mWidth, mHeight);
    }

    protected GameObject(Context context, int drawable, float initialX, float initialY, int width, int height) {
        this.mContext = context;
        this.mBitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
        mPosition.set(initialX, initialY);
        mWidth = width;
        mHeight = height;

        mCollisionRect.set(0, 0, mWidth, mHeight);
    }

    protected GameObject(Context context, int drawable, float initialX, float initialY) {
        this.mContext = context;
        this.mBitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
        mPosition.set(initialX, initialY);
        mWidth = mBitmap.getWidth();
        mHeight = mBitmap.getHeight();

        mCollisionRect.set(0, 0, mWidth, mHeight);
    }

    public GameObject(Bitmap bitmap, float initialX, float initialY) {
        this.mBitmap = bitmap;
        mPosition.set(initialX, initialY);
        mWidth = mBitmap.getWidth();
        mHeight = mBitmap.getHeight();

        mCollisionRect.set(0, 0, mWidth, mHeight);
    }

    public abstract void draw(Canvas canvas);

    public PointF getPosition() {
        return mPosition;
    }

    public void setPosition(float x, float y) {
        mPosition.set(x, y);
    }


    public boolean collidesWith(GameObject object) {
        return (object.pointInObject(mPosition.x + mCollisionRect.right, mPosition.y + mCollisionRect.top)
                || object.pointInObject(mPosition.x + mCollisionRect.right, mPosition.y + mCollisionRect.bottom)
                || object.pointInObject(mPosition.x + mCollisionRect.left, mPosition.y + mCollisionRect.top)
                || object.pointInObject(mPosition.x + mCollisionRect.left, mPosition.y + mCollisionRect.bottom));
    }

    private boolean pointInObject(float x, float y) {
        return (x >= mPosition.x
                && x <= mPosition.x + mWidth
                && y >= mPosition.y
                && y <= mPosition.y + mHeight);
    }

    public void setWidth(int width) {
        this.mWidth = width;
        mCollisionRect.set(0, 0, mWidth, mHeight);
    }

    public void setHeight(int height) {
        this.mHeight = height;
        mCollisionRect.set(0, 0, mWidth, mHeight);
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void dispose() {
        mBitmap.recycle();
        mBitmap = null;
    }
}
