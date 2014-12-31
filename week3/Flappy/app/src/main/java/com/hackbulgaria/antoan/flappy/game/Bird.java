package com.hackbulgaria.antoan.flappy.game;

import android.content.Context;
import android.graphics.Canvas;

import com.hackbulgaria.antoan.flappy.R;
import com.hackbulgaria.antoan.flappy.geometry.Vector;

/**
 * Created by Antoan on 09-Nov-14.
 */
public class Bird extends GameObject {

    private final static float JUMP_STRENGTH = 40f * 0.7f ;
    private final static float JUMP_STEP = 16f;
    private final static double RADIANS_IN_DEGREE = Math.PI / 180f;

    private final Vector mDir = new Vector(0, 0);
    private float mJumpAngle;
    private float mRotation;

    public Bird(Context context, float initialX, float initialY) {
        super(context, R.drawable.bird, initialX, initialY);

        mCollisionRect.set(50, 0, mWidth, mHeight);
        mRotation = 0;
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        mDir.y = - JUMP_STRENGTH * (float) func(mJumpAngle);

        mJumpAngle += JUMP_STEP;

        mPosition.y += mDir.y;

        mRotation = (90 - mJumpAngle) / 3;
    }

    private double func(float mJumpAngle) {
        if(mJumpAngle <= 180)
            return Math.cos(mJumpAngle * RADIANS_IN_DEGREE);

        return -(mJumpAngle - 180)/30-1;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        final int saveCount = canvas.save();
        try {
            canvas.rotate(-mRotation, mPosition.x + getWidth() * 0.5f, mPosition.y + getHeight() * 0.5f);
            canvas.drawBitmap(mBitmap, mPosition.x, mPosition.y, null);
        } finally {
            canvas.restoreToCount(saveCount);
        }
    }

    public void flap() {
        mJumpAngle = 0;
    }

    public void die() {
        mJumpAngle = 0;
        mRotation = 0;
        mDir.set(0, 0);
    }
}
