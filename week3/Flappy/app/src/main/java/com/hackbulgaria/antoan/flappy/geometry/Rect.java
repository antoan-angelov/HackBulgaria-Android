package com.hackbulgaria.antoan.flappy.geometry;

/**
 * Created by Antoan on 29-Dec-14.
 */
public class Rect {

    private int mX;
    private int mY;
    private int mWidth;
    private int mHeight;

    public Rect(int x, int y, int width, int height) {
        mX = x;
        mY = y;
        mWidth = width;
        mHeight = height;
    }

    public boolean isInRect(int x, int y) {
        return x >= mX && y >= mY && x <= mX + mWidth && y <= mY + mHeight;
    }

    @Override
    public String toString() {
        return "Rect(x="+mX+", y="+mY+", width="+mWidth+", height="+mHeight+")";
    }
}
